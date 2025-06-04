package co.edu.co.lilfac.data.dao.entity;

import java.util.List;

import co.edu.co.lilfac.crosscutting.excepciones.LilfacException;

public interface RetrieveDAO<E, ID> {
	
	List<E> listByFilter(E filter)throws LilfacException;
	
	List<E> listAll()throws LilfacException;
	
	E listById(ID id)throws LilfacException;
	

}
