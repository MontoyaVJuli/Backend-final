package co.edu.co.lilfac.businesslogic.facade;

import java.util.List;
import java.util.UUID;

import co.edu.co.lilfac.crosscutting.excepciones.LilfacException;
import co.edu.co.lilfac.dto.DepartamentoDTO;

public interface DepartamentoFacade {
	
	void registrarNuevoDepartamento(DepartamentoDTO departamento)throws LilfacException;
	void modificarDepartamentoExistente(UUID id, DepartamentoDTO departamento)throws LilfacException;
	void darBajaDefinitivamenteDepartamentoExistente(UUID id)throws LilfacException;
	DepartamentoDTO consultarDepartamentoPorId(UUID id)throws LilfacException;
	List<DepartamentoDTO> consultarDepartamentosFiltro(DepartamentoDTO filtro)throws LilfacException;
	List<DepartamentoDTO>consultarDepartamentos()throws LilfacException;

}
