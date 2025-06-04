package co.edu.co.lilfac.data.dao.entity.factura.impl.postgresql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import co.edu.co.lilfac.crosscutting.excepciones.DataLilfacException;
import co.edu.co.lilfac.crosscutting.excepciones.LilfacException;
import co.edu.co.lilfac.crosscutting.utilitarios.UtilUUID;
import co.edu.co.lilfac.data.dao.entity.factura.FacturaDAO;
import co.edu.co.lilfac.entity.ClienteEntity;
import co.edu.co.lilfac.entity.CostoAdicionalEntity;
import co.edu.co.lilfac.entity.EmpleadoEntity;
import co.edu.co.lilfac.entity.EmpresaEntity;
import co.edu.co.lilfac.entity.FacturaEntity;
import co.edu.co.lilfac.entity.PedidoEntity;

public class FacturaPostgreSQLDAO implements FacturaDAO{
	
	private Connection conexion;
	
	public FacturaPostgreSQLDAO(Connection conexion) {
		this.conexion=conexion;
	}

	@Override
	public void create(FacturaEntity entity) throws LilfacException {
		var sentenciaSQL = new StringBuilder();
		
		sentenciaSQL.append("INSERT INTO factura (id, codigo, fechaGeneracion, costoTotal, empresa, empleado, cliente, costoAdicional, pedido) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
		
		try(var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString())){
			
			sentenciaPreparada.setObject(1, entity.getId());
			sentenciaPreparada.setString(2,  entity.getCodigo());
			sentenciaPreparada.setString(3, entity.getFechaGeneracion());
			sentenciaPreparada.setFloat(4, entity.getCostoTotal());
			sentenciaPreparada.setObject(5, entity.getEmpresa().getId());
			sentenciaPreparada.setObject(6, entity.getEmpleado().getId());
			sentenciaPreparada.setObject(7, entity.getCliente().getId());
			sentenciaPreparada.setObject(8, entity.getCostoAdicional().getId());
			sentenciaPreparada.setObject(9, entity.getPedido().getId());
			
			sentenciaPreparada.executeUpdate();
			
		} catch (SQLException exception) {
			var mensajeUsuario="Se ha presentado un problema tratando de registrar la información de una nueva factura";
			var mensajeTecnico="Se presentó una excepción de tipo SQLexception tratando de hacer un INSERT en la tabla Factura,  para tener más detalles revise el log de errores";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de registrar la información de una nueva factura";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de hacer un INSERT en la tabla Factura";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}
	}

