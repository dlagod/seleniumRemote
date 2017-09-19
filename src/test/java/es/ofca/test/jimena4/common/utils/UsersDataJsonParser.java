package es.ofca.test.jimena4.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import es.ofca.test.jimena4.common.beans.UserData;
import es.ofca.test.jimena4.common.beans.UsersData.Roles;

/**
 * Clase que serializa UserData; convierte el Json al mapa de usuarios
 * @author dlago
 *
 */
public final class UsersDataJsonParser {

	/**
	 * El subsistema de logs
	 */
	private static final Logger LOGGER = Logger.getLogger(UsersDataJsonParser.class);

	public static HashMap<String, List<UserData>> paserJsonToMap(JsonArray jsonElement){
		
		HashMap<String, List<UserData>> mapUsers = new HashMap<String, List<UserData>>();
		
		JsonArray jsonArray = (JsonArray) jsonElement;
		
		for (JsonElement element : jsonArray.getAsJsonArray()) {
		        JsonObject jsonLineItem = (JsonObject) element;
		        
		        // Se crea el Mapa de usuarios
		        if (jsonLineItem.get(Roles.ADMINISTRADOR.getValue()) != null) {
		        	mapUsers.put(Roles.ADMINISTRADOR.getValue(), createListUserData(jsonLineItem.get(Roles.ADMINISTRADOR.getValue())));
		        } else {
		        	  if (jsonLineItem.get(Roles.FACULTATIVO.getValue()) != null) {
		        		  mapUsers.put(Roles.FACULTATIVO.getValue(), createListUserData(jsonLineItem.get(Roles.FACULTATIVO.getValue())));
				      } else {
				    	  if (jsonLineItem.get(Roles.ENFERMERA.getValue()) != null) {
				    		  mapUsers.put(Roles.ENFERMERA.getValue(), createListUserData(jsonLineItem.get(Roles.ENFERMERA.getValue())));
					      } else {
					    	  if (jsonLineItem.get(Roles.RESIDENTES.getValue()) != null) {
					    		  mapUsers.put(Roles.RESIDENTES.getValue(), createListUserData(jsonLineItem.get(Roles.RESIDENTES.getValue())));
						      } else {
						    	  if (jsonLineItem.get(Roles.RESIDENTE.getValue()) != null) {
						    		  mapUsers.put(Roles.RESIDENTE.getValue(), createListUserData(jsonLineItem.get(Roles.RESIDENTE.getValue())));
							      } else {
							    	  if (jsonLineItem.get(Roles.LOGIN_ERROR.getValue()) != null) {
							    		  mapUsers.put(Roles.LOGIN_ERROR.getValue(), createListUserData(jsonLineItem.get(Roles.LOGIN_ERROR.getValue())));	
								      } else {
								    	  if (LOGGER.isInfoEnabled()) {
												LOGGER.info("No se ha cargado correctamente el siguiente Objecto" + jsonLineItem.toString());
											}
								      }
							      }
						      }
					      }
				      }
		        }
		}
		
		return mapUsers;
	}
	
	private static List<UserData> createListUserData(JsonElement jsonElement){
		
		List<UserData> listUserData = null;
		
		if (jsonElement != null) {
			listUserData = new ArrayList<UserData>();
			
			if (jsonElement instanceof JsonArray) {
				JsonArray jsonArray = (JsonArray) jsonElement;
				
				for (JsonElement element : jsonArray.getAsJsonArray()) {
					UserData userdata = UserData.fromJSON(element.toString());
					
					listUserData.add(userdata);
				}
			}
		}
		 
		return listUserData;
	}
}
