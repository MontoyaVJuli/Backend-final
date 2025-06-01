package co.edu.co.lilfac.businesslogic.businesslogic.impl;

import java.util.UUID;

import co.edu.co.lilfac.businesslogic.businesslogic.InventarioBusinessLogic;
import co.edu.co.lilfac.businesslogic.businesslogic.assembler.inventario.entity.InventarioEntityAssembler;
import co.edu.co.lilfac.businesslogic.businesslogic.domain.InventarioDomain;
import co.edu.co.lilfac.crosscutting.excepciones.LilfacException;
import co.edu.co.lilfac.data.dao.factory.DAOFactory;

public class InventarioBusinessLogicImpl implements InventarioBusinessLogic {
	
private DAOFactory factory;
	
	public InventarioBusinessLogicImpl(DAOFactory factory) {
		this.factory = factory;
	}

	@Override
	public void registrarNuevoProductoInventario(InventarioDomain inventario) throws LilfacException {
		var inventarioEntity = InventarioEntityAssembler.getInstance().toEntity(inventario);
		factory.getInventarioDAO().create(inventarioEntity);
	}

	@Override
	public void modificarInventarioExistente(UUID id, InventarioDomain inventario) throws LilfacException {
		var inventarioEntity = InventarioEntityAssembler.getInstance().toEntity(inventario);
		factory.getInventarioDAO().update(id, inventarioEntity);
	}

	@Override
	public void darBajaDefinitivamenteInventarioExistente(UUID id) throws LilfacException {
		factory.getInventarioDAO().delete(id);
	}

	@Override
	public InventarioDomain consultarInventarioPorId(UUID id) throws LilfacException {
		var inventarioCostoEntity = factory.getInventarioDAO().listById(id);
		return InventarioEntityAssembler.getInstance().toDomain(inventarioCostoEntity);
	}

}
