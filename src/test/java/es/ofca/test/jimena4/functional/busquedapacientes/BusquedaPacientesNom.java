package es.ofca.test.jimena4.functional.busquedapacientes;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.AssertJUnit;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import es.ofca.test.jimena4.common.beans.UserData;
import es.ofca.test.jimena4.common.beans.UsersData;
import es.ofca.test.jimena4.common.beans.UsersData.Roles;
import es.ofca.test.jimena4.common.utils.DataProviderUtils;
import es.ofca.test.jimena4.functional.GeneralForm;
import es.ofca.test.jimena4.functional.helper.BusquedaPacientesHelper;

/**
 * Clase que permite filtrar pacientes por Nombre y Apellidos.
 * JIMEIV-1621
 * @author dlago
 */
public class BusquedaPacientesNom extends GeneralForm {
	private static final Logger LOGGER = Logger.getLogger(BusquedaPacientesNom.class.getName());
	

	
	/**
	 * JIMEIV-1621: Busqueda de pacientes por Nombre un perfil administrador
	 * @param userData Usuario de Acceso
	 * @throws Exception
	 */
	@Test(priority = 1, dataProvider = "accessAdministrador", groups = { "grupo.busqueda.filtro" })
	public void busquedaNombreAdm(UserData userData)
			throws Exception {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("BusquedaPacientesNom.busquedaNombreAdm - JIMEIV-1621");
		}
		try {
			busquedaNombre(userData);
		} catch (Exception e) {
			LOGGER.error("Se ha producido un error en BusquedaPacientesNom.busquedaNombreAdm", e);
			throw e;
		}
	}
	
	
	/**
	 * JIMEIV-1621: Busqueda de pacientes por Nombre un perfil facultativo
	 * @param userData Usuario de Acceso
	 * @throws Exception
	 */
	@Test(priority = 2, dataProvider = "accessFacultativo", groups = { "grupo.busqueda.filtro" })
	public void busquedaNombreFac(UserData userData)
			throws Exception {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("BusquedaPacientesNom.busquedaNombreFac - JIMEIV-1621");
		}
		try {
			busquedaNombre(userData);
		} catch (Exception e) {
			LOGGER.error("Se ha producido un error en BusquedaPacientesNom.busquedaNHCFac", e);
			throw e;
		}
	}
	
	private void busquedaNombre(UserData userData) throws Exception {
		login(userData.getUserName(), userData.getPassword());
		BusquedaPacientesHelper.accessMenuBusquedaPacientes(getDriver());
		filtroPacientesNombre();
		cerrarSesion();
	}
	
	/**
	 * Filtro por Nombre
	 * @throws Exception
	 */
	private void filtroPacientesNombre() throws Exception {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("BusquedaPacientesNom.filtroPacientesNombre");
		}
		
		//Nombre: N
		String nombre = "N";
		
		By name = By.id("formFiltroPacientes:nombre");
		untilClearElement(name);
		untilSendKeysElement(name, nombre);
		untilClickElement(By.id("formFiltroPacientes:btnFiltrar"));
		
		//Verificar el formato de la tabla
		BusquedaPacientesHelper.tablaBusquedaPacientesHead(getDriver());
		
		//Verificar si todos los nombres coinciden (Nombre situado en la segunda columna)
		By resultFilas = By.xpath("//tbody[@id='listado_busqueda_pacientes:tabla_busqueda_pacientes_data']/tr/td[2]");
		List<WebElement> registers =  untilFindElements(resultFilas);
				
		for (WebElement webElement : registers) {
			AssertJUnit.assertTrue("El nombre '" +  webElement.getText() + "' no contiene el filtro '" + nombre +"'", (webElement.getText().toUpperCase()).contains(nombre));
		}
		
		//comparar el orden de los registros
		BusquedaPacientesHelper.compareOrderRegisters(getDriver(), registers.size());
		
		//Revisar la páginación
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
