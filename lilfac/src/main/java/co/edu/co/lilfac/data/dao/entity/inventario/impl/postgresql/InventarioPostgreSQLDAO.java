package co.edu.co.lilfac.data.dao.entity.inventario.impl.postgresql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import co.edu.co.lilfac.crosscutting.excepciones.DataLilfacException;
import co.edu.co.lilfac.crosscutting.excepciones.LilfacException;
import co.edu.co.lilfac.crosscutting.utilitarios.UtilUUID;
import co.edu.co.lilfac.data.dao.entity.inventario.InventarioDAO;
import co.edu.co.lilfac.entity.EmpresaEntity;
import co.edu.co.lilfac.entity.HistorialCostoEntity;
import co.edu.co.lilfac.entity.InventarioEntity;
import co.edu.co.lilfac.entity.ProductoEntity;

public class InventarioPostgreSQLDAO implements InventarioDAO{
	
	private Connection conexion;
	
	public InventarioPostgreSQLDAO(Connection conexion) {
		this.conexion=conexion;
	}


	@Override
	public void create(InventarioEntity entity) throws LilfacException {
		var sentenciaSQL = new StringBuilder();
		
		sentenciaSQL.append("INSERT INTO inventario (id, totalUnidades, unidadesAlquiladas, unidadesAfectadas, UnidadesDisponibles, empresa, producto, histotialCosto) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
		
		try(var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString())){
			
			sentenciaPreparada.setObject(1, entity.getId());
			sentenciaPreparada.setInt(2, entity.getTotalUnidades());
			sentenciaPreparada.setInt(3, entity.getUnidadesAlquiladas());
			sentenciaPreparada.setInt(4, entity.getUnidadesAfectadas());
			sentenciaPreparada.setInt(5,  entity.getUnidadesDisponibles());
			sentenciaPreparada.setObject(6,  entity.getEmpresa().getId());
			sentenciaPreparada.setObject(7,  entity.getProducto().getId());
			sentenciaPreparada.setObject(8,  entity.getHistorialCosto().getId());
			sentenciaPreparada.executeUpdate();
			
		} catch (SQLException exception) {
			var mensajeUsuario="Se ha presentado un problema tratando de registrar la información de un nuevo inventario";
			var mensajeTecnico="Se presentó una excepción de tipo SQLexception tratando de hacer un INSERT en la tabla Inventario,  para tener más detalles revise el log de errores";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de registrar la información de un nuevo inventario";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de hacer un INSERT en la tabla Inventario";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}	
	}

