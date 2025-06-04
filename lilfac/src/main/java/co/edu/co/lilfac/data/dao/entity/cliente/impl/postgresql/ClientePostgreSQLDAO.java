package co.edu.co.lilfac.data.dao.entity.cliente.impl.postgresql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import co.edu.co.lilfac.crosscutting.excepciones.DataLilfacException;
import co.edu.co.lilfac.crosscutting.excepciones.LilfacException;
import co.edu.co.lilfac.crosscutting.utilitarios.UtilUUID;
import co.edu.co.lilfac.data.dao.entity.cliente.ClienteDAO;
import co.edu.co.lilfac.entity.ClienteEntity;
import co.edu.co.lilfac.entity.CiudadEntity;


public class ClientePostgreSQLDAO implements ClienteDAO{
	
	private Connection conexion;
	
	public ClientePostgreSQLDAO(Connection conexion) {
		this.conexion=conexion;
	}
	
	private UUID obtenerIdCiudadPorNombre(String nombreCiudad) throws SQLException {
	    var sql = "SELECT id FROM ciudad WHERE nombre = ?";
	    
	    try (var sentencia = conexion.prepareStatement(sql)) {
	        sentencia.setString(1, nombreCiudad);

	        try (var resultado = sentencia.executeQuery()) {
	            if (resultado.next()) {
	                return UUID.fromString(resultado.getString("id"));
	            } else {
	                throw new SQLException("No se encontró ninguna ciudad con nombre: " + nombreCiudad);
	            }
	        }
	    }
	}

	
	public void create(ClienteEntity entity) throws LilfacException {
		var sentenciaSQL = new StringBuilder();
		
		sentenciaSQL.append("INSERT INTO cliente (id, nombre, apellido, cedula, telefono, correo, direccionresidencia, ciudad) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
		
		try(var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString())){
			
			UUID ciudad = obtenerIdCiudadPorNombre(entity.getCiudad().getNombre());
			
			sentenciaPreparada.setObject(1, entity.getId());
			sentenciaPreparada.setString(2, entity.getNombre());
			sentenciaPreparada.setString(3, entity.getApellido());
			sentenciaPreparada.setString(4, entity.getCedula());
			sentenciaPreparada.setString(5,  entity.getTelefono());
			sentenciaPreparada.setString(6,  entity.getCorreo());
			sentenciaPreparada.setString(7,  entity.getDireccionResidencia());
			sentenciaPreparada.setObject(8,  ciudad);
			
			sentenciaPreparada.executeUpdate();
			
		} catch (SQLException exception) {
			var mensajeUsuario="Se ha presentado un problema tratando de registrar la información de un nuevo cliente";
			var mensajeTecnico="Se presentó una excepción de tipo SQLexception tratando de hacer un INSERT en la tabla Cliente,  para tener más detalles revise el log de errores";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de registrar la información de un nuevo cliente";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de hacer un INSERT en la tabla Cliente";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}
		
	}

	
	public List<ClienteEntity> listByFilter(ClienteEntity filter) throws LilfacException {
		var listaClientes = new java.util.ArrayList<ClienteEntity>();
		var sentenciaSQL = new StringBuilder();
		sentenciaSQL.append("SELECT CL.id, CL.nombre, CL.apellido, CL.cedula, CL.telefono, CL.correo,"
				+ " CL.direccionresidencia, C.nombre AS nombre_ciudad FROM cliente CL JOIN ciudad C ON CL.ciudad = C.id WHERE 1=1");
		
		if (filter != null) {
			if (filter.getId() != null) {
				sentenciaSQL.append(" AND CL.id = ?");
			}
			if (filter.getNombre() != null && !filter.getNombre().isBlank()) {
				sentenciaSQL.append(" AND CL.nombre LIKE ?");
			}
			if (filter.getApellido() != null && !filter.getApellido().isBlank()) {
				sentenciaSQL.append(" AND CL.apellido LIKE ?");
			}
			if (filter.getCedula() != null) {
				sentenciaSQL.append(" AND CL.cedula LIKE ?");
			}
			if (filter.getTelefono() != null) {
				sentenciaSQL.append(" AND CL.telefono LIKE ?");
			}
			if (filter.getCorreo() != null && !filter.getCorreo().isBlank()) {
				sentenciaSQL.append(" AND CL.correo LIKE ?");
			}
			if (filter.getDireccionResidencia() != null && !filter.getDireccionResidencia().isBlank()) {
				sentenciaSQL.append(" AND CL.direccionResidencia LIKE ?");
			}
			if (filter.getCiudad() != null) {
				sentenciaSQL.append(" AND C.nombre = ?");
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
				if (filter.getApellido() != null && !filter.getApellido().isBlank()) {
					sentenciaPreparada.setString(indiceParametro++, "%" + filter.getApellido() + "%");
				}
				if (filter.getCedula() != null) {
					sentenciaPreparada.setString(indiceParametro++, "%" + filter.getCedula() + "%");
				}
				if (filter.getTelefono() != null) {
					sentenciaPreparada.setString(indiceParametro++, "%" + filter.getTelefono() + "%");
				}
				if (filter.getCorreo() != null && !filter.getCorreo().isBlank()) {
					sentenciaPreparada.setString(indiceParametro++, "%" + filter.getCorreo() + "%");
				}
				if (filter.getDireccionResidencia() != null && !filter.getDireccionResidencia().isBlank()) {
					sentenciaPreparada.setString(indiceParametro++, "%" + filter.getDireccionResidencia() + "%");
				}
				if (filter.getCiudad() != null) {
					sentenciaPreparada.setObject(indiceParametro++, filter.getCiudad().getNombre());
				}
			}
			
			try (var cursorResultados = sentenciaPreparada.executeQuery()) {
				
				while (cursorResultados.next()) {
		            var cliente = new ClienteEntity();
		            cliente.setId(UtilUUID.convertirAUUID(cursorResultados.getString("id")));
		            cliente.setNombre(cursorResultados.getString("nombre"));
		            cliente.setApellido(cursorResultados.getString("apellido"));
		            cliente.setCedula(cursorResultados.getString("cedula"));
		            cliente.setTelefono(cursorResultados.getString("telefono"));
		            cliente.setCorreo(cursorResultados.getString("correo"));
		            cliente.setDireccionResidencia(cursorResultados.getString("direccionResidencia"));

		            var ciudad = new CiudadEntity();
		            ciudad.setNombre(cursorResultados.getString("nombre_ciudad"));
		            cliente.setCiudad(ciudad);

		            listaClientes.add(cliente);
				}
			}
			
		} catch (SQLException exception) {
			var mensajeUsuario = "Se ha presentado un problema tratando de consultar los clientes con los filtros deseados.";
			var mensajeTecnico = "Se presentó una excepción SQLException ejecutando SELECT con filtros en la tabla Cliente.";

			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
			
		} catch (Exception exception) {
			var mensajeUsuario = "Se ha presentado un problema inesperado tratando de consultar los clientes con los filtros deseados.";
			var mensajeTecnico = "Se presentó una excepción NO CONTROLADA ejecutando SELECT con filtros en la tabla Cliente.";

			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}
		
		return listaClientes;
	}

	
	public List<ClienteEntity> listAll() throws LilfacException {
	    List<ClienteEntity> listaClientes = new ArrayList<>();
	    var sentenciaSQL = new StringBuilder();

	    sentenciaSQL.append("SELECT CL.id, CL.nombre, CL.apellido, CL.cedula, CL.telefono, CL.correo, CL.direccionResidencia, C.nombre AS nombre_ciudad FROM cliente CL JOIN ciudad C ON CL.ciudad = C.id");

	    try (var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString());
	         var resultados = sentenciaPreparada.executeQuery()) {

	        while (resultados.next()) {
	            var cliente = new ClienteEntity();
	            cliente.setId(UtilUUID.convertirAUUID(resultados.getString("id")));
	            cliente.setNombre(resultados.getString("nombre"));
	            cliente.setApellido(resultados.getString("apellido"));
	            cliente.setCedula(resultados.getString("cedula"));
	            cliente.setTelefono(resultados.getString("telefono"));
	            cliente.setCorreo(resultados.getString("correo"));
	            cliente.setDireccionResidencia(resultados.getString("direccionResidencia"));

	            var ciudad = new CiudadEntity();
	            ciudad.setNombre(resultados.getString("nombre_ciudad"));
	            cliente.setCiudad(ciudad);

	            listaClientes.add(cliente);
	        }

	    } catch (SQLException exception) {
	        var mensajeUsuario = "Se ha presentado un problema tratando de consultar la información de los clientes";
	        var mensajeTecnico = "Se presentó una excepción de tipo SQLexception tratando de hacer un SELECT en la tabla Cliente";
	        throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
	    } catch (Exception exception) {
	        var mensajeUsuario = "Se ha presentado un problema INESPERADO tratando de consultar la información de los clientes";
	        var mensajeTecnico = "Excepción NO CONTROLADA al hacer SELECT en la tabla Cliente";
	        throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
	    }

