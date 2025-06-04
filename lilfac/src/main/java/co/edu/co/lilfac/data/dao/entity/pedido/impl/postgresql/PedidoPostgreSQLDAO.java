package co.edu.co.lilfac.data.dao.entity.pedido.impl.postgresql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import co.edu.co.lilfac.crosscutting.excepciones.DataLilfacException;
import co.edu.co.lilfac.crosscutting.excepciones.LilfacException;
import co.edu.co.lilfac.crosscutting.utilitarios.UtilUUID;
import co.edu.co.lilfac.data.dao.entity.pedido.PedidoDAO;
import co.edu.co.lilfac.entity.CiudadEntity;
import co.edu.co.lilfac.entity.ClienteEntity;
import co.edu.co.lilfac.entity.EmpleadoEntity;
import co.edu.co.lilfac.entity.PedidoEntity;

public class PedidoPostgreSQLDAO implements PedidoDAO{
	
	private Connection conexion;
	
	public PedidoPostgreSQLDAO(Connection conexion) {
		this.conexion=conexion;
	}

	@Override
	public void create(PedidoEntity entity) throws LilfacException {
		var sentenciaSQL = new StringBuilder();
		
		sentenciaSQL.append("INSERT INTO pedido (id, fechaReserva, fechaVencimiento, direccionEntrega, costo, abono, restante, ciudad, cliente, empleado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		
		try(var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString())){
			
			sentenciaPreparada.setObject(1, entity.getId());
			sentenciaPreparada.setString(2, entity.getFechaReserva());
			sentenciaPreparada.setString(3, entity.getFechaVencimiento());
			sentenciaPreparada.setString(4, entity.getDireccionEntrega());
			sentenciaPreparada.setFloat(5,  entity.getCosto());
			sentenciaPreparada.setFloat(6,  entity.getAbono());
			sentenciaPreparada.setFloat(7,  entity.getRestante());
			sentenciaPreparada.setObject(8,  entity.getCiudad().getId());
			sentenciaPreparada.setObject(9,  entity.getCliente().getId());
			sentenciaPreparada.setObject(10,  entity.getEmpleado().getId());
			sentenciaPreparada.executeUpdate();
			
		} catch (SQLException exception) {
			var mensajeUsuario="Se ha presentado un problema tratando de registrar la información de un nuevo pedido";
			var mensajeTecnico="Se presentó una excepción de tipo SQLexception tratando de hacer un INSERT en la tabla Pedido,  para tener más detalles revise el log de errores";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de registrar la información de un nuevo pedido";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de hacer un INSERT en la tabla Pedido";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}	
	}

