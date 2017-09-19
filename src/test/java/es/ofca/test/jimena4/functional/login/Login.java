package es.ofca.test.jimena4.functional.login;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.testng.AssertJUnit;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import es.ofca.test.jimena4.common.beans.UserData;
import es.ofca.test.jimena4.common.beans.UsersData;
import es.ofca.test.jimena4.common.beans.UsersData.Roles;
import es.ofca.test.jimena4.common.constant.Constants;
import es.ofca.test.jimena4.common.selenium.WebDriverSelenium;
import es.ofca.test.jimena4.common.utils.DataProviderUtils;
import es.ofca.test.jimena4.common.utils.PropertiesFile;

/**
 * Clase que permite realizar las pruebas funcionales del subplan de pruebas:
 * JIMEIV-716: Acceso al sistema
 * @author dlago
 */
public class Login extends WebDriverSelenium{

	private static final Logger LOGGER = Logger.getLogger(Login.class.getName());
	private static final String MESSAGE_lOGIN_ERROR_PWD_BAD = "login.messageLoginErrorPasswordBad";

	/**
	 * JIMEIV-718: Identificación con credenciales usuario/contraseña 
	 * @throws Exception
	 */
	@Test(groups = { "grupo.login.error" })
	public void loginErroneoConUsuarioPasswordVacios() throws Exception {

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Login.loginErroneoConUsuarioPasswordVacios - JIMEIV-718");
		}

		loginPage(Constants.CADENA_VACIA, Constants.CADENA_VACIA);

		AssertJUnit.assertEquals(PropertiesFile.getValue(MESSAGE_lOGIN_ERROR_PWD_BAD),
				untilGetTextElement(By.id("msg")));
	}

	/**
	 * JIMEIV-718: Identificación con credenciales usuario/contraseña 
	 * @throws Exception
	 */
	@Test(dataProvider = "datosLoginError", groups = { "grupo.login.error" })
	public void loginErroneoConPasswordVacio(UserData userData) throws Exception {

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Login.loginErroneoConPasswordVacio - JIMEIV-718");
		}

		loginPage(userData.getUserName(), Constants.CADENA_VACIA);

		AssertJUnit.assertEquals(PropertiesFile.getValue(MESSAGE_lOGIN_ERROR_PWD_BAD),
				untilGetTextElement(By.id("msg")));
	}

	/**
	 * JIMEIV-718: Identificación con credenciales usuario/contraseña 
	 * @throws Exception
	 */
	@Test(dataProvider = "datosLoginError", groups = { "grupo.login.error" })
	public void loginErroneoConUsuarioVacio(UserData userData) throws Exception {

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Login.loginErroneoConUsuarioVacio - JIMEIV-718");
		}

		loginPage(Constants.CADENA_VACIA, userData.getPassword());

		AssertJUnit.assertEquals(PropertiesFile.getValue(MESSAGE_lOGIN_ERROR_PWD_BAD),
				untilGetTextElement(By.id("msg")));
	}

	/**
	 * JIMEIV-718: Identificación con credenciales usuario/contraseña 
	 * @throws Exception
	 */
	@Test(dataProvider = "datosLoginError", groups = { "grupo.login.error" })
	public void loginErroneoConPasswordErroneo(UserData userData)
			throws Exception {

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Login.loginErroneoConPasswordErroneo - JIMEIV-718");
		}

		loginPage(userData.getUserName(), userData.getPassword());

		AssertJUnit.assertEquals(PropertiesFile.getValue(MESSAGE_lOGIN_ERROR_PWD_BAD),
				untilGetTextElement(By.id("msg")));
	}
	
	/**
	 * JIMEIV-718: Identificación con credenciales usuario/contraseña 
	 * @throws Exception
	 */
	@Test(dataProvider = "datosLoginError", groups = {"grupo.login.error"})
	public void loginInicialLimpiarFormulario(UserData userData) throws Exception {
		if(LOGGER.isInfoEnabled()){
			LOGGER.info("Login.loginInicialLimpiarFormulario - JIMEIV-718");
		}
		
		loginFormPage(userData.getUserName(), userData.getPassword());
		
		By locator = By.name("reset");
		untilClickElement(locator);
		
		//El botón 'Limpiar' borrará las credenciales tecleadas por el usuario la primera vez.
		AssertJUnit.assertEquals(Constants.CADENA_VACIA, untilGetAttributeElement(By.id("username"), Constants.VALUE) + untilGetAttributeElement(By.id("password"), Constants.VALUE));
	}
	
	/**
	 * JIMEIV-718: Identificación con credenciales usuario/contraseña 
	 * @throws Exception
	 */
	@Test(dataProvider = "datosLoginError", groups = {"grupo.login.error"})
	public void loginErrorLimpiarFormulario(UserData userData) throws Exception {
		if(LOGGER.isInfoEnabled()){
			LOGGER.info("Login.loginErrorLimpiarFormulario - JIMEIV-718");
		}
		
		// Se establec el Loogin de la página
		loginPage(userData.getUserName(), userData.getPassword());
		
		// Se limpia el NIF
		By locator = By.name("reset");
		untilClickElement(locator);
				
		//Se verifica que se han limpiado ambos campos.
		AssertJUnit.assertEquals(Constants.CADENA_VACIA, untilGetAttributeElement(By.id("username"), Constants.VALUE));
		AssertJUnit.assertEquals(Constants.CADENA_VACIA, untilGetAttributeElement(By.id("password"), Constants.VALUE));
	}

	/**
	 * JIMEIV-718: Identificación con credenciales usuario/contraseña 
	 * @throws Exception
	 */
	@Test(dataProvider = "datosLoginOK", groups = { "grupo.login.ok" })
	public void loginOK(UserData userData) throws Exception {
		login(userData.getUserName(), userData.getPassword());
	}
	
	
	/* Parametros de un login OK*/
	@DataProvider(name = "datosLoginOK")
	public Object[][] createDataLoginOK() {
		//Se recogen los usuarios
		UsersData.getInstance();
		Map<String, List<UserData>> mapUsers = UsersData.getMapUsers();
		
		String[] roles =  new String[]{Roles.FACULTATIVO.getValue()};
		Object[][] result = DataProviderUtils.createObjectMultiArray(mapUsers, 2, roles);
		
		return result;
	}
	
	/* Parametros de un login Error*/
	@DataProvider(name = "datosLoginError")
	public Object[][] createDataLoginError() {
		//Se recogen los usuarios
		UsersData.getInstance();
		Map<String, List<UserData>> mapUsers = UsersData.getMapUsers();
		
		String[] roles =  new String[]{Roles.LOGIN_ERROR.getValue()};
		Object[][] result = DataProviderUtils.createObjectMultiArray(mapUsers, -1, roles);
		
		return result;
	}
}
