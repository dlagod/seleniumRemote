package es.ofca.test.jimena4.functional.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.AssertJUnit;

import es.ofca.test.jimena4.common.constant.Constants;
import es.ofca.test.jimena4.common.selenium.WebDriverSelenium;
import es.ofca.test.jimena4.common.utils.PropertiesFile;
import es.ofca.test.jimena4.functional.beans.Patient;
import es.ofca.test.jimena4.functional.constant.FunctionalConstants;

/**
 * @author dlago
 * Contiene la lógica de negocio común del suite Busqueda Pacientes
 * JIMEIV-1618
 */
public final class BusquedaPacientesHelper {
	
	private static final Logger LOGGER = Logger.getLogger(BusquedaPacientesHelper.class.getName());
	
	private static final String LIMPIAR_PATIENTS = "patients.btnClear";
	private static final String TABLE_NHC = "patients.table.busqueda.head.nhc";
	private static final String TABLE_NOMBRE = "patients.table.busqueda.head.nombre";
	private static final String TABLE_APELLIDOS = "patients.table.busqueda.head.apellidos";
	private static final String TABLE_EDAD = "patients.table.busqueda.head.edad";
	private static final String TABLE_CIPA = "patients.table.busqueda.head.cipa";
	private static final String TABLE_SEXO = "patients.table.busqueda.head.sexo";
	private static final String TABLE_INFORM = "patients.table.busqueda.head.inform";
	private static final String TABLE_ACTIONS = "patients.table.busqueda.head.actions";


	public static void accessMenuBusquedaPacientes(WebDriver webdriver) throws Exception {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("BusquedaPacientesHelper.accessMenuBusquedaPacientes");
		}
		
		By locator = By.xpath("//*[local-name()='a'][div/div/h3/text() [contains(.,'"
				+ PropertiesFile.getValue(FunctionalConstants.PATIENTS) + "')]]");
		WebDriverSelenium.untilClickElement(webdriver, locator);
		
