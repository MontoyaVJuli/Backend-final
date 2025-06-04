package co.edu.co.lilfac.businesslogic.businesslogic.impl;

import java.util.List;
import java.util.UUID;

import co.edu.co.lilfac.businesslogic.businesslogic.PaisBusinessLogic;
import co.edu.co.lilfac.businesslogic.businesslogic.assembler.pais.entity.PaisEntityAssembler;
import co.edu.co.lilfac.businesslogic.businesslogic.domain.PaisDomain;
import co.edu.co.lilfac.crosscutting.excepciones.LilfacException;
import co.edu.co.lilfac.data.dao.factory.DAOFactory;
import co.edu.co.lilfac.entity.PaisEntity;

public class PaisBusinessLogicImpl implements PaisBusinessLogic {

	private DAOFactory factory;
	
	public PaisBusinessLogicImpl(DAOFactory factory) {
		this.factory = factory;
	}
	
	@Override
	public void registrarNuevoPais(PaisDomain pais) throws LilfacException {
		var paisEntity = PaisEntityAssembler.getInstance().toEntity(pais);
		factory.getPaisDAO().create(paisEntity);
	}

	@Override
	public void modificarPaisExistente(UUID id, PaisDomain pais) throws LilfacException {
		var paisEntity = PaisEntityAssembler.getInstance().toEntity(pais);
		factory.getPaisDAO().update(id, paisEntity);
	}

	@Override
	public void darBajaDefinitivamentePaisExistente(UUID id) throws LilfacException {
		factory.getPaisDAO().delete(id);
	}

	@Override
	public PaisDomain consultarPaisPorId(UUID id) throws LilfacException {
		var paisEntity = factory.getPaisDAO().listById(id);
		return PaisEntityAssembler.getInstance().toDomain(paisEntity);
	}

	@Override
	public List<PaisDomain> consultarPaises(PaisDomain filtro) throws LilfacException {

		var paisFilter = PaisEntityAssembler.getInstance().toEntity(filtro);
		List<PaisEntity> paisEntities = factory.getPaisDAO().listByFilter(paisFilter);
		return PaisEntityAssembler.getInstance().toDomain(paisEntities);
		
	}

}
