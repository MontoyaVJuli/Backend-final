package co.edu.co.lilfac.businesslogic.facade.impl;

import java.util.List;
import java.util.UUID;

import co.edu.co.lilfac.businesslogic.businesslogic.EmpleadoBusinessLogic;
import co.edu.co.lilfac.businesslogic.businesslogic.assembler.empleado.dto.EmpleadoDTOAssembler;
import co.edu.co.lilfac.businesslogic.businesslogic.domain.EmpleadoDomain;
import co.edu.co.lilfac.businesslogic.businesslogic.impl.EmpleadoBusinessLogicImpl;
import co.edu.co.lilfac.businesslogic.facade.EmpleadoFacade;
import co.edu.co.lilfac.crosscutting.excepciones.BusinessLogicLilfacException;
import co.edu.co.lilfac.crosscutting.excepciones.LilfacException;
import co.edu.co.lilfac.data.dao.factory.DAOFactory;
import co.edu.co.lilfac.data.dao.factory.Factory;
import co.edu.co.lilfac.dto.EmpleadoDTO;

public class EmpleadoFacadeImpl implements EmpleadoFacade{

	private DAOFactory daoFactory;
	private EmpleadoBusinessLogic empleadoBusinessLogic;
	
	public EmpleadoFacadeImpl() throws LilfacException {
		daoFactory = DAOFactory.getFactory(Factory.POSTGRE_SQL);
		empleadoBusinessLogic = new EmpleadoBusinessLogicImpl(daoFactory);
	} 
	
	public void registrarNuevoEmpleado(EmpleadoDTO empleado) throws LilfacException {
		
		try {
			daoFactory.abrirConexion();
			daoFactory.iniciarTransaccion();
			EmpleadoDomain empleadoDomain = EmpleadoDTOAssembler.getInstance().toDomain(empleado);
			empleadoBusinessLogic.registrarNuevoEmpleado(empleadoDomain);
			daoFactory.confirmarTransaccion();
			
		} catch (LilfacException exception) {
			daoFactory.cancelarTransaccion();
			throw exception;
			
		} catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de registrar la información de un nuevo empleado";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de registrar la información de un nuevo empleado";
			
			throw BusinessLogicLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		
		}finally {
			daoFactory.cerrarConexion();
		}
	}


	public void modificarEmpleadoExistente(UUID id, EmpleadoDTO empleado) throws LilfacException {
		
		try {
			daoFactory.abrirConexion();
			daoFactory.iniciarTransaccion();
			EmpleadoDomain empleadoDomain = EmpleadoDTOAssembler.getInstance().toDomain(empleado);
			empleadoBusinessLogic.modificarEmpleadoExistente(id, empleadoDomain);
			daoFactory.confirmarTransaccion();
		
		} catch (LilfacException exception) {
			daoFactory.cancelarTransaccion();
			throw exception;
		
		} catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de modificar la información de el empleado con el identificador ingresado";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de modificar la información de el empleado con el identificador ingresado";
			
			throw BusinessLogicLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		
		}finally {
			daoFactory.cerrarConexion();
		}
	}

	
	public void darBajaDefinitivamenteEmpleadoExistente(UUID id) throws LilfacException {
		
		try {
			daoFactory.abrirConexion();
			daoFactory.iniciarTransaccion();
			empleadoBusinessLogic.darBajaDefinitivamenteEmpleadoExistente(id);
			daoFactory.confirmarTransaccion();
		
		} catch (LilfacException exception) {
			daoFactory.cancelarTransaccion();
			throw exception;
		
		} catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de borrar la información de un nuevo empleado";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de borrar la información de un nuevo empleado";
			
			throw BusinessLogicLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		
		}finally {
			daoFactory.cerrarConexion();
		}
	}

	
	public EmpleadoDTO consultarEmpleadoPorId(UUID id) throws LilfacException {
		
		try {
			var empleadoDomainResultado = empleadoBusinessLogic.consultarEmpleadoPorId(id);
			return EmpleadoDTOAssembler.getInstance().toDto(empleadoDomainResultado);
		
		} catch (LilfacException exception) {
			daoFactory.cancelarTransaccion();
			throw exception;
		
		} catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de consultar la información de un empleado con el id deseado";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de consultar la informacion de un empleado con el id ingresado";
			
			throw BusinessLogicLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		
		}finally {
			daoFactory.cerrarConexion();
		}
	}

	
	public List<EmpleadoDTO> consultarEmpleados(EmpleadoDTO filtro) throws LilfacException {
		
		try {
			var empleadoFilter = EmpleadoDTOAssembler.getInstance().toDomain(filtro);
			List<EmpleadoDomain> empleadosDomain = empleadoBusinessLogic.consultarEmpleados(empleadoFilter);
			return EmpleadoDTOAssembler.getInstance().toDto(empleadosDomain);
		
		} catch (LilfacException exception) {
			daoFactory.cancelarTransaccion();
			throw exception;
		
		} catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de consultar la información de los empleados";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de consultar la informacion de los empleados";
			
			throw BusinessLogicLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}
	}
}
