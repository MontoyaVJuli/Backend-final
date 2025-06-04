package co.edu.co.lilfac.businesslogic.businesslogic.impl;

import java.util.List;
import java.util.UUID;

import co.edu.co.lilfac.businesslogic.businesslogic.EmpleadoBusinessLogic;
import co.edu.co.lilfac.businesslogic.businesslogic.assembler.empleado.entity.EmpleadoEntityAssembler;
import co.edu.co.lilfac.businesslogic.businesslogic.domain.EmpleadoDomain;
import co.edu.co.lilfac.crosscutting.excepciones.BusinessLogicLilfacException;
import co.edu.co.lilfac.crosscutting.excepciones.LilfacException;
import co.edu.co.lilfac.crosscutting.utilitarios.UtilCorreo;
import co.edu.co.lilfac.crosscutting.utilitarios.UtilTexto;
import co.edu.co.lilfac.crosscutting.utilitarios.UtilUUID;
import co.edu.co.lilfac.data.dao.factory.DAOFactory;
import co.edu.co.lilfac.entity.EmpleadoEntity;

public class EmpleadoBusinessLogicImpl implements EmpleadoBusinessLogic {

	private DAOFactory factory;
	
	
	public EmpleadoBusinessLogicImpl(DAOFactory factory) {
		this.factory = factory;
	}
	
	private void validarIntegridadInformacionRegistroEmpleado(EmpleadoDomain empleado) throws LilfacException {
		validarIntegridadNombreEmpleado(empleado.getNombre());
		validarIntegridadApellidoEmpleado(empleado.getApellido());
		validarIntegridadCedulaEmpleado(empleado.getCedula());
		validarIntegridadTelefonoEmpleado(empleado.getTelefono());
		validarIntegridadCorreoEmpleado(empleado.getCorreo());
		validarIntegridadDireccionResidenciaEmpleado(empleado.getDireccionResidencia());
		validarCiudadExistente(empleado.getCiudad().getId());
		validarEmpresaExistente(empleado.getEmpresa().getId());
		
	}

	private void validarIntegridadNombreEmpleado(String nombreEmpleado) throws LilfacException {
		if (UtilTexto.getInstance().esNula(nombreEmpleado)) {
			throw BusinessLogicLilfacException.reportar("El nombre del empleado no puede ser un valor nulo");
		}
		if (UtilTexto.getInstance().estaVacia(nombreEmpleado)) {
			throw BusinessLogicLilfacException.reportar("el nombre de el empleado es un campo obligatorio");
		}
		if (UtilTexto.getInstance().quitarEspaciosBlancoInicioFin(nombreEmpleado).length() > 100) {
			throw BusinessLogicLilfacException.reportar("el nombre de el empleado supera los 100 caracteres");
		}
		if (!UtilTexto.getInstance().ContieneSoloLetrasEspacios(nombreEmpleado)) {
			throw BusinessLogicLilfacException.reportar("el nombre de el empleado solo puede contener letras y espacios");
		}
	}
	
	private void validarIntegridadApellidoEmpleado(String apellidoCliente) throws LilfacException {
		if (UtilTexto.getInstance().esNula(apellidoCliente)) {
			throw BusinessLogicLilfacException.reportar("El apellido del cliente no puede ser un valor nulo");
		}
		if (UtilTexto.getInstance().estaVacia(apellidoCliente)) {
			throw BusinessLogicLilfacException.reportar("el apellido de el cliente es un campo obligatorio");
		}
		if (UtilTexto.getInstance().quitarEspaciosBlancoInicioFin(apellidoCliente).length() > 100) {
			throw BusinessLogicLilfacException.reportar("el apellido de el cliente supera los 100 caracteres");
		}
		if (!UtilTexto.getInstance().ContieneSoloLetrasEspacios(apellidoCliente)) {
			throw BusinessLogicLilfacException.reportar("el apellido de el cliente solo puede contener letras y espacios");
		}
	}
	
	private void validarIntegridadCedulaEmpleado(String cedulaCliente) throws LilfacException {
		if (UtilTexto.getInstance().esNula(cedulaCliente)) {
			throw BusinessLogicLilfacException.reportar("la cedula del cliente no puede ser un valor nulo");
		}
		if (UtilTexto.getInstance().estaVacia(cedulaCliente)) {
			throw BusinessLogicLilfacException.reportar("la cedula de el cliente es un campo obligatorio");
		}
	}
	
	private void validarIntegridadTelefonoEmpleado(String telefonoCliente) throws LilfacException {
		if (UtilTexto.getInstance().esNula(telefonoCliente)) {
			throw BusinessLogicLilfacException.reportar("El telefono del cliente no puede ser un valor nulo");
		}
		if (UtilTexto.getInstance().estaVacia(telefonoCliente)) {
			throw BusinessLogicLilfacException.reportar("el telefono de el cliente es un campo obligatorio");
		}
	}
	
	private void validarIntegridadCorreoEmpleado(String correoCliente) throws LilfacException {
		if (UtilTexto.getInstance().esNula(correoCliente)) {
			throw BusinessLogicLilfacException.reportar("El correo del cliente no puede ser un valor nulo");
		}
		if (UtilTexto.getInstance().estaVacia(correoCliente)) {
			throw BusinessLogicLilfacException.reportar("el correo de el cliente es un campo obligatorio");
		}
		if (UtilTexto.getInstance().quitarEspaciosBlancoInicioFin(correoCliente).length() > 320) {
			throw BusinessLogicLilfacException.reportar("el correo de el cliente supera los 320 caracteres");
		}
		if (!UtilCorreo.getInstance().esCorreoValido(correoCliente)) {
			throw BusinessLogicLilfacException.reportar("el correo ingresado no es de formato válido");
		}
	}
	
