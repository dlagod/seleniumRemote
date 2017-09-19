package es.ofca.test.jimena4.functional.consultas;

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
 * Clase que permite realizar las pruebas funcionales de Consultas.
 * 
 * @author jluis
 */
public class Consultas extends WebDriverSelenium{
	private static final Logger LOGGER = Logger.getLogger(Consultas.class.getName());
	
	private static final String MESSAGE_AGENDA_NULL = "consultas.agendaNull";
	private static final String DIA_SEL = "consultas.diaSel";
	private static final String SERVICE_SEL = "consultas.serviceSel";
	private static final String CONSULTAS = "consultas.moduleName";

	/**
	 * JIMEIV-2444: Acceso Consultas	
	 * @param userData
	 * @throws Exception
	 */
	@Test(dataProvider = "datosConsultas", groups = { "grupo.consultas.filtro" })
	public void consultasAgendaNullDP(UserData userData)
			throws Exception {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Consultas.consultasAgendaNullDP - JIMEIV-2444");
		}

		try {
			login(userData.getUserName(), userData.getPassword());
			consultasAgendaNullDP();
			cerrarSesion();
		} catch (Exception e) {
			LOGGER.error("Se ha producido un error en Consultas.consultasAgendaNullDP", e);
			throw e;
		}
	}

	
	private void consultasAgendaNullDP() throws Exception {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("consultasAgendaNullDP");
		}
		
		By locator = By.xpath("//*[local-name()='a'][div/div/h3/text() [contains(.,'"
				+ PropertiesFile.getValue(CONSULTAS) + "')]]");
		untilClickElement(locator);
	
		AssertJUnit.assertEquals(PropertiesFile.getValue(MESSAGE_AGENDA_NULL),
				untilGetTextElement(By.cssSelector("#lista_busqueda_citas_data > tr.ui-datatable-empty-message > td")));
	}
	
	/* Parametros desde un proveedor de datos */
	@DataProvider(name = "datosConsultas")
	public Object[][] createDataConsultas() {
		//Se recogen los usuarios
		UsersData.getInstance();
		Map<String, List<UserData>> mapUsers = UsersData.getMapUsers();
		
		String[] roles =  new String[]{Roles.FACULTATIVO.getValue()};
		Object[][] result = DataProviderUtils.createObjectMultiArray(mapUsers, 2, roles);
		
		return result;
	}

}
