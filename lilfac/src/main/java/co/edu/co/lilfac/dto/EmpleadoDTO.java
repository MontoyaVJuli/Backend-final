package co.edu.co.lilfac.dto;

import java.util.UUID;

import co.edu.co.lilfac.crosscutting.utilitarios.UtilObjeto;
import co.edu.co.lilfac.crosscutting.utilitarios.UtilTexto;
import co.edu.co.lilfac.crosscutting.utilitarios.UtilUUID;

public class EmpleadoDTO {
	private UUID id;
	private String nombre;
	private String apellido;
	private String cedula;
	private String telefono;
	private String correo;
	private String direccionResidencia;
	private CiudadDTO ciudad;
	private EmpresaDTO empresa;
	
	public EmpleadoDTO () {
		setId(UtilUUID.obtenerValorDefecto());
		setNombre(UtilTexto.getInstance().obtenerValorDefecto());
		setApellido(UtilTexto.getInstance().obtenerValorDefecto());
		setCedula(UtilTexto.getInstance().obtenerValorDefecto());
		setTelefono(UtilTexto.getInstance().obtenerValorDefecto());
		setCorreo(UtilTexto.getInstance().obtenerValorDefecto());
		setDireccionResidencia(UtilTexto.getInstance().obtenerValorDefecto());
		setCiudad(CiudadDTO.obtenerValorDefecto());
		setEmpresa(EmpresaDTO.obtenerValorDefecto());
	}
	
	public EmpleadoDTO(final UUID id) {
		setId(id);
		setNombre(UtilTexto.getInstance().obtenerValorDefecto());
		setApellido(UtilTexto.getInstance().obtenerValorDefecto());
		setCedula(UtilTexto.getInstance().obtenerValorDefecto());
		setTelefono(UtilTexto.getInstance().obtenerValorDefecto());
		setCorreo(UtilTexto.getInstance().obtenerValorDefecto());
		setDireccionResidencia(UtilTexto.getInstance().obtenerValorDefecto());
		setCiudad(CiudadDTO.obtenerValorDefecto());
		setEmpresa(EmpresaDTO.obtenerValorDefecto());
	}
	
	public EmpleadoDTO (final UUID id, final String nombre, final String apellido,
						final String cedula, final String telefono, final String correo,
						final String direccionResidencia, final CiudadDTO ciudad, 
						final EmpresaDTO empresa) {
		
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
	
	public static EmpleadoDTO obtenerValorDefecto() {
		return new EmpleadoDTO();
	}
	
	public static EmpleadoDTO obtenerValorDefecto(final EmpleadoDTO empleado ) {
		return UtilObjeto.getInstance().obtenerValorDefecto(empleado, obtenerValorDefecto());
	}

	public UUID getId() {
		return id;
	}

	public EmpleadoDTO setId(final UUID id) {
		this.id = UtilUUID.obtenerValorDefecto(id);
		return this;
	}

	public String getNombre() {
		return nombre;
	}

	public EmpleadoDTO setNombre(final String nombre) {
		this.nombre = UtilTexto.getInstance().quitarEspaciosBlancoInicioFin(nombre);
		return this;
	}

	public String getApellido() {
		return apellido;
	}

	public EmpleadoDTO setApellido(final String apellido) {
		this.apellido = UtilTexto.getInstance().quitarEspaciosBlancoInicioFin(apellido);
		return this;
	}

	public String getCedula() {
		return cedula;
	}

	public EmpleadoDTO setCedula(final String cedula) {
		this.cedula = UtilTexto.getInstance().quitarEspaciosBlancoInicioFin(cedula);
		return this;
	}

	public String getTelefono() {
		return telefono;
	}

	public EmpleadoDTO setTelefono(final String telefono) {
		this.telefono = UtilTexto.getInstance().quitarEspaciosBlancoInicioFin(telefono);
		return this;
	}

	public String getCorreo() {
		return correo;
	}

	public EmpleadoDTO setCorreo(final String correo) {
		this.correo = correo;
		return this;
	}

	public String getDireccionResidencia() {
		return direccionResidencia;
	}

	public EmpleadoDTO setDireccionResidencia(String direccionResidencia) {
		this.direccionResidencia = UtilTexto.getInstance().quitarEspaciosBlancoInicioFin(direccionResidencia);
		return this;
	}

	public CiudadDTO getCiudad() {
		return ciudad;
	}

	public EmpleadoDTO setCiudad(CiudadDTO ciudad) {
		this.ciudad = CiudadDTO.obtenerValorDefecto(ciudad);
		return this;
	}
	
	public EmpresaDTO getEmpresa() {
		return empresa;
	}

	public EmpleadoDTO setEmpresa(EmpresaDTO empresa) {
		this.empresa = EmpresaDTO.obtenerValorDefecto(empresa);
		return this;
}
}
