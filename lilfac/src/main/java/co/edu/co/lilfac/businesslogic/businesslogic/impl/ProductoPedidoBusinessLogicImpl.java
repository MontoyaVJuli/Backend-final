package co.edu.co.lilfac.businesslogic.businesslogic.impl;

import java.util.List;
import java.util.UUID;

import co.edu.co.lilfac.businesslogic.businesslogic.ProductoPedidoBusinessLogic;
import co.edu.co.lilfac.businesslogic.businesslogic.assembler.productopedido.entity.ProductoPedidoEntityAssembler;
import co.edu.co.lilfac.businesslogic.businesslogic.domain.ProductoPedidoDomain;
import co.edu.co.lilfac.crosscutting.excepciones.LilfacException;
import co.edu.co.lilfac.data.dao.factory.DAOFactory;
import co.edu.co.lilfac.entity.ProductoPedidoEntity;

public class ProductoPedidoBusinessLogicImpl implements ProductoPedidoBusinessLogic {

	private DAOFactory factory;
	
	public ProductoPedidoBusinessLogicImpl(DAOFactory factory) {
		this.factory = factory;
	}
	
	@Override
	public void registrarNuevoProductoPedido(ProductoPedidoDomain productoPedido) throws LilfacException {
		ProductoPedidoEntity productopedidoEntity = ProductoPedidoEntityAssembler.getInstance().toEntity(productoPedido);
		factory.getProductoPedidoDAO().create(productopedidoEntity);
	}

	@Override
	public void modificarProductoPedidoExistente(UUID id, ProductoPedidoDomain productoPedido) throws LilfacException {
		ProductoPedidoEntity productopedidoEntity = ProductoPedidoEntityAssembler.getInstance().toEntity(productoPedido);
		factory.getProductoPedidoDAO().update(id, productopedidoEntity);
	}

	@Override
	public void darBajaDefinitivamenteProductoPedidoExistente(UUID id) throws LilfacException {
		factory.getProductoPedidoDAO().delete(id);
	}

	@Override
	public ProductoPedidoDomain consultarProductoPedidoPorId(UUID id) throws LilfacException {
		ProductoPedidoEntity productoPedidoEntity = factory.getProductoPedidoDAO().listById(id);
		return ProductoPedidoEntityAssembler.getInstance().toDomain(productoPedidoEntity);
	}

	@Override
	public List<ProductoPedidoDomain> consultarProductosPedidos(ProductoPedidoDomain filtro) throws LilfacException {
		
		ProductoPedidoEntity productoPedidoFilter = ProductoPedidoEntityAssembler.getInstance().toEntity(filtro);
		List<ProductoPedidoEntity> productoPedidoEntities = factory.getProductoPedidoDAO().listByFilter(productoPedidoFilter);
		return ProductoPedidoEntityAssembler.getInstance().toDomain(productoPedidoEntities);
	}

}
