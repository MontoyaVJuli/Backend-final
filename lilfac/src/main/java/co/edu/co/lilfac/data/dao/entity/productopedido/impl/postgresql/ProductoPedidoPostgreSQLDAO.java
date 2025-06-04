package co.edu.co.lilfac.data.dao.entity.productopedido.impl.postgresql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import co.edu.co.lilfac.crosscutting.excepciones.DataLilfacException;
import co.edu.co.lilfac.crosscutting.excepciones.LilfacException;
import co.edu.co.lilfac.crosscutting.utilitarios.UtilUUID;
import co.edu.co.lilfac.data.dao.entity.productopedido.ProductoPedidoDAO;
import co.edu.co.lilfac.entity.PedidoEntity;
import co.edu.co.lilfac.entity.ProductoEntity;
import co.edu.co.lilfac.entity.ProductoPedidoEntity;

public class ProductoPedidoPostgreSQLDAO implements ProductoPedidoDAO{
	
	private Connection conexion;
	
	public ProductoPedidoPostgreSQLDAO(Connection conexion) {
		this.conexion=conexion;
	}

	@Override
	public void create(ProductoPedidoEntity entity) throws LilfacException {
		var sentenciaSQL = new StringBuilder();
		
		sentenciaSQL.append("INSERT INTO productoPedido (id, cantidad, producto, pedido) VALUES (?, ?, ?, ?)");
		
		try(var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString())){
			
			sentenciaPreparada.setObject(1, entity.getId());
			sentenciaPreparada.setInt(2, entity.getCantidad());
			sentenciaPreparada.setObject(3, entity.getProducto().getId());
			sentenciaPreparada.setObject(4, entity.getPedido().getId());
			sentenciaPreparada.executeUpdate();
			
		} catch (SQLException exception) {
			var mensajeUsuario="Se ha presentado un problema tratando de registrar la información de un nuevo producto pedido";
			var mensajeTecnico="Se presentó una excepción de tipo SQLexception tratando de hacer un INSERT en la tabla ProductoPedido,  para tener más detalles revise el log de errores";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de registrar la información de un nuevo producto pedido";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de hacer un INSERT en la tabla ProductoPedido";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}
	}

	@Override
	public List<ProductoPedidoEntity> listByFilter(ProductoPedidoEntity filter) throws LilfacException {
		var listaProductosPedidos = new java.util.ArrayList<ProductoPedidoEntity>();
		var sentenciaSQL = new StringBuilder();
		sentenciaSQL.append("SELECT PP.id, PP.cantidad, PR.nombre AS nombre_producto, PE.id AS pedido FROM productoPedido PP JOIN producto PR ON PP.producto = PR.id JOIN pedido PE ON PP.pedido = PE.id WHERE 1=1");
		
		if (filter != null) {
			if (filter.getId() != null) {
				sentenciaSQL.append(" AND PP.id = ?");
			}
			if (filter.getCantidad() != null) {
				sentenciaSQL.append(" AND PP.cantidad = ?");
			}		
			if (filter.getProducto() != null) {
				sentenciaSQL.append(" AND PR.nombre = ?");
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
				if (filter.getCantidad() != null) {
					sentenciaPreparada.setInt(indiceParametro++, filter.getCantidad());
				}		
				if (filter.getProducto() != null) {
					sentenciaPreparada.setObject(indiceParametro++, filter.getProducto().getNombre());
				}
				if (filter.getPedido() != null) {
					sentenciaPreparada.setObject(indiceParametro++, filter.getPedido().getId());
				}
			}
			
			try (var cursorResultados = sentenciaPreparada.executeQuery()) {
				
				while (cursorResultados.next()) {
		            var productoPedido = new ProductoPedidoEntity();
		            productoPedido.setId(UtilUUID.convertirAUUID(cursorResultados.getString("id")));
		            productoPedido.setCantidad(cursorResultados.getInt("cantidad"));

		            var producto = new ProductoEntity();
		            producto.setNombre(cursorResultados.getString("nombre_producto"));
		            productoPedido.setProducto(producto);
		            
		            var pedido = new PedidoEntity();
		            pedido.setId(UtilUUID.convertirAUUID(cursorResultados.getString("pedido")));
		            productoPedido.setPedido(pedido);

		            listaProductosPedidos.add(productoPedido);
				}
			}
			
		} catch (SQLException exception) {
			var mensajeUsuario = "Se ha presentado un problema tratando de consultar los productos pedidos con los filtros deseados.";
			var mensajeTecnico = "Se presentó una excepción SQLException ejecutando SELECT con filtros en la tabla ProductoPedido.";

			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
			
		} catch (Exception exception) {
			var mensajeUsuario = "Se ha presentado un problema inesperado tratando de consultar los productos pedidos con los filtros deseados.";
			var mensajeTecnico = "Se presentó una excepción NO CONTROLADA ejecutando SELECT con filtros en la tabla ProductoPedido.";

			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}
		
		return listaProductosPedidos;
	}

	@Override
	public List<ProductoPedidoEntity> listAll() throws LilfacException {
	    List<ProductoPedidoEntity> listaProductosPedidos = new ArrayList<>();
	    var sentenciaSQL = new StringBuilder();

	    sentenciaSQL.append("SELECT PP.id, PP.cantidad, PR.nombre AS nombre_producto, PE.id AS pedido FROM productoPedido PP JOIN producto PR ON PP.producto = PR.id JOIN pedido PE ON PP.pedido = PE.id");

	    try (var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString());
	         var resultados = sentenciaPreparada.executeQuery()) {

	        while (resultados.next()) {
	            var productoPedido = new ProductoPedidoEntity();
	            productoPedido.setId(UtilUUID.convertirAUUID(resultados.getString("id")));
	            productoPedido.setCantidad(resultados.getInt("cantidad"));

	            var producto = new ProductoEntity();
	            producto.setNombre(resultados.getString("nombre_producto"));
	            productoPedido.setProducto(producto);
	            
	            var pedido = new PedidoEntity();
	            pedido.setId(UtilUUID.convertirAUUID(resultados.getString("pedido")));
	            productoPedido.setPedido(pedido);

	            listaProductosPedidos.add(productoPedido);
	        }

	    } catch (SQLException exception) {
	        var mensajeUsuario = "Se ha presentado un problema tratando de consultar la información de los productos pedidos";
	        var mensajeTecnico = "Se presentó una excepción de tipo SQLexception tratando de hacer un SELECT en la tabla ProductoPedido";
	        throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
	    } catch (Exception exception) {
	        var mensajeUsuario = "Se ha presentado un problema INESPERADO tratando de consultar la información de los productos pedidos";
	        var mensajeTecnico = "Excepción NO CONTROLADA al hacer SELECT en la tabla ProductoPedido";
	        throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
	    }

	    return listaProductosPedidos;
	}

	@Override
	public ProductoPedidoEntity listById(UUID id) throws LilfacException {
		var productoPedidoEntityRetorno=new ProductoPedidoEntity();
		var sentenciaSQL = new StringBuilder();
		
		sentenciaSQL.append("SELECT PP.id, PP.cantidad, PR.nombre AS nombre_producto, PE.id AS pedido FROM productoPedido PP JOIN producto PR ON PP.producto = PR.id JOIN pedido PE ON PP.pedido = PE.id WHERE PP.id = ?");
		
		try(var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString())){
			
			sentenciaPreparada.setObject(1, id);
			
			try (var cursorResultados = sentenciaPreparada.executeQuery()){
				
				if (cursorResultados.next()) {
					productoPedidoEntityRetorno.setId(UtilUUID.convertirAUUID(cursorResultados.getString("id")));
					productoPedidoEntityRetorno.setCantidad(cursorResultados.getInt("cantidad"));
	
					var producto = new ProductoEntity();
					producto.setNombre(cursorResultados.getString("nombre_producto"));
					productoPedidoEntityRetorno.setProducto(producto);
					
					var pedido = new PedidoEntity();
					pedido.setId(UtilUUID.convertirAUUID(cursorResultados.getString("pedido")));
					productoPedidoEntityRetorno.setProducto(producto);
					
				}
				
			}
			
		} catch (SQLException exception) {
			var mensajeUsuario="Se ha presentado un problema tratando de consultar la información del producto pedido con el identificador deseado";
			var mensajeTecnico="Se presentó una excepción de tipo SQLexception tratando de hacer un SELECT en la tabla ProductoPedido,  para tener más detalles revise el log de errores";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de consultar la información del producto pedido con el identificador deseado";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de hacer un SELECT en la tabla ProductoPedido";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}
		
		return productoPedidoEntityRetorno;
	}

	@Override
	public void update(UUID id, ProductoPedidoEntity entity) throws LilfacException {
		var sentenciaSQL = new StringBuilder();
		
		sentenciaSQL.append("UPDATE productoPedido SET cantidad = ?, producto = ?, pedido = ? WHERE id = ?");
		
		try(var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString())){
			
			sentenciaPreparada.setInt(1, entity.getCantidad());
			sentenciaPreparada.setObject(2, entity.getProducto().getId());
			sentenciaPreparada.setObject(3,  entity.getPedido().getId());
			sentenciaPreparada.setObject(4, id);
			sentenciaPreparada.executeUpdate();
			
		} catch (SQLException exception) {
			var mensajeUsuario="Se ha presentado un problema tratando de actualizar la información de un producto pedido con el identificador ingresado";
			var mensajeTecnico="Se presentó una excepción de tipo SQLexception tratando de hacer un UPDATE en la tabla ProductoPedido,  para tener más detalles revise el log de errores";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de actualizar la información de un producto pedido con el identificador ingresado";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de hacer un UPDATE en la tabla ProductoPedido";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}	
	}

	@Override
	public void delete(UUID id) throws LilfacException {
		var sentenciaSQL = new StringBuilder();
		
		sentenciaSQL.append("DELETE FROM productoPedido WHERE id = ?");
		
		try(var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString())){
			
			sentenciaPreparada.setObject(1, id);
			sentenciaPreparada.executeUpdate();
			
		} catch (SQLException exception) {
			var mensajeUsuario="Se ha presentado un problema tratando de eliminar la información de un producto pedido con el identificador ingresado";
			var mensajeTecnico="Se presentó una excepción de tipo SQLexception tratando de hacer un DELETE en la tabla ProductoPedido,  para tener más detalles revise el log de errores";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de eliminar la información de un producto pedido con el identificador ingresado";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de hacer un DELETE en la tabla ProductoPedido";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}
	}

}
