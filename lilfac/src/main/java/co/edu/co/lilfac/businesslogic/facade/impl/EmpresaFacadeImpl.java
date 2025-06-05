package co.edu.co.lilfac.businesslogic.facade.impl;

import java.util.List;
import java.util.UUID;

import co.edu.co.lilfac.businesslogic.businesslogic.EmpresaBusinessLogic;
import co.edu.co.lilfac.businesslogic.businesslogic.assembler.empresa.dto.EmpresaDTOAssembler;
import co.edu.co.lilfac.businesslogic.businesslogic.domain.EmpresaDomain;
import co.edu.co.lilfac.businesslogic.businesslogic.impl.EmpresaBusinessLogicImpl;
import co.edu.co.lilfac.businesslogic.facade.EmpresaFacade;
import co.edu.co.lilfac.crosscutting.excepciones.BusinessLogicLilfacException;
import co.edu.co.lilfac.crosscutting.excepciones.LilfacException;
import co.edu.co.lilfac.data.dao.factory.DAOFactory;
import co.edu.co.lilfac.data.dao.factory.Factory;
import co.edu.co.lilfac.dto.EmpresaDTO;

public class EmpresaFacadeImpl implements EmpresaFacade{

	private DAOFactory daoFactory;
	private EmpresaBusinessLogic empresaBusinessLogic;
	
	public EmpresaFacadeImpl() throws LilfacException {
		daoFactory = DAOFactory.getFactory(Factory.POSTGRE_SQL);
		empresaBusinessLogic = new EmpresaBusinessLogicImpl(daoFactory);

	} 
	
	@Override
	public void registrarInformacionEmpresa(EmpresaDTO empresa) throws LilfacException {
		try {
			daoFactory.iniciarTransaccion();
			EmpresaDomain empresaDomain = EmpresaDTOAssembler.getInstance().toDomain(empresa);
			empresaBusinessLogic.registrarInformacionEmpresa(empresaDomain);
			daoFactory.confirmarTransaccion();
		} catch (LilfacException exception) {
			daoFactory.cancelarTransaccion();
			throw exception;
		} catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de registrar la información de una nueva empresa";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de registrar la información de una nueva empresa";
			
			throw BusinessLogicLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}finally {
			daoFactory.cerrarConexion();
		}
	}

	@Override
	public void modificarEmpresaExistente(UUID id, EmpresaDTO empresa) throws LilfacException {
		try {
			daoFactory.iniciarTransaccion();
			EmpresaDomain empresaDomain = EmpresaDTOAssembler.getInstance().toDomain(empresa);
			empresaBusinessLogic.modificarEmpresaExistente(id, empresaDomain);
			daoFactory.confirmarTransaccion();
		} catch (LilfacException exception) {
			daoFactory.cancelarTransaccion();
			throw exception;
		} catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de modificar la información de la empresa con el identificador ingresado";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de modificar la información de la empresa con el identificador ingresado";
			
			throw BusinessLogicLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}finally {
			daoFactory.cerrarConexion();
		}
	}

	@Override
	public EmpresaDTO consultarEmpresaPorId(UUID id) throws LilfacException {
		try {
			var empresaDomainResultado = empresaBusinessLogic.consultarEmpresaPorId(id);		
			return EmpresaDTOAssembler.getInstance().toDto(empresaDomainResultado);
		} catch (LilfacException exception) {
			daoFactory.cancelarTransaccion();
			throw exception;
		} catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de consultar la información de una empresa con el id deseado";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de consultar la informacion de una empresa con el id ingresado";
			
			throw BusinessLogicLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}finally {
			daoFactory.cerrarConexion();
		}

	}

	@Override
	public List<EmpresaDTO> consultarEmpresas() throws LilfacException {
		try{
			daoFactory.abrirConexion();
			List<EmpresaDomain> empresaDomain = empresaBusinessLogic.consultarEmpresas();
			return EmpresaDTOAssembler.getInstance().toDto(empresaDomain);
		}catch(LilfacException exception) {
			daoFactory.cancelarTransaccion();
			throw exception;
		} catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de consultar la información de las empresas";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de consultar la informacion de las empresas";
			
			throw BusinessLogicLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}finally {
			daoFactory.cerrarConexion();
		}
		
	}
}
