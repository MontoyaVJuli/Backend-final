package co.edu.co.lilfac.data.dao.entity.categoria.impl.postgresql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import co.edu.co.lilfac.crosscutting.excepciones.DataLilfacException;
import co.edu.co.lilfac.crosscutting.excepciones.LilfacException;
import co.edu.co.lilfac.crosscutting.utilitarios.UtilUUID;
import co.edu.co.lilfac.data.dao.entity.categoria.CategoriaDAO;
import co.edu.co.lilfac.entity.CategoriaEntity;

public class CategoriaPostgreSQLDAO implements CategoriaDAO{
	
	private Connection conexion;
	
	public CategoriaPostgreSQLDAO(Connection conexion) {
		this.conexion=conexion;
	}

	@Override
	public void create(CategoriaEntity entity) throws LilfacException {
		var sentenciaSQL = new StringBuilder();
		
		sentenciaSQL.append("INSERT INTO categoria (id, nombre, descripcion) VALUES (?, ?, ?)");
		
		try(var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString())){
			
			sentenciaPreparada.setObject(1, entity.getId());
			sentenciaPreparada.setString(2, entity.getNombre());
			sentenciaPreparada.setString(3, entity.getDescripcion());
			sentenciaPreparada.executeUpdate();
			
		} catch (SQLException exception) {
			var mensajeUsuario="Se ha presentado un problema tratando de registrar la información de una nueva categoría";
			var mensajeTecnico="Se presentó una excepción de tipo SQLexception tratando de hacer un INSERT en la tabla Categoria,  para tener más detalles revise el log de errores";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de registrar la información de una nueva categoría";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de hacer un INSERT en la tabla Categoria";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}
			
	}


	@Override
	public List<CategoriaEntity> listByFilter(CategoriaEntity filter) throws LilfacException {
		var listaCategorias = new java.util.ArrayList<CategoriaEntity>();
		var sentenciaSQL = new StringBuilder();
		sentenciaSQL.append("SELECT id, nombre, descripcion FROM categoria WHERE 1=1");
		
		if (filter != null) {
			if (filter.getId() != null) {
				sentenciaSQL.append(" AND id = ?");
			}
			if (filter.getNombre() != null && !filter.getNombre().isBlank()) {
				sentenciaSQL.append(" AND nombre LIKE ?");
			}
			if (filter.getDescripcion() != null && !filter.getDescripcion().isBlank()) {
				sentenciaSQL.append(" AND descripcion LIKE ?");
			}
		}
		
		try (var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString())) {
			
			var indiceParametro = 1;
			
			if (filter != null) {
				if (filter.getId() != null) {
					sentenciaPreparada.setObject(indiceParametro++, filter.getId());
				}
				if (filter.getNombre() != null && !filter.getNombre().isBlank()) {
					sentenciaPreparada.setString(indiceParametro++, "%" + filter.getNombre() + "%");
				}
				if (filter.getDescripcion() != null && !filter.getDescripcion().isBlank()) {
					sentenciaPreparada.setString(indiceParametro++, "%" + filter.getDescripcion() + "%");
				}
			}
			
			try (var cursorResultados = sentenciaPreparada.executeQuery()) {
				
				while (cursorResultados.next()) {
		            var categoria = new CategoriaEntity();
		            categoria.setId(UtilUUID.convertirAUUID(cursorResultados.getString("id")));
		            categoria.setNombre(cursorResultados.getString("nombre"));
		            categoria.setDescripcion(cursorResultados.getString("descripcion"));

		            listaCategorias.add(categoria);
				}
			}
			
		} catch (SQLException exception) {
			var mensajeUsuario = "Se ha presentado un problema tratando de consultar las categorias con los filtros deseados.";
			var mensajeTecnico = "Se presentó una excepción SQLException ejecutando SELECT con filtros en la tabla Categoria.";

			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
			
		} catch (Exception exception) {
			var mensajeUsuario = "Se ha presentado un problema inesperado tratando de consultar las categorias con los filtros deseados.";
			var mensajeTecnico = "Se presentó una excepción NO CONTROLADA ejecutando SELECT con filtros en la tabla Categoria.";

			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}
		
		return listaCategorias;
	}

	@Override
	public List<CategoriaEntity> listAll() throws LilfacException {
	    List<CategoriaEntity> listaCategorias = new ArrayList<>();
	    var sentenciaSQL = new StringBuilder();

	    sentenciaSQL.append("SELECT id, nombre, descripcion FROM categoria");

	    try (var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString());
	         var resultados = sentenciaPreparada.executeQuery()) {

	        while (resultados.next()) {
	            var categoria = new CategoriaEntity();
	            categoria.setId(UtilUUID.convertirAUUID(resultados.getString("id")));
	            categoria.setNombre(resultados.getString("nombre"));
	            categoria.setDescripcion(resultados.getString("descripcion"));

	            listaCategorias.add(categoria);
	        }

	    } catch (SQLException exception) {
	        var mensajeUsuario = "Se ha presentado un problema tratando de consultar la información de las categorias";
	        var mensajeTecnico = "Se presentó una excepción de tipo SQLexception tratando de hacer un SELECT en la tabla Categoria";
	        throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
	    } catch (Exception exception) {
	        var mensajeUsuario = "Se ha presentado un problema INESPERADO tratando de consultar la información de las categorias";
	        var mensajeTecnico = "Excepción NO CONTROLADA al hacer SELECT en la tabla Categoria";
	        throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
	    }

	    return listaCategorias;
	}

	@Override
	public CategoriaEntity listById(UUID id) throws LilfacException {
		
		var categoriaEntityRetorno=new CategoriaEntity();
		var sentenciaSQL = new StringBuilder();
		
		sentenciaSQL.append("SELECT id, nombre, descripcion FROM categoria WHERE id = ?");
		
		try(var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString())){
			
			sentenciaPreparada.setObject(1, id);
			
			try (var cursorResultados = sentenciaPreparada.executeQuery()){
				
				if (cursorResultados.next()) {
					categoriaEntityRetorno.setId(UtilUUID.convertirAUUID(cursorResultados.getString("id")));
					categoriaEntityRetorno.setNombre(cursorResultados.getString("nombre"));
					categoriaEntityRetorno.setDescripcion(cursorResultados.getString("descripcion"));
				}
				
			}
			
		} catch (SQLException exception) {
			var mensajeUsuario="Se ha presentado un problema tratando de consultar la información de la categoría con el identificador deseado";
			var mensajeTecnico="Se presentó una excepción de tipo SQLexception tratando de hacer un SELECT en la tabla Categoria,  para tener más detalles revise el log de errores";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de consultar la información de la categoría con el identificador deseado";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de hacer un SELECT en la tabla Categoria";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}
		
		return categoriaEntityRetorno;
	}

	@Override
	public void update(UUID id, CategoriaEntity entity) throws LilfacException {
		var sentenciaSQL = new StringBuilder();
		
		sentenciaSQL.append("UPDATE categoria SET nombre = ?,  descripcion = ? WHERE id = ?");
		
		try(var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString())){
			
			sentenciaPreparada.setString(1, entity.getNombre());
			sentenciaPreparada.setString(2, entity.getDescripcion());
			sentenciaPreparada.setObject(3, id);
			sentenciaPreparada.executeUpdate();
			
		} catch (SQLException exception) {
			var mensajeUsuario="Se ha presentado un problema tratando de actualizar la información de una categoría con el identificador ingresado";
			var mensajeTecnico="Se presentó una excepción de tipo SQLexception tratando de hacer un UPDATE en la tabla Categoria,  para tener más detalles revise el log de errores";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de actualizar la información de una categoría con el identificador ingresado";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de hacer un UPDATE en la tabla Categoria";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}
		
	}

	@Override
	public void delete(UUID id) throws LilfacException {
		var sentenciaSQL = new StringBuilder();
		
		sentenciaSQL.append("DELETE FROM categoria WHERE id = ?");
		
		try(var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString())){
			
			sentenciaPreparada.setObject(1, id);
			sentenciaPreparada.executeUpdate();
			
		} catch (SQLException exception) {
			var mensajeUsuario="Se ha presentado un problema tratando de eliminar la información de una categoría con el identificador ingresado";
			var mensajeTecnico="Se presentó una excepción de tipo SQLexception tratando de hacer un DELETE en la tabla Categoria,  para tener más detalles revise el log de errores";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de eliminar la información de una categoría con el identificador ingresado";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de hacer un DELETE en la tabla Categoria";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}
			
	}

}
