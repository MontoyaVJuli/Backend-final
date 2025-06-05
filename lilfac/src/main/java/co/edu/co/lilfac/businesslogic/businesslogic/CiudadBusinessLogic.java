package co.edu.co.lilfac.businesslogic.businesslogic;

import java.util.List;
import java.util.UUID;

import co.edu.co.lilfac.businesslogic.businesslogic.domain.CiudadDomain;
import co.edu.co.lilfac.crosscutting.excepciones.LilfacException;

public interface CiudadBusinessLogic {
	
	void registrarNuevaCiudad(CiudadDomain ciudad)throws LilfacException;
	void modificarCiudadExistente(UUID id, CiudadDomain ciudad)throws LilfacException;
	void darBajaDefinitivamenteCiudadExistente(UUID id)throws LilfacException;
	CiudadDomain consultarCiudadPorId(UUID id)throws LilfacException;
	List<CiudadDomain> consultarCiudadesFiltro(CiudadDomain filtro)throws LilfacException;
	List<CiudadDomain>consultarCiudades()throws LilfacException;

}
