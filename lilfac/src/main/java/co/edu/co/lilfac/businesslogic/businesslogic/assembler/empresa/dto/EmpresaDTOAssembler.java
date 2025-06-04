package co.edu.co.lilfac.businesslogic.businesslogic.assembler.empresa.dto;

import java.util.ArrayList;
import java.util.List;

import co.edu.co.lilfac.businesslogic.businesslogic.assembler.DTOAssembler;
import co.edu.co.lilfac.businesslogic.businesslogic.assembler.ciudad.dto.CiudadDTOAssembler;
import co.edu.co.lilfac.businesslogic.businesslogic.domain.EmpresaDomain;
import co.edu.co.lilfac.crosscutting.utilitarios.UtilObjeto;
import co.edu.co.lilfac.dto.EmpresaDTO;

public class EmpresaDTOAssembler implements DTOAssembler<EmpresaDTO, EmpresaDomain>{
	
	private static final EmpresaDTOAssembler INSTANCE = new EmpresaDTOAssembler();
	
	public static EmpresaDTOAssembler getInstance() {
		return INSTANCE;
	}

	
	public EmpresaDTO toDto(final EmpresaDomain domain) {
		return UtilObjeto.getInstance().esNulo(domain) ? EmpresaDTO.obtenerValorDefecto() : 
			new EmpresaDTO(domain.getId(), domain.getNombre(),domain.getNit(), domain.getTelefono(), domain.getCorreo(), domain.getDireccion(), CiudadDTOAssembler.getInstance().toDto(domain.getCiudad()));
	}

	
	public EmpresaDomain toDomain(final EmpresaDTO dto) {
		var empresaDTOAEnsamblar = EmpresaDTO.obtenerValorDefecto(dto);
		return new EmpresaDomain(empresaDTOAEnsamblar.getId(), empresaDTOAEnsamblar.getNombre(), empresaDTOAEnsamblar.getNit(), empresaDTOAEnsamblar.getTelefono(), empresaDTOAEnsamblar.getCorreo(), empresaDTOAEnsamblar.getDireccion(), CiudadDTOAssembler.getInstance().toDomain(empresaDTOAEnsamblar.getCiudad()));
	}

	
	public List<EmpresaDomain> toDomain(List<EmpresaDTO> dtoList) {
		var listaResultado = new ArrayList<EmpresaDomain>();
		
		for (EmpresaDTO empresaDTO : dtoList) {
			listaResultado.add(toDomain(empresaDTO));
		}
		return listaResultado;
	}

	
	public List<EmpresaDTO> toDto(List<EmpresaDomain> domainList) {
		var listaResultado = new ArrayList<EmpresaDTO>();
		for (EmpresaDomain empresaDomain : domainList) {
			listaResultado.add(toDto(empresaDomain));
		}
		return listaResultado;
	}



}
