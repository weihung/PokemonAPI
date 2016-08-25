package com.sample.pokemonapi;

import java.util.Collection;
import java.util.List;

import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.device.DeviceInfo;
import com.pokegoapi.api.map.Map;
import com.pokegoapi.api.map.MapObjects;
import com.pokegoapi.api.map.fort.Pokestop;
import com.pokegoapi.api.map.pokemon.CatchResult;
import com.pokegoapi.api.map.pokemon.CatchablePokemon;
import com.pokegoapi.api.map.pokemon.encounter.EncounterResult;
import com.pokegoapi.auth.GoogleUserCredentialProvider;
import com.pokegoapi.auth.PtcCredentialProvider;
import com.pokegoapi.exceptions.AsyncPokemonGoException;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.NoSuchItemException;
import com.pokegoapi.exceptions.RemoteServerException;
import com.samczsun.skype4j.chat.Chat;

import okhttp3.OkHttpClient;

public class GoApi {
	private static GoApi goApi = null;
	public PokemonGo go;
	private DeviceInfo deviceInfo = new DeviceInfo();
	private Chat chat;
	
	private EventListener listener;
	public interface EventListener {
		void onCaought(Chat chat, CatchablePokemon catchablePokemon, int cp);
		void onFlee(Chat chat, CatchablePokemon catchablePokemon, int cp);
		void onPokeStop(Chat chat, Pokestop pokestop);
		void onError(Chat chat, String errorMessage);
	}
	
	private GoApi(EventListener eventListener){
		initDeviceInfo();
		this.listener = eventListener;
	}
	
	public static GoApi getInstance(EventListener eventListener){
		if (goApi == null) {
			goApi = new GoApi(eventListener);
		}
		return goApi;
	}
	
	public static GoApi getInstance(Chat chat){
		if (goApi != null)
			goApi.chat = chat;
		return goApi;
	}
	
	private void initDeviceInfo(){
//		deviceInfo.setDeviceId("1234abcd");	// Must Change
		deviceInfo.setAndroidBoardName("msm8916");
		deviceInfo.setAndroidBootloader("unknown");
		deviceInfo.setDeviceBrand("OPPO");
		deviceInfo.setDeviceModel("LMY47V");
		deviceInfo.setDeviceModelIdentifier("ubuntu-WX-122-176");
		deviceInfo.setDeviceModelBoot("qcom");
		deviceInfo.setHardwareManufacturer("OPPO");
		deviceInfo.setHardwareModel("F1f");
		deviceInfo.setFirmwareBrand("ubuntu-WX-122-176");
		deviceInfo.setFirmwareTags("release-keys");
		deviceInfo.setFirmwareType("user");
		deviceInfo.setFirmwareFingerprint("OPPO/F1f/F1f:5.1.1/LMY47V/1446204931:user/release-keys");
	}
	
	public void googleLogin(String authcode) {
		OkHttpClient http = new OkHttpClient();
//		try {
//			// TODO new GoogleUserCredentialProvider
//			
//			// TODO Provider login
//			
//			// new PokemonGO
//			
//			// go.setDeviceInfo(deviceInfo);
//		} catch (LoginFailedException | RemoteServerException | AsyncPokemonGoException e) {
//			e.printStackTrace();
//		}
	}
	
	public void ptcLogin(String username, String password) {
		OkHttpClient http = new OkHttpClient();
//		try {
//			// TODO new PtcCredentialProvider
//			
//			// new PokemonGO
//			
//			// go.setDeviceInfo(deviceInfo);
//		} catch (LoginFailedException | RemoteServerException | AsyncPokemonGoException e) {
//			e.printStackTrace();
//		}
	}
	
	public void setLocation(String latitude, String longitude, String altitude) {
		try {
			go.setLocation(Double.parseDouble(latitude), Double.parseDouble(longitude), Double.parseDouble(altitude));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setLocation(double latitude, double longitude, double altitude) {
		go.setLocation(latitude, longitude, altitude);
	}
	
	public void catchPokemon(){
		if (go == null) {
			if (listener != null) {
				listener.onError(chat, "尚未登入");
			}
			return;
		}
		
		// get map
		
//		try {
//			// get CatchablePokemon
//			
//			// EncounterResult a atchablePokemon
//			
//			// get cp
//			
//			// catchPokemon and get CatchResult
//			
//		} catch (LoginFailedException | RemoteServerException | NoSuchItemException e) {
//			e.printStackTrace();
//		}
	}
	
	public void findStop(){
		Map map = go.getMap();
//		try {
//			// get MapObjects
//			
//			// get Pokestops
//			
//			// if pokestop canLoot then loot
//		} catch (LoginFailedException | RemoteServerException e) {
//			e.printStackTrace();
//		}
	}
}
