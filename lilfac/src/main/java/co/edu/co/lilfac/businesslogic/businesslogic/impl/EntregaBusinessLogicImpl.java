package co.edu.co.lilfac.businesslogic.businesslogic.impl;

import java.util.List;
import java.util.UUID;

import co.edu.co.lilfac.businesslogic.businesslogic.EntregaBusinessLogic;
import co.edu.co.lilfac.businesslogic.businesslogic.assembler.entrega.entity.EntregaEntityAssembler;
import co.edu.co.lilfac.businesslogic.businesslogic.domain.EntregaDomain;
import co.edu.co.lilfac.crosscutting.excepciones.LilfacException;
import co.edu.co.lilfac.data.dao.factory.DAOFactory;
import co.edu.co.lilfac.entity.EntregaEntity;

public class EntregaBusinessLogicImpl implements EntregaBusinessLogic {
	
	private DAOFactory factory;
	
	public EntregaBusinessLogicImpl(DAOFactory factory) {
		this.factory = factory;
	}
	
	@Override
	public void registrarNuevaEntrega(EntregaDomain entrega) throws LilfacException {
		var entregaEntity = EntregaEntityAssembler.getInstance().toEntity(entrega);
		factory.getEntregaDAO().create(entregaEntity);
	}

	@Override
	public void modificarEntregaExistente(UUID id, EntregaDomain entrega) throws LilfacException {
		var entregaEntity = EntregaEntityAssembler.getInstance().toEntity(entrega);
		factory.getEntregaDAO().update(id, entregaEntity);
	}

	@Override
	public void darBajaDefinitivamenteEntregaExistente(UUID id) throws LilfacException {
		factory.getEntregaDAO().delete(id);
	}

	@Override
	public EntregaDomain consultarEntregaPorId(UUID id) throws LilfacException {
		var entregaEntity = factory.getEntregaDAO().listById(id);
		return EntregaEntityAssembler.getInstance().toDomain(entregaEntity);
	}

	@Override
	public List<EntregaDomain> consultarEntregas(EntregaDomain filtro) throws LilfacException {
		
		var entregaFilter = EntregaEntityAssembler.getInstance().toEntity(filtro);
		List<EntregaEntity> entregaEntities = factory.getEntregaDAO().listByFilter(entregaFilter);
		return EntregaEntityAssembler.getInstance().toDomain(entregaEntities);
	}

}
