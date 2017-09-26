package es.ofca.test.jimena4.functional;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.AssertJUnit;

import es.ofca.test.jimena4.common.constant.Constants;
import es.ofca.test.jimena4.common.selenium.WebDriverSelenium;
import es.ofca.test.jimena4.common.utils.ConfigFile;
import es.ofca.test.jimena4.common.utils.PropertiesFile;



/**
 * Clase que contiene los métodos principales del formulario General de PSEL
 * 
 * @author dlago
 */
public class GeneralForm extends WebDriverSelenium {

	private static final Logger LOGGER = Logger.getLogger(GeneralForm.class.getName());
	
	public void login(String userName, String password)
			throws Exception {

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("GeneralForm.login - JIMEIV-718");
		}

		loginPage(userName, password);

		AssertJUnit.assertEquals(PropertiesFile.getValue(MESSAGE_lOGIN),
				untilGetTextElement(By.cssSelector("div.lado_derecho > div.h2")));
	}
	
	protected void loginPage(String userName, String password) 
			throws Exception {
		loginFormPage(userName,password);
		getDriver().findElement(By.name("submit")).click();
	}
	
	protected void loginFormPage(String userName, String password) 
			throws Exception {
		getDriver().get(ConfigFile.getValue(Constants.BASE_URL));
		
		// Completamos el usuario
		By user = By.id("username");
		untilClearElement(user);
		untilSendKeysElement(user, userName);
		
		//Completamos la password
		By pass = By.id("password");
		untilClearElement(pass);
		untilSendKeysElement(pass, password);
	}
	
	
	
	/**
	 * JIMEIV-720: Cierre de sesión
	 * @throws Exception Excepción producida
	 */
	protected void cerrarSesion() throws Exception {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("GeneralForm.cerrarSesion - JIMEIV-720");
		}
		//waitForLayout();
		
		By cerrar = By.cssSelector("img[title=\"" + PropertiesFile.getValue(BTN_CLOSE) + "\"]");
		clickWhenReady(cerrar);
		//untilClickElement(cerrar);
	}
	
	/**
	 * Espera a que desaparzca la capa de espera
	 * @throws Exception Excepción producida
	 */
	protected void waitForLayout() throws Exception {
		//Esperar hasta ocultar Capa Popup modal
		By capa = By.xpath("//*[local-name()='div'] [contains(@id,'_modal') and (not(contains(@id,'formModalPanels:'))) and (contains(@class,'ui-widget-overlay'))]");
		WebDriverWait wait = new WebDriverWait(getDriver(), Constants.TIME_OUT_SECONDS);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(capa));
	}

}
