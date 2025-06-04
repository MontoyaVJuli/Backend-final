package co.edu.co.lilfac.businesslogic.businesslogic.impl;

import java.util.List;
import java.util.UUID;

import co.edu.co.lilfac.businesslogic.businesslogic.ClienteBusinessLogic;
import co.edu.co.lilfac.businesslogic.businesslogic.assembler.cliente.entity.ClienteEntityAssembler;
import co.edu.co.lilfac.businesslogic.businesslogic.domain.ClienteDomain;
import co.edu.co.lilfac.crosscutting.excepciones.BusinessLogicLilfacException;
import co.edu.co.lilfac.crosscutting.excepciones.LilfacException;
import co.edu.co.lilfac.crosscutting.utilitarios.UtilCorreo;
import co.edu.co.lilfac.crosscutting.utilitarios.UtilTexto;
import co.edu.co.lilfac.crosscutting.utilitarios.UtilUUID;
import co.edu.co.lilfac.data.dao.factory.DAOFactory;
import co.edu.co.lilfac.entity.CiudadEntity;
import co.edu.co.lilfac.entity.ClienteEntity;
import co.edu.co.lilfac.entity.DepartamentoEntity;

public class ClienteBusinessLogicImpl implements ClienteBusinessLogic {

private DAOFactory factory;
	
	public ClienteBusinessLogicImpl(DAOFactory factory) {
		this.factory = factory;
	}
	
	private void validarIntegridadInformacionRegistroCliente(ClienteDomain cliente) throws LilfacException {
		validarIntegridadNombreCliente(cliente.getNombre());
		validarIntegridadApellidoCliente(cliente.getApellido());
		validarIntegridadCedulaCliente(cliente.getCedula());
		validarIntegridadTelefonoCliente(cliente.getTelefono());
		validarIntegridadCorreoCliente(cliente.getCorreo());
		validarIntegridadDireccionResidenciaCliente(cliente.getDireccionResidencia());
		validarDepartamentoExistente(cliente.getCiudad().getDepartamento().getNombre());
		validarCiudadExistente(cliente.getCiudad().getNombre());
	}
	
	private void validarIntegridadNombreCliente(String nombreCliente) throws LilfacException {
		if (UtilTexto.getInstance().esNula(nombreCliente)) {
			throw BusinessLogicLilfacException.reportar("El nombre del cliente no puede ser un valor nulo");
		}
		if (UtilTexto.getInstance().estaVacia(nombreCliente)) {
			throw BusinessLogicLilfacException.reportar("el nombre de el cliente es un campo obligatorio");
		}
		if (UtilTexto.getInstance().quitarEspaciosBlancoInicioFin(nombreCliente).length() > 100) {
			throw BusinessLogicLilfacException.reportar("el nombre de el cliente supera los 100 caracteres");
		}
		if (!UtilTexto.getInstance().ContieneSoloLetrasEspacios(nombreCliente)) {
			throw BusinessLogicLilfacException.reportar("el nombre de el cliente solo puede contener letras y espacios");
		}
	}
	
