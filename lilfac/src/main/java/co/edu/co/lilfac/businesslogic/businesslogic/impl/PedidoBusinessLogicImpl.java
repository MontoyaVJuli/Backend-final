package co.edu.co.lilfac.businesslogic.businesslogic.impl;

import java.util.List;
import java.util.UUID;

import co.edu.co.lilfac.businesslogic.businesslogic.PedidoBusinessLogic;
import co.edu.co.lilfac.businesslogic.businesslogic.assembler.pedido.entity.PedidoEntityAssembler;
import co.edu.co.lilfac.businesslogic.businesslogic.domain.PedidoDomain;
import co.edu.co.lilfac.crosscutting.excepciones.LilfacException;
import co.edu.co.lilfac.data.dao.factory.DAOFactory;
import co.edu.co.lilfac.entity.PedidoEntity;

public class PedidoBusinessLogicImpl implements PedidoBusinessLogic {

	private DAOFactory factory;
	
	public PedidoBusinessLogicImpl(DAOFactory factory) {
		this.factory = factory;
	}
	
	@Override
	public void registrarNuevoPedido(PedidoDomain pedido) throws LilfacException {
		var pedidoEntity = PedidoEntityAssembler.getInstance().toEntity(pedido);
		factory.getPedidoDAO().create(pedidoEntity);
	}

	@Override
	public void modificarPedidoExistente(UUID id, PedidoDomain pedido) throws LilfacException {
		var pedidoEntity = PedidoEntityAssembler.getInstance().toEntity(pedido);
		factory.getPedidoDAO().update(id, pedidoEntity);
	}

	@Override
	public void darBajaDefinitivamentePedidoExistente(UUID id) throws LilfacException {
		factory.getPedidoDAO().delete(id);
	}

	@Override
	public PedidoDomain consultarPedidoPorId(UUID id) throws LilfacException {
		var pedidoEntity = factory.getPedidoDAO().listById(id);
		return PedidoEntityAssembler.getInstance().toDomain(pedidoEntity);
	}

	@Override
	public List<PedidoDomain> consultarPedidos(PedidoDomain filtro) throws LilfacException {
		
		var pedidoFilter = PedidoEntityAssembler.getInstance().toEntity(filtro);
		List<PedidoEntity> pedidoEntities = factory.getPedidoDAO().listByFilter(pedidoFilter);
		return PedidoEntityAssembler.getInstance().toDomain(pedidoEntities);
		
	}

}
