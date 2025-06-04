package co.edu.co.lilfac.businesslogic.businesslogic.assembler.ciudad.dto;

import java.util.ArrayList;
import java.util.List;

import co.edu.co.lilfac.businesslogic.businesslogic.assembler.DTOAssembler;
import co.edu.co.lilfac.businesslogic.businesslogic.assembler.departamento.dto.DepartamentoDTOAssembler;
import co.edu.co.lilfac.businesslogic.businesslogic.domain.CiudadDomain;
import co.edu.co.lilfac.crosscutting.utilitarios.UtilObjeto;
import co.edu.co.lilfac.dto.CiudadDTO;

public class CiudadDTOAssembler implements DTOAssembler<CiudadDTO, CiudadDomain>{
	
	private static final CiudadDTOAssembler INSTANCE = new CiudadDTOAssembler();
	
	public static CiudadDTOAssembler getInstance() {
		return INSTANCE;
	}

	
	public CiudadDTO toDto(final CiudadDomain domain) {
		return UtilObjeto.getInstance().esNulo(domain) ? CiudadDTO.obtenerValorDefecto() : 
			new CiudadDTO(domain.getId(), domain.getNombre(), DepartamentoDTOAssembler.getInstance().toDto(domain.getDepartamento()));
	}

	
	public CiudadDomain toDomain(final CiudadDTO dto) {
		var ciudadDTOAEnsamblar = CiudadDTO.obtenerValorDefecto(dto);
		return new CiudadDomain(ciudadDTOAEnsamblar.getId(), ciudadDTOAEnsamblar.getNombre(), DepartamentoDTOAssembler.getInstance().toDomain(ciudadDTOAEnsamblar.getDepartamento()));
	}

	
	public List<CiudadDomain> toDomain(List<CiudadDTO> dtoList) {
		var listaResultado = new ArrayList<CiudadDomain>();
		
		for (CiudadDTO ciudadDTO : dtoList) {
			listaResultado.add(toDomain(ciudadDTO));
		}
		return listaResultado;
	}

	
	public List<CiudadDTO> toDto(List<CiudadDomain> domainList) {
		var listaResultado = new ArrayList<CiudadDTO>();
		
		for (CiudadDomain ciudadDomain : domainList) {
			listaResultado.add(toDto(ciudadDomain));
		}
		
		return listaResultado;
	}



}
