package co.edu.co.lilfac.businesslogic.businesslogic.impl;

import java.util.List;
import java.util.UUID;

import co.edu.co.lilfac.businesslogic.businesslogic.HistorialCostoBusinessLogic;
import co.edu.co.lilfac.businesslogic.businesslogic.assembler.historialcosto.entity.HistorialCostoEntityAssembler;
import co.edu.co.lilfac.businesslogic.businesslogic.domain.HistorialCostoDomain;
import co.edu.co.lilfac.crosscutting.excepciones.LilfacException;
import co.edu.co.lilfac.data.dao.factory.DAOFactory;
import co.edu.co.lilfac.entity.HistorialCostoEntity;

public class HistorialCostoBusinessLogicImpl implements HistorialCostoBusinessLogic {

	private DAOFactory factory;
	
	public HistorialCostoBusinessLogicImpl(DAOFactory factory) {
		this.factory = factory;
	}
	

	@Override
	public void registrarNuevoHistorialCosto(HistorialCostoDomain historialCosto) throws LilfacException {
		var historialCostoEntity = HistorialCostoEntityAssembler.getInstance().toEntity(historialCosto);
		factory.getHistorialCostoDAO().create(historialCostoEntity);
	}

	@Override
	public void modificarHistorialCostoExistente(UUID id, HistorialCostoDomain historialCosto) throws LilfacException {
		var historialCostoEntity = HistorialCostoEntityAssembler.getInstance().toEntity(historialCosto);
		factory.getHistorialCostoDAO().update(id, historialCostoEntity);
	}

	@Override
	public void darBajaDefinitivamenteHistorialCostoExistente(UUID id) throws LilfacException {
		factory.getHistorialCostoDAO().delete(id);
	}

	@Override
	public HistorialCostoDomain consultarHistorialCostoPorId(UUID id) throws LilfacException {
		var historialCostoEntity = factory.getHistorialCostoDAO().listById(id);
		return HistorialCostoEntityAssembler.getInstance().toDomain(historialCostoEntity);
	}

	@Override
	public List<HistorialCostoDomain> consultarHistorialesCosto(HistorialCostoDomain filtro) throws LilfacException {
		
		var historialCostoFilter = HistorialCostoEntityAssembler.getInstance().toEntity(filtro);
		List<HistorialCostoEntity> historialCostoEntities = factory.getHistorialCostoDAO().listByFilter(historialCostoFilter);
		return HistorialCostoEntityAssembler.getInstance().toDomain(historialCostoEntities);
	}

}
