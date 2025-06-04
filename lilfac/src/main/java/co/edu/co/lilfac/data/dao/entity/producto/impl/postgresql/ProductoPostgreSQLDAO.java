package co.edu.co.lilfac.data.dao.entity.producto.impl.postgresql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import co.edu.co.lilfac.crosscutting.excepciones.DataLilfacException;
import co.edu.co.lilfac.crosscutting.excepciones.LilfacException;
import co.edu.co.lilfac.crosscutting.utilitarios.UtilUUID;
import co.edu.co.lilfac.data.dao.entity.producto.ProductoDAO;
import co.edu.co.lilfac.entity.ProductoEntity;

public class ProductoPostgreSQLDAO implements ProductoDAO{
	
	private Connection conexion;
	
	public ProductoPostgreSQLDAO(Connection conexion) {
		this.conexion=conexion;
	}


	@Override
	public void create(ProductoEntity entity) throws LilfacException {
		var sentenciaSQL = new StringBuilder();
		
		sentenciaSQL.append("INSERT INTO producto (id, nombre, codigo, caracteristicas, estado) VALUES (?, ?, ?, ?, ?)");
		
		try(var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString())){
			
			sentenciaPreparada.setObject(1, entity.getId());
			sentenciaPreparada.setString(2, entity.getNombre());
			sentenciaPreparada.setInt(3, entity.getCodigo());
			sentenciaPreparada.setString(4, entity.getCaracteristicas());
			sentenciaPreparada.setString(5, entity.getEstado());
			sentenciaPreparada.executeUpdate();
			
		} catch (SQLException exception) {
			var mensajeUsuario="Se ha presentado un problema tratando de registrar la información de un nuevo producto";
			var mensajeTecnico="Se presentó una excepción de tipo SQLexception tratando de hacer un INSERT en la tabla Producto,  para tener más detalles revise el log de errores";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de registrar la información de un nuevo producto";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de hacer un INSERT en la tabla Producto";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}
		
	}

	@Override
	public List<ProductoEntity> listByFilter(ProductoEntity filter) throws LilfacException {
		var listaProductos = new java.util.ArrayList<ProductoEntity>();
		var sentenciaSQL = new StringBuilder();
		sentenciaSQL.append("SELECT id, nombre, codigo, caracteristicas, estado FROM producto WHERE 1=1");
		
		if (filter != null) {
			if (filter.getId() != null) {
				sentenciaSQL.append(" AND id = ?");
			}
			if (filter.getNombre() != null && !filter.getNombre().isBlank()) {
				sentenciaSQL.append(" AND nombre LIKE ?");
			}
			if (filter.getCodigo() != null) {
				sentenciaSQL.append(" AND codigo = ?");
			}
			if (filter.getCaracteristicas() != null && !filter.getCaracteristicas().isBlank()) {
				sentenciaSQL.append(" AND caracteristicas LIKE ?");
			}
			if (filter.getEstado() != null && !filter.getEstado().isBlank()) {
				sentenciaSQL.append(" AND estado LIKE ?");
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
				if (filter.getCodigo() != null) {
					sentenciaPreparada.setInt(indiceParametro++, filter.getCodigo());
				}
				if (filter.getCaracteristicas() != null && !filter.getCaracteristicas().isBlank()) {
					sentenciaPreparada.setString(indiceParametro++, "%" + filter.getCaracteristicas() + "%");
				}
				if (filter.getEstado() != null && !filter.getEstado().isBlank()) {
					sentenciaPreparada.setString(indiceParametro++, "%" + filter.getEstado() + "%");
				}
			}
			
			try (var cursorResultados = sentenciaPreparada.executeQuery()) {
				
				while (cursorResultados.next()) {
		            var producto = new ProductoEntity();
		            producto.setId(UtilUUID.convertirAUUID(cursorResultados.getString("id")));
		            producto.setNombre(cursorResultados.getString("nombre"));
		            producto.setCodigo(cursorResultados.getInt("codigo"));
		            producto.setCaracteristicas(cursorResultados.getString("caracteristicas"));
		            producto.setEstado(cursorResultados.getString("estado"));

		            listaProductos.add(producto);
				}
			}
			
		} catch (SQLException exception) {
			var mensajeUsuario = "Se ha presentado un problema tratando de consultar los productos con los filtros deseados.";
			var mensajeTecnico = "Se presentó una excepción SQLException ejecutando SELECT con filtros en la tabla Producto.";

			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
			
		} catch (Exception exception) {
			var mensajeUsuario = "Se ha presentado un problema inesperado tratando de consultar los productos con los filtros deseados.";
			var mensajeTecnico = "Se presentó una excepción NO CONTROLADA ejecutando SELECT con filtros en la tabla Producto.";

			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}
		
		return listaProductos;
	}

	@Override
	public List<ProductoEntity> listAll() throws LilfacException {
	    List<ProductoEntity> listaProductos = new ArrayList<>();
	    var sentenciaSQL = new StringBuilder();

	    sentenciaSQL.append("SELECT id, nombre, codigo, caracteristicas, estado FROM producto");

	    try (var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString());
	         var resultados = sentenciaPreparada.executeQuery()) {

	        while (resultados.next()) {
	            var producto = new ProductoEntity();
	            producto.setId(UtilUUID.convertirAUUID(resultados.getString("id")));
	            producto.setNombre(resultados.getString("nombre"));
	            producto.setCodigo(resultados.getInt("codigo"));
	            producto.setCaracteristicas(resultados.getString("caracteristicas"));
	            producto.setEstado(resultados.getString("estado"));

	            listaProductos.add(producto);
	        }

	    } catch (SQLException exception) {
	        var mensajeUsuario = "Se ha presentado un problema tratando de consultar la información de los productos";
	        var mensajeTecnico = "Se presentó una excepción de tipo SQLexception tratando de hacer un SELECT en la tabla Producto";
	        throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
	    } catch (Exception exception) {
	        var mensajeUsuario = "Se ha presentado un problema INESPERADO tratando de consultar la información de los productos";
	        var mensajeTecnico = "Excepción NO CONTROLADA al hacer SELECT en la tabla Producto";
	        throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
	    }

	    return listaProductos;
	}

	@Override
	public ProductoEntity listById(UUID id) throws LilfacException {
		var productoEntityRetorno=new ProductoEntity();
		var sentenciaSQL = new StringBuilder();
		
		sentenciaSQL.append("SELECT id, nombre, codigo, caracteristicas, estado FROM producto WHERE id = ?");
		
		try(var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString())){
			
			sentenciaPreparada.setObject(1, id);

			
			try (var cursorResultados = sentenciaPreparada.executeQuery()){
				
				if (cursorResultados.next()) {
					productoEntityRetorno.setId(UtilUUID.convertirAUUID(cursorResultados.getString("id")));
					productoEntityRetorno.setNombre(cursorResultados.getString("nombre"));
					productoEntityRetorno.setCaracteristicas(cursorResultados.getString("caracteristicas"));
					productoEntityRetorno.setEstado(cursorResultados.getString("estado"));
				}
				
			}
			
		} catch (SQLException exception) {
			var mensajeUsuario="Se ha presentado un problema tratando de consultar la información del producto con el identificador deseado";
			var mensajeTecnico="Se presentó una excepción de tipo SQLexception tratando de hacer un SELECT en la tabla Producto,  para tener más detalles revise el log de errores";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de consultar la información del producto con el identificador deseado";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de hacer un SELECT en la tabla Producto";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}
		
		return productoEntityRetorno;
	}

	@Override
	public void update(UUID id, ProductoEntity entity) throws LilfacException {
		var sentenciaSQL = new StringBuilder();
		
		sentenciaSQL.append("UPDATE producto SET nombre = ?, codigo = ?, caracteristicas = ?, estado = ? WHERE id = ?");
		
		try(var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString())){
			
			sentenciaPreparada.setString(1, entity.getNombre());
			sentenciaPreparada.setInt(2, entity.getCodigo());
			sentenciaPreparada.setString(3, entity.getCaracteristicas());
			sentenciaPreparada.setString(4, entity.getEstado());
			sentenciaPreparada.setObject(5, id);
			sentenciaPreparada.executeUpdate();
			
		} catch (SQLException exception) {
			var mensajeUsuario="Se ha presentado un problema tratando de actualizar la información de un producto con el identificador ingresado";
			var mensajeTecnico="Se presentó una excepción de tipo SQLexception tratando de hacer un UPDATE en la tabla Producto,  para tener más detalles revise el log de errores";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de actualizar la información de un producto con el identificador ingresado";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de hacer un UPDATE en la tabla Producto";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}
	}

	@Override
	public void delete(UUID id) throws LilfacException {
		var sentenciaSQL = new StringBuilder();
		
		sentenciaSQL.append("DELETE FROM producto WHERE id = ?");
		
		try(var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString())){
			
			sentenciaPreparada.setObject(1, id);
			sentenciaPreparada.executeUpdate();
			
		} catch (SQLException exception) {
			var mensajeUsuario="Se ha presentado un problema tratando de eliminar la información de un producto con el identificador ingresado";
			var mensajeTecnico="Se presentó una excepción de tipo SQLexception tratando de hacer un DELETE en la tabla Producto,  para tener más detalles revise el log de errores";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de eliminar la información de un producto con el identificador ingresado";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de hacer un DELETE en la tabla Producto";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}
	}

}