	private void validarIntegridadDireccionResidenciaEmpleado(String direccionResidenciaCliente) throws LilfacException {
		if (UtilTexto.getInstance().esNula(direccionResidenciaCliente)) {
			throw BusinessLogicLilfacException.reportar("la direccion del cliente no puede ser un valor nulo");
		}
		if (UtilTexto.getInstance().quitarEspaciosBlancoInicioFin(direccionResidenciaCliente).length() > 100) {
			throw BusinessLogicLilfacException.reportar("la direccion de el cliente supera los 100 caracteres");
		}
	}
	
	private void validarCiudadExistente(UUID idCiudad) throws LilfacException {
		if (UtilUUID.esValorDefecto(idCiudad)) {
			throw BusinessLogicLilfacException.reportar("El ID de la ciudad no es válido.");
		}

		var ciudad = factory.getCiudadDAO().listById(idCiudad);

		if (UtilUUID.esValorDefecto(ciudad.getId())) {
			throw BusinessLogicLilfacException.reportar("La ciudad no existe.");
		}
	}

	private void validarEmpresaExistente(UUID idEmpresa) throws LilfacException {
		if (UtilUUID.esValorDefecto(idEmpresa)) {
			throw BusinessLogicLilfacException.reportar("El ID de la empresa no es válido.");
		}

		var empresa = factory.getEmpresaDAO().listById(idEmpresa);

		if (UtilUUID.esValorDefecto(empresa.getId())) {
			throw BusinessLogicLilfacException.reportar("La empresa no existe.");
		}
	}
	
	private void validarNoExisteEmpleadoMismaCedula(String cedula) throws LilfacException {
		var filtro = new EmpleadoEntity();
		filtro.setCedula(cedula);
		var listaResultados = factory.getEmpleadoDAO().listByFilter(filtro);
		if (!listaResultados.isEmpty()) {
			throw BusinessLogicLilfacException.reportar("Ya existe un empleado con la cedula ingresada");
		}
	}
	
	private void validarNoExisteEmpleadoMismaCorreo(String correo) throws LilfacException {
		var filtro = new EmpleadoEntity();
		filtro.setCorreo(correo);
		var listaResultados = factory.getEmpleadoDAO().listByFilter(filtro);
		if (!listaResultados.isEmpty()) {
			throw BusinessLogicLilfacException.reportar("Ya existe un empleado con el correo ingresado.");
		}
	}
	
	private void validarEmpleadoExistente(UUID id) throws LilfacException {
		var filtro = new EmpleadoEntity();
		filtro.setId(id);
		var listaResultados = factory.getEmpleadoDAO().listByFilter(filtro);
		if (listaResultados.isEmpty()) {
			throw BusinessLogicLilfacException.reportar("no existe un empleado con el id ingresado");
		}
	}
	
	private UUID generarIdentificadorNuevoEmpleado() throws LilfacException {
		
		UUID nuevoId;
		var existeId = false;
		
		do {
			nuevoId = UtilUUID.generarNuevoUUID();
			var empleado = factory.getEmpleadoDAO().listById(nuevoId);
			existeId = !UtilUUID.esValorDefecto(empleado.getId());
		} while (existeId);
		
		return nuevoId;
	}

	
	public void registrarNuevoEmpleado(EmpleadoDomain empleado) throws LilfacException {
		validarIntegridadInformacionRegistroEmpleado(empleado);
		validarNoExisteEmpleadoMismaCedula(empleado.getCedula());
		validarNoExisteEmpleadoMismaCorreo(empleado.getCorreo());
		var id = generarIdentificadorNuevoEmpleado();
		var EmpleadoDomainACrear = new EmpleadoDomain(id, empleado.getNombre(), empleado.getApellido(), empleado.getCedula(), empleado.getTelefono(),
														  empleado.getCorreo(), empleado.getDireccionResidencia(), empleado.getCiudad(), empleado.getEmpresa());

		var empleadoEntity = EmpleadoEntityAssembler.getInstance().toEntity(EmpleadoDomainACrear);
		factory.getEmpleadoDAO().create(empleadoEntity);
	}

	
	public void modificarEmpleadoExistente(UUID id, EmpleadoDomain empleado) throws LilfacException {
		validarEmpleadoExistente(id);
		var empleadoEntity = EmpleadoEntityAssembler.getInstance().toEntity(empleado);
		factory.getEmpleadoDAO().update(id, empleadoEntity);
		validarIntegridadInformacionRegistroEmpleado(empleado);
		validarNoExisteEmpleadoMismaCedula(empleado.getCedula());
		validarNoExisteEmpleadoMismaCorreo(empleado.getCorreo());

	}

	
	public void darBajaDefinitivamenteEmpleadoExistente(UUID id) throws LilfacException {
		validarEmpleadoExistente(id);
		factory.getEmpleadoDAO().delete(id);
	}

	
	public EmpleadoDomain consultarEmpleadoPorId(UUID id) throws LilfacException {
		validarEmpleadoExistente(id);
		var empleadoEntity = factory.getEmpleadoDAO().listById(id);
		return EmpleadoEntityAssembler.getInstance().toDomain(empleadoEntity);
	}

	
	public List<EmpleadoDomain> consultarEmpleados(EmpleadoDomain filtro) throws LilfacException {
		
		var empleadoFilter = EmpleadoEntityAssembler.getInstance().toEntity(filtro);
		List<EmpleadoEntity> empleadoEntities = factory.getEmpleadoDAO().listByFilter(empleadoFilter);
		return EmpleadoEntityAssembler.getInstance().toDomain(empleadoEntities);
	}

}
