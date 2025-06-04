package co.edu.co.lilfac.businesslogic.businesslogic.impl;

import java.util.List;
import java.util.UUID;

import co.edu.co.lilfac.businesslogic.businesslogic.CategoriaBusinessLogic;
import co.edu.co.lilfac.businesslogic.businesslogic.assembler.categoria.entity.CategoriaEntityAssembler;
import co.edu.co.lilfac.businesslogic.businesslogic.domain.CategoriaDomain;
import co.edu.co.lilfac.crosscutting.excepciones.LilfacException;
import co.edu.co.lilfac.data.dao.factory.DAOFactory;
import co.edu.co.lilfac.entity.CategoriaEntity;

public class CategoriaBusinessLogicImpl implements CategoriaBusinessLogic {

	private DAOFactory factory;
	
	public CategoriaBusinessLogicImpl(DAOFactory factory) {
		this.factory = factory;
	}
	
	@Override
	public void registrarNuevaCategoria(CategoriaDomain categoria) throws LilfacException {
		var categoriaEntity = CategoriaEntityAssembler.getInstance().toEntity(categoria);
		factory.getCategoriaDAO().create(categoriaEntity);
	}

	@Override
	public void modificarCategoriaExistente(UUID id, CategoriaDomain categoria) throws LilfacException {
		var categoriaEntity = CategoriaEntityAssembler.getInstance().toEntity(categoria);
		factory.getCategoriaDAO().update(id, categoriaEntity);
	}

	@Override
	public void darBajaDefinitivamenteCategoriaExistente(UUID id) throws LilfacException {
		factory.getCategoriaDAO().delete(id);
	}

	@Override
	public CategoriaDomain consultarCategoriaPorId(UUID id) throws LilfacException {
		var categoriaentity = factory.getCategoriaDAO().listById(id);
		return CategoriaEntityAssembler.getInstance().toDomain(categoriaentity);
	}

	@Override
	public List<CategoriaDomain> consultarCategorias(CategoriaDomain filtro) throws LilfacException {
		
		var categoriaFilter = CategoriaEntityAssembler.getInstance().toEntity(filtro);
		List<CategoriaEntity> categoriaEntities = factory.getCategoriaDAO().listByFilter(categoriaFilter);
		return CategoriaEntityAssembler.getInstance().toDomain(categoriaEntities);
	}

}
