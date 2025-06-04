package co.edu.co.lilfac.businesslogic.businesslogic.assembler.cliente.dto;

import java.util.ArrayList;
import java.util.List;

import co.edu.co.lilfac.businesslogic.businesslogic.assembler.DTOAssembler;
import co.edu.co.lilfac.businesslogic.businesslogic.assembler.ciudad.dto.CiudadDTOAssembler;
import co.edu.co.lilfac.businesslogic.businesslogic.domain.ClienteDomain;
import co.edu.co.lilfac.crosscutting.utilitarios.UtilObjeto;
import co.edu.co.lilfac.dto.ClienteDTO;

public class ClienteDTOAssembler implements DTOAssembler<ClienteDTO, ClienteDomain>{
	
	private static final ClienteDTOAssembler INSTANCE = new ClienteDTOAssembler();
	
	public static ClienteDTOAssembler getInstance() {
		return INSTANCE;
	}

	
	public ClienteDTO toDto(final ClienteDomain domain) {
		return UtilObjeto.getInstance().esNulo(domain) ? ClienteDTO.obtenerValorDefecto() : 
			new ClienteDTO(domain.getId(), domain.getNombre(), domain.getApellido(), domain.getCedula(), domain.getTelefono(), domain.getCorreo(), domain.getDireccionResidencia(), CiudadDTOAssembler.getInstance().toDto(domain.getCiudad()));
	}

	public ClienteDomain toDomain(final ClienteDTO dto) {
		var clienteDTOAEnsamblar = ClienteDTO.obtenerValorDefecto(dto);
		return new ClienteDomain(clienteDTOAEnsamblar.getId(), clienteDTOAEnsamblar.getNombre(), clienteDTOAEnsamblar.getApellido(), clienteDTOAEnsamblar.getCedula(), clienteDTOAEnsamblar.getTelefono(), clienteDTOAEnsamblar.getCorreo(), clienteDTOAEnsamblar.getDireccionResidencia(), CiudadDTOAssembler.getInstance().toDomain(clienteDTOAEnsamblar.getCiudad()));
	}

	
	public List<ClienteDomain> toDomain(List<ClienteDTO> dtoList) {
		var listaResultado = new ArrayList<ClienteDomain>();
		
		for (ClienteDTO clienteDTO : dtoList) {
			listaResultado.add(toDomain(clienteDTO));
		}
		return listaResultado;
	}

	public List<ClienteDTO> toDto(List<ClienteDomain> domainList) {
		var listaResultado = new ArrayList<ClienteDTO>();
		
		for (ClienteDomain clienteDomain : domainList) {
			listaResultado.add(toDto(clienteDomain));
		}
		return listaResultado;
	}



}