	@Override
	public List<InventarioEntity> listByFilter(InventarioEntity filter) throws LilfacException {
		var listaInventario = new java.util.ArrayList<InventarioEntity>();
		var sentenciaSQL = new StringBuilder();
		sentenciaSQL.append("SELECT I.id, I.totalUnidades, I.unidadesAlquiladas, I.unidadesAfectadas, I.UnidadesDisponibles, E.nombre AS nombre_empresa, P.nombre AS nombre_producto, H.costo AS costo FROM inventario I JOIN empresa E ON I.empresa = E.id JOIN producto P ON I.producto = P.id JOIN historialCosto H ON I.historialCosto = H.id WHERE 1=1");
		
		if (filter != null) {
			if (filter.getId() != null) {
				sentenciaSQL.append(" AND I.id = ?");
			}
			if (filter.getTotalUnidades() != null) {
				sentenciaSQL.append(" AND I.totalUnidades = ?");
			}
			if (filter.getUnidadesAlquiladas() != null) {
				sentenciaSQL.append(" AND I.unidadesAlquiladas = ?");
			}
			if (filter.getUnidadesAfectadas() != null) {
				sentenciaSQL.append(" AND I.unidadesAfectadas = ?");
			}
			if (filter.getUnidadesDisponibles() != null) {
				sentenciaSQL.append(" AND I.unidadesDisponibles = ?");
			}
			if (filter.getEmpresa() != null) {
				sentenciaSQL.append(" AND E.nombre = ?");
			}
			if (filter.getProducto() != null) {
				sentenciaSQL.append(" AND P.nombre = ?");
			}
			if (filter.getHistorialCosto() != null) {
				sentenciaSQL.append(" AND H.costo = ?");
			}
		}
		
		try (var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString())) {
			
			var indiceParametro = 1;
			
			if (filter != null) {
				if (filter.getId() != null) {
					sentenciaPreparada.setObject(indiceParametro++, filter.getId());
				}
				if (filter.getTotalUnidades() != null) {
					sentenciaPreparada.setObject(indiceParametro++, filter.getTotalUnidades());
				}
				if (filter.getUnidadesAlquiladas() != null) {
					sentenciaPreparada.setObject(indiceParametro++, filter.getUnidadesAlquiladas());
				}
				if (filter.getUnidadesAfectadas() != null) {
					sentenciaPreparada.setObject(indiceParametro++, filter.getUnidadesAfectadas());
				}
				if (filter.getUnidadesDisponibles() != null) {
					sentenciaPreparada.setObject(indiceParametro++, filter.getUnidadesDisponibles());
				}
				if (filter.getEmpresa() != null) {
					sentenciaPreparada.setObject(indiceParametro++, filter.getEmpresa().getNombre());
				}
				if (filter.getProducto() != null) {
					sentenciaPreparada.setObject(indiceParametro++, filter.getProducto().getNombre());
				}
				if (filter.getHistorialCosto() != null) {
					sentenciaPreparada.setObject(indiceParametro++, filter.getHistorialCosto().getCosto());
				}
			}
			
			try (var cursorResultados = sentenciaPreparada.executeQuery()) {
				
				while (cursorResultados.next()) {
		            var inventario = new InventarioEntity();
		            inventario.setId(UtilUUID.convertirAUUID(cursorResultados.getString("id")));
		            inventario.setTotalUnidades(cursorResultados.getInt("totalUnidades"));
		            inventario.setUnidadesAlquiladas(cursorResultados.getInt("unidadesAlquiladas"));
		            inventario.setUnidadesAfectadas(cursorResultados.getInt("unidadesAfectadas"));
		            inventario.setUnidadesDisponibles(cursorResultados.getInt("unidadesDisponibles"));

		            var empresa = new EmpresaEntity();
		            empresa.setNombre(cursorResultados.getString("nombre_empresa"));
		            inventario.setEmpresa(empresa);

		            var producto = new ProductoEntity();
		            producto.setNombre(cursorResultados.getString("nombre_producto"));
		            inventario.setProducto(producto);

		            var historialCosto = new HistorialCostoEntity();
		            historialCosto.setCosto(cursorResultados.getFloat("costo"));
		            inventario.setHistorialCosto(historialCosto);

		            listaInventario.add(inventario);
				}
			}
			
		} catch (SQLException exception) {
			var mensajeUsuario = "Se ha presentado un problema tratando de consultar el inventario con los filtros deseados.";
			var mensajeTecnico = "Se presentó una excepción SQLException ejecutando SELECT con filtros en la tabla Inventario.";

			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
			
		} catch (Exception exception) {
			var mensajeUsuario = "Se ha presentado un problema inesperado tratando de consultar inventario con los filtros deseados.";
			var mensajeTecnico = "Se presentó una excepción NO CONTROLADA ejecutando SELECT con filtros en la tabla Inventario.";

			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}
		