	    return listaClientes;
	}

	
	public ClienteEntity listById(UUID id) throws LilfacException {
		var clienteEntityRetorno=new ClienteEntity();
		var sentenciaSQL = new StringBuilder();
		
		sentenciaSQL.append("SELECT CL.id, CL.nombre, CL.apellido, CL.cedula, CL.telefono, CL.correo, CL.direccionResidencia, C.nombre AS nombre_ciudad FROM cliente CL JOIN ciudad C ON CL.ciudad = C.id WHERE CL.id = ?");
		
		try(var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString())){
			
			sentenciaPreparada.setObject(1, id);
			
			try (var cursorResultados = sentenciaPreparada.executeQuery()){
				
				if (cursorResultados.next()) {
					clienteEntityRetorno.setId(UtilUUID.convertirAUUID(cursorResultados.getString("id")));
					clienteEntityRetorno.setNombre(cursorResultados.getString("nombre"));
					clienteEntityRetorno.setApellido(cursorResultados.getString("apellido"));
					clienteEntityRetorno.setCedula(cursorResultados.getString("cedula"));
					clienteEntityRetorno.setTelefono(cursorResultados.getString("telefono"));
					clienteEntityRetorno.setCorreo(cursorResultados.getString("correo"));
					clienteEntityRetorno.setDireccionResidencia(cursorResultados.getString("direccionResidencia"));
					var ciudad = new CiudadEntity();
					ciudad.setNombre(cursorResultados.getString("nombre_ciudad"));
					clienteEntityRetorno.setCiudad(ciudad);
				}
				
			}
			
		} catch (SQLException exception) {
			var mensajeUsuario="Se ha presentado un problema tratando de consultar la información del cliente con el identificador deseado";
			var mensajeTecnico="Se presentó una excepción de tipo SQLexception tratando de hacer un SELECT en la tabla Cliente,  para tener más detalles revise el log de errores";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de consultar la información del cliente con el identificador deseado";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de hacer un SELECT en la tabla Cliente";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}
		
		return clienteEntityRetorno;
	}

	
	public void update(UUID id, ClienteEntity entity) throws LilfacException {
		var sentenciaSQL = new StringBuilder();
		
		sentenciaSQL.append("UPDATE cliente SET nombre = ?, apellido = ?, cedula = ?, telefono = ?, correo = ?, direccionResidencia = ?, ciudad = ? WHERE id = ?");
		
		try(var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString())){
			
			sentenciaPreparada.setString(1, entity.getNombre());
			sentenciaPreparada.setString(2, entity.getApellido());
			sentenciaPreparada.setString(3,  entity.getCedula());
			sentenciaPreparada.setString(4,  entity.getTelefono());
			sentenciaPreparada.setString(5,  entity.getCorreo());
			sentenciaPreparada.setString(6,  entity.getDireccionResidencia());
			sentenciaPreparada.setObject(7,  entity.getCiudad().getId());
			sentenciaPreparada.setObject(8, id);
			sentenciaPreparada.executeUpdate();
			
		} catch (SQLException exception) {
			var mensajeUsuario="Se ha presentado un problema tratando de actualizar la información de un cliente con el identificador ingresado";
			var mensajeTecnico="Se presentó una excepción de tipo SQLexception tratando de hacer un UPDATE en la tabla Cliente,  para tener más detalles revise el log de errores";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de actualizar la información de un cliente con el identificador ingresado";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de hacer un UPDATE en la tabla Cliente";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}
		
	}

	
	public void delete(UUID id) throws LilfacException {
		var sentenciaSQL = new StringBuilder();
		
		sentenciaSQL.append("DELETE FROM cliente WHERE id = ?");
		
		try(var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString())){
			
			sentenciaPreparada.setObject(1, id);
			sentenciaPreparada.executeUpdate();
			
		} catch (SQLException exception) {
			var mensajeUsuario="Se ha presentado un problema tratando de eliminar la información de un cliente con el identificador ingresado";
			var mensajeTecnico="Se presentó una excepción de tipo SQLexception tratando de hacer un DELETE en la tabla Cliente,  para tener más detalles revise el log de errores";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de eliminar la información de un cliente con el identificador ingresado";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de hacer un DELETE en la tabla Cliente";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}
		
	}

}