	private void validarIntegridadApellidoCliente(String apellidoCliente) throws LilfacException {
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
	
	private void validarIntegridadCedulaCliente(String cedulaCliente) throws LilfacException {
		if (UtilTexto.getInstance().esNula(cedulaCliente)) {
			throw BusinessLogicLilfacException.reportar("la cedula del cliente no puede ser un valor nulo");
		}
		if (UtilTexto.getInstance().estaVacia(cedulaCliente)) {
			throw BusinessLogicLilfacException.reportar("la cedula de el cliente es un campo obligatorio");
		}
	}
	
	private void validarIntegridadTelefonoCliente(String telefonoCliente) throws LilfacException {
		if (UtilTexto.getInstance().esNula(telefonoCliente)) {
			throw BusinessLogicLilfacException.reportar("El telefono del cliente no puede ser un valor nulo");
		}
		if (UtilTexto.getInstance().estaVacia(telefonoCliente)) {
			throw BusinessLogicLilfacException.reportar("el telefono de el cliente es un campo obligatorio");
		}
	}
	
	private void validarIntegridadCorreoCliente(String correoCliente) throws LilfacException {
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
	
	private void validarIntegridadDireccionResidenciaCliente(String direccionResidenciaCliente) throws LilfacException {
		if (UtilTexto.getInstance().esNula(direccionResidenciaCliente)) {
			throw BusinessLogicLilfacException.reportar("la direccion del cliente no puede ser un valor nulo");
		}
		if (UtilTexto.getInstance().quitarEspaciosBlancoInicioFin(direccionResidenciaCliente).length() > 100) {
			throw BusinessLogicLilfacException.reportar("la direccion de el cliente supera los 100 caracteres");
		}
	}
	
	private void validarCiudadExistente(String nombreCiudad) throws LilfacException {
		var filtro = new CiudadEntity();
		filtro.setNombre(UtilTexto.getInstance().quitarEspaciosBlancoInicioFin(nombreCiudad));
		
		var listaResultados = factory.getCiudadDAO().listByFilter(filtro);
		
		if (listaResultados.isEmpty()) {
		    throw BusinessLogicLilfacException.reportar("la ciudad no existe");
		}
	}
	
	private void validarDepartamentoExistente(String nombreDepartamento) throws LilfacException {
		var filtro = new DepartamentoEntity();
		filtro.setNombre(UtilTexto.getInstance().quitarEspaciosBlancoInicioFin(nombreDepartamento));
		
		var listaResultados = factory.getDepartamentoDAO().listByFilter(filtro);
		
		if (listaResultados.isEmpty()) {
		    throw BusinessLogicLilfacException.reportar("la ciudad no existe");
		}
	}
	
	private void validarNoExisteClienteMismaCedula(String cedula) throws LilfacException {
		var filtro = new ClienteEntity();
		filtro.setCedula(cedula);
		var listaResultados = factory.getClienteDAO().listByFilter(filtro);
		if (!listaResultados.isEmpty()) {
			throw BusinessLogicLilfacException.reportar("Ya existe un cliente con la cedula ingresada");
		}
	}
	
	private void validarClienteExistente(UUID id) throws LilfacException {
		var filtro = new ClienteEntity();
		filtro.setId(id);
		var listaResultados = factory.getClienteDAO().listByFilter(filtro);
		if (listaResultados.isEmpty()) {
			throw BusinessLogicLilfacException.reportar("no existe un cliente con el id ingresado");
		}
	}
	
	private UUID generarIdentificadorNuevoCliente() throws LilfacException {
		
		UUID nuevoId;
		var existeId = false;
		
		do {
			nuevoId = UtilUUID.generarNuevoUUID();
			var cliente = factory.getClienteDAO().listById(nuevoId);
			existeId = !UtilUUID.esValorDefecto(cliente.getId());
		} while (existeId);
		
		return nuevoId;
	}
	
	@Override
	public void registrarNuevoCliente(ClienteDomain cliente) throws LilfacException {
		validarIntegridadInformacionRegistroCliente(cliente);
		validarNoExisteClienteMismaCedula(cliente.getCedula());
		var id = generarIdentificadorNuevoCliente();
		var clienteDomainACrear = new ClienteDomain(id, cliente.getNombre(), cliente.getApellido(), cliente.getCedula(), cliente.getTelefono(), cliente.getCorreo(), cliente.getDireccionResidencia(), cliente.getCiudad());
		var clienteEntity = ClienteEntityAssembler.getInstance().toEntity(clienteDomainACrear);
		factory.getClienteDAO().create(clienteEntity);

	}

	@Override
	public void modificarClienteExistente(UUID id, ClienteDomain cliente) throws LilfacException {
		validarClienteExistente(id);
		var clienteEntity = ClienteEntityAssembler.getInstance().toEntity(cliente);
		factory.getClienteDAO().update(id, clienteEntity);
		validarIntegridadInformacionRegistroCliente(cliente);

	}

	@Override
	public void darBajaDefinitivamenteClienteExistente(UUID id) throws LilfacException {
		validarClienteExistente(id);
		factory.getClienteDAO().delete(id);
	}

	@Override
	public ClienteDomain consultarClientePorId(UUID id) throws LilfacException {
		validarClienteExistente(id);
		var clienteEntity = factory.getClienteDAO().listById(id);
		return ClienteEntityAssembler.getInstance().toDomain(clienteEntity);
	}

	@Override
	public List<ClienteDomain> consultarClientes(ClienteDomain filtro) throws LilfacException {
		
		var clienteFilter = ClienteEntityAssembler.getInstance().toEntity(filtro);
		List<ClienteEntity> clienteEntities = factory.getClienteDAO().listByFilter(clienteFilter);
		return ClienteEntityAssembler.getInstance().toDomain(clienteEntities);
	}

}
