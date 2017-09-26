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
import es.ofca.test.jimena4.common.utils.DataProviderUtils;
import es.ofca.test.jimena4.common.utils.PropertiesFile;
import es.ofca.test.jimena4.functional.GeneralForm;
import es.ofca.test.jimena4.functional.helper.BusquedaPacientesHelper;

/**
 * Clase que permite filtrar pacientes por NHC.
 * JIMEIV-1620
 * @author dlago
 */
public class BusquedaPacientesNHC extends GeneralForm {
	private static final Logger LOGGER = Logger.getLogger(BusquedaPacientesNHC.class.getName());
	
	
	private static final String MGS_ERROR_NHC = "patients.mgs.error.nhc";
	private static final String MGS_ERROR_MAX_RESULTS = "patients.mgs.error.max.result";
	
	/**
	 * JIMEIV-1620: Busqueda de pacientes por NHC un perfil Administrador
	 * @param userData Usuario de Acceso
	 * @throws Exception
	 */
	@Test(priority = 1, dataProvider = "accessAdministrador", groups = { "grupo.busqueda.filtro" })
	public void busquedaNHCAdm(UserData userData)
			throws Exception {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("BusquedaPacientesNHC.busquedaNHCAdm - JIMEIV-1620");
		}
		try {
			busquedaNHC(userData);
		} catch (Exception e) {
			LOGGER.error("Se ha producido un error en BusquedaPacientesNHC.busquedaNHCAdm", e);
			throw e;
		}
	}
	
	/**
	 * JIMEIV-1620: Busqueda de pacientes por NHC un perfil facultativo
	 * @param userData Usuario de Acceso
	 * @throws Exception
	 */
	@Test(priority = 2, dataProvider = "accessFacultativo", groups = { "grupo.busqueda.filtro" })
	public void busquedaNHCFac(UserData userData)
			throws Exception {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("BusquedaPacientesNHC.busquedaNHCFac - JIMEIV-1620");
		}
		try {
			busquedaNHC(userData);
		} catch (Exception e) {
			LOGGER.error("Se ha producido un error en BusquedaPacientesNHC.busquedaNHCFac", e);
			throw e;
		}
	}
	
	private void busquedaNHC(UserData userData) throws Exception {
		login(userData.getUserName(), userData.getPassword());
		BusquedaPacientesHelper.accessMenuBusquedaPacientes(getDriver());
		filtroPacientesNHC();
		cerrarSesion();
	}
	
	/**
	 * Filtro por NHC
	 * @throws Exception
	 */
	private void filtroPacientesNHC() throws Exception {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("BusquedaPacientesNHC.filtroPacientesNHC");
		}
		
		//Usuario: 724925 NORA	MARTINEZ ARROYO
		String nhc = "724925";
		
		By numerohc = By.id("formFiltroPacientes:numerohc");
		untilClearElement(numerohc);
		untilSendKeysElement(numerohc, nhc);
		untilClickElement(By.id("formFiltroPacientes:btnFiltrar"));
		
		//Verificar el formato de la tabla
		BusquedaPacientesHelper.tablaBusquedaPacientesHead(getDriver());
		
		// Solo hay un único resultado
		AssertJUnit.assertTrue(
				untilFindElements(By.cssSelector("#panelPacientes_content > form > div.ui-datatable.ui-widget > div.ui-datatable-tablewrapper > table > tbody > tr"))
						.size() == 1);
		
		//La primera columna de la tabla es el NHC
		By NHCLocator = By.xpath("//tbody[@id='listado_busqueda_pacientes:tabla_busqueda_pacientes_data']/tr[@data-ri='0']/td");
		
		//Se comprueba que busque correctamente
		AssertJUnit.assertEquals(nhc, untilGetTextElement(NHCLocator));
	}
	
	
	/**
	 * JIMEIV-1620:NHC Númerico
	 * @param userData Usuario de Acceso
	 * @throws Exception
	 */
	@Test(priority = 3, dataProvider = "accessFacultativo", groups = { "grupo.busqueda.filtro" })
	public void busquedaNHCErrorNumber(UserData userData)
			throws Exception {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("BusquedaPacientesNHC.busquedaNHCErrorNumber - JIMEIV-1620");
		}
		try {
			login(userData.getUserName(), userData.getPassword());
			BusquedaPacientesHelper.accessMenuBusquedaPacientes(getDriver());
			filtroPacientesNHCErrorNumber();
			cerrarSesion();
		} catch (Exception e) {
			LOGGER.error("Se ha producido un error en BusquedaPacientesNHC.busquedaNHCErrorNumber", e);
			throw e;
		}
	}
	
	
	/**
	 * Filtro por NHC Not Númerico
	 * @throws Exception
	 */
	private void filtroPacientesNHCErrorNumber() throws Exception {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("BusquedaPacientesNHC.filtroPacientesNHCErrorNumber");
		}
		
		String nhc = "XXX";
		
		By numerohc = By.id("formFiltroPacientes:numerohc");
		untilClearElement(numerohc);
		untilSendKeysElement(numerohc, nhc);
		untilClickElement(By.id("formFiltroPacientes:btnFiltrar"));
		
		//Mensaje de error
		By message = By.xpath("//div[@class='ui-growl-message']/span[@class='ui-growl-title']");
		
		//Coincide el mensaje de error
		AssertJUnit.assertEquals(PropertiesFile.getValue(MGS_ERROR_NHC), untilGetTextElement(message));
	}
	
	
	/**
	 * JIMEIV-1620: Número máximo de usuario
	 * @param userData Usuario de Acceso
	 * @throws Exception
	 */
	@Test(priority = 4, dataProvider = "accessFacultativo", groups = { "grupo.busqueda.filtro" })
	public void busquedaErrorMaxResult(UserData userData)
			throws Exception {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("BusquedaPacientesNHC.busquedaErrorMaxResult - JIMEIV-1620");
		}
		try {
			login(userData.getUserName(), userData.getPassword());
			BusquedaPacientesHelper.accessMenuBusquedaPacientes(getDriver());
			filtroPacientesErrorMaxResult();
			cerrarSesion();
		} catch (Exception e) {
			LOGGER.error("Se ha producido un error en BusquedaPacientesNHC.busquedaErrorMaxResult", e);
			throw e;
		}
	}
	
	
	/**
	 * Número máximo de usuario
	 * @throws Exception
	 */
	private void filtroPacientesErrorMaxResult() throws Exception {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("BusquedaPacientesNHC.filtroPacientesErrorMaxResult");
		}
		
		//Se borra el filtro
		BusquedaPacientesHelper.clickButtonLimpiar(getDriver());
		
		//Se filtra con todos los campos
		untilClickElement(By.id("formFiltroPacientes:btnFiltrar"));
		
		//Mensaje de error
		By message = By.xpath("//div[@class='ui-growl-message']/span[@class='ui-growl-title']");
				
		//Coincide el mensaje de error
		AssertJUnit.assertEquals(PropertiesFile.getValue(MGS_ERROR_MAX_RESULTS), untilGetTextElement(message));
	}
	
	
	/* Acceso con perfil Administrador */
	@DataProvider(name = "accessAdministrador")
	public Object[][] createDataAdministrador() {
		//Se recogen los usuarios
		UsersData.getInstance();
		Map<String, List<UserData>> mapUsers = UsersData.getMapUsers();
		
		String[] roles =  new String[]{Roles.ADMINISTRADOR.getValue()};
		Object[][] result = DataProviderUtils.createObjectMultiArray(mapUsers, 1, roles);
		
		return result;
	}
	
	/* Acceso con Perfil Facultativo */
	@DataProvider(name = "accessFacultativo")
	public Object[][] createDataFacultativo() {
		//Se recogen los usuarios
		UsersData.getInstance();
		Map<String, List<UserData>> mapUsers = UsersData.getMapUsers();
		
		String[] roles =  new String[]{Roles.FACULTATIVO.getValue()};
		Object[][] result = DataProviderUtils.createObjectMultiArray(mapUsers, 1, roles);
		
		return result;
	}

}
