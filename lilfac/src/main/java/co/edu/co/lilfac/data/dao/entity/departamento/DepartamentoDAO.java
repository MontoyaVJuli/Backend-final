package co.edu.co.lilfac.data.dao.entity.departamento;

import java.util.UUID;

import co.edu.co.lilfac.data.dao.entity.CreateDAO;
import co.edu.co.lilfac.data.dao.entity.DeleteDAO;
import co.edu.co.lilfac.data.dao.entity.RetrieveDAO;
import co.edu.co.lilfac.data.dao.entity.UpdateDAO;
import co.edu.co.lilfac.entity.DepartamentoEntity;

public interface DepartamentoDAO 
		extends CreateDAO<DepartamentoEntity>, RetrieveDAO<DepartamentoEntity, UUID>, UpdateDAO<DepartamentoEntity, UUID>, DeleteDAO<UUID>{

}
