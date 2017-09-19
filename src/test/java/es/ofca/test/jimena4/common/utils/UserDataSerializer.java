package es.ofca.test.jimena4.common.utils;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import es.ofca.test.jimena4.common.beans.UserData;
/**
 * Bean que serializa el objeto UserData convertiendolo a JSON
 * <br>
 * @author dlago
 */
public class UserDataSerializer implements JsonSerializer<UserData>{


	/* (non-Javadoc)
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type, com.google.gson.JsonSerializationContext)
	 */
	@Override
	public JsonElement serialize(UserData userData, Type type,
			JsonSerializationContext context) {

		// Inicializamos la variable
		JsonObject jsonObject = null;

		if (userData != null) {
			// Simplemente añadimos los elementos que tenemos que codificar.
			jsonObject = new JsonObject();
		
			jsonObject.addProperty("userName", userData.getUserName());
			jsonObject.addProperty("password", userData.getPassword());
		}

		return jsonObject;
	}
}

