package co.edu.co.lilfac.businesslogic.businesslogic.impl;

import java.util.List;
import java.util.UUID;

import co.edu.co.lilfac.businesslogic.businesslogic.DepartamentoBusinessLogic;
import co.edu.co.lilfac.businesslogic.businesslogic.assembler.departamento.entity.DepartamentoEntityAssembler;
import co.edu.co.lilfac.businesslogic.businesslogic.domain.DepartamentoDomain;
import co.edu.co.lilfac.crosscutting.excepciones.LilfacException;
import co.edu.co.lilfac.data.dao.factory.DAOFactory;
import co.edu.co.lilfac.entity.DepartamentoEntity;

public class DepartamentoBusinessLogicImpl implements DepartamentoBusinessLogic {

	private DAOFactory factory;
	
	public DepartamentoBusinessLogicImpl(DAOFactory factory) {
		this.factory = factory;
	}
	
	@Override
	public void registrarNuevoDepartamento(DepartamentoDomain departamento) throws LilfacException {
		var departamentoEntity = DepartamentoEntityAssembler.getInstance().toEntity(departamento);
		factory.getDepartamentoDAO().create(departamentoEntity);
	}

	@Override
	public void modificarDepartamentoExistente(UUID id, DepartamentoDomain departamento) throws LilfacException {
		var departamentoEntity = DepartamentoEntityAssembler.getInstance().toEntity(departamento);
		factory.getDepartamentoDAO().update(id, departamentoEntity);
	}

	@Override
	public void darBajaDefinitivamenteDepartamentoExistente(UUID id) throws LilfacException {
		factory.getDepartamentoDAO().delete(id);
	}

	@Override
	public DepartamentoDomain consultarDepartamentoPorId(UUID id) throws LilfacException {
		var departamEntotity = factory.getDepartamentoDAO().listById(id);
		return DepartamentoEntityAssembler.getInstance().toDomain(departamEntotity);
	}

	@Override
	public List<DepartamentoDomain> consultarDepartamentos(DepartamentoDomain filtro) throws LilfacException {
		
		var departamentoFilter = DepartamentoEntityAssembler.getInstance().toEntity(filtro); 
		List<DepartamentoEntity> departamentoEntities = factory.getDepartamentoDAO().listByFilter(departamentoFilter);
		return DepartamentoEntityAssembler.getInstance().toDomain(departamentoEntities);
		
	}

}
