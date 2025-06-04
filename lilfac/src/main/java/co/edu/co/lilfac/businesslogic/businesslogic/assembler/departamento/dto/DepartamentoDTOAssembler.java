package co.edu.co.lilfac.businesslogic.businesslogic.assembler.departamento.dto;

import java.util.ArrayList;
import java.util.List;

import co.edu.co.lilfac.businesslogic.businesslogic.assembler.DTOAssembler;
import co.edu.co.lilfac.businesslogic.businesslogic.assembler.pais.dto.PaisDTOAssembler;
import co.edu.co.lilfac.businesslogic.businesslogic.domain.DepartamentoDomain;
import co.edu.co.lilfac.crosscutting.utilitarios.UtilObjeto;
import co.edu.co.lilfac.dto.DepartamentoDTO;

public class DepartamentoDTOAssembler implements DTOAssembler<DepartamentoDTO, DepartamentoDomain>{
	
	private static final DepartamentoDTOAssembler INSTANCE = new DepartamentoDTOAssembler();
	
	public static DepartamentoDTOAssembler getInstance() {
		return INSTANCE;
	}

	@Override
	public DepartamentoDTO toDto(final DepartamentoDomain domain) {
		return UtilObjeto.getInstance().esNulo(domain) ? DepartamentoDTO.obtenerValorDefecto() : 
			new DepartamentoDTO(domain.getId(), domain.getNombre(), PaisDTOAssembler.getInstance().toDto(domain.getPais()));
	}

	@Override
	public DepartamentoDomain toDomain(final DepartamentoDTO dto) {
		var departamentoDTOAEnsamblar = DepartamentoDTO.obtenerValorDefecto(dto);
		return new DepartamentoDomain(departamentoDTOAEnsamblar.getId(), departamentoDTOAEnsamblar.getNombre(), PaisDTOAssembler.getInstance().toDomain(departamentoDTOAEnsamblar.getPais()));
	}

	@Override
	public List<DepartamentoDomain> toDomain(List<DepartamentoDTO> dtoList) {
		var listaResultado = new ArrayList<DepartamentoDomain>();
		
		for (DepartamentoDTO departamentoDTO : dtoList) {
			listaResultado.add(toDomain(departamentoDTO));
		}
		return listaResultado;
	}

	@Override
	public List<DepartamentoDTO> toDto(List<DepartamentoDomain> domainList) {
		var listaResultado = new ArrayList<DepartamentoDTO>();
		
		for (DepartamentoDomain departamentoDomain : domainList) {
			listaResultado.add(toDto(departamentoDomain));
		}
		return listaResultado;
	}



}