		return listaInventario;
	}

	@Override
	public List<InventarioEntity> listAll() throws LilfacException {
	    List<InventarioEntity> listaInventario = new ArrayList<>();
	    var sentenciaSQL = new StringBuilder();

	    sentenciaSQL.append("SELECT I.id, I.totalUnidades, I.unidadesAlquiladas, I.unidadesAfectadas, I.UnidadesDisponibles, E.nombre AS nombre_empresa, P.nombre AS nombre_producto, H.costo AS costo FROM inventario I JOIN empresa E ON I.empresa = E.id JOIN producto P ON I.producto = P.id JOIN historialCosto H ON I.historialCosto = H.id");

	    try (var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString());
	         var resultados = sentenciaPreparada.executeQuery()) {

	        while (resultados.next()) {
	            var inventario = new InventarioEntity();
	            inventario.setId(UtilUUID.convertirAUUID(resultados.getString("id")));
	            inventario.setTotalUnidades(resultados.getInt("totalUnidades"));
	            inventario.setUnidadesAlquiladas(resultados.getInt("unidadesAlquiladas"));
	            inventario.setUnidadesAfectadas(resultados.getInt("unidadesAfectadas"));
	            inventario.setUnidadesDisponibles(resultados.getInt("unidadesDisponibles"));

	            var empresa = new EmpresaEntity();
	            empresa.setNombre(resultados.getString("nombre_empresa"));
	            inventario.setEmpresa(empresa);

	            var producto = new ProductoEntity();
	            producto.setNombre(resultados.getString("nombre_producto"));
	            inventario.setProducto(producto);

	            var historialCosto = new HistorialCostoEntity();
	            historialCosto.setCosto(resultados.getFloat("costo"));
	            inventario.setHistorialCosto(historialCosto);

	            listaInventario.add(inventario);
	        }

	    } catch (SQLException exception) {
	        var mensajeUsuario = "Se ha presentado un problema tratando de consultar la información del inventario";
	        var mensajeTecnico = "Se presentó una excepción de tipo SQLexception tratando de hacer un SELECT en la tabla Inventario";
	        throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
	    } catch (Exception exception) {
	        var mensajeUsuario = "Se ha presentado un problema INESPERADO tratando de consultar la información del inventario";
	        var mensajeTecnico = "Excepción NO CONTROLADA al hacer SELECT en la tabla Inventario";
	        throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
	    }

	    return listaInventario;
	}

	@Override
	public InventarioEntity listById(UUID id) throws LilfacException {
		var inventarioEntityRetorno=new InventarioEntity();
		var sentenciaSQL = new StringBuilder();
		
		sentenciaSQL.append("SELECT I.id, I.totalUnidades, I.unidadesAlquiladas, I.unidadesAfectadas, I.UnidadesDisponibles, E.nombre AS nombre_empresa, P.nombre AS nombre_producto, H.costo AS costo FROM inventario I JOIN empresa E ON I.empresa = E.id JOIN producto P ON I.producto = P.id JOIN historialCosto H ON I.historialCosto = H.id WHERE I.id = ?");
		
		try(var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString())){
			
			sentenciaPreparada.setObject(1, id);
			
			try (var cursorResultados = sentenciaPreparada.executeQuery()){
				
				if (cursorResultados.next()) {
					inventarioEntityRetorno.setId(UtilUUID.convertirAUUID(cursorResultados.getString("id")));
					inventarioEntityRetorno.setTotalUnidades(cursorResultados.getInt("totalUnidades"));
					inventarioEntityRetorno.setUnidadesAlquiladas(cursorResultados.getInt("unidadesAlquiladas"));
					inventarioEntityRetorno.setUnidadesAfectadas(cursorResultados.getInt("unidadesAfectadas"));
					inventarioEntityRetorno.setUnidadesDisponibles(cursorResultados.getInt("unidadesDisponibles"));
					
					var empresa = new EmpresaEntity();
					empresa.setNombre(cursorResultados.getString("nombre_empresa"));
					inventarioEntityRetorno.setEmpresa(empresa);
					
					var producto = new ProductoEntity();
					producto.setNombre(cursorResultados.getString("nombre_producto"));
					inventarioEntityRetorno.setProducto(producto);
					
					var historialCosto = new HistorialCostoEntity();
					historialCosto.setCosto(cursorResultados.getFloat("costo"));
					inventarioEntityRetorno.setHistorialCosto(historialCosto);
				}
				
			}
			
		} catch (SQLException exception) {
			var mensajeUsuario="Se ha presentado un problema tratando de consultar la información del inventario con el identificador deseado";
			var mensajeTecnico="Se presentó una excepción de tipo SQLexception tratando de hacer un SELECT en la tabla Inventario,  para tener más detalles revise el log de errores";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de consultar la información del inventario con el identificador deseado";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de hacer un SELECT en la tabla Inventario";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}
		
		return inventarioEntityRetorno;
	}

	@Override
	public void update(UUID id, InventarioEntity entity) throws LilfacException {
		var sentenciaSQL = new StringBuilder();
		
		sentenciaSQL.append("UPDATE invetario SET totalUnidades = ?, unidadesAlquiladas = ?, unidadesAfectadas = ?, UnidadesDisponibles = ?, empresa = ?, producto = ?, histotialCosto = ? WHERE id = ?");
		
		try(var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString())){
			
			sentenciaPreparada.setInt(1, entity.getTotalUnidades());
			sentenciaPreparada.setInt(2, entity.getUnidadesAlquiladas());
			sentenciaPreparada.setInt(3,  entity.getUnidadesAfectadas());
			sentenciaPreparada.setInt(4,  entity.getUnidadesDisponibles());
			sentenciaPreparada.setObject(5,  entity.getEmpresa().getId());
			sentenciaPreparada.setObject(6,  entity.getProducto().getId());
			sentenciaPreparada.setObject(7,  entity.getHistorialCosto().getId());
			sentenciaPreparada.setObject(8, id);
			sentenciaPreparada.executeUpdate();
			
		} catch (SQLException exception) {
			var mensajeUsuario="Se ha presentado un problema tratando de actualizar la información de un inventario con el identificador ingresado";
			var mensajeTecnico="Se presentó una excepción de tipo SQLexception tratando de hacer un UPDATE en la tabla Inventario,  para tener más detalles revise el log de errores";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de actualizar la información de un invetario con el identificador ingresado";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de hacer un UPDATE en la tabla Inventario";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}
	}

	@Override
	public void delete(UUID id) throws LilfacException {
		var sentenciaSQL = new StringBuilder();
		
		sentenciaSQL.append("DELETE FROM inventario WHERE id = ?");
		
		try(var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString())){
			
			sentenciaPreparada.setObject(1, id);
			sentenciaPreparada.executeUpdate();
			
		} catch (SQLException exception) {
			var mensajeUsuario="Se ha presentado un problema tratando de eliminar la información de un inventario con el identificador ingresado";
			var mensajeTecnico="Se presentó una excepción de tipo SQLexception tratando de hacer un DELETE en la tabla Inventario,  para tener más detalles revise el log de errores";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de eliminar la información de un inventario con el identificador ingresado";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de hacer un DELETE en la tabla Inventario";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}
	}

}