	@Override
	public List<FacturaEntity> listByFilter(FacturaEntity filter) throws LilfacException {
		var listaFacturas = new java.util.ArrayList<FacturaEntity>();
		var sentenciaSQL = new StringBuilder();
		sentenciaSQL.append("SELECT F.id, F.codigo, F.fechaGeneracion, F.costoTotal, E.nombre AS nombre_empresa, EM.nombre AS nombre_empleado, CL.nombre AS nombre_cliente, CA.valor AS costo_adicional, PE.id AS pedido FROM factura F JOIN empresa E ON F.empresa = E.id JOIN empleado EM ON F.empleado = EM.id JOIN cliente CL ON F.cliente = CL.id JOIN costoAdicional CA ON F.costoAdicional = CA.id JOIN pedido PE ON F.pedido = PE.id WHERE 1=1");
		
		if (filter != null) {
			if (filter.getId() != null) {
				sentenciaSQL.append(" AND F.id = ?");
			}
			if (filter.getCodigo() != null) {
				sentenciaSQL.append(" AND F.codigo = ?");
			}
			if (filter.getFechaGeneracion() != null && !filter.getFechaGeneracion().isBlank()) {
				sentenciaSQL.append(" AND F.fechaGeneracion LIKE ?");
			}
			if (filter.getCostoTotal() != null) {
				sentenciaSQL.append(" AND F.costoTotal = ?");
			}
			if (filter.getEmpresa() != null) {
				sentenciaSQL.append(" AND E.nombre = ?");
			}
			if (filter.getEmpleado() != null) {
				sentenciaSQL.append(" AND EM.nombre = ?");
			}
			if (filter.getCliente() != null) {
				sentenciaSQL.append(" AND CL.nombre = ?");
			}
			if (filter.getCostoAdicional() != null) {
				sentenciaSQL.append(" AND CA.valor = ?");
			}
			if (filter.getPedido() != null) {
				sentenciaSQL.append(" AND PE.id = ?");
			}

		}
		
		try (var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString())) {
			
			var indiceParametro = 1;
			
			if (filter != null) {
				if (filter.getId() != null) {
					sentenciaPreparada.setObject(indiceParametro++, filter.getId());
				}
				if (filter.getCodigo() != null) {
					sentenciaPreparada.setString(indiceParametro++, filter.getCodigo());
				}
				if (filter.getFechaGeneracion() != null && !filter.getFechaGeneracion().isBlank()) {
					sentenciaPreparada.setString(indiceParametro++, "%" + filter.getFechaGeneracion() + "%");
				}
				if (filter.getCostoTotal() != null) {
					sentenciaPreparada.setFloat(indiceParametro++, filter.getCostoTotal());
				}
				if (filter.getEmpresa() != null) {
					sentenciaPreparada.setObject(indiceParametro++, filter.getEmpresa().getNombre());
				}
				if (filter.getEmpleado() != null) {
					sentenciaPreparada.setObject(indiceParametro++, filter.getEmpleado().getNombre());
				}
				if (filter.getCliente() != null) {
					sentenciaPreparada.setObject(indiceParametro++, filter.getCliente().getNombre());
				}
				if (filter.getCostoAdicional() != null){
					sentenciaPreparada.setObject(indiceParametro++, filter.getCostoAdicional().getValor());
				}
				if (filter.getPedido() != null) {
					sentenciaPreparada.setObject(indiceParametro++, filter.getPedido().getId());
				}
				
			}
			
			try (var cursorResultados = sentenciaPreparada.executeQuery()) {
				
				while (cursorResultados.next()) {
		            var factura = new FacturaEntity();
		            factura.setId(UtilUUID.convertirAUUID(cursorResultados.getString("id")));
		            factura.setCodigo(cursorResultados.getString("codigo"));
		            factura.setFechaGeneracion(cursorResultados.getString("fechaGeneracion"));
		            factura.setCostoTotal(cursorResultados.getFloat("costoTotal"));

		            var empresa = new EmpresaEntity();
		            empresa.setNombre(cursorResultados.getString("nombre_empresa"));
		            factura.setEmpresa(empresa);

		            var empleado = new EmpleadoEntity();
		            empleado.setNombre(cursorResultados.getString("nombre_empleado"));
		            factura.setEmpleado(empleado);

		            var cliente = new ClienteEntity();
		            cliente.setNombre(cursorResultados.getString("nombre_cliente"));
		            factura.setCliente(cliente);

		            var costoAdicional = new CostoAdicionalEntity();
		            costoAdicional.setValor(cursorResultados.getFloat("costo_adicional"));
		            factura.setCostoAdicional(costoAdicional);

		            var pedido = new PedidoEntity();
		            pedido.setId(UtilUUID.convertirAUUID(cursorResultados.getString("pedido")));
		            factura.setPedido(pedido);

		            listaFacturas.add(factura);
				}
			}
			
		} catch (SQLException exception) {
			var mensajeUsuario = "Se ha presentado un problema tratando de consultar las facturas con los filtros deseados.";
			var mensajeTecnico = "Se presentó una excepción SQLException ejecutando SELECT con filtros en la tabla Factura.";

			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
			
		} catch (Exception exception) {
			var mensajeUsuario = "Se ha presentado un problema inesperado tratando de consultar las facturas con los filtros deseados.";
			var mensajeTecnico = "Se presentó una excepción NO CONTROLADA ejecutando SELECT con filtros en la tabla Factura.";

			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}
		
