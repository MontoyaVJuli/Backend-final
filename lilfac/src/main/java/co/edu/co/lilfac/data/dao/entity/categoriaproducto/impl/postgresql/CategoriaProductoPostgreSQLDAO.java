package co.edu.co.lilfac.data.dao.entity.categoriaproducto.impl.postgresql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import co.edu.co.lilfac.crosscutting.excepciones.DataLilfacException;
import co.edu.co.lilfac.crosscutting.excepciones.LilfacException;
import co.edu.co.lilfac.crosscutting.utilitarios.UtilUUID;
import co.edu.co.lilfac.data.dao.entity.categoriaproducto.CategoriaProductoDAO;
import co.edu.co.lilfac.entity.CategoriaEntity;
import co.edu.co.lilfac.entity.CategoriaProductoEntity;
import co.edu.co.lilfac.entity.ProductoEntity;

public class CategoriaProductoPostgreSQLDAO implements CategoriaProductoDAO{
	
	private Connection conexion;
	
	public CategoriaProductoPostgreSQLDAO(Connection conexion) {
		this.conexion=conexion;
	}

	@Override
	public void create(CategoriaProductoEntity entity) throws LilfacException {
		var sentenciaSQL = new StringBuilder();
		
		sentenciaSQL.append("INSERT INTO categoriaProducto (id, producto, categoria) VALUES (?, ?, ?)");
		
		try(var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString())){
			
			sentenciaPreparada.setObject(1, entity.getId());
			sentenciaPreparada.setObject(2, entity.getProducto().getId());
			sentenciaPreparada.setObject(3, entity.getCategoria().getId());
			sentenciaPreparada.executeUpdate();
			
		} catch (SQLException exception) {
			var mensajeUsuario="Se ha presentado un problema tratando de registrar la información de una nueva categoría de producto";
			var mensajeTecnico="Se presentó una excepción de tipo SQLexception tratando de hacer un INSERT en la tabla CategoriaProducto,  para tener más detalles revise el log de errores";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de registrar la información de una nueva categoría de producto";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de hacer un INSERT en la tabla CategoriaProducto";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}
	}

	@Override
	public List<CategoriaProductoEntity> listByFilter(CategoriaProductoEntity filter) throws LilfacException {
		var listaCategoriaProducto = new java.util.ArrayList<CategoriaProductoEntity>();
		var sentenciaSQL = new StringBuilder();
		sentenciaSQL.append("SELECT CP.id, P.nombre AS nombre_producto, C.nombre AS nombre_categoria FROM categoriaProducto CP JOIN producto P ON CP.producto = P.id JOIN categoria C ON CP.categoria = C.id WHERE 1=1");
		
		if (filter != null) {
			if (filter.getId() != null) {
				sentenciaSQL.append(" AND CP.id = ?");
			}
			if (filter.getProducto() != null) {
				sentenciaSQL.append(" AND P.nombre = ?");
			}
			if (filter.getCategoria() != null) {
				sentenciaSQL.append(" AND C.nombre = ?");
			}
		}
		
		try (var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString())) {
			
			var indiceParametro = 1;
			
			if (filter != null) {
				if (filter.getId() != null) {
					sentenciaPreparada.setObject(indiceParametro++, filter.getId());
				}
				if (filter.getProducto() != null) {
					sentenciaPreparada.setObject(indiceParametro++, filter.getProducto().getNombre());
				}
				if (filter.getCategoria() != null) {
					sentenciaPreparada.setObject(indiceParametro++, filter.getCategoria().getNombre());
				}
			}
			
			try (var cursorResultados = sentenciaPreparada.executeQuery()) {
				
				while (cursorResultados.next()) {
		            var categoriaProducto = new CategoriaProductoEntity();
		            categoriaProducto.setId(UtilUUID.convertirAUUID(cursorResultados.getString("id")));

		            var producto = new ProductoEntity();
		            producto.setNombre(cursorResultados.getString("nombre_producto"));
		            categoriaProducto.setProducto(producto);

		            var categoria = new CategoriaEntity();
		            categoria.setNombre(cursorResultados.getString("nombre_categoria"));
		            categoriaProducto.setCategoria(categoria);

		            listaCategoriaProducto.add(categoriaProducto);
				}
			}
			
		} catch (SQLException exception) {
			var mensajeUsuario = "Se ha presentado un problema tratando de consultar las categorias de producto con los filtros deseados.";
			var mensajeTecnico = "Se presentó una excepción SQLException ejecutando SELECT con filtros en la tabla CategoriaProducto.";

			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
			
		} catch (Exception exception) {
			var mensajeUsuario = "Se ha presentado un problema inesperado tratando de consultar las categorias de producto con los filtros deseados.";
			var mensajeTecnico = "Se presentó una excepción NO CONTROLADA ejecutando SELECT con filtros en la tabla CategoriaProducto.";

			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}
		
		return listaCategoriaProducto;
	}

	@Override
	public List<CategoriaProductoEntity> listAll() throws LilfacException {
	    List<CategoriaProductoEntity> listaCategoriaProducto = new ArrayList<>();
	    var sentenciaSQL = new StringBuilder();

	    sentenciaSQL.append("SELECT CP.id, P.nombre AS nombre_producto, C.nombre AS nombre_categoria FROM categoriaProducto CP JOIN producto P ON CP.producto = P.id JOIN categoria C ON CP.categoria = C.id");

	    try (var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString());
	         var resultados = sentenciaPreparada.executeQuery()) {

	        while (resultados.next()) {
	            var categoriaProducto = new CategoriaProductoEntity();
	            categoriaProducto.setId(UtilUUID.convertirAUUID(resultados.getString("id")));

	            var producto = new ProductoEntity();
	            producto.setNombre(resultados.getString("nombre_producto"));
	            categoriaProducto.setProducto(producto);

	            var categoria = new CategoriaEntity();
	            categoria.setNombre(resultados.getString("nombre_categoria"));
	            categoriaProducto.setCategoria(categoria);

	            listaCategoriaProducto.add(categoriaProducto);
	        }

	    } catch (SQLException exception) {
	        var mensajeUsuario = "Se ha presentado un problema tratando de consultar la información de las categorias de producto";
	        var mensajeTecnico = "Se presentó una excepción de tipo SQLexception tratando de hacer un SELECT en la tabla CategoriaProducto";
	        throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
	    } catch (Exception exception) {
	        var mensajeUsuario = "Se ha presentado un problema INESPERADO tratando de consultar la información de las categorias de producto";
	        var mensajeTecnico = "Excepción NO CONTROLADA al hacer SELECT en la tabla CategoriaProducto";
	        throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
	    }

	    return listaCategoriaProducto;
	}

	@Override
	public CategoriaProductoEntity listById(UUID id) throws LilfacException {
		var categoriaProductoEntityRetorno=new CategoriaProductoEntity();
		var sentenciaSQL = new StringBuilder();
		
		sentenciaSQL.append("SELECT CP.id, P.nombre AS nombre_producto, C.nombre AS nombre_categoria FROM categoriaProducto CP JOIN producto P ON CP.producto = P.id JOIN categoria C ON CP.categoria = C.id WHERE id = ?");
		
		try(var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString())){
			
			sentenciaPreparada.setObject(1, id);
			
			try (var cursorResultados = sentenciaPreparada.executeQuery()){
				
				if (cursorResultados.next()) {
					categoriaProductoEntityRetorno.setId(UtilUUID.convertirAUUID(cursorResultados.getString("id")));
					
					var producto = new ProductoEntity();
					producto.setNombre(cursorResultados.getString("nombre_producto"));
					categoriaProductoEntityRetorno.setProducto(producto);
					
					var categoria = new CategoriaEntity();
					categoria.setNombre(cursorResultados.getString("nombre_categoria"));
					categoriaProductoEntityRetorno.setCategoria(categoria);
				}
				
			}
			
		} catch (SQLException exception) {
			var mensajeUsuario="Se ha presentado un problema tratando de consultar la información de la categoría de producto con el identificador deseado";
			var mensajeTecnico="Se presentó una excepción de tipo SQLexception tratando de hacer un SELECT en la tabla CategoriaProducto,  para tener más detalles revise el log de errores";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de consultar la información de la categoría de producto con el identificador deseado";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de hacer un SELECT en la tabla CategoriaProducto";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}
		
		return categoriaProductoEntityRetorno;
	}

	@Override
	public void update(UUID id, CategoriaProductoEntity entity) throws LilfacException {
		var sentenciaSQL = new StringBuilder();
		
		sentenciaSQL.append("UPDATE CategoriaProducto SET producto = ?,  categoria = ? WHERE id = ?");
		
		try(var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString())){
			
			sentenciaPreparada.setObject(1, entity.getProducto().getId());
			sentenciaPreparada.setObject(2, entity.getCategoria().getId());
			sentenciaPreparada.setObject(3, id);
			sentenciaPreparada.executeUpdate();
			
		} catch (SQLException exception) {
			var mensajeUsuario="Se ha presentado un problema tratando de actualizar la información de una categoría de producto con el identificador ingresado";
			var mensajeTecnico="Se presentó una excepción de tipo SQLexception tratando de hacer un UPDATE en la tabla CategoriaProducto,  para tener más detalles revise el log de errores";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de actualizar la información de una categoría de producto con el identificador ingresado";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de hacer un UPDATE en la tabla CategoriaProducto";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}
	}

	@Override
	public void delete(UUID id) throws LilfacException {
		var sentenciaSQL = new StringBuilder();
		
		sentenciaSQL.append("DELETE FROM CategoriaProducto WHERE id = ?");
		
		try(var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString())){
			
			sentenciaPreparada.setObject(1, id);
			sentenciaPreparada.executeUpdate();
			
		} catch (SQLException exception) {
			var mensajeUsuario="Se ha presentado un problema tratando de eliminar la información de una categoría de producto con el identificador ingresado";
			var mensajeTecnico="Se presentó una excepción de tipo SQLexception tratando de hacer un DELETE en la tabla CategoriaProducto,  para tener más detalles revise el log de errores";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de eliminar la información de una categoría de producto con el identificador ingresado";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de hacer un DELETE en la tabla CategoriaProducto";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}
	}

}
