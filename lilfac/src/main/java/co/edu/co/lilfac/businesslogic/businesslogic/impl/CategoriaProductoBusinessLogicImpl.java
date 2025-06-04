package co.edu.co.lilfac.businesslogic.businesslogic.impl;

import java.util.List;
import java.util.UUID;

import co.edu.co.lilfac.businesslogic.businesslogic.CategoriaProductoBusinessLogic;
import co.edu.co.lilfac.businesslogic.businesslogic.assembler.categoriaproducto.entity.CategoriaProductoEntityAssembler;
import co.edu.co.lilfac.businesslogic.businesslogic.domain.CategoriaProductoDomain;
import co.edu.co.lilfac.crosscutting.excepciones.LilfacException;
import co.edu.co.lilfac.data.dao.factory.DAOFactory;
import co.edu.co.lilfac.entity.CategoriaProductoEntity;

public class CategoriaProductoBusinessLogicImpl implements CategoriaProductoBusinessLogic {

	private DAOFactory factory;
	
	public CategoriaProductoBusinessLogicImpl(DAOFactory factory) {
		this.factory = factory;
	}
	
	@Override
	public void registrarNuevaCategoriaProducto(CategoriaProductoDomain categoriaProducto) throws LilfacException {
		var categoriaProdcutoEntity = CategoriaProductoEntityAssembler.getInstance().toEntity(categoriaProducto);
		factory.getCategoriaProductoDAO().create(categoriaProdcutoEntity);
	}

	@Override
	public void modificarCategoriaProductoExistente(UUID id, CategoriaProductoDomain categoriaProducto) throws LilfacException {
		var categoriaProdcutoEntity = CategoriaProductoEntityAssembler.getInstance().toEntity(categoriaProducto);
		factory.getCategoriaProductoDAO().update(id, categoriaProdcutoEntity);
	}

	@Override
	public void darBajaDefinitivamenteCategoriaProductoExistente(UUID id) throws LilfacException {
		factory.getCategoriaProductoDAO().delete(id);
	}

	@Override
	public CategoriaProductoDomain consultarCategoriaProductoPorId(UUID id) throws LilfacException {
		var categoriaProductoEntity = factory.getCategoriaProductoDAO().listById(id);
		return CategoriaProductoEntityAssembler.getInstance().toDomain(categoriaProductoEntity);
	}

	@Override
	public List<CategoriaProductoDomain> consultarCategoriasProducto(CategoriaProductoDomain filtro) throws LilfacException {
		
		var categoriaProductoFilter = CategoriaProductoEntityAssembler.getInstance().toEntity(filtro);
		List<CategoriaProductoEntity> categoriaProductoEntities = factory.getCategoriaProductoDAO().listByFilter(categoriaProductoFilter);
		return CategoriaProductoEntityAssembler.getInstance().toDomain(categoriaProductoEntities);
	}

}
