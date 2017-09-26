package es.ofca.test.jimena4.functional.helper;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import es.ofca.test.jimena4.common.selenium.WebDriverSelenium;
import es.ofca.test.jimena4.common.utils.PropertiesFile;
import es.ofca.test.jimena4.functional.constant.FunctionalConstants;

/**
 * Clase que contiene los métodos comunes del módulo de Administracción
 * @author dlago
 */
public final class AdministrationHelper {
	
	private static final Logger LOGGER = Logger.getLogger(AdministrationHelper.class.getName());
	
	public static void accessMenuAdministracion(WebDriver webdriver) throws Exception {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("AdministrationHelper.accessMenuAdministracion");
		}
		
		// Se accede primero a la sessión de Administración
		By administration = By.xpath("//*[local-name()='a'][div/div/h3/text() [contains(.,'"
				+ PropertiesFile.getValue(FunctionalConstants.ADMINISTRATION) + "')]]");
		WebDriverSelenium.untilClickElement(webdriver, administration);
	}

}
