package es.ofca.test.jimena4.functional.beans;

import java.io.Serializable;

/**
 * Bean que permite almacenar los datos de un paciente
 * @author dlago
 *
 */
public class Patient implements Serializable, Comparable<Patient> {

	private static final long serialVersionUID = 1L;
	
	private String nhc = null;
	private String nombre = null;
	private String apellidos = null;
	
	
	public String getNhc() {
		return nhc;
	}
	public void setNhc(String nhc) {
		this.nhc = nhc;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellidos() {
		return apellidos;
	}
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	
	@Override
	public int compareTo(Patient o) {
		if ((this.apellidos).compareTo(o.apellidos) < -1) {
			return -1;
		} else {
			if ((this.apellidos).compareTo(o.apellidos) == 0) {
				return this.nombre.compareTo(o.nombre);
			}  else {
				return 1;
			}
		}
	}
}
