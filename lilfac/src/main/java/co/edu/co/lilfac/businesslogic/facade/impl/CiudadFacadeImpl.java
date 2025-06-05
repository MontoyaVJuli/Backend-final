package co.edu.co.lilfac.businesslogic.facade.impl;

import java.util.List;
import java.util.UUID;

import co.edu.co.lilfac.businesslogic.businesslogic.CiudadBusinessLogic;
import co.edu.co.lilfac.businesslogic.businesslogic.assembler.ciudad.dto.CiudadDTOAssembler;
import co.edu.co.lilfac.businesslogic.businesslogic.domain.CiudadDomain;
import co.edu.co.lilfac.businesslogic.businesslogic.impl.CiudadBusinessLogicImpl;
import co.edu.co.lilfac.businesslogic.facade.CiudadFacade;
import co.edu.co.lilfac.crosscutting.excepciones.BusinessLogicLilfacException;
import co.edu.co.lilfac.crosscutting.excepciones.LilfacException;
import co.edu.co.lilfac.data.dao.factory.DAOFactory;
import co.edu.co.lilfac.data.dao.factory.Factory;
import co.edu.co.lilfac.dto.CiudadDTO;

public class CiudadFacadeImpl implements CiudadFacade {
	
	private DAOFactory daoFactory;
	private CiudadBusinessLogic ciudadBusinessLogic;
	
	public CiudadFacadeImpl() throws LilfacException {
		daoFactory = DAOFactory.getFactory(Factory.POSTGRE_SQL);
		ciudadBusinessLogic = new CiudadBusinessLogicImpl(daoFactory);
	} 
	
	
	@Override
	public void registrarNuevaCiudad(CiudadDTO ciudad) throws LilfacException {
		try {
			daoFactory.iniciarTransaccion();
			CiudadDomain ciudadDomain = CiudadDTOAssembler.getInstance().toDomain(ciudad);
			ciudadBusinessLogic.registrarNuevaCiudad(ciudadDomain);
			daoFactory.confirmarTransaccion();
		} catch (LilfacException exception) {
			daoFactory.cancelarTransaccion();
			throw exception;
		} catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de registrar la información de una nueva ciudad";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de registrar la información de una nueva ciudad";
			
			throw BusinessLogicLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}finally {
			daoFactory.cerrarConexion();
		}
	}

	@Override
	public void modificarCiudadExistente(UUID id, CiudadDTO ciudad) throws LilfacException {
		try {
			daoFactory.iniciarTransaccion();
			CiudadDomain ciudadDomain = CiudadDTOAssembler.getInstance().toDomain(ciudad);
			ciudadBusinessLogic.modificarCiudadExistente(id, ciudadDomain);
			daoFactory.confirmarTransaccion();
		} catch (LilfacException exception) {
			daoFactory.cancelarTransaccion();
			throw exception;
		} catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de modificar la informacion de la ciudad con el id ingresado";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de modificar la informacion de la ciudad con el id ingresado";
			
			throw BusinessLogicLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}finally {
			daoFactory.cerrarConexion();
		}
	}

	@Override
	public void darBajaDefinitivamenteCiudadExistente(UUID id) throws LilfacException {
		try {
			daoFactory.iniciarTransaccion();
			ciudadBusinessLogic.darBajaDefinitivamenteCiudadExistente(id);
			daoFactory.confirmarTransaccion();
		} catch (LilfacException exception) {
			daoFactory.cancelarTransaccion();
			throw exception;
		} catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de borrar la informacion de la ciudad con el id ingresado";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de borrar la informacion de la ciudad con el id ingresado";
			
			throw BusinessLogicLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}finally {
			daoFactory.cerrarConexion();
		}
	}

	@Override
	public CiudadDTO consultarCiudadPorId(UUID id) throws LilfacException {
		try {
			var ciudadDomainResultado = ciudadBusinessLogic.consultarCiudadPorId(id);
			return CiudadDTOAssembler.getInstance().toDto(ciudadDomainResultado);
		} catch (LilfacException exception) {
			daoFactory.cancelarTransaccion();
			throw exception;
		} catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de consultar la información de una ciudad con el id deseado";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de consultar la informacion de una ciudad con el id ingresado";
			
			throw BusinessLogicLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}finally {
			daoFactory.cerrarConexion();
		}
		
	}

	@Override
	public List<CiudadDTO> consultarCiudadesFiltro(CiudadDTO filtro) throws LilfacException {
		try {
			var ciudadFilter = CiudadDTOAssembler.getInstance().toDomain(filtro);
			List<CiudadDomain> ciudadesDomain = ciudadBusinessLogic.consultarCiudadesFiltro(ciudadFilter);
			return CiudadDTOAssembler.getInstance().toDto(ciudadesDomain);
		} catch (LilfacException exception) {
			daoFactory.cancelarTransaccion();
			throw exception;
		} catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de consultar la información de las ciudades";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de consultar la informacion de las ciudades";
			
			throw BusinessLogicLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}
	}

	@Override
	public List<CiudadDTO> consultarCiudades() throws LilfacException {
		try {
			daoFactory.abrirConexion();
			List<CiudadDomain> ciudadesDomain = ciudadBusinessLogic.consultarCiudades();
			return CiudadDTOAssembler.getInstance().toDto(ciudadesDomain);
		} catch (LilfacException exception) {
			daoFactory.cancelarTransaccion();
			throw exception;
		} catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de consultar la información de las ciudades";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de consultar la informacion de las ciudades";
			
			throw BusinessLogicLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}finally {
			daoFactory.cerrarConexion();
		}
	}

}