	@Override
	public List<PedidoEntity> listByFilter(PedidoEntity filter) throws LilfacException {
		var listaPedidos = new java.util.ArrayList<PedidoEntity>();
		var sentenciaSQL = new StringBuilder();
		sentenciaSQL.append("SELECT P.id, P.fechaReserva, P.fechaVencimiento, P.direccionEntrega, P.costo, P.abono, P.restante, C.nombre AS nombre_ciudad, CL.nombre AS nombre_cliente, E.nombre AS nombre_empleado FROM pedido P JOIN ciudad C ON P.ciudad = C.id JOIN cliente CL ON P.cliente = CL.id JOIN empleado E ON P.empleado = E.id WHERE 1=1");
		
		if (filter != null) {
			if (filter.getId() != null) {
				sentenciaSQL.append(" AND P.id = ?");
			}
			if (filter.getFechaReserva() != null && !filter.getFechaReserva().isBlank()) {
				sentenciaSQL.append(" AND P.fechaReserva LIKE ?");
			}
			if (filter.getFechaVencimiento() != null && !filter.getFechaVencimiento().isBlank()) {
				sentenciaSQL.append(" AND P.fechaVencimiento LIKE ?");
			}
			if (filter.getDireccionEntrega() != null && !filter.getDireccionEntrega().isBlank()) {
				sentenciaSQL.append(" AND P.direccionEntrega LIKE ?");
			}
			if (filter.getCosto() != null) {
				sentenciaSQL.append(" AND P.costo = ?");
			}
			if (filter.getAbono() != null) {
				sentenciaSQL.append(" AND P.abono = ?");
			}
			if (filter.getRestante() != null) {
				sentenciaSQL.append(" AND P.restante = ?");
			}
			if (filter.getCiudad() != null) {
				sentenciaSQL.append(" AND C.nombre = ?");
			}
			if (filter.getCliente() != null) {
				sentenciaSQL.append(" AND CL.nombre = ?");
			}
			if (filter.getEmpleado() != null) {
				sentenciaSQL.append(" AND E.nombre = ?");
			}
		}
		
		try (var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString())) {
			
			var indiceParametro = 1;
			
			if (filter != null) {
				if (filter.getId() != null) {
					sentenciaPreparada.setObject(indiceParametro++, filter.getId());
				}
				if (filter.getFechaReserva() != null && !filter.getFechaReserva().isBlank()) {
					sentenciaPreparada.setString(indiceParametro++, "%" + filter.getFechaReserva() + "%");
				}
				if (filter.getFechaVencimiento() != null && !filter.getFechaVencimiento().isBlank()) {
					sentenciaPreparada.setString(indiceParametro++, "%" + filter.getFechaVencimiento() + "%");
				}
				if (filter.getDireccionEntrega() != null && !filter.getDireccionEntrega().isBlank()) {
					sentenciaPreparada.setString(indiceParametro++, "%" + filter.getDireccionEntrega() + "%");
				}
				if (filter.getCosto() != null) {
					sentenciaPreparada.setObject(indiceParametro++, filter.getCosto());
				}
				if (filter.getAbono() != null) {
					sentenciaPreparada.setObject(indiceParametro++, filter.getAbono());
				}
				if (filter.getRestante() != null) {
					sentenciaPreparada.setObject(indiceParametro++, filter.getRestante());
				}
				if (filter.getCiudad() != null) {
					sentenciaPreparada.setObject(indiceParametro++, filter.getCiudad().getNombre());
				}
				if (filter.getCliente() != null) {
					sentenciaPreparada.setObject(indiceParametro++, filter.getCliente().getNombre());
				}
				if (filter.getEmpleado() != null) {
					sentenciaPreparada.setObject(indiceParametro++, filter.getEmpleado().getNombre());
				}
			}
			
			try (var cursorResultados = sentenciaPreparada.executeQuery()) {
				
				while (cursorResultados.next()) {
		            var pedido = new PedidoEntity();
		            pedido.setId(UtilUUID.convertirAUUID(cursorResultados.getString("id")));
		            pedido.setFechaReserva(cursorResultados.getString("fechaReserva"));
		            pedido.setFechaVencimiento(cursorResultados.getString("fechaVencimiento"));
		            pedido.setDireccionEntrega(cursorResultados.getString("direccionEntrega"));
		            pedido.setCosto(cursorResultados.getFloat("costo"));
		            pedido.setAbono(cursorResultados.getFloat("abono"));
		            pedido.setRestante(cursorResultados.getFloat("restante"));

		            var ciudad = new CiudadEntity();
		            ciudad.setNombre(cursorResultados.getString("nombre_ciudad"));
		            pedido.setCiudad(ciudad);

		            var cliente = new ClienteEntity();
		            cliente.setNombre(cursorResultados.getString("nombre_cliente"));
		            pedido.setCliente(cliente);

		            var empleado = new EmpleadoEntity();
		            empleado.setNombre(cursorResultados.getString("nombre_empleado"));
		            pedido.setEmpleado(empleado);

		            listaPedidos.add(pedido);
				}
			}
			
		} catch (SQLException exception) {
			var mensajeUsuario = "Se ha presentado un problema tratando de consultar los pedidos con los filtros deseados.";
			var mensajeTecnico = "Se presentó una excepción SQLException ejecutando SELECT con filtros en la tabla Pedido.";

			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
			
		} catch (Exception exception) {
			var mensajeUsuario = "Se ha presentado un problema inesperado tratando de consultar los pedidos con los filtros deseados.";
			var mensajeTecnico = "Se presentó una excepción NO CONTROLADA ejecutando SELECT con filtros en la tabla Pedido.";

			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}
		
