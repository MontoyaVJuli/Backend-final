package co.edu.co.lilfac.dto;

import java.util.UUID;

import co.edu.co.lilfac.crosscutting.utilitarios.UtilObjeto;
import co.edu.co.lilfac.crosscutting.utilitarios.UtilTexto;
import co.edu.co.lilfac.crosscutting.utilitarios.UtilUUID;

public class ClienteDTO {
	private UUID id;
	private String nombre;
	private String apellido;
	private String cedula;
	private String telefono;
	private String correo;
	private String direccionResidencia;
	private CiudadDTO ciudad;
	
	public ClienteDTO () {
		setId(UtilUUID.obtenerValorDefecto());
		setNombre(UtilTexto.getInstance().obtenerValorDefecto());
		setApellido(UtilTexto.getInstance().obtenerValorDefecto());
		setCedula(UtilTexto.getInstance().obtenerValorDefecto());
		setTelefono(UtilTexto.getInstance().obtenerValorDefecto());
		setCorreo(UtilTexto.getInstance().obtenerValorDefecto());
		setDireccionResidencia(UtilTexto.getInstance().obtenerValorDefecto());
		setCiudad(CiudadDTO.obtenerValorDefecto());
	}
	
	public ClienteDTO(final UUID id) {
		setId(id);
		setNombre(UtilTexto.getInstance().obtenerValorDefecto());
		setApellido(UtilTexto.getInstance().obtenerValorDefecto());
		setCedula(UtilTexto.getInstance().obtenerValorDefecto());
		setTelefono(UtilTexto.getInstance().obtenerValorDefecto());
		setCorreo(UtilTexto.getInstance().obtenerValorDefecto());
		setDireccionResidencia(UtilTexto.getInstance().obtenerValorDefecto());
		setCiudad(CiudadDTO.obtenerValorDefecto());
	}
	
	public ClienteDTO (final UUID id, final String nombre, final String apellido, final String cedula, 
						final String telefono, final String correo, final String direccionResidencia, 
						final CiudadDTO ciudad) {
		
		setId(id);
		setNombre(nombre);
		setApellido(apellido);
		setCedula(cedula);
		setTelefono(telefono);
		setCorreo(correo);
		setDireccionResidencia(direccionResidencia);
		setCiudad(ciudad);
	}
	
	public static ClienteDTO obtenerValorDefecto() {
		return new ClienteDTO();
	}
	
	public static ClienteDTO obtenerValorDefecto(final ClienteDTO cliente ) {
		return UtilObjeto.getInstance().obtenerValorDefecto(cliente, obtenerValorDefecto());
	}

	public UUID getId() {
		return id;
	}

	public ClienteDTO setId(final UUID id) {
		this.id = UtilUUID.obtenerValorDefecto(id);
		return this;
	}

	public String getNombre() {
		return nombre;
	}

	public ClienteDTO setNombre(final String nombre) {
		this.nombre = UtilTexto.getInstance().quitarEspaciosBlancoInicioFin(nombre);
		return this;
	}

	public String getApellido() {
		return apellido;
	}

	public ClienteDTO setApellido(final String apellido) {
		this.apellido = UtilTexto.getInstance().quitarEspaciosBlancoInicioFin(apellido);
		return this;
	}

	public String getCedula() {
		return cedula;
	}

	public ClienteDTO setCedula(final String cedula) {
		this.cedula = UtilTexto.getInstance().quitarEspaciosBlancoInicioFin(cedula);
		return this;
	}

	public String getTelefono() {
		return telefono;
	}

	public ClienteDTO setTelefono(final String telefono) {
		this.telefono = UtilTexto.getInstance().quitarEspaciosBlancoInicioFin(telefono);
		return this;
	}

	public String getCorreo() {
		return correo;
	}

	public ClienteDTO setCorreo(final String correo) {
		this.correo = correo;
		return this;
	}
	
	public String getDireccionResidencia() {
		return direccionResidencia;
	}

	public ClienteDTO setDireccionResidencia(String direccionResidencia) {
		this.direccionResidencia = UtilTexto.getInstance().quitarEspaciosBlancoInicioFin(direccionResidencia);
		return this;
	}

	public CiudadDTO getCiudad() {
		return ciudad;
	}

	public ClienteDTO setCiudad(CiudadDTO ciudad) {
		this.ciudad = CiudadDTO.obtenerValorDefecto(ciudad);
		return this;
	}

}
