package co.edu.co.lilfac.businesslogic.businesslogic.impl;

import java.util.List;
import java.util.UUID;

import co.edu.co.lilfac.businesslogic.businesslogic.CostoAdicionalBusinessLogic;
import co.edu.co.lilfac.businesslogic.businesslogic.assembler.costoadicional.entity.CostoAdicionalEntityAssembler;
import co.edu.co.lilfac.businesslogic.businesslogic.domain.CostoAdicionalDomain;
import co.edu.co.lilfac.crosscutting.excepciones.LilfacException;
import co.edu.co.lilfac.data.dao.factory.DAOFactory;
import co.edu.co.lilfac.entity.CostoAdicionalEntity;

public class CostoAdicionalBusinessLogicImpl implements CostoAdicionalBusinessLogic {

	private DAOFactory factory;
	
	public CostoAdicionalBusinessLogicImpl(DAOFactory factory) {
		this.factory = factory;
	}
	
	@Override
	public void registrarNuevoCostoAdicional(CostoAdicionalDomain costoAdicional) throws LilfacException {
		var costoAdicionalEntity = CostoAdicionalEntityAssembler.getInstance().toEntity(costoAdicional);
		factory.getCostoAdicionalDAO().create(costoAdicionalEntity);
	}

	@Override
	public void modificarCostoAdicionalExistente(UUID id, CostoAdicionalDomain costoAdicional) throws LilfacException {
		var costoAdicionalEntity = CostoAdicionalEntityAssembler.getInstance().toEntity(costoAdicional);
		factory.getCostoAdicionalDAO().update(id, costoAdicionalEntity);
	}

	@Override
	public void darBajaDefinitivamenteCostoAdicionalExistente(UUID id) throws LilfacException {
		factory.getCostoAdicionalDAO().delete(id);
	}

	@Override
	public CostoAdicionalDomain consultarCostoAdicionalPorId(UUID id) throws LilfacException {
		var costoAdicionalEntity = factory.getCostoAdicionalDAO().listById(id);
		return CostoAdicionalEntityAssembler.getInstance().toDomain(costoAdicionalEntity);
	}

	@Override
	public List<CostoAdicionalDomain> consultarCostosAdicionales(CostoAdicionalDomain filtro) throws LilfacException {
		
		var costoAdicionalFilter = CostoAdicionalEntityAssembler.getInstance().toEntity(filtro);
		List<CostoAdicionalEntity> costoAdicionalEntities = factory.getCostoAdicionalDAO().listByFilter(costoAdicionalFilter);
		return CostoAdicionalEntityAssembler.getInstance().toDomain(costoAdicionalEntities);
	}

}
