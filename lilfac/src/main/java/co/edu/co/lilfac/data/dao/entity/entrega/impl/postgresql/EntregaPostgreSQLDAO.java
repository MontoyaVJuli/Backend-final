package co.edu.co.lilfac.data.dao.entity.entrega.impl.postgresql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import co.edu.co.lilfac.crosscutting.excepciones.DataLilfacException;
import co.edu.co.lilfac.crosscutting.excepciones.LilfacException;
import co.edu.co.lilfac.crosscutting.utilitarios.UtilUUID;
import co.edu.co.lilfac.data.dao.entity.entrega.EntregaDAO;
import co.edu.co.lilfac.entity.CiudadEntity;
import co.edu.co.lilfac.entity.EmpleadoEntity;
import co.edu.co.lilfac.entity.EntregaEntity;
import co.edu.co.lilfac.entity.PedidoEntity;

public class EntregaPostgreSQLDAO implements EntregaDAO{
	
	private Connection conexion;
	
	public EntregaPostgreSQLDAO(Connection conexion) {
		this.conexion=conexion;
	}

	@Override
	public void create(EntregaEntity entity) throws LilfacException {
		var sentenciaSQL = new StringBuilder();
		
		sentenciaSQL.append("INSERT INTO entrega (id, fecha, estado, direccion, ciudad, empleado, pedido) VALUES (?, ?, ?, ?, ?, ?, ?)");
		
		try(var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString())){
			
			sentenciaPreparada.setObject(1, entity.getId());
			sentenciaPreparada.setString(2, entity.getFecha());
			sentenciaPreparada.setString(3, entity.getEstado());
			sentenciaPreparada.setString(4, entity.getDireccion());
			sentenciaPreparada.setObject(5, entity.getCiudad().getId());
			sentenciaPreparada.setObject(6, entity.getEmpleado().getId());
			sentenciaPreparada.setObject(7, entity.getPedido().getId());
			
			sentenciaPreparada.executeUpdate();
			
		} catch (SQLException exception) {
			var mensajeUsuario="Se ha presentado un problema tratando de registrar la información de una nueva entrega";
			var mensajeTecnico="Se presentó una excepción de tipo SQLexception tratando de hacer un INSERT en la tabla Entrega,  para tener más detalles revise el log de errores";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de registrar la información de una nueva entrega";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de hacer un INSERT en la tabla Entrega";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}	
	}

	@Override
	public List<EntregaEntity> listByFilter(EntregaEntity filter) throws LilfacException {
		var listaEntregas = new java.util.ArrayList<EntregaEntity>();
		var sentenciaSQL = new StringBuilder();
		sentenciaSQL.append("SELECT E.id, E.fecha, E.estado, E.direccion, C.nombre AS nombre_ciudad, EM.nombre AS nombre_empleado, P.id AS pedido FROM entrega E JOIN ciudad C ON E.ciudad = C.id JOIN empleado EM ON E.empleado = EM.id JOIN pedido P ON E.pedido = P.id WHERE 1=1");
		
		if (filter != null) {
			if (filter.getId() != null) {
				sentenciaSQL.append(" AND E.id = ?");
			}
			if (filter.getFecha() != null && !filter.getFecha().isBlank()) {
				sentenciaSQL.append(" AND E.fecha LIKE ?");
			}
			if (filter.getEstado() != null && !filter.getEstado().isBlank()) {
				sentenciaSQL.append(" AND E.estado LIKE ?");
			}
			if (filter.getDireccion() != null && !filter.getDireccion().isBlank()) {
				sentenciaSQL.append(" AND E.direccion LIKE ?");
			}
			if (filter.getCiudad() != null) {
				sentenciaSQL.append(" AND C.nombre = ?");
			}
			if (filter.getEmpleado() != null) {
				sentenciaSQL.append(" AND EM.nombre = ?");
			}
			if (filter.getPedido() != null) {
				sentenciaSQL.append(" AND P.id = ?");
			}
			
		}
		
		try (var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString())) {
			
			var indiceParametro = 1;
			
			if (filter != null) {
				if (filter.getId() != null) {
					sentenciaPreparada.setObject(indiceParametro++, filter.getId());
				}
				if (filter.getFecha() != null && !filter.getFecha().isBlank()) {
					sentenciaPreparada.setString(indiceParametro++, "%" + filter.getFecha() + "%");
				}
				if (filter.getEstado() != null && !filter.getEstado().isBlank()) {
					sentenciaPreparada.setString(indiceParametro++, "%" + filter.getEstado() + "%");
				}
				if (filter.getDireccion() != null && !filter.getDireccion().isBlank()) {
					sentenciaPreparada.setString(indiceParametro++, "%" + filter.getDireccion() + "%");
				}
				if (filter.getCiudad() != null) {
					sentenciaPreparada.setObject(indiceParametro++, filter.getCiudad().getNombre());
				}
				if (filter.getEmpleado() != null) {
					sentenciaPreparada.setObject(indiceParametro++, filter.getEmpleado().getNombre());
				}
				if (filter.getPedido() != null) {
					sentenciaPreparada.setObject(indiceParametro++, filter.getPedido().getId());
				}
			}
			
			try (var cursorResultados = sentenciaPreparada.executeQuery()) {
				
				while (cursorResultados.next()) {
		            var entrega = new EntregaEntity();
		            entrega.setId(UtilUUID.convertirAUUID(cursorResultados.getString("id")));
		            entrega.setFecha(cursorResultados.getString("fecha"));
		            entrega.setEstado(cursorResultados.getString("Estado"));
		            entrega.setDireccion(cursorResultados.getString("direccion"));

		            var ciudad = new CiudadEntity();
		            ciudad.setNombre(cursorResultados.getString("nombre_ciudad"));
		            entrega.setCiudad(ciudad);

		            var empleado = new EmpleadoEntity();
		            empleado.setNombre(cursorResultados.getString("nombre_empleado"));
		            entrega.setEmpleado(empleado);

		            var pedido = new PedidoEntity();
		            pedido.setId(UtilUUID.convertirAUUID(cursorResultados.getString("pedido")));
		            entrega.setPedido(pedido);

		            listaEntregas.add(entrega);
				}
			}
			
		} catch (SQLException exception) {
			var mensajeUsuario = "Se ha presentado un problema tratando de consultar las entregas con los filtros deseados.";
			var mensajeTecnico = "Se presentó una excepción SQLException ejecutando SELECT con filtros en la tabla Entrega.";

			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
			
		} catch (Exception exception) {
			var mensajeUsuario = "Se ha presentado un problema inesperado tratando de consultar las entregas con los filtros deseados.";
			var mensajeTecnico = "Se presentó una excepción NO CONTROLADA ejecutando SELECT con filtros en la tabla Entrega.";

			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}
		
		return listaEntregas;
	}

	@Override
	public List<EntregaEntity> listAll() throws LilfacException {
	    List<EntregaEntity> listaEntregas = new ArrayList<>();
	    var sentenciaSQL = new StringBuilder();

	    sentenciaSQL.append("SELECT E.id, E.fecha, E.estado, E.direccion, C.nombre AS nombre_ciudad, EM.nombre AS nombre_empleado, P.id AS pedido FROM entrega E JOIN ciudad C ON E.ciudad = C.id JOIN empleado EM ON E.empleado = EM.id JOIN pedido P ON E.pedido = P.id");

	    try (var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString());
	         var resultados = sentenciaPreparada.executeQuery()) {

	        while (resultados.next()) {
	            var entrega = new EntregaEntity();
	            entrega.setId(UtilUUID.convertirAUUID(resultados.getString("id")));
	            entrega.setFecha(resultados.getString("fecha"));
	            entrega.setEstado(resultados.getString("Estado"));
	            entrega.setDireccion(resultados.getString("direccion"));

	            var ciudad = new CiudadEntity();
	            ciudad.setNombre(resultados.getString("nombre_ciudad"));
	            entrega.setCiudad(ciudad);

	            var empleado = new EmpleadoEntity();
	            empleado.setNombre(resultados.getString("nombre_empleado"));
	            entrega.setEmpleado(empleado);

	            var pedido = new PedidoEntity();
	            pedido.setId(UtilUUID.convertirAUUID(resultados.getString("pedido")));
	            entrega.setPedido(pedido);

	            listaEntregas.add(entrega);
	        }

	    } catch (SQLException exception) {
	        var mensajeUsuario = "Se ha presentado un problema tratando de consultar la información de las entregas";
	        var mensajeTecnico = "Se presentó una excepción de tipo SQLexception tratando de hacer un SELECT en la tabla Entrega";
	        throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
	    } catch (Exception exception) {
	        var mensajeUsuario = "Se ha presentado un problema INESPERADO tratando de consultar la información de las entregas";
	        var mensajeTecnico = "Excepción NO CONTROLADA al hacer SELECT en la tabla Entrega";
	        throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
	    }

	    return listaEntregas;
	}

	@Override
	public EntregaEntity listById(UUID id) throws LilfacException {
		var entregaEntityRetorno=new EntregaEntity();
		var sentenciaSQL = new StringBuilder();
		
		sentenciaSQL.append("SELECT E.id, E.fecha, E.estado, E.direccion, C.nombre AS nombre_ciudad, EM.nombre AS nombre_empleado, P.id AS pedido FROM entrega E JOIN ciudad C ON E.ciudad = C.id JOIN empleado EM ON E.empleado = EM.id JOIN pedido P ON E.pedido = P.id WHERE E.id = ?");
		
		try(var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString())){
			
			sentenciaPreparada.setObject(1, id);
			
			try (var cursorResultados = sentenciaPreparada.executeQuery()){
				
				if (cursorResultados.next()) {
					entregaEntityRetorno.setId(UtilUUID.convertirAUUID(cursorResultados.getString("id")));
					entregaEntityRetorno.setFecha(cursorResultados.getString("fecha"));
					entregaEntityRetorno.setEstado(cursorResultados.getString("estado"));
					entregaEntityRetorno.setDireccion(cursorResultados.getString("direccion"));
	
					var ciudad = new CiudadEntity();
					ciudad.setNombre(cursorResultados.getString("nombre_ciudad"));
					entregaEntityRetorno.setCiudad(ciudad);
					
					var empleado = new EmpleadoEntity();
					empleado.setNombre(cursorResultados.getString("nombre_empleado"));
					entregaEntityRetorno.setEmpleado(empleado);
					
					var pedido = new PedidoEntity();
					pedido.setId(UtilUUID.convertirAUUID(cursorResultados.getString("pedido")));
					entregaEntityRetorno.setPedido(pedido);
					
				}
				
			}
			
		} catch (SQLException exception) {
			var mensajeUsuario="Se ha presentado un problema tratando de consultar la información de la entrega con el identificador deseado";
			var mensajeTecnico="Se presentó una excepción de tipo SQLexception tratando de hacer un SELECT en la tabla Entrega,  para tener más detalles revise el log de errores";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de consultar la información de la entrega con el identificador deseado";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de hacer un SELECT en la tabla Entrega";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}
		
		return entregaEntityRetorno;
	}

	@Override
	public void update(UUID id, EntregaEntity entity) throws LilfacException {
		var sentenciaSQL = new StringBuilder();
		
		sentenciaSQL.append("UPDATE entrega SET  fecha = ?, estado = ?, direccion = ?, ciudad = ?, empleado = ?, pedido = ? WHERE id = ?");
		
		try(var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString())){
			
			sentenciaPreparada.setString(1, entity.getFecha());
			sentenciaPreparada.setString(2, entity.getEstado());
			sentenciaPreparada.setString(3,  entity.getDireccion());
			sentenciaPreparada.setObject(4,  entity.getCiudad().getId());
			sentenciaPreparada.setObject(5,  entity.getEmpleado().getId());
			sentenciaPreparada.setObject(6,  entity.getPedido().getId());
			sentenciaPreparada.setObject(7, id);
			sentenciaPreparada.executeUpdate();
			
		} catch (SQLException exception) {
			var mensajeUsuario="Se ha presentado un problema tratando de actualizar la información de una entrega con el identificador ingresado";
			var mensajeTecnico="Se presentó una excepción de tipo SQLexception tratando de hacer un UPDATE en la tabla Entrega,  para tener más detalles revise el log de errores";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de actualizar la información de una entrega con el identificador ingresado";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de hacer un UPDATE en la tabla Entrega";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}
	}

	@Override
	public void delete(UUID id) throws LilfacException {
		var sentenciaSQL = new StringBuilder();
		
		sentenciaSQL.append("DELETE FROM entrega WHERE id = ?");
		
		try(var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString())){
			
			sentenciaPreparada.setObject(1, id);
			sentenciaPreparada.executeUpdate();
			
		} catch (SQLException exception) {
			var mensajeUsuario="Se ha presentado un problema tratando de eliminar la información de una entrega con el identificador ingresado";
			var mensajeTecnico="Se presentó una excepción de tipo SQLexception tratando de hacer un DELETE en la tabla Entrega,  para tener más detalles revise el log de errores";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de eliminar la información de una entrega pedido con el identificador ingresado";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de hacer un DELETE en la tabla Entrega";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}	
	}

}
