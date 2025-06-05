package co.edu.co.lilfac.businesslogic.facade;

import java.util.List;
import java.util.UUID;

import co.edu.co.lilfac.crosscutting.excepciones.LilfacException;
import co.edu.co.lilfac.dto.CiudadDTO;

public interface CiudadFacade {
	
	void registrarNuevaCiudad(CiudadDTO ciudad)throws LilfacException;
	void modificarCiudadExistente(UUID id, CiudadDTO ciudad)throws LilfacException;
	void darBajaDefinitivamenteCiudadExistente(UUID id)throws LilfacException;
	CiudadDTO consultarCiudadPorId(UUID id)throws LilfacException;
	List<CiudadDTO> consultarCiudadesFiltro(CiudadDTO filtro)throws LilfacException;
	List<CiudadDTO>consultarCiudades()throws LilfacException;
}
