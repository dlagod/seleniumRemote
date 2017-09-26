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
import es.ofca.test.jimena4.common.utils.DataProviderUtils;
import es.ofca.test.jimena4.common.utils.PropertiesFile;
import es.ofca.test.jimena4.functional.GeneralForm;
import es.ofca.test.jimena4.functional.constant.FunctionalConstants;

/**
 * Clase que valida el cierre de la sessión
 * JIMEIV-720: Clase que valida el cierre de la sessión
 * 
 * @author dlago
 */
public class SessionClose extends GeneralForm {

	private static final Logger LOGGER = Logger.getLogger(SessionClose.class.getName());
	
	/**
	 * JIMEIV-720: Cerrar la sessión
	 * 
	 * @throws Exception
	 */
	@Test(dataProvider = "datosLoginOK", groups = { "grupo.login.ok" })
	public void sessionCloseCheck(UserData userData) throws Exception {
		
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("SessionClose.sessionCloseCheck - JIMEIV-720");
		}
		
		// Se accede a la aplicación
		login(userData.getUserName(), userData.getPassword());

		// Verificación del Login OK (Administrador)
		By userLocator = By.xpath("//li[@class='datos_usuario_conectado']");
		AssertJUnit.assertTrue(untilFindElement(userLocator).isDisplayed());
		
		// Se cierra la sessión
		cerrarSesion();
		
		//Verificar la pantalla de login
		By access = By.xpath("//*[local-name()='div'][@id='login']/form/h2[text()='"
				+ PropertiesFile.getValue(FunctionalConstants.ACCESS) + "']");
		
		AssertJUnit.assertTrue(untilFindElement(access).isDisplayed());
	}

	/* Parametros de un login OK */
	@DataProvider(name = "datosLoginOK")
	public Object[][] createDataLoginOK() {
		// Se recogen los usuarios
		UsersData.getInstance();
		Map<String, List<UserData>> mapUsers = UsersData.getMapUsers();

		// Rol de Administrador
		String[] roles = new String[] { Roles.ADMINISTRADOR.getValue() };
		Object[][] result = DataProviderUtils.createObjectMultiArray(mapUsers, 1, roles);

		return result;
	}


}