		return listaFacturas;
	}

	@Override
	public List<FacturaEntity> listAll() throws LilfacException {
	    List<FacturaEntity> listaFacturas = new ArrayList<>();
	    var sentenciaSQL = new StringBuilder();

	    sentenciaSQL.append("SELECT F.id, F.codigo, F.fechaGeneracion, F.costoTotal, E.nombre AS nombre_empresa, EM.nombre AS nombre_empleado, CL.nombre AS nombre_cliente, CA.valor AS costo_adicional, PE.id AS pedido FROM factura F JOIN empresa E ON F.empresa = E.id JOIN empleado EM ON F.empleado = EM.id JOIN cliente CL ON F.cliente = CL.id JOIN costoAdicional CA ON F.costoAdicional = CA.id JOIN pedido PE ON F.pedido = PE.id");

	    try (var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString());
	         var resultados = sentenciaPreparada.executeQuery()) {

	        while (resultados.next()) {
	            var factura = new FacturaEntity();
	            factura.setId(UtilUUID.convertirAUUID(resultados.getString("id")));
	            factura.setCodigo(resultados.getString("codigo"));
	            factura.setFechaGeneracion(resultados.getString("fechaGeneracion"));
	            factura.setCostoTotal(resultados.getFloat("costoTotal"));

	            var empresa = new EmpresaEntity();
	            empresa.setNombre(resultados.getString("nombre_empresa"));
	            factura.setEmpresa(empresa);

	            var empleado = new EmpleadoEntity();
	            empleado.setNombre(resultados.getString("nombre_empleado"));
	            factura.setEmpleado(empleado);

	            var cliente = new ClienteEntity();
	            cliente.setNombre(resultados.getString("nombre_cliente"));
	            factura.setCliente(cliente);

	            var costoAdicional = new CostoAdicionalEntity();
	            costoAdicional.setValor(resultados.getFloat("costo_adicional"));
	            factura.setCostoAdicional(costoAdicional);

	            var pedido = new PedidoEntity();
	            pedido.setId(UtilUUID.convertirAUUID(resultados.getString("pedido")));
	            factura.setPedido(pedido);

	            listaFacturas.add(factura);
	        }

	    } catch (SQLException exception) {
	        var mensajeUsuario = "Se ha presentado un problema tratando de consultar la información de las facturas";
	        var mensajeTecnico = "Se presentó una excepción de tipo SQLexception tratando de hacer un SELECT en la tabla Factura";
	        throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
	    } catch (Exception exception) {
	        var mensajeUsuario = "Se ha presentado un problema INESPERADO tratando de consultar la información de las facturas";
	        var mensajeTecnico = "Excepción NO CONTROLADA al hacer SELECT en la tabla Factura";
	        throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
	    }

	    return listaFacturas;
	}


	@Override
	public FacturaEntity listById(UUID id) throws LilfacException {
		var facturaEntityRetorno=new FacturaEntity();
		var sentenciaSQL = new StringBuilder();
		
		sentenciaSQL.append("SELECT F.id, F.codigo, F.fechaGeneracion, F.costoTotal, E.nombre AS nombre_empresa, EM.nombre AS nombre_empleado, CL.nombre AS nombre_cliente, CA.valor AS costo_adicional, PE.id AS pedido FROM factura F JOIN empresa E ON F.empresa = E.id JOIN empleado EM ON F.empleado = EM.id JOIN cliente CL ON F.cliente = CL.id JOIN costoAdicional CA ON F.costoAdicional = CA.id JOIN pedido PE ON F.pedido = PE.id WHERE F.id = ?");
		
		try(var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString())){
			
			sentenciaPreparada.setObject(1, id);
			
			try (var cursorResultados = sentenciaPreparada.executeQuery()){
				
				if (cursorResultados.next()) {
					facturaEntityRetorno.setId(UtilUUID.convertirAUUID(cursorResultados.getString("id")));
					facturaEntityRetorno.setCodigo(cursorResultados.getString("codigo"));
					facturaEntityRetorno.setFechaGeneracion(cursorResultados.getString("fechaGeneracion"));
					facturaEntityRetorno.setCostoTotal(cursorResultados.getFloat("costoTotal"));
					
					var empresa = new EmpresaEntity();
					empresa.setNombre(cursorResultados.getString("nombre_empresa"));
					facturaEntityRetorno.setEmpresa(empresa);
					
					var empleado = new EmpleadoEntity();
					empleado.setNombre(cursorResultados.getString("nombre_empleado"));
					facturaEntityRetorno.setEmpleado(empleado);
	
					var cliente = new ClienteEntity();
					cliente.setNombre(cursorResultados.getString("nombre_cliente"));
					facturaEntityRetorno.setCliente(cliente);
					
					var costoAdicional = new CostoAdicionalEntity();
					costoAdicional.setValor(cursorResultados.getFloat("costo_adicional"));
					facturaEntityRetorno.setCostoAdicional(costoAdicional);
					
					var pedido = new PedidoEntity();
					pedido.setId(UtilUUID.convertirAUUID(cursorResultados.getString("pedido")));
					facturaEntityRetorno.setPedido(pedido);
					
				}
				
			}
			
		} catch (SQLException exception) {
			var mensajeUsuario="Se ha presentado un problema tratando de consultar la información de la factura con el identificador deseado";
			var mensajeTecnico="Se presentó una excepción de tipo SQLexception tratando de hacer un SELECT en la tabla Factura,  para tener más detalles revise el log de errores";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de consultar la información de la factura con el identificador deseado";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de hacer un SELECT en la tabla Factura";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}
		
		return facturaEntityRetorno;
	}

	@Override
	public void update(UUID id, FacturaEntity entity) throws LilfacException {
		var sentenciaSQL = new StringBuilder();
		
		sentenciaSQL.append("UPDATE factura SET  codigo = ?, fechaGeneracion = ?, costoTotal = ?, empresa = ?, empleado = ?, cliente = ?, costoAdicional = ?, pedido = ? WHERE id = ?");
		
		try(var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString())){
			
			sentenciaPreparada.setString(1, entity.getCodigo());
			sentenciaPreparada.setString(2, entity.getFechaGeneracion());
			sentenciaPreparada.setFloat(3,  entity.getCostoTotal());
			sentenciaPreparada.setObject(4,  entity.getEmpresa().getId());
			sentenciaPreparada.setObject(5,  entity.getEmpleado().getId());
			sentenciaPreparada.setObject(6,  entity.getCliente().getId());
			sentenciaPreparada.setObject(7,  entity.getCostoAdicional().getId());
			sentenciaPreparada.setObject(8,  entity.getPedido().getId());
			sentenciaPreparada.setObject(9, id);
			sentenciaPreparada.executeUpdate();
			
		} catch (SQLException exception) {
			var mensajeUsuario="Se ha presentado un problema tratando de actualizar la información de una factura con el identificador ingresado";
			var mensajeTecnico="Se presentó una excepción de tipo SQLexception tratando de hacer un UPDATE en la tabla Factura,  para tener más detalles revise el log de errores";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de actualizar la información de una factura con el identificador ingresado";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de hacer un UPDATE en la tabla Factura";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}
	}

	@Override
	public void delete(UUID id) throws LilfacException {
		var sentenciaSQL = new StringBuilder();
		
		sentenciaSQL.append("DELETE FROM factura WHERE id = ?");
		
		try(var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString())){
			
			sentenciaPreparada.setObject(1, id);
			sentenciaPreparada.executeUpdate();
			
		} catch (SQLException exception) {
			var mensajeUsuario="Se ha presentado un problema tratando de eliminar la información de una factura con el identificador ingresado";
			var mensajeTecnico="Se presentó una excepción de tipo SQLexception tratando de hacer un DELETE en la tabla Factura,  para tener más detalles revise el log de errores";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de eliminar la información de una factura pedido con el identificador ingresado";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de hacer un DELETE en la tabla Factura";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}
	}

}
