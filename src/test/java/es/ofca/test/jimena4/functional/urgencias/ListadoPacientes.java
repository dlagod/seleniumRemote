package es.ofca.test.jimena4.functional.urgencias;

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
 * Clase que permite realizar las pruebas funcionales de urgencias.
 * 
 * @author jluis
 */
public class ListadoPacientes extends GeneralForm {
	
	private static final Logger LOGGER = Logger.getLogger(ListadoPacientes.class.getName());
	
	private static final String MEDICAL = "urgency.medical";
		
	/**
	 * JIMEIV-722: Acceso por un facultativo/enfermera al área de Urgencias
	 * @param userData
	 * @throws Exception
	 */
	@Test(dataProvider = "datosUrgencias", groups = { "grupo.urgencias.filtro" })
	public void listadoFiltroPacientesDependDP(UserData userData)
			throws Exception {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Urgencias.listadoFiltroPacientesDependDP - JIMEIV-722");
		}
		try {
			login( userData.getUserName(), userData.getPassword());
			listadoFiltroPacientesDependDP();
			cerrarSesion();
		} catch (Exception e) {
			LOGGER.error("Se ha producido un error en ListadoPacientes.listadoFiltroPacientesDependDP", e);
			throw e;
		}
	}
	
	/**
	 * JIMEIV-744: Acceder al episodio de un paciente
	 * @param userData
	 * @throws Exception
	 */
	@Test(dataProvider = "datosUrgencias", groups = { "grupo.urgencias.filtro" })
	public void consultarHistoriaClinicaPacienteUrgenciasDP(UserData userData)
			throws Exception {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Urgencias.consultarHistoriaClinicaPacienteUrgenciasDP - JIMEIV-744");
		}
		try {
			login( userData.getUserName(), userData.getPassword());
			listadoFiltroPacientesDependDP();
			consultarHistoriaClinicaPacienteUrgencias();
			cerrarSesion();
		} catch (Exception e) {
			LOGGER.error("Se ha producido un error en ListadoPacientes.consultarHistoriaClinicaPacienteUrgenciasDP", e);
			throw e;
		}
	}
	
	private void listadoFiltroPacientesDependDP() throws Exception {

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("listadoFiltroPacientesDependDP");
		}
		
		By urgency = By.xpath("//*[local-name()='a'][div/div/h3/text() [contains(.,'"
				+ PropertiesFile.getValue(FunctionalConstants.URGENCY) + "')]]");
		untilClickElement(urgency);
		
		//Se espera por la capa de en Progreso
		waitForLayout();
		
		untilClickElement(By.xpath("//*[local-name()='fieldset'][@id='panelFiltro']/*[local-name()='legend']"));
		
		//Médico Adjunto
		By medical = By.xpath("//*[local-name()='li'][label/text() [contains(.,'"
				+ PropertiesFile.getValue(MEDICAL) + "')]]/div");
		
		String medicalId = untilGetAttributeElement(medical, "id");
		
		//Se despliega el select
		untilClickElement(By.id(medicalId+"_label"));
		/*
		
		By medical = By.xpath("//*[local-name()='li'][label/text() [contains(.,'"
				+ PropertiesFile.getValue(MEDICAL) + "')]]/div/div[contains(@class,'ui-selectonemenu-trigger')]/span");
		untilClickElement(medical);
		*/		
		untilSelectElement(By.xpath("//*[local-name()='ul'][@id='"+ medicalId +"_items']/*[local-name()='li']"),"M ANGELES CISNEROS MARTIN");
		
		//Botón de Filtrar
		untilClickElement(By.id("filtroListadoUrgencias:formFiltro:btnFiltrar"));
		
		//Se espera por la capa de en Progreso
		waitForLayout();
		
		AssertJUnit.assertTrue(
				untilFindElements(By.xpath("//*[local-name()='tbody'][@id='formListadoUrgencias:tablaListadoUrgencias_data']/*[local-name()='tr']"))
						.size() > 0);
		
	}
	
	private void consultarHistoriaClinicaPacienteUrgencias() throws Exception {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("consultarHistoriaClinicaPacienteUrgencias");
		}
		
		//seleccionar el nombre del primer paciente para entrar en su historia clínica
		untilClickElement(By.xpath("//*[local-name()='tbody'][@id='formListadoUrgencias:tablaListadoUrgencias_data']/*[local-name()='tr'][@data-ri='0']/*[local-name()='td'][*[local-name()='table'][@class='tablaDetallePaciente']]/table/tbody/tr/td/table/tbody/tr/td/a"));
	
		//Se espera por la capa de en Progreso
		waitForLayout();
				
		//Comprobar si se muestra la imagen del paciente
		AssertJUnit.assertTrue(untilFindElement(By.className("redondeadoImagentrue")).isDisplayed());
		
		//descargar informe clínico de Urgencias en PDF
		untilClickElement(By.id("componentePaciente:botonInforme:generarInformeAlta"));
		
		//Se comprueba el informe PDF
		AssertJUnit.assertTrue(untilFindElement(By.xpath("//*[local-name()='div'][@id='componentePaciente:botonInforme:generarInformeAlta_dlg']")).isDisplayed());
			
		//Cerrar el pop-pup
		untilClickElement(By.xpath("//*[local-name()='div'][@id='componentePaciente:botonInforme:generarInformeAlta_dlg']/*[local-name()='div']/*[local-name()='a']"));
		
		//visualizar interconsulta
		//getDriver().findElement(By.xpath("//table[@summary='Interconsultas']/tbody/tr/td[7]/button")).click();
		
		//comprobar petición de interconsulta
		//AssertJUnit.assertTrue(getDriver().findElement(By.xpath("//form[@id='componentePaciente:formDocumentoEHR']")).isDisplayed());
		
	}

	/* Parametros desde un proveedor de datos */
	@DataProvider(name = "datosUrgencias")
	public Object[][] createDataUrgencias() {

		//Se recogen los usuarios
		UsersData.getInstance();
		Map<String, List<UserData>> mapUsers = UsersData.getMapUsers();
		
		String[] roles =  new String[]{Roles.FACULTATIVO.getValue()};
		Object[][] result = DataProviderUtils.createObjectMultiArray(mapUsers, 2, roles);
		
		return result;
	}

}
