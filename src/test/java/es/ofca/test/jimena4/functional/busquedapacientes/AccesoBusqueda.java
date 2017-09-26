package es.ofca.test.jimena4.functional.busquedapacientes;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import es.ofca.test.jimena4.common.beans.UserData;
import es.ofca.test.jimena4.common.beans.UsersData;
import es.ofca.test.jimena4.common.beans.UsersData.Roles;
import es.ofca.test.jimena4.common.utils.DataProviderUtils;
import es.ofca.test.jimena4.functional.GeneralForm;
import es.ofca.test.jimena4.functional.helper.AdministrationHelper;
import es.ofca.test.jimena4.functional.helper.BusquedaPacientesHelper;

/**
 * Clase que permite acceder a la de Busqueda Pacientes.
 * JIMEIV-1619
 * @author dlago
 */
public class AccesoBusqueda extends GeneralForm {
	private static final Logger LOGGER = Logger.getLogger(AccesoBusqueda.class.getName());
	
	/**
	 * JIMEIV-1619: Acceso Búsqueda Pacientes
	 * @param userData
	 * @throws Exception
	 */
	@Test(dataProvider = "accessAdministrador", groups = { "grupo.busqueda.filtro" })
	public void accesoMenuBusquedaPacientes(UserData userData)
			throws Exception {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("AccesoBusqueda.accesoMenuBusquedaPacientes - JIMEIV-1619");
		}
		try {
			login(userData.getUserName(), userData.getPassword());
			BusquedaPacientesHelper.accessMenuBusquedaPacientes(getDriver());
			cerrarSesion();
		} catch (Exception e) {
			LOGGER.error("Se ha producido un error en AccesoBusqueda.accesoMenuBusquedaPacientes", e);
			throw e;
		}
	}
	
	
	/**
	 * JIMEIV-1619: Acceso Búsqueda Pacientes
	 * @param userData
	 * @throws Exception
	 */
	@Test(dataProvider = "accessAdministrador", groups = { "grupo.busqueda.filtro" })
	public void accesoButtonBusquedaPacientes(UserData userData)
			throws Exception {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("AccesoBusqueda.accesoButtonBusquedaPacientes - JIMEIV-1619");
		}
		try {
			login(userData.getUserName(), userData.getPassword());
			AdministrationHelper.accessMenuAdministracion(getDriver());
			BusquedaPacientesHelper.accessButtonBusquedaPacientes(getDriver());
			cerrarSesion();
		} catch (Exception e) {
			LOGGER.error("Se ha producido un error en AccesoBusqueda.accesoButtonBusquedaPacientes", e);
			throw e;
		}
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
}
