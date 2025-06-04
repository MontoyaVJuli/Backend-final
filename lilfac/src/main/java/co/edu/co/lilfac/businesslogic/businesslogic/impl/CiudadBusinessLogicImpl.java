package co.edu.co.lilfac.businesslogic.businesslogic.impl;

import java.util.List;
import java.util.UUID;

import co.edu.co.lilfac.businesslogic.businesslogic.CiudadBusinessLogic;
import co.edu.co.lilfac.businesslogic.businesslogic.assembler.ciudad.entity.CiudadEntityAssembler;
import co.edu.co.lilfac.businesslogic.businesslogic.domain.CiudadDomain;
import co.edu.co.lilfac.crosscutting.excepciones.LilfacException;
import co.edu.co.lilfac.data.dao.factory.DAOFactory;
import co.edu.co.lilfac.entity.CiudadEntity;

public class CiudadBusinessLogicImpl implements CiudadBusinessLogic {

	private DAOFactory factory;
	
	public CiudadBusinessLogicImpl(DAOFactory factory) {
		this.factory = factory;
	}
	
	@Override
	public void registrarNuevaCiudad(CiudadDomain ciudad) throws LilfacException {
		var ciudadEntity = CiudadEntityAssembler.getInstance().toEntity(ciudad);
		factory.getCiudadDAO().create(ciudadEntity);
	}

	@Override
	public void modificarCiudadExistente(UUID id, CiudadDomain ciudad) throws LilfacException {
		var ciudadEntity = CiudadEntityAssembler.getInstance().toEntity(ciudad);
		factory.getCiudadDAO().update(id, ciudadEntity);
	}

	@Override
	public void darBajaDefinitivamenteCiudadExistente(UUID id) throws LilfacException {
		factory.getCiudadDAO().delete(id);
	}

	@Override
	public CiudadDomain consultarCiudadPorId(UUID id) throws LilfacException {
		var ciudadEntity = factory.getCiudadDAO().listById(id);
		return CiudadEntityAssembler.getInstance().toDomain(ciudadEntity);
	}

	@Override
	public List<CiudadDomain> consultarCiudades(CiudadDomain filtro) throws LilfacException {
		
		var ciudadFilter = CiudadEntityAssembler.getInstance().toEntity(filtro);
		List<CiudadEntity> ciudadEntities = factory.getCiudadDAO().listByFilter(ciudadFilter);
		return CiudadEntityAssembler.getInstance().toDomain(ciudadEntities);
	}

}
