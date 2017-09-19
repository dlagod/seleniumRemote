package es.ofca.test.jimena4.common.beans;
import java.io.Serializable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import es.ofca.test.jimena4.common.utils.UserDataDeserializer;
import es.ofca.test.jimena4.common.utils.UserDataSerializer;

/**
 * @author dlago
 *
 * Clase que contiene el usuario de acceso a la aplicación
 */
public class UserData implements Serializable {
	
	/**
	 * Identificador de la serialización
	 */
	private static final long serialVersionUID = 1L;

	
	/**
	 * Usuario de acceso a la aplicación
	 */
	private String userName = null;
	
	/**
	 * Contraseña del usuario
	 */
	private String password = null;
	
	
	/**
     * El constructor que tenemos asociado al elemento Gson.
     */
    private static final GsonBuilder gb = new GsonBuilder().setPrettyPrinting()
                                                           .registerTypeAdapter(UserData.class, new UserDataSerializer())
                                                           .registerTypeAdapter(UserData.class, new UserDataDeserializer());
    
	/**
	 * Devuelve el nombre del usuario
	 * @return Nombre del usuario
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Establece el Nombre del usuario
	 * @param userName Nombre del usuario
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * Devuelve la contraseña
	 * @return Contraseña
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Establece la contraseña
	 * @param password Contraseña
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Convierte el objeto UrgencyData a JSON
	 * @return Json del objeto UrgencyData
	 */
	public String toJSON() {
		Gson g = gb.create();
		return g.toJson(this);
	}

	/**
	 * Genera un objeto UrgencyData a partir de un objeto en formato JSON
	 * @param input Json a partir del cual se generar objeto UrgencyData
	 * @return Devuelve el objeto UrgencyData
	 */
	public static final UserData fromJSON(String input) {
		Gson g = gb.create();
		return g.fromJson(input, UserData.class);
	}
}
