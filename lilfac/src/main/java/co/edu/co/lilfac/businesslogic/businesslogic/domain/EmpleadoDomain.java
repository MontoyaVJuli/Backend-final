package co.edu.co.lilfac.businesslogic.businesslogic.domain;

import java.util.UUID;

import co.edu.co.lilfac.crosscutting.utilitarios.UtilObjeto;
import co.edu.co.lilfac.crosscutting.utilitarios.UtilTexto;
import co.edu.co.lilfac.crosscutting.utilitarios.UtilUUID;

public class EmpleadoDomain {
	private UUID id;
	private String nombre;
	private String apellido;
	private String cedula;
	private String telefono;
	private String correo;
	private String direccionResidencia;
	private CiudadDomain ciudad;
	private EmpresaDomain empresa;
	
	EmpleadoDomain () {
		setId(UtilUUID.obtenerValorDefecto());
		setNombre(UtilTexto.getInstance().obtenerValorDefecto());
		setApellido(UtilTexto.getInstance().obtenerValorDefecto());
		setCedula(UtilTexto.getInstance().obtenerValorDefecto());
		setTelefono(UtilTexto.getInstance().obtenerValorDefecto());
		setCorreo(UtilTexto.getInstance().obtenerValorDefecto());
		setDireccionResidencia(UtilTexto.getInstance().obtenerValorDefecto());
		setCiudad(CiudadDomain.obtenerValorDefecto());
		setEmpresa(EmpresaDomain.obtenerValorDefecto());
	}
	
	public EmpleadoDomain (final UUID id, final String nombre, final String apellido, final String cedula, final String telefono,
						   final String correo, final String direccionResidencia, final CiudadDomain ciudad, final EmpresaDomain empresa) {
		setId(id);
		setNombre(nombre);
		setApellido(apellido);
		setCedula(cedula);
		setTelefono(telefono);
		setCorreo(correo);
		setDireccionResidencia(direccionResidencia);
		setCiudad(ciudad);
		setEmpresa(empresa);
		
	}
	
	static EmpleadoDomain obtenerValorDefecto() {
		return new EmpleadoDomain();
	}
	
	public static EmpleadoDomain obtenerValorDefecto(final EmpleadoDomain empleado ) {
		return UtilObjeto.getInstance().obtenerValorDefecto(empleado, obtenerValorDefecto());
	}

	public UUID getId() {
		return id;
	}

	private void setId(final UUID id) {
		this.id = UtilUUID.obtenerValorDefecto(id);
	}

	public String getNombre() {
		return nombre;
	}

	private void setNombre(final String nombre) {
		this.nombre = UtilTexto.getInstance().quitarEspaciosBlancoInicioFin(nombre);
	}

	public String getApellido() {
		return apellido;
	}

	private void setApellido(final String apellido) {
		this.apellido = UtilTexto.getInstance().quitarEspaciosBlancoInicioFin(apellido);
	}

	public String getCedula() {
		return cedula;
	}

	private void setCedula(final String cedula) {
		this.cedula = UtilTexto.getInstance().quitarEspaciosBlancoInicioFin(cedula);
	}

	public String getTelefono() {
		return telefono;
	}

	private void setTelefono(final String telefono) {
		this.telefono = UtilTexto.getInstance().quitarEspaciosBlancoInicioFin(telefono);
	}

	
	public String getCorreo() {
		return correo;
	}

	private void setCorreo(final String correo) {
		this.correo = correo;
	}
	
	public String getDireccionResidencia() {
		return direccionResidencia;
	}

	private void setDireccionResidencia(String direccionResidencia) {
		this.direccionResidencia = UtilTexto.getInstance().quitarEspaciosBlancoInicioFin(direccionResidencia);
	}

	public CiudadDomain getCiudad() {
		return ciudad;
	}

	private void setCiudad(CiudadDomain ciudad) {
		this.ciudad = CiudadDomain.obtenerValorDefecto(ciudad);
	}
	
	public EmpresaDomain getEmpresa() {
		return empresa;
	}

	private void setEmpresa(EmpresaDomain empresa) {
		this.empresa = EmpresaDomain.obtenerValorDefecto(empresa);
	}

}
