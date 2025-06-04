package co.edu.co.lilfac.businesslogic.businesslogic.impl;

import java.util.List;
import java.util.UUID;

import co.edu.co.lilfac.businesslogic.businesslogic.ProductoBusinessLogic;
import co.edu.co.lilfac.businesslogic.businesslogic.assembler.producto.entity.ProductoEntityAssembler;
import co.edu.co.lilfac.businesslogic.businesslogic.domain.ProductoDomain;
import co.edu.co.lilfac.crosscutting.excepciones.LilfacException;
import co.edu.co.lilfac.data.dao.factory.DAOFactory;
import co.edu.co.lilfac.entity.ProductoEntity;

public class ProductoBusinessLogicImpl implements ProductoBusinessLogic {

	private DAOFactory factory;
	
	public ProductoBusinessLogicImpl(DAOFactory factory) {
		this.factory = factory;
	}
	
	@Override
	public void registrarNuevoProducto(ProductoDomain producto) throws LilfacException {
		ProductoEntity productoEntity = ProductoEntityAssembler.getInstance().toEntity(producto);
		factory.getProductoDAO().create(productoEntity);
	}

	@Override
	public void modificarProductoExistente(UUID id, ProductoDomain producto) throws LilfacException {
		ProductoEntity productoEntity = ProductoEntityAssembler.getInstance().toEntity(producto);
		factory.getProductoDAO().update(id, productoEntity);
	}

	@Override
	public void darBajaDefinitivamenteProductoExistente(UUID id) throws LilfacException {
		factory.getProductoDAO().delete(id);
	}

	@Override
	public ProductoDomain consultarProductoPorId(UUID id) throws LilfacException {
		ProductoEntity productoEntity = factory.getProductoDAO().listById(id);
		return ProductoEntityAssembler.getInstance().toDomain(productoEntity);
	}

	@Override
	public List<ProductoDomain> consultarProductos(ProductoDomain filtro) throws LilfacException {
		
		ProductoEntity productoFilter = ProductoEntityAssembler.getInstance().toEntity(filtro); // MAGIA DE TRADUCIR DE domain-›entity 
		List<ProductoEntity> productoEntities = factory.getProductoDAO().listByFilter(productoFilter);
		return ProductoEntityAssembler.getInstance().toDomain(productoEntities);
	}

}
