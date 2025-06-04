package co.edu.co.lilfac.data.dao.entity.historialcosto.impl.postgresql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import co.edu.co.lilfac.crosscutting.excepciones.DataLilfacException;
import co.edu.co.lilfac.crosscutting.excepciones.LilfacException;
import co.edu.co.lilfac.crosscutting.utilitarios.UtilUUID;
import co.edu.co.lilfac.data.dao.entity.historialcosto.HistorialCostoDAO;
import co.edu.co.lilfac.entity.HistorialCostoEntity;
import co.edu.co.lilfac.entity.ProductoEntity;

public class HistorialCostoPostgreSQLDAO implements HistorialCostoDAO{
	
	private Connection conexion;
	
	public HistorialCostoPostgreSQLDAO(Connection conexion) {
		this.conexion=conexion;
	}

	@Override
	public void create(HistorialCostoEntity entity) throws LilfacException {
		var sentenciaSQL = new StringBuilder();
		
		sentenciaSQL.append("INSERT INTO historialCosto (id, codigo, fechaInicio, fechaFin, estado, costo, producto) VALUES (?, ?, ?, ?, ?, ?, ?)");
		
		try(var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString())){
			
			sentenciaPreparada.setObject(1, entity.getId());
			sentenciaPreparada.setInt(2, entity.getCodigo());
			sentenciaPreparada.setString(3, entity.getFechaInicio());
			sentenciaPreparada.setString(4, entity.getFechaFin());
			sentenciaPreparada.setBoolean(5, entity.isEstado());
			sentenciaPreparada.setFloat(6, entity.getCosto());
			sentenciaPreparada.setObject(7, entity.getProducto().getId());
			
			
			sentenciaPreparada.executeUpdate();
			
		} catch (SQLException exception) {
			var mensajeUsuario="Se ha presentado un problema tratando de registrar la información de un nuevo historial de costos";
			var mensajeTecnico="Se presentó una excepción de tipo SQLexception tratando de hacer un INSERT en la tabla HistorialCosto,  para tener más detalles revise el log de errores";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de registrar la información de un nuevo historial de costos";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de hacer un INSERT en la tabla HistorialCosto";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}
	}

	@Override
	public List<HistorialCostoEntity> listByFilter(HistorialCostoEntity filter) throws LilfacException {
		var listaHistorialesCosto = new java.util.ArrayList<HistorialCostoEntity>();
		var sentenciaSQL = new StringBuilder();
		sentenciaSQL.append("SELECT H.id, H.codigo, H.fechaInicio, H.fechaFin, H.estado, H.costo, P.nombre AS nombre_producto FROM historialCosto H JOIN producto P ON H.producto = P.id WHERE 1=1");
		
		if (filter != null) {
			if (filter.getId() != null) {
				sentenciaSQL.append(" AND H.id = ?");
			}
			if (filter.getCodigo() != null) {
				sentenciaSQL.append(" AND H.codigo = ?");
			}
			if (filter.getFechaInicio() != null && !filter.getFechaInicio().isBlank()) {
				sentenciaSQL.append(" AND H.fechaInicio LIKE ?");
			}
			if (filter.getFechaFin() != null && !filter.getFechaFin().isBlank()) {
				sentenciaSQL.append(" AND H.fechaFin LIKE ?");
			}
			if (filter.isEstado() != null) {
				sentenciaSQL.append(" AND H.estado = ?");
			}
			if (filter.getCosto() != null) {
				sentenciaSQL.append(" AND H.costo = ?");
			}
			if (filter.getProducto() != null) {
				sentenciaSQL.append(" AND P.nombre = ?");
			}
		}
		
		try (var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString())) {
			
			var indiceParametro = 1;
			
			if (filter != null) {
				if (filter.getId() != null) {
					sentenciaPreparada.setObject(indiceParametro++, filter.getId());
				}
				if (filter.getCodigo() != null) {
					sentenciaPreparada.setObject(indiceParametro++, filter.getCodigo());
				}
				if (filter.getFechaInicio() != null && !filter.getFechaInicio().isBlank()) {
					sentenciaPreparada.setString(indiceParametro++, "%" + filter.getFechaInicio() + "%");
				}
				if (filter.getFechaFin() != null && !filter.getFechaFin().isBlank()) {
					sentenciaPreparada.setString(indiceParametro++, "%" + filter.getFechaFin() + "%");
				}
				if (filter.isEstado() != null) {
					sentenciaPreparada.setObject(indiceParametro++, filter.isEstado());
				}
				if (filter.getCosto() != null) {
					sentenciaPreparada.setObject(indiceParametro++, filter.getCosto());
				}
				if (filter.getProducto() != null) {
					sentenciaPreparada.setObject(indiceParametro++, filter.getProducto().getNombre());
				}
			}
			
			try (var cursorResultados = sentenciaPreparada.executeQuery()) {
				
				while (cursorResultados.next()) {
		            var historialCosto = new HistorialCostoEntity();
		            historialCosto.setId(UtilUUID.convertirAUUID(cursorResultados.getString("id")));
		            historialCosto.setCodigo(cursorResultados.getInt("codigo"));
		            historialCosto.setFechaInicio(cursorResultados.getString("fechaInicio"));
		            historialCosto.setFechaFin(cursorResultados.getString("fechaFin"));
		            historialCosto.setEstado(cursorResultados.getBoolean("estado"));
		            historialCosto.setCosto(cursorResultados.getFloat("costo"));

		            var producto = new ProductoEntity();
		            producto.setNombre(cursorResultados.getString("nombre_producto"));
		            historialCosto.setProducto(producto);

		            listaHistorialesCosto.add(historialCosto);
				}
			}
			
		} catch (SQLException exception) {
			var mensajeUsuario = "Se ha presentado un problema tratando de consultar los historiales de costo con los filtros deseados.";
			var mensajeTecnico = "Se presentó una excepción SQLException ejecutando SELECT con filtros en la tabla HistorialCosto.";

			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
			
		} catch (Exception exception) {
			var mensajeUsuario = "Se ha presentado un problema inesperado tratando de consultar los historiales de costo con los filtros deseados.";
			var mensajeTecnico = "Se presentó una excepción NO CONTROLADA ejecutando SELECT con filtros en la tabla HistorialCosto.";

			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}
		
		return listaHistorialesCosto;
	}

	@Override
	public List<HistorialCostoEntity> listAll() throws LilfacException {
	    List<HistorialCostoEntity> listaHistorialesCosto = new ArrayList<>();
	    var sentenciaSQL = new StringBuilder();

	    sentenciaSQL.append("SELECT H.id, H.codigo, H.fechaInicio, H.fechaFin, H.estado, H.costo, P.nombre AS nombre_producto FROM historialCosto H JOIN producto P ON H.producto = P.id");

	    try (var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString());
	         var resultados = sentenciaPreparada.executeQuery()) {

	        while (resultados.next()) {
	            var historialCosto = new HistorialCostoEntity();
	            historialCosto.setId(UtilUUID.convertirAUUID(resultados.getString("id")));
	            historialCosto.setCodigo(resultados.getInt("codigo"));
	            historialCosto.setFechaInicio(resultados.getString("fechaInicio"));
	            historialCosto.setFechaFin(resultados.getString("fechaFin"));
	            historialCosto.setEstado(resultados.getBoolean("estado"));
	            historialCosto.setCosto(resultados.getFloat("costo"));

	            var producto = new ProductoEntity();
	            producto.setNombre(resultados.getString("nombre_producto"));
	            historialCosto.setProducto(producto);

	            listaHistorialesCosto.add(historialCosto);
	        }

	    } catch (SQLException exception) {
	        var mensajeUsuario = "Se ha presentado un problema tratando de consultar la información de los historiales de costo";
	        var mensajeTecnico = "Se presentó una excepción de tipo SQLexception tratando de hacer un SELECT en la tabla HistorialCosto";
	        throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
	    } catch (Exception exception) {
	        var mensajeUsuario = "Se ha presentado un problema INESPERADO tratando de consultar la información de los historiales de costo";
	        var mensajeTecnico = "Excepción NO CONTROLADA al hacer SELECT en la tabla HistorialCosto";
	        throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
	    }

	    return listaHistorialesCosto;
	}

	@Override
	public HistorialCostoEntity listById(UUID id) throws LilfacException {
		var historialCostoEntityRetorno=new HistorialCostoEntity();
		var sentenciaSQL = new StringBuilder();
		
		sentenciaSQL.append("SELECT H.id, H.codigo, H.fechaInicio, H.fechaFin, H.estado, H.costo, P.nombre AS nombre_producto FROM historialCosto H JOIN producto P ON H.producto = P.id WHERE H.id = ?");
		
		try(var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString())){
			
			sentenciaPreparada.setObject(1, id);
			
			try (var cursorResultados = sentenciaPreparada.executeQuery()){
				
				if (cursorResultados.next()) {
					historialCostoEntityRetorno.setId(UtilUUID.convertirAUUID(cursorResultados.getString("id")));
					historialCostoEntityRetorno.setCodigo(cursorResultados.getInt("codigo"));
					historialCostoEntityRetorno.setFechaInicio(cursorResultados.getString("fechaInicio"));
					historialCostoEntityRetorno.setFechaFin(cursorResultados.getString("fechaFin"));
					historialCostoEntityRetorno.setEstado(cursorResultados.getBoolean("estado"));
					historialCostoEntityRetorno.setCosto(cursorResultados.getFloat("costo"));
					var producto = new ProductoEntity();
					producto.setNombre(cursorResultados.getString("nombre_producto"));
					historialCostoEntityRetorno.setProducto(producto);
				}
				
			}
			
		} catch (SQLException exception) {
			var mensajeUsuario="Se ha presentado un problema tratando de consultar la información del historial de costos con el identificador deseado";
			var mensajeTecnico="Se presentó una excepción de tipo SQLexception tratando de hacer un SELECT en la tabla HistorialCosto,  para tener más detalles revise el log de errores";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de consultar la información del historial de costos con el identificador deseado";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de hacer un SELECT en la tabla HistorialCosto";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}
		
		return historialCostoEntityRetorno;
	}

	@Override
	public void update(UUID id, HistorialCostoEntity entity) throws LilfacException {
		var sentenciaSQL = new StringBuilder();
		
		sentenciaSQL.append("UPDATE historialCosto SET codigo = ?, fechaInicio = ?, fechaFin = ?, estado = ?, costo = ?, producto = ? WHERE id = ?");
		
		try(var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString())){
			
			sentenciaPreparada.setInt(1,  entity.getCodigo());
			sentenciaPreparada.setString(2, entity.getFechaInicio());
			sentenciaPreparada.setString(3, entity.getFechaFin());
			sentenciaPreparada.setBoolean(4, entity.isEstado());
			sentenciaPreparada.setFloat(5,  entity.getCosto());
			sentenciaPreparada.setObject(6,  entity.getProducto().getId());
			sentenciaPreparada.setObject(7,  id);
			sentenciaPreparada.executeUpdate();
			
			
			
		} catch (SQLException exception) {
			var mensajeUsuario="Se ha presentado un problema tratando de actualizar la información de un historial de costos con el identificador ingresado";
			var mensajeTecnico="Se presentó una excepción de tipo SQLexception tratando de hacer un UPDATE en la tabla HistorialCosto,  para tener más detalles revise el log de errores";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de actualizar la información de un historial de costos con el identificador ingresado";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de hacer un UPDATE en la tabla HistorialCosto";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}
	}

	@Override
	public void delete(UUID id) throws LilfacException {
		var sentenciaSQL = new StringBuilder();
		
		sentenciaSQL.append("DELETE FROM historialCosto WHERE id = ?");
		
		try(var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString())){
			
			sentenciaPreparada.setObject(1, id);
			sentenciaPreparada.executeUpdate();
			
		} catch (SQLException exception) {
			var mensajeUsuario="Se ha presentado un problema tratando de eliminar la información de un historial de costos con el identificador ingresado";
			var mensajeTecnico="Se presentó una excepción de tipo SQLexception tratando de hacer un DELETE en la tabla HistorialCosto,  para tener más detalles revise el log de errores";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de eliminar la información de un historial de costos con el identificador ingresado";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de hacer un DELETE en la tabla HistorialCosto";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}
	}

}
