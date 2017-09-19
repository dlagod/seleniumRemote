package es.ofca.test.jimena4.common.utils;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import es.ofca.test.jimena4.common.beans.UserData;

/**
 * Clase que deserializa UserData; convierte el Json al Objecto DatosUrgencias
 * @author dlago
 *
 */
public class UserDataDeserializer implements JsonDeserializer<UserData>{

	/* (non-Javadoc)
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type, com.google.gson.JsonDeserializationContext)
	 */
	@Override
	public UserData deserialize(JsonElement input, Type type,
			JsonDeserializationContext context) throws JsonParseException {

		UserData userData = null;
		
		if (input != null) {
			if(!(input instanceof JsonObject)) throw new JsonParseException(input.getClass().toString());
			JsonObject jsonObject = (JsonObject) input;

			//Reservamos memoria
			userData = new UserData();
			
			String userName = (jsonObject.get("userName") != null ? jsonObject.get("userName").getAsString() : null);
			String password = (jsonObject.get("password") != null ? jsonObject.get("password").getAsString() : null);

			// Se añaden los atributos
			userData.setUserName(userName);
			userData.setPassword(password);
		}

		return userData;
	}
}