		//Se comprueba que es la página de busqueda de pacientes
		existfiltroPacientes(webdriver);
	}
	
	
	public static void accessButtonBusquedaPacientes(WebDriver webdriver) throws Exception {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("BusquedaPacientesHelper.accessButtonBusquedaPacientes");
		}
		
		//Lupa del menú de navegación
		By search = By.xpath("//*[local-name()='a'][@title='"
				+ PropertiesFile.getValue(FunctionalConstants.BUTTON_PATIENTS) + "']");
		WebDriverSelenium.untilClickElement(webdriver, search);
		
		//Se comprueba que es la página de busqueda de pacientes
		existfiltroPacientes(webdriver);
	}
	
	
	private static void existfiltroPacientes(WebDriver webdriver) throws Exception {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("BusquedaPacientesHelper.existfiltroPacientes");
		}
		
		By filtroPaciente = By.id("formFiltroPacientes");
		AssertJUnit.assertTrue(WebDriverSelenium.untilFindElement(webdriver, filtroPaciente).isDisplayed());
	}
	
	
	public static void clickButtonLimpiar(WebDriver webdriver) throws Exception {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("BusquedaPacientesHelper.clickButtonLimpiar");
		}
		
		//Botón Limpiar
		By clear = By.xpath("//button[contains(@id,'formFiltroPacientes:') and contains(span/text(),'"
				+ PropertiesFile.getValue(LIMPIAR_PATIENTS) + "')]");
		WebDriverSelenium.untilClickElement(webdriver, clear);
		
		//Se comrpuebasn todos los campos del fitro de búsqueda están vacios
		AssertJUnit.assertEquals(Constants.CADENA_VACIA, WebDriverSelenium.untilGetTextElement(webdriver, By.id("formFiltroPacientes:numerohc")));
		AssertJUnit.assertEquals(Constants.CADENA_VACIA, WebDriverSelenium.untilGetTextElement(webdriver, By.id("formFiltroPacientes:nombre")));
		AssertJUnit.assertEquals(Constants.CADENA_VACIA, WebDriverSelenium.untilGetTextElement(webdriver, By.id("formFiltroPacientes:apellido1")));
		AssertJUnit.assertEquals(Constants.CADENA_VACIA, WebDriverSelenium.untilGetTextElement(webdriver, By.id("formFiltroPacientes:apellido2")));
	}
	
	public static void tablaBusquedaPacientesHead(WebDriver webdriver) throws Exception {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("BusquedaPacientesHelper.tablaBusquedaPacientesHead");
		}
		
		//Se comprueban los campos de la tabla
		AssertJUnit.assertTrue(WebDriverSelenium.untilFindElement(webdriver, 
				By.xpath("//thead[@id='listado_busqueda_pacientes:tabla_busqueda_pacientes_head']/tr/th/span[text()='" + PropertiesFile.getValue(TABLE_NHC) + "']")).isDisplayed());
		AssertJUnit.assertTrue(WebDriverSelenium.untilFindElement(webdriver, 
				By.xpath("//thead[@id='listado_busqueda_pacientes:tabla_busqueda_pacientes_head']/tr/th/span[text()='" + PropertiesFile.getValue(TABLE_NOMBRE) + "']")).isDisplayed());
		AssertJUnit.assertTrue(WebDriverSelenium.untilFindElement(webdriver,
				By.xpath("//thead[@id='listado_busqueda_pacientes:tabla_busqueda_pacientes_head']/tr/th/span[text()='" + PropertiesFile.getValue(TABLE_APELLIDOS) + "']")).isDisplayed());
		AssertJUnit.assertTrue(WebDriverSelenium.untilFindElement(webdriver, 
				By.xpath("//thead[@id='listado_busqueda_pacientes:tabla_busqueda_pacientes_head']/tr/th/span[text()='" + PropertiesFile.getValue(TABLE_EDAD) + "']")).isDisplayed());
		AssertJUnit.assertTrue(WebDriverSelenium.untilFindElement(webdriver, 
				By.xpath("//thead[@id='listado_busqueda_pacientes:tabla_busqueda_pacientes_head']/tr/th/span[text()='" + PropertiesFile.getValue(TABLE_CIPA) + "']")).isDisplayed());
		AssertJUnit.assertTrue(WebDriverSelenium.untilFindElement(webdriver, 
				By.xpath("//thead[@id='listado_busqueda_pacientes:tabla_busqueda_pacientes_head']/tr/th/span[text()='" + PropertiesFile.getValue(TABLE_SEXO) + "']")).isDisplayed());
		AssertJUnit.assertTrue(WebDriverSelenium.untilFindElement(webdriver, 
				By.xpath("//thead[@id='listado_busqueda_pacientes:tabla_busqueda_pacientes_head']/tr/th/span[text()='" + PropertiesFile.getValue(TABLE_INFORM) + "']")).isDisplayed());
		AssertJUnit.assertTrue(WebDriverSelenium.untilFindElement(webdriver, 
				By.xpath("//thead[@id='listado_busqueda_pacientes:tabla_busqueda_pacientes_head']/tr/th/span[text()='" + PropertiesFile.getValue(TABLE_ACTIONS) + "']")).isDisplayed());
	}
	
	
	/**
	 * Compara si los rrgistros de la tabla "Busqueda pacientes" se muestran en el orden correcto
	 * @param webdriver Web Driver
	 * @param count Número de Registros Excepción producida
	 * @throws Exception
	 */
	public static void compareOrderRegisters(WebDriver webdriver, int count) throws Exception {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("BusquedaPacientesHelper.compareOrderRegisters");
		}

		// Se crea el Objeto para comparar el Orden de los registros
		List<Patient> listPatients = null;
		
		//Verificar solo los registros de la primera hoja.
		int max = maxPagRegisters(webdriver);
		if (max > count) {
			max = count;
		}
		
		
		if (max != 0) {

			// Se reserva memoria
			listPatients = new ArrayList<Patient>(max);
			for (int i = 0; i < count; i++) {
				Patient patient = new Patient();

				// nhc
				By nhc = By.xpath("//tbody[@id='listado_busqueda_pacientes:tabla_busqueda_pacientes_data']/tr[@data-ri=" + i + "]/td[1]");
				patient.setNhc(WebDriverSelenium.untilGetTextElement(webdriver, nhc));

				// nom
				By nom = By.xpath("//tbody[@id='listado_busqueda_pacientes:tabla_busqueda_pacientes_data']/tr[@data-ri=" + i + "]/td[2]");
				patient.setNombre(WebDriverSelenium.untilGetTextElement(webdriver, nom));

				// apellidos
				By apell = By
						.xpath("//tbody[@id='listado_busqueda_pacientes:tabla_busqueda_pacientes_data']/tr[@data-ri=" + i + "]/td[3]");
				patient.setApellidos(WebDriverSelenium.untilGetTextElement(webdriver, apell));

				listPatients.add(patient);
			}
		}

		List<Patient> listPatientsOrd = new ArrayList<Patient>(max);
		listPatientsOrd.addAll(listPatients);

		Collections.sort(listPatientsOrd);

		// Comprobar la Ordenación (Apellido, Nombre, NHC)
		AssertJUnit.assertTrue("El orden de los registros no es correcto",listPatients.equals(listPatientsOrd));
	}
	
	/**
	 * Devuleve el número de máximo de registros para la paginación
	 * @param webdriver
	 * @return
	 * @throws Exception
	 */
	public static int maxPagRegisters(WebDriver webdriver) throws Exception {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("BusquedaPacientesHelper.maxPagRegisters");
		}
		
		By count = By.xpath("//select[@id='listado_busqueda_pacientes:tabla_busqueda_pacientes_rppDD']/option[@selected='selected']");

		//Devuelve el número configurado del paginador (registros por página)
		return Integer.parseInt(WebDriverSelenium.untilGetTextElement(webdriver, count));
	}
}
