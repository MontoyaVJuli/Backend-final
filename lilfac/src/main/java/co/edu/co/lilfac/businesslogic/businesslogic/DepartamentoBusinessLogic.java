package co.edu.co.lilfac.businesslogic.businesslogic;

import java.util.List;
import java.util.UUID;

import co.edu.co.lilfac.businesslogic.businesslogic.domain.DepartamentoDomain;
import co.edu.co.lilfac.crosscutting.excepciones.LilfacException;

public interface DepartamentoBusinessLogic {
	
	void registrarNuevoDepartamento(DepartamentoDomain departamento)throws LilfacException;
	void modificarDepartamentoExistente(UUID id, DepartamentoDomain departamento)throws LilfacException;
	void darBajaDefinitivamenteDepartamentoExistente(UUID id)throws LilfacException;
	DepartamentoDomain consultarDepartamentoPorId(UUID id)throws LilfacException;
	List<DepartamentoDomain> consultarDepartamentosFiltro(DepartamentoDomain filtro)throws LilfacException;
	List<DepartamentoDomain>consultarDepartamentos()throws LilfacException;

}
