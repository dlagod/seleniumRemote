package es.ofca.test.jimena4.functional.busquedapacientes;

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
import es.ofca.test.jimena4.common.selenium.WebDriverSelenium;
import es.ofca.test.jimena4.common.utils.DataProviderUtils;
import es.ofca.test.jimena4.common.utils.PropertiesFile;

/**
 * Clase que permite realizar las pruebas funcionales de Busqueda de Pacientes.
 * 
 * @author jluis
 */
public class Busqueda extends WebDriverSelenium{
	private static final Logger LOGGER = Logger.getLogger(Busqueda.class.getName());
	
	private static String PATIENTS_NAME = "patients.moduleName";
	private static String PATIENTS_BTN_VIEW_PATIENS = "patients.btnViewPantient";

	/**
	 * JIMEIV-1619: Acceso Búsqueda Pacientes
	 * JIMEIV-1621: Filtrado de pacientes por Nombre y/o Apellidos	
	 * @param userData
	 * @throws Exception
	 */
	@Test(dataProvider = "datosBusqueda", groups = { "grupo.busqueda.filtro" })
	public void busquedaPacientesDP(UserData userData)
			throws Exception {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Busqueda.busquedaPacientesDP - JIMEIV-1619, JIMEIV-1621");
		}
		try {
			login(userData.getUserName(), userData.getPassword());
			busquedaPacientesDP();
			cerrarSesion();
		} catch (Exception e) {
			LOGGER.error("Se ha producido un error en Busqueda.busquedaPacientesDP", e);
			throw e;
		}
	}
	
	/**
	 * JIMEIV-1624: Acceso a un episodio abierto (urgencias)
	 * @param userData
	 * @throws Exception
	 */
	@Test(dataProvider = "datosBusqueda", groups = { "grupo.busqueda.filtro" })
	public void accesoEpisodioPacienteDP(UserData userData)
			throws Exception {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Busqueda.accesoEpisodioPacienteDP - JIMEIV-1624");
		}
		try {
			login(userData.getUserName(), userData.getPassword());
			busquedaPacientesDP();
			accesoEpisodioPacienteDP();
			cerrarSesion();
		} catch (Exception e) {
			LOGGER.error("Se ha producido un error en Busqueda.accesoEpisodioPacienteDP", e);
			throw e;
		}
	}

	private void busquedaPacientesDP() throws Exception {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("busquedaPacientesDP");
		}
		
		By locator = By.xpath("//*[local-name()='a'][div/div/h3/text() [contains(.,'"
				+ PropertiesFile.getValue(PATIENTS_NAME) + "')]]");
		untilClickElement(locator);
		
		By name = By.id("formFiltroPacientes:nombre");
		untilClearElement(name);
		untilSendKeysElement(name, "nom");
		untilClickElement(By.id("formFiltroPacientes:btnFiltrar"));
		
		AssertJUnit.assertTrue(
				driver.findElements(By.cssSelector("#panelPacientes_content > form > div.ui-datatable.ui-widget > div.ui-datatable-tablewrapper > table > tbody > tr"))
						.size() > 0);
	}
	
	private void accesoEpisodioPacienteDP() throws Exception {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("busquedaPacientesDP");
		}
		
		//Pulsar en la opción VER en las acciones de la búsqueda del primer paciente
		By patient = By.xpath("//*[local-name()='a'][@id [contains(.,'listado_busqueda_pacientes:tabla_busqueda_pacientes:0:')]]");
		untilClickElement(patient);
		
		//Indicar el motivo por el que se desea acceder al paciente: "Informática: resolución de incidencia"
		By selectLocator = By.id("formModalPanels:motivosSelect");
		untilSelectTextElement(selectLocator, "Informática: resolución de incidencia");
	
		/*
		//Se selecciona el motivo "Informática: resolución de incidencia"
		By selectOption = By.xpath("//select[@id='formModalPanels:motivosSelect']/option[normalize-space(text())='Informática: resolución de incidencia']");
		untilClickElement(selectOption);
		*/
	   
	    By comments = By.id("formModalPanels:comentariosInputText");
	    //Se selecciona el campo "Comentarios"
	    untilClearElement(comments);
	   
	    //Se introduce un texto de comentario
	    untilSendKeysElement(comments, "prueba");
	
	    //Pulsar el botón "Ver Paciente"	
	    By btnView = By.xpath("//*[local-name()='button'][*[local-name()='span']/text() [contains(.,'"
				+ PropertiesFile.getValue(PATIENTS_BTN_VIEW_PATIENS) + "')]]");
		
		//By btnView = By.xpath("//*[local-name()='button'][*[local-name()='span']/text() [contains(.,'Cancelar')]]");
	    untilClickElement(btnView);   
	    
	    //Capa de espera
	    waitForLayout();
	    
	    //Espera de recarga de página
	 	//getDriver().manage().timeouts().pageLoadTimeout(Constants.TIME_OUT_SECONDS, TimeUnit.SECONDS);
	    	
	 	//TODO
	    //Comprobar que se ha accedido al Episodio del Paciente
	    //AssertJUnit.assertTrue(getDriver().findElement(By.className("redondeadoImagentrue")).isDisplayed());
	}
	/* Parametros desde un proveedor de datos */
	@DataProvider(name = "datosBusqueda")
	public Object[][] createDataConsultas() {
		//Se recogen los usuarios
		UsersData.getInstance();
		Map<String, List<UserData>> mapUsers = UsersData.getMapUsers();
		
		String[] roles =  new String[]{Roles.FACULTATIVO.getValue()};
		Object[][] result = DataProviderUtils.createObjectMultiArray(mapUsers, 2, roles);
		
		return result;
	}

}
