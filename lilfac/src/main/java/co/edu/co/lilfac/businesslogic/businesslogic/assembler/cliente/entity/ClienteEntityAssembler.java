package co.edu.co.lilfac.businesslogic.businesslogic.assembler.cliente.entity;

import java.util.ArrayList;
import java.util.List;

import co.edu.co.lilfac.businesslogic.businesslogic.assembler.EntityAssembler;
import co.edu.co.lilfac.businesslogic.businesslogic.assembler.ciudad.entity.CiudadEntityAssembler;
import co.edu.co.lilfac.businesslogic.businesslogic.domain.ClienteDomain;
import co.edu.co.lilfac.crosscutting.utilitarios.UtilObjeto;
import co.edu.co.lilfac.entity.ClienteEntity;

public class ClienteEntityAssembler implements EntityAssembler<ClienteEntity, ClienteDomain>{
	
	private static final ClienteEntityAssembler INSTANCE = new ClienteEntityAssembler();
	
	public static ClienteEntityAssembler getInstance() {
		return INSTANCE;
	}

	
	public ClienteEntity toEntity(final ClienteDomain domain) {
		return UtilObjeto.getInstance().esNulo(domain) ? ClienteEntity.obtenerValorDefecto() : 
			new ClienteEntity(domain.getId(), domain.getNombre(), domain.getApellido(), domain.getCedula(), domain.getTelefono(), domain.getCorreo(), domain.getDireccionResidencia(), CiudadEntityAssembler.getInstance().toEntity(domain.getCiudad()));
	}

	
	public ClienteDomain toDomain(final ClienteEntity entity) {
		var clienteEntityAEnsamblar = ClienteEntity.obtenerValorDefecto(entity);
		return new ClienteDomain(clienteEntityAEnsamblar.getId(), clienteEntityAEnsamblar.getNombre(), clienteEntityAEnsamblar.getApellido(), clienteEntityAEnsamblar.getCedula(), clienteEntityAEnsamblar.getTelefono(), clienteEntityAEnsamblar.getCorreo(), clienteEntityAEnsamblar.getDireccionResidencia(), CiudadEntityAssembler.getInstance().toDomain(clienteEntityAEnsamblar.getCiudad()));
	}

	
	public List<ClienteDomain> toDomain(List<ClienteEntity> entityList) {
		var listaResultado = new ArrayList<ClienteDomain>();
		
		for (ClienteEntity clienteEntity : entityList) {
			listaResultado.add(toDomain(clienteEntity));
		}
		return listaResultado;
	}



}
