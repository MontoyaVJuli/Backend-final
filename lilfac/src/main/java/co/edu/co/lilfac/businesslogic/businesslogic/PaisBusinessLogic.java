package co.edu.co.lilfac.businesslogic.businesslogic;

import java.util.List;
import java.util.UUID;

import co.edu.co.lilfac.businesslogic.businesslogic.domain.PaisDomain;

public interface PaisBusinessLogic {
	
	void registrarNuevoPais(PaisDomain pais);
	void modificarPaisExistente(UUID id, PaisDomain pais);
	void darBajaDefinitivamentePaisExistente(UUID id);
	PaisDomain consultarPaisPorId(UUID id);
	List<PaisDomain> consultarPaises(PaisDomain filtro);

}
