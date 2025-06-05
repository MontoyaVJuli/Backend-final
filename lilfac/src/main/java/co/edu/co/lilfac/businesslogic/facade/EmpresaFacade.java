package co.edu.co.lilfac.businesslogic.facade;

import java.util.List;
import java.util.UUID;

import co.edu.co.lilfac.crosscutting.excepciones.LilfacException;
import co.edu.co.lilfac.dto.EmpresaDTO;

public interface EmpresaFacade {
	
	void registrarInformacionEmpresa(EmpresaDTO empresa)throws LilfacException;
	void modificarEmpresaExistente(UUID id, EmpresaDTO empresa)throws LilfacException;
	EmpresaDTO consultarEmpresaPorId(UUID id)throws LilfacException;
	List<EmpresaDTO>consultarEmpresas()throws LilfacException;
}
