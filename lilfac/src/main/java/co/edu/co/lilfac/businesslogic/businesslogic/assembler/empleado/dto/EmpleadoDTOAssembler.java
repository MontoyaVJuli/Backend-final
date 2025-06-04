package co.edu.co.lilfac.businesslogic.businesslogic.assembler.empleado.dto;

import java.util.ArrayList;
import java.util.List;

import co.edu.co.lilfac.businesslogic.businesslogic.assembler.DTOAssembler;
import co.edu.co.lilfac.businesslogic.businesslogic.assembler.ciudad.dto.CiudadDTOAssembler;
import co.edu.co.lilfac.businesslogic.businesslogic.assembler.empresa.dto.EmpresaDTOAssembler;
import co.edu.co.lilfac.businesslogic.businesslogic.domain.EmpleadoDomain;
import co.edu.co.lilfac.crosscutting.utilitarios.UtilObjeto;
import co.edu.co.lilfac.dto.EmpleadoDTO;

public class EmpleadoDTOAssembler implements DTOAssembler<EmpleadoDTO, EmpleadoDomain>{
	
	private static final EmpleadoDTOAssembler INSTANCE = new EmpleadoDTOAssembler();
	
	public static EmpleadoDTOAssembler getInstance() {
		return INSTANCE;
	}

	
	public EmpleadoDTO toDto(final EmpleadoDomain domain) {
		return UtilObjeto.getInstance().esNulo(domain) ? EmpleadoDTO.obtenerValorDefecto() : 
			new EmpleadoDTO(domain.getId(), domain.getNombre(), domain.getApellido(),
					domain.getCedula(), domain.getTelefono(), domain.getCorreo(), 
					domain.getDireccionResidencia(), CiudadDTOAssembler.getInstance().toDto(domain.getCiudad()),
					EmpresaDTOAssembler.getInstance().toDto(domain.getEmpresa()));
	}

	
	public EmpleadoDomain toDomain(final EmpleadoDTO dto) {
		var empleadoDTOAEnsamblar = EmpleadoDTO.obtenerValorDefecto(dto);
		return new EmpleadoDomain(empleadoDTOAEnsamblar.getId(), empleadoDTOAEnsamblar.getNombre(), 
				empleadoDTOAEnsamblar.getApellido(), empleadoDTOAEnsamblar.getCedula(), 
				empleadoDTOAEnsamblar.getTelefono(), empleadoDTOAEnsamblar.getCorreo(), 
				empleadoDTOAEnsamblar.getDireccionResidencia(), 
				CiudadDTOAssembler.getInstance().toDomain(empleadoDTOAEnsamblar.getCiudad()),
				EmpresaDTOAssembler.getInstance().toDomain(empleadoDTOAEnsamblar.getEmpresa()));

	}

	
	public List<EmpleadoDomain> toDomain(List<EmpleadoDTO> dtoList) {
		var listaResultado = new ArrayList<EmpleadoDomain>();
		
		for (EmpleadoDTO empleadoDTO : dtoList) {
			listaResultado.add(toDomain(empleadoDTO));
		}
		return listaResultado;
	}

	
	public List<EmpleadoDTO> toDto(List<EmpleadoDomain> domainList) {
		var listaResultado = new ArrayList<EmpleadoDTO>();
		
		for (EmpleadoDomain empleadoDomain : domainList) {
			listaResultado.add(toDto(empleadoDomain));
		}
		return listaResultado;
	}

}
