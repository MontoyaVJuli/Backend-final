package co.edu.co.lilfac.data.dao.entity.departamento.impl.postgresql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import co.edu.co.lilfac.crosscutting.excepciones.DataLilfacException;
import co.edu.co.lilfac.crosscutting.excepciones.LilfacException;
import co.edu.co.lilfac.crosscutting.utilitarios.UtilObjeto;
import co.edu.co.lilfac.crosscutting.utilitarios.UtilTexto;
import co.edu.co.lilfac.crosscutting.utilitarios.UtilUUID;
import co.edu.co.lilfac.data.dao.entity.departamento.DepartamentoDAO;
import co.edu.co.lilfac.entity.DepartamentoEntity;
import co.edu.co.lilfac.entity.PaisEntity;

public class DepartamentoPostgreSQLDAO implements DepartamentoDAO{
	
	private Connection conexion;
	
	public DepartamentoPostgreSQLDAO(Connection conexion) {
		this.conexion=conexion;
	}

	
	public void create(DepartamentoEntity entity) throws LilfacException {
		var sentenciaSQL = new StringBuilder();
		
		sentenciaSQL.append("INSERT INTO departamento (id, nombre, pais) VALUES (?, ?, ?)");
		
		try(var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString())){
			
			sentenciaPreparada.setObject(1, entity.getId());
			sentenciaPreparada.setString(2, entity.getNombre());
			sentenciaPreparada.setObject(3, entity.getPais().getId());
			
			sentenciaPreparada.executeUpdate();
			
		} catch (SQLException exception) {
			var mensajeUsuario="Se ha presentado un problema tratando de registrar la información de un nuevo departamento";
			var mensajeTecnico="Se presentó una excepción de tipo SQLexception tratando de hacer un INSERT en la tabla Departamento,  para tener más detalles revise el log de errores";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de registrar la información de un nuevo departamento";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de hacer un INSERT en la tabla Departamento";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}
		
	}

	
	public List<DepartamentoEntity> listByFilter(DepartamentoEntity filter) throws LilfacException {
		var listaDepartamentos = new java.util.ArrayList<DepartamentoEntity>();
		var sentenciaSQL = new StringBuilder();
		sentenciaSQL.append("SELECT d.id, d.nombre, p.id AS pais_id FROM departamento d  ")
				    .append("INNER JOIN pais p ON d.pais = p.id ")
				    .append("WHERE 1=1 ");
		
		 var parametros = new ArrayList<Object>();

		 if (!UtilObjeto.getInstance().esNulo(filter)) {

		        if (!UtilObjeto.getInstance().esNulo(filter.getId()) && !UtilUUID.esValorDefecto(filter.getId())) {
		            sentenciaSQL.append("AND d.id = ? ");
		            parametros.add(filter.getId());
		        }
	
		        if (!UtilTexto.getInstance().estaVacia(filter.getNombre())) {
		            sentenciaSQL.append("AND d.nombre ILIKE ? ");
		            parametros.add("%" + filter.getNombre().trim() + "%");
		        }
		        
		        if (!UtilObjeto.getInstance().esNulo(filter.getPais()) && !UtilUUID.esValorDefecto(filter.getPais().getId())) {
		            sentenciaSQL.append("AND d.pais = ? ");
		            parametros.add(filter.getPais().getId());
		        }
			
		        try (var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString())) {
		            for (int i = 0; i < parametros.size(); i++) {
		                sentenciaPreparada.setObject(i + 1, parametros.get(i));
		            }
	
		            try (var cursor = sentenciaPreparada.executeQuery()) {
		                while (cursor.next()) {
		                    var departamento = new DepartamentoEntity();
		                    departamento.setId(UtilUUID.convertirAUUID(cursor.getString("id")));
		                    departamento.setNombre(cursor.getString("nombre"));
		                    
		                    var pais = new PaisEntity();
		                    pais.setId(UtilUUID.convertirAUUID(cursor.getString("pais_id")));
		                    departamento.setPais(pais);
		                    
		                    listaDepartamentos.add(departamento);	                    
		                }	                  
		            }
		            
		        } catch (SQLException exception) {
		            throw DataLilfacException.reportar(
		                    "Se ha presentado un problema consultando empleados con filtro.",
		                    "SQLException ejecutando SELECT en Empleado con filtros.",
		                    exception
		            );
		        } catch (Exception exception) {
		            throw DataLilfacException.reportar(
		                    "Se ha presentado un problema inesperado consultando empleados.",
		                    "Excepción no controlada ejecutando SELECT en Empleado con filtros.",
		                    exception
		            );
		        }
		 }  
	            
		return listaDepartamentos;
	}

	
	public List<DepartamentoEntity> listAll() throws LilfacException {
	    List<DepartamentoEntity> listaDepartamentos = new ArrayList<>();
	    var sentenciaSQL = new StringBuilder();

	    sentenciaSQL.append("SELECT D.id, D.nombre, P.id AS pais_id FROM departamento D JOIN pais P ON D.pais = P.id");

	    try (var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString());
	         var resultados = sentenciaPreparada.executeQuery()) {

	        while (resultados.next()) {
	            var departamento = new DepartamentoEntity();
	            departamento.setId(UtilUUID.convertirAUUID(resultados.getString("id")));
	            departamento.setNombre(resultados.getString("nombre"));

	            var pais = new PaisEntity();
                pais.setId(UtilUUID.convertirAUUID(resultados.getString("pais_id")));
	            departamento.setPais(pais);

	            listaDepartamentos.add(departamento);
	        }

	    } catch (SQLException exception) {
	        var mensajeUsuario = "Se ha presentado un problema tratando de consultar la información de los departamentos";
	        var mensajeTecnico = "Se presentó una excepción de tipo SQLexception tratando de hacer un SELECT en la tabla Departamento";
	        throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
	        
	    } catch (Exception exception) {
	        var mensajeUsuario = "Se ha presentado un problema INESPERADO tratando de consultar la información de los departamentos";
	        var mensajeTecnico = "Excepción NO CONTROLADA al hacer SELECT en la tabla Departamento";
	        throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
	    }

	    return listaDepartamentos;
	}

	
	public DepartamentoEntity listById(UUID id) throws LilfacException {
		var departamentoEntityRetorno=new DepartamentoEntity();
		var sentenciaSQL = new StringBuilder();
		
		sentenciaSQL.append("SELECT D.id, D.nombre, P.id AS pais_id FROM departamento D JOIN pais P ON D.pais = P.id WHERE D.id = ?");
		
		try(var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString())){
			
			sentenciaPreparada.setObject(1, id);
			
			try (var cursorResultados = sentenciaPreparada.executeQuery()){
				
				if (cursorResultados.next()) {
					departamentoEntityRetorno.setId(UtilUUID.convertirAUUID(cursorResultados.getString("id")));
					departamentoEntityRetorno.setNombre(cursorResultados.getString("nombre"));
					var pais = new PaisEntity();
	                pais.setId(UtilUUID.convertirAUUID(cursorResultados.getString("pais_id")));
					departamentoEntityRetorno.setPais(pais);
				}
				
			}
			
		} catch (SQLException exception) {
			var mensajeUsuario="Se ha presentado un problema tratando de consultar la información del departamento con el identificador deseado";
			var mensajeTecnico="Se presentó una excepción de tipo SQLexception tratando de hacer un SELECT en la tabla Departamento,  para tener más detalles revise el log de errores";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de consultar la información del departamento con el identificador deseado";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de hacer un SELECT en la tabla Departamento";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}
		
		return departamentoEntityRetorno;
	}

	
	public void update(UUID id, DepartamentoEntity entity) throws LilfacException {
		var sentenciaSQL = new StringBuilder();
		
		sentenciaSQL.append("UPDATE departamento SET nombre = ?, SET pais = ? WHERE id = ?");
		
		try(var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString())){
			
			sentenciaPreparada.setString(1, entity.getNombre());
			sentenciaPreparada.setObject(2, entity.getPais().getId());
			sentenciaPreparada.setObject(3, id);
			sentenciaPreparada.executeUpdate();
			
		} catch (SQLException exception) {
			var mensajeUsuario="Se ha presentado un problema tratando de actualizar la información de un departamento con el identificador ingresado";
			var mensajeTecnico="Se presentó una excepción de tipo SQLexception tratando de hacer un UPDATE en la tabla Departamento,  para tener más detalles revise el log de errores";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de actualizar la información de un departaento con el identificador ingresado";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de hacer un UPDATE en la tabla Departamento";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}
		
	}

	
	public void delete(UUID id) throws LilfacException {
		var sentenciaSQL = new StringBuilder();
		
		sentenciaSQL.append("DELETE FROM departamento WHERE id = ?");
		
		try(var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString())){
			
			sentenciaPreparada.setObject(1, id);
			sentenciaPreparada.executeUpdate();
			
		} catch (SQLException exception) {
			var mensajeUsuario="Se ha presentado un problema tratando de eliminar la información de un departamento con el identificador ingresado";
			var mensajeTecnico="Se presentó una excepción de tipo SQLexception tratando de hacer un DELETE en la tabla Departamento,  para tener más detalles revise el log de errores";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de eliminar la información de un departamento con el identificador ingresado";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de hacer un DELETE en la tabla Departamento";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}
		
	}

}
