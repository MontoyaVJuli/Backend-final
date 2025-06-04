package co.edu.co.lilfac.entity;

import java.util.UUID;

import co.edu.co.lilfac.crosscutting.utilitarios.UtilObjeto;
import co.edu.co.lilfac.crosscutting.utilitarios.UtilTexto;
import co.edu.co.lilfac.crosscutting.utilitarios.UtilUUID;

public class EmpleadoEntity {
	private UUID id;
	private String nombre;
	private String apellido;
	private String cedula;
	private String telefono;
	private String correo;
	private String direccionResidencia;
	private CiudadEntity ciudad;
	private EmpresaEntity empresa;
	private boolean objetoVacio;
	
	public EmpleadoEntity () {
		setId(UtilUUID.obtenerValorDefecto());
		setNombre(UtilTexto.getInstance().obtenerValorDefecto());
		setApellido(UtilTexto.getInstance().obtenerValorDefecto());
		setCedula(UtilTexto.getInstance().obtenerValorDefecto());
		setTelefono(UtilTexto.getInstance().obtenerValorDefecto());
		setCorreo(UtilTexto.getInstance().obtenerValorDefecto());
		setDireccionResidencia(UtilTexto.getInstance().obtenerValorDefecto());
		setCiudad(CiudadEntity.obtenerValorDefecto());
		setEmpresa(EmpresaEntity.obtenerValorDefecto());
	}
	
	public EmpleadoEntity(final UUID id) {
		setId(id);
		setNombre(UtilTexto.getInstance().obtenerValorDefecto());
		setApellido(UtilTexto.getInstance().obtenerValorDefecto());
		setCedula(UtilTexto.getInstance().obtenerValorDefecto());
		setTelefono(UtilTexto.getInstance().obtenerValorDefecto());
		setCorreo(UtilTexto.getInstance().obtenerValorDefecto());
		setDireccionResidencia(UtilTexto.getInstance().obtenerValorDefecto());
		setCiudad(CiudadEntity.obtenerValorDefecto());
		setEmpresa(EmpresaEntity.obtenerValorDefecto());
	}
	
	public EmpleadoEntity (final UUID id, final String nombre, final String apellido,
			final String cedula, final String telefono, final String correo,
			final String direccionResidencia, final CiudadEntity ciudad, 
			final EmpresaEntity empresa) {
		
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
	
	public static EmpleadoEntity obtenerValorDefecto() {
		return new EmpleadoEntity();
	}
	
	public static EmpleadoEntity obtenerValorDefecto(final EmpleadoEntity empleado ) {
		return UtilObjeto.getInstance().obtenerValorDefecto(empleado, obtenerValorDefecto());
	}

	public UUID getId() {
		return id;
	}

	public void setId(final UUID id) {
		this.id = UtilUUID.obtenerValorDefecto(id);
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(final String nombre) {
		this.nombre = UtilTexto.getInstance().quitarEspaciosBlancoInicioFin(nombre);
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(final String apellido) {
		this.apellido = UtilTexto.getInstance().quitarEspaciosBlancoInicioFin(apellido);
	}

	public String getCedula() {
		return cedula;
	}

	public void setCedula(final String cedula) {
		this.cedula = UtilTexto.getInstance().quitarEspaciosBlancoInicioFin(cedula);
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(final String telefono) {
		this.telefono = UtilTexto.getInstance().quitarEspaciosBlancoInicioFin(telefono);
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(final String correo) {
		this.correo = correo;
	}
	
	public String getDireccionResidencia() {
		return direccionResidencia;
	}

	public void setDireccionResidencia(String direccionResidencia) {
		this.direccionResidencia = UtilTexto.getInstance().quitarEspaciosBlancoInicioFin(direccionResidencia);
	}

	public CiudadEntity getCiudad() {
		return ciudad;
	}

	public void setCiudad(CiudadEntity ciudad) {
		this.ciudad = CiudadEntity.obtenerValorDefecto(ciudad);
	}
	
	public EmpresaEntity getEmpresa() {
		return empresa;
	}

	public void setEmpresa(EmpresaEntity empresa) {
		this.empresa = EmpresaEntity.obtenerValorDefecto(empresa);
	}
	
	
	public boolean isObjetoVacio() {
		return objetoVacio;
	}

}
