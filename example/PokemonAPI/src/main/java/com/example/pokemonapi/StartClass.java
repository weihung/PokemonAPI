package com.example.pokemonapi;

import com.example.pokemonapi.GoApi.EventListener;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.inventory.PokeBank;
import com.pokegoapi.api.map.fort.Pokestop;
import com.pokegoapi.api.map.pokemon.CatchablePokemon;
import com.pokegoapi.api.pokemon.Pokemon;
import com.pokegoapi.auth.GoogleUserCredentialProvider;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.samczsun.skype4j.Skype;
import com.samczsun.skype4j.SkypeBuilder;
import com.samczsun.skype4j.chat.Chat;
import com.samczsun.skype4j.chat.messages.ChatMessage;
import com.samczsun.skype4j.events.EventHandler;
import com.samczsun.skype4j.events.Listener;
import com.samczsun.skype4j.events.chat.message.MessageEvent;
import com.samczsun.skype4j.events.contact.ContactRequestEvent;
import com.samczsun.skype4j.exceptions.ChatNotFoundException;
import com.samczsun.skype4j.exceptions.ConnectionException;
import com.samczsun.skype4j.exceptions.InvalidCredentialsException;
import com.samczsun.skype4j.exceptions.NotParticipatingException;
import com.samczsun.skype4j.exceptions.handler.ErrorHandler;
import com.samczsun.skype4j.exceptions.handler.ErrorSource;

public class StartClass {
	
	private static Skype skype;
	private final static String USERNAME = "";
	private final static String PASSWORD = "";
	
	public static void main(String[] args) {
		initSkype();
		GoApi.getInstance(eventListener);
		System.out.println("開工了");
	}
	
	private static void initSkype(){
		skype = new SkypeBuilder(USERNAME, PASSWORD)
				.withAllResources()
				.withExceptionHandler(new ErrorHandler() {
					
					@Override
					public void handle(ErrorSource arg0, Throwable arg1, boolean arg2) {
						
					}
				})
				.build();
		try {
			skype.login();
			skype.subscribe();
			skype.getEventDispatcher().registerListener(new Listener() {
				@EventHandler
		        public void onContact(ContactRequestEvent e) {
					try {
						e.getRequest().accept();
						Chat chat = skype.getOrLoadChat("8:" + e.getRequest().getSender().getUsername());
						String message = "Hi!\n如果使用 Google 帳號，請先至 " + GoogleUserCredentialProvider.LOGIN_URL + 
								" 取得授權碼";
						sendMessage(chat, message);
					} catch (ConnectionException | ChatNotFoundException e1) {
						e1.printStackTrace();
					} 
				}
				
				@EventHandler
				public void onMessage(MessageEvent e){
					Chat chat = e.getChat();
					ChatMessage message = e.getMessage();
					String content = message.getContent().asPlaintext();
					System.out.println(content);
					if (!message.getSender().getId().equals("8:" + skype.getUsername()))	// 不分析自己的話
						analysis(chat, content);
				}
			});
		} catch (ConnectionException | NotParticipatingException | InvalidCredentialsException e) {
			e.printStackTrace();
		}
	}
	
	private static void sendMessage(Chat chat, String message){
		try {
			chat.sendMessage(message);
		} catch (ConnectionException e) {
			e.printStackTrace();
		}
	}
	
	private static void analysis(Chat chat, String content) {
		if (content == null || content.isEmpty())
			return;
		String message = "很抱歉，我聽不懂。\n如果使用 Google 帳號，請先至 " + 
			GoogleUserCredentialProvider.LOGIN_URL + 
			" 取得授權碼";
		String[] command = content.split(" ");
		try {
		switch (command[0].toLowerCase()) {
			case "code":
				if (command.length > 1) {
					GoApi.getInstance(chat).googleLogin(command[1]);
					message = "Google 帳號註冊完成";
				}
				break;
			
			case "ptc":
				if (command.length > 2) {
					GoApi.getInstance(chat).ptcLogin(command[1], command[2]);
					message = "PTC 帳號註冊完成";
				}
				break;
			
			case "location":
				if (command.length > 3) {
					GoApi.getInstance(chat).setLocation(command[1], command[2], command[3]);
					message = "已設定";
				}
				break;
			
			case "catchpokemon":
				GoApi.getInstance(chat).catchPokemon();
				message = "";
				break;
				
			case "pokestop":
				GoApi.getInstance(chat).findStop();
				message = "";
				break;
			
			case "list":
				PokemonGo go = GoApi.getInstance(chat).go;
				if (go == null) {
					message = "尚未註冊成功。";
				} else {
					go.getInventories().updateInventories();
					PokeBank bank = go.getInventories().getPokebank();
					for (Pokemon pokemon : bank.getPokemons()){
						message = pokemon.getPokemonId().getNumber() + "-" + 
							pokemon.getPokemonId().name() +
							" CP:" + pokemon.getCp() +
							" IV:" + pokemon.getIvRatio() * 100;
						sendMessage(chat, message);
					}
					message = "就這樣了。";
				}
				break;

			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		sendMessage(chat, message);
	}

	private static EventListener eventListener = new EventListener(){

		@Override
		public void onCaought(Chat chat, CatchablePokemon catchablePokemon, int cp) {
			String message = "抓到 " + catchablePokemon.getPokemonId().getNumber() + "-" +
					catchablePokemon.getPokemonId().name() + "(CP:" + cp + ")";
			sendMessage(chat, message);
		}

		@Override
		public void onFlee(Chat chat, CatchablePokemon catchablePokemon, int cp) {
			String message = "沒抓到 " + catchablePokemon.getPokemonId().getNumber() + "-" +
					catchablePokemon.getPokemonId().name() + "(CP:" + cp + ")";
			sendMessage(chat, message);
		}

		@Override
		public void onPokeStop(Chat chat, Pokestop pokestop) {
			try {
				sendMessage(chat, "經過 " + pokestop.getDetails().getDescription());
			} catch (LoginFailedException | RemoteServerException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onError(Chat chat, String errorMessage) {
			sendMessage(chat, errorMessage);
		}
		
	};
}
