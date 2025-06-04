package co.edu.co.lilfac.businesslogic.facade.impl;

import java.util.List;
import java.util.UUID;

import co.edu.co.lilfac.businesslogic.businesslogic.ClienteBusinessLogic;
import co.edu.co.lilfac.businesslogic.businesslogic.assembler.cliente.dto.ClienteDTOAssembler;
import co.edu.co.lilfac.businesslogic.businesslogic.domain.ClienteDomain;
import co.edu.co.lilfac.businesslogic.businesslogic.impl.ClienteBusinessLogicImpl;
import co.edu.co.lilfac.businesslogic.facade.ClienteFacade;
import co.edu.co.lilfac.crosscutting.excepciones.BusinessLogicLilfacException;
import co.edu.co.lilfac.crosscutting.excepciones.LilfacException;
import co.edu.co.lilfac.data.dao.factory.DAOFactory;
import co.edu.co.lilfac.data.dao.factory.Factory;
import co.edu.co.lilfac.dto.ClienteDTO;

public class ClienteFacadeImpl implements ClienteFacade{

	private DAOFactory daoFactory;
	private ClienteBusinessLogic clienteBusinessLogic;
	
	public ClienteFacadeImpl() throws LilfacException {
		daoFactory = DAOFactory.getFactory(Factory.POSTGRE_SQL);
		clienteBusinessLogic = new ClienteBusinessLogicImpl(daoFactory);
	} 
	
	
	public void registrarNuevoCliente(ClienteDTO cliente) throws LilfacException {
		
		try {
			daoFactory.abrirConexion();
			daoFactory.iniciarTransaccion();
			ClienteDomain clienteDomain = ClienteDTOAssembler.getInstance().toDomain(cliente);
			clienteBusinessLogic.registrarNuevoCliente(clienteDomain);
			daoFactory.confirmarTransaccion();
		
		} catch (LilfacException exception) {
			daoFactory.cancelarTransaccion();
			throw exception;
		
		} catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de registrar la información de un nuevo cliente";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de registrar la información de un nuevo cliente";
			
			throw BusinessLogicLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		
		}finally {
			daoFactory.cerrarConexion();
		}
	}

	
	public void modificarClienteExistente(UUID id, ClienteDTO cliente) throws LilfacException {
		
		try {
			daoFactory.abrirConexion();
			daoFactory.iniciarTransaccion();
			ClienteDomain clienteDomain = ClienteDTOAssembler.getInstance().toDomain(cliente);
			clienteBusinessLogic.modificarClienteExistente(id, clienteDomain);
			daoFactory.confirmarTransaccion();
		
		} catch (LilfacException exception) {
			daoFactory.cancelarTransaccion();
			throw exception;
		
		} catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de modificar la información de el cliente con el identificador ingresado";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de modificar la información de el cliente con el identificador ingresado";
			
			throw BusinessLogicLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		
		}finally {
			daoFactory.cerrarConexion();
		}
	}

	
	public void darBajaDefinitivamenteClienteExistente(UUID id) throws LilfacException {
		
		try {
			daoFactory.abrirConexion();
			daoFactory.iniciarTransaccion();
			clienteBusinessLogic.darBajaDefinitivamenteClienteExistente(id);
			daoFactory.confirmarTransaccion();
		
		} catch (LilfacException exception) {
			daoFactory.cancelarTransaccion();
			throw exception;
		
		} catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de borrar la información de el cliente con el identificador ingresado";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de borrar la información de el cliente con el identificador ingresado";
			
			throw BusinessLogicLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		
		}finally {
			daoFactory.cerrarConexion();
		}
	}

	
	public ClienteDTO consultarClientePorId(UUID id) throws LilfacException {
		
		try {
			var clienteDomainResultado = clienteBusinessLogic.consultarClientePorId(id);
			return ClienteDTOAssembler.getInstance().toDto(clienteDomainResultado);
		
		} catch (LilfacException exception) {
			daoFactory.cancelarTransaccion();
			throw exception;
		
		} catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de consultar la información de un cliente con el id deseado";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de consultar la informacion de un cliente con el id ingresado";
			
			throw BusinessLogicLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		
		}finally {
			daoFactory.cerrarConexion();
		}
		
	}

	
	public List<ClienteDTO> consultarClientes(ClienteDTO filtro) throws LilfacException {
		
		try {
			var clienteFilter = ClienteDTOAssembler.getInstance().toDomain(filtro);
			List<ClienteDomain> clientesDomain = clienteBusinessLogic.consultarClientes(clienteFilter);
			return ClienteDTOAssembler.getInstance().toDto(clientesDomain);
		
		} catch (LilfacException exception) {
			daoFactory.cancelarTransaccion();
			throw exception;
		
		} catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de consultar la información de los clientes";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de consultar la informacion de los clientes";
			
			throw BusinessLogicLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}
		
	}

}
