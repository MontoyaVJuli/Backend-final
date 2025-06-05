package co.edu.co.lilfac.businesslogic.businesslogic;

import java.util.List;
import java.util.UUID;

import co.edu.co.lilfac.businesslogic.businesslogic.domain.EmpresaDomain;
import co.edu.co.lilfac.crosscutting.excepciones.LilfacException;

public interface EmpresaBusinessLogic {
	
	void registrarInformacionEmpresa(EmpresaDomain empresa)throws LilfacException;
	void modificarEmpresaExistente(UUID id, EmpresaDomain empresa)throws LilfacException;
	EmpresaDomain consultarEmpresaPorId(UUID id)throws LilfacException;
	List<EmpresaDomain>consultarEmpresas()throws LilfacException;
}
