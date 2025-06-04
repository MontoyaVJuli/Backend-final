package co.edu.co.lilfac.businesslogic.businesslogic.impl;

import java.util.List;
import java.util.UUID;

import co.edu.co.lilfac.businesslogic.businesslogic.FacturaBusinessLogic;
import co.edu.co.lilfac.businesslogic.businesslogic.assembler.factura.entity.FacturaEntityAssembler;
import co.edu.co.lilfac.businesslogic.businesslogic.domain.FacturaDomain;
import co.edu.co.lilfac.crosscutting.excepciones.LilfacException;
import co.edu.co.lilfac.data.dao.factory.DAOFactory;
import co.edu.co.lilfac.entity.FacturaEntity;

public class FacturaBusinessLogicImpl implements FacturaBusinessLogic {

	private DAOFactory factory;
	
	public FacturaBusinessLogicImpl(DAOFactory factory) {
		this.factory = factory;
	}
	
	@Override
	public void registrarNuevaFactura(FacturaDomain factura) throws LilfacException {
		var facturaEntity = FacturaEntityAssembler.getInstance().toEntity(factura);
		factory.getFacturaDAO().create(facturaEntity);
	}

	@Override
	public void modificarFacturaExistente(UUID id, FacturaDomain factura) throws LilfacException {
		var facturaEntity = FacturaEntityAssembler.getInstance().toEntity(factura);
		factory.getFacturaDAO().update(id, facturaEntity);
	}

	@Override
	public void darBajaDefinitivamenteFacturaExistente(UUID id) throws LilfacException {
		factory.getFacturaDAO().delete(id);
	}

	@Override
	public FacturaDomain consultarFacturaPorId(UUID id) throws LilfacException {
		var facturaEntity = factory.getFacturaDAO().listById(id);
		return FacturaEntityAssembler.getInstance().toDomain(facturaEntity);
	}

	@Override
	public List<FacturaDomain> consultarFacturas(FacturaDomain filtro) throws LilfacException {
		
		var facturaFilter = FacturaEntityAssembler.getInstance().toEntity(filtro);
		List<FacturaEntity> facturaEntities = factory.getFacturaDAO().listByFilter(facturaFilter);
		return FacturaEntityAssembler.getInstance().toDomain(facturaEntities);
	}

}