		return listaPedidos;
	}

	@Override
	public List<PedidoEntity> listAll() throws LilfacException {
	    List<PedidoEntity> listaPedidos = new ArrayList<>();
	    var sentenciaSQL = new StringBuilder();

	    sentenciaSQL.append("SELECT P.id, P.fechaReserva, P.fechaVencimiento, P.direccionEntrega, P.costo, P.abono, P.restante, C.nombre AS nombre_ciudad, CL.nombre AS nombre_cliente, E.nombre AS nombre_empleado FROM pedido P JOIN ciudad C ON P.ciudad = C.id JOIN cliente CL ON P.cliente = CL.id JOIN empleado E ON P.empleado = E.id");

	    try (var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString());
	         var resultados = sentenciaPreparada.executeQuery()) {

	        while (resultados.next()) {
	            var pedido = new PedidoEntity();
	            pedido.setId(UtilUUID.convertirAUUID(resultados.getString("id")));
	            pedido.setFechaReserva(resultados.getString("fechaReserva"));
	            pedido.setFechaVencimiento(resultados.getString("fechaVencimiento"));
	            pedido.setDireccionEntrega(resultados.getString("direccionEntrega"));
	            pedido.setCosto(resultados.getFloat("costo"));
	            pedido.setAbono(resultados.getFloat("abono"));
	            pedido.setRestante(resultados.getFloat("restante"));

	            var ciudad = new CiudadEntity();
	            ciudad.setNombre(resultados.getString("nombre_ciudad"));
	            pedido.setCiudad(ciudad);

	            var cliente = new ClienteEntity();
	            cliente.setNombre(resultados.getString("nombre_cliente"));
	            pedido.setCliente(cliente);

	            var empleado = new EmpleadoEntity();
	            empleado.setNombre(resultados.getString("nombre_empleado"));
	            pedido.setEmpleado(empleado);

	            listaPedidos.add(pedido);
	        }

	    } catch (SQLException exception) {
	        var mensajeUsuario = "Se ha presentado un problema tratando de consultar la información de los pedidos";
	        var mensajeTecnico = "Se presentó una excepción de tipo SQLexception tratando de hacer un SELECT en la tabla Pedido";
	        throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
	    } catch (Exception exception) {
	        var mensajeUsuario = "Se ha presentado un problema INESPERADO tratando de consultar la información de los pedidos";
	        var mensajeTecnico = "Excepción NO CONTROLADA al hacer SELECT en la tabla Pedido";
	        throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
	    }

	    return listaPedidos;
	}

	@Override
	public PedidoEntity listById(UUID id) throws LilfacException {
		var pedidoEntityRetorno=new PedidoEntity();
		var sentenciaSQL = new StringBuilder();
		
		sentenciaSQL.append("SELECT P.id, P.fechaReserva, P.fechaVencimiento, P.direccionEntrega, P.costo, P.abono, P.restante, C.nombre AS nombre_ciudad, CL.nombre AS nombre_cliente, E.nombre AS nombre_empleado FROM pedido P JOIN ciudad C ON P.ciudad = C.id JOIN cliente CL ON P.cliente = CL.id JOIN empleado E ON P.empleado = E.id WHERE P.id = ? ");
		
		try(var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString())){
			
			sentenciaPreparada.setObject(1, id);
			
			try (var cursorResultados = sentenciaPreparada.executeQuery()){
				
				if (cursorResultados.next()) {
					pedidoEntityRetorno.setId(UtilUUID.convertirAUUID(cursorResultados.getString("id")));
					pedidoEntityRetorno.setFechaReserva(cursorResultados.getString("fechaReserva"));
					pedidoEntityRetorno.setFechaVencimiento(cursorResultados.getString("fechaVencimiento"));
					pedidoEntityRetorno.setDireccionEntrega(cursorResultados.getString("direccionEntrega"));
					pedidoEntityRetorno.setCosto(cursorResultados.getFloat("costo"));
					pedidoEntityRetorno.setAbono(cursorResultados.getFloat("abono"));
					pedidoEntityRetorno.setRestante(cursorResultados.getFloat("restante"));
					
					var ciudad = new CiudadEntity();
					ciudad.setNombre(cursorResultados.getString("nombre_ciudad"));
					pedidoEntityRetorno.setCiudad(ciudad);
					
					var cliente = new ClienteEntity();
					cliente.setNombre(cursorResultados.getString("nombre_cliente"));
					pedidoEntityRetorno.setCliente(cliente);
					
					var empleado = new EmpleadoEntity();
					empleado.setNombre(cursorResultados.getString("nombre_empleado"));
					pedidoEntityRetorno.setEmpleado(empleado);
				}
				
			}
			
		} catch (SQLException exception) {
			var mensajeUsuario="Se ha presentado un problema tratando de consultar la información del pedido con el identificador deseado";
			var mensajeTecnico="Se presentó una excepción de tipo SQLexception tratando de hacer un SELECT en la tabla Pedido,  para tener más detalles revise el log de errores";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de consultar la información del pedido con el identificador deseado";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de hacer un SELECT en la tabla Pedido";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}
		
		return pedidoEntityRetorno;
	}

	@Override
	public void update(UUID id, PedidoEntity entity) throws LilfacException {
		var sentenciaSQL = new StringBuilder();
		
		sentenciaSQL.append("UPDATE pedido SET fechaReserva = ?, fechaVencimiento = ?, direccionEntrega = ?, costo = ?, abono = ?, restante = ?, ciudad = ?, cliente = ?, empleado = ? WHERE id = ?");
		
		try(var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString())){
			
			sentenciaPreparada.setString(1, entity.getFechaReserva());
			sentenciaPreparada.setString(2, entity.getFechaVencimiento());
			sentenciaPreparada.setString(3,  entity.getDireccionEntrega());
			sentenciaPreparada.setFloat(4,  entity.getCosto());
			sentenciaPreparada.setFloat(5,  entity.getAbono());
			sentenciaPreparada.setFloat(6,  entity.getRestante());
			sentenciaPreparada.setObject(7,  entity.getCiudad().getId());
			sentenciaPreparada.setObject(8,  entity.getCliente().getId());
			sentenciaPreparada.setObject(9,  entity.getEmpleado().getId());
			sentenciaPreparada.setObject(10, id);
			sentenciaPreparada.executeUpdate();
			
		} catch (SQLException exception) {
			var mensajeUsuario="Se ha presentado un problema tratando de actualizar la información de un pedido con el identificador ingresado";
			var mensajeTecnico="Se presentó una excepción de tipo SQLexception tratando de hacer un UPDATE en la tabla Pedido,  para tener más detalles revise el log de errores";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de actualizar la información de un pedido con el identificador ingresado";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de hacer un UPDATE en la tabla Pedido";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}
	}


	@Override
	public void delete(UUID id) throws LilfacException {
		var sentenciaSQL = new StringBuilder();
		
		sentenciaSQL.append("DELETE FROM pedido WHERE id = ?");
		
		try(var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString())){
			
			sentenciaPreparada.setObject(1, id);
			sentenciaPreparada.executeUpdate();
			
		} catch (SQLException exception) {
			var mensajeUsuario="Se ha presentado un problema tratando de eliminar la información de un pedido con el identificador ingresado";
			var mensajeTecnico="Se presentó una excepción de tipo SQLexception tratando de hacer un DELETE en la tabla Pedido,  para tener más detalles revise el log de errores";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de eliminar la información de un pedido con el identificador ingresado";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de hacer un DELETE en la tabla Pedido";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}
	}

}
