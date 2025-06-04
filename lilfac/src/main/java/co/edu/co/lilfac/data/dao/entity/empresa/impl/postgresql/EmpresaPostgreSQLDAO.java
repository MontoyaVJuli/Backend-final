package co.edu.co.lilfac.data.dao.entity.empresa.impl.postgresql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import co.edu.co.lilfac.crosscutting.excepciones.DataLilfacException;
import co.edu.co.lilfac.crosscutting.excepciones.LilfacException;
import co.edu.co.lilfac.crosscutting.utilitarios.UtilObjeto;
import co.edu.co.lilfac.crosscutting.utilitarios.UtilTexto;
import co.edu.co.lilfac.crosscutting.utilitarios.UtilUUID;
import co.edu.co.lilfac.data.dao.entity.empresa.EmpresaDAO;
import co.edu.co.lilfac.entity.CiudadEntity;
import co.edu.co.lilfac.entity.EmpresaEntity;

public class EmpresaPostgreSQLDAO implements EmpresaDAO{
	
	private Connection conexion;
	
	public EmpresaPostgreSQLDAO(Connection conexion) {
		this.conexion=conexion;
	}


	
	public void create(EmpresaEntity entity) throws LilfacException {
		var sentenciaSQL = new StringBuilder();
		
		sentenciaSQL.append("INSERT INTO empresa (id, nombre, nit, telefono, correo, direccion, ciudad)")
					.append(" VALUES (?, ?, ?, ?, ?, ?, ?)");
		
		try(var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString())){
			
			sentenciaPreparada.setObject(1, entity.getId());
			sentenciaPreparada.setString(2, entity.getNombre());
			sentenciaPreparada.setString(3, entity.getNit());
			sentenciaPreparada.setString(4, entity.getTelefono());
			sentenciaPreparada.setString(5, entity.getCorreo());
			sentenciaPreparada.setString(6, entity.getDireccion());
			sentenciaPreparada.setObject(7, entity.getCiudad().getId());
			sentenciaPreparada.executeUpdate();
			
		} catch (SQLException exception) {
			var mensajeUsuario="Se ha presentado un problema tratando de registrar la información de una nueva empresa";
			var mensajeTecnico="Se presentó una excepción de tipo SQLexception tratando de hacer un INSERT en la tabla Empresa,  para tener más detalles revise el log de errores";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de registrar la información de una nueva empresa";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de hacer un INSERT en la tabla Empresa";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}
		
	}

	
	public List<EmpresaEntity> listByFilter(EmpresaEntity filter) throws LilfacException {
		var listaEmpresas = new java.util.ArrayList<EmpresaEntity>();
		var sentenciaSQL = new StringBuilder();
		sentenciaSQL.append("SELECT e.id, e.nombre, e.nit, e.telefono, e.correo, ")
        			.append("e.direccion, c.id AS ciudad_id, ")
			        .append("FROM empresa e ")
			        .append("INNER JOIN ciudad c ON e.ciudad = c.id ")
			        .append("WHERE 1=1 ");
		
        var parametros = new ArrayList<Object>();
        
        if (!UtilObjeto.getInstance().esNulo(filter)) {

	        if (!UtilObjeto.getInstance().esNulo(filter.getId()) && !UtilUUID.esValorDefecto(filter.getId())) {
	            sentenciaSQL.append("AND e.id = ? ");
	            parametros.add(filter.getId());
	        }
	
	        if (!UtilTexto.getInstance().estaVacia(filter.getNombre())) {
	            sentenciaSQL.append("AND e.nombre ILIKE ? ");
	            parametros.add("%" + filter.getNombre().trim() + "%");
	        }	
	        
	        if (!UtilTexto.getInstance().estaVacia(filter.getNit())) {
	            sentenciaSQL.append("AND e.nit = ? ");
	            parametros.add(filter.getNit().trim());
	        }
	
	        if (!UtilTexto.getInstance().estaVacia(filter.getTelefono())) {
	            sentenciaSQL.append("AND e.telefono = ? ");
	            parametros.add(filter.getTelefono().trim());
	        }
	
	        if (!UtilTexto.getInstance().estaVacia(filter.getCorreo())) {
	            sentenciaSQL.append("AND e.correo ILIKE ? ");
	            parametros.add("%" + filter.getCorreo().trim() + "%");
	        }
	
	        if (!UtilTexto.getInstance().estaVacia(filter.getDireccion())) {
	            sentenciaSQL.append("AND e.direccion ILIKE ? ");
	            parametros.add("%" + filter.getDireccion().trim() + "%");
	        }
	
	        if (!UtilObjeto.getInstance().esNulo(filter.getCiudad()) && !UtilUUID.esValorDefecto(filter.getCiudad().getId())) {
	            sentenciaSQL.append("AND e.ciudad = ? ");
	            parametros.add(filter.getCiudad().getId());
	        }
	        
	        try (var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString())) {
	            for (int i = 0; i < parametros.size(); i++) {
	                sentenciaPreparada.setObject(i + 1, parametros.get(i));
	            }
	
	            try (var cursor = sentenciaPreparada.executeQuery()) {
	                while (cursor.next()) {
	                    var empresa = new EmpresaEntity();
	                    empresa.setId(UtilUUID.convertirAUUID(cursor.getString("id")));
	                    empresa.setNombre(cursor.getString("nombre"));
	                    empresa.setNit(cursor.getString("nit"));
	                    empresa.setTelefono(cursor.getString("telefono"));
	                    empresa.setCorreo(cursor.getString("correo"));
	                    empresa.setDireccion(cursor.getString("direccion"));
	
	                    var ciudad = new CiudadEntity();
	                    ciudad.setId(UtilUUID.convertirAUUID(cursor.getString("ciudad_id")));
	                    empresa.setCiudad(ciudad);
	
	                    listaEmpresas.add(empresa);
	                }
	            }
	
	        } catch (SQLException exception) {
	            throw DataLilfacException.reportar(
	                    "Se ha presentado un problema consultando empleados con filtro.",
	                    "SQLException ejecutando SELECT en Empleado con filtros.",
	                    exception
	            );
	        } catch (Exception exception) {
	            throw DataLilfacException.reportar(
	                    "Se ha presentado un problema inesperado consultando empleados.",
	                    "Excepción no controlada ejecutando SELECT en Empleado con filtros.",
	                    exception
	            );
	        }
        }
		
		return listaEmpresas;
	}

	
	public List<EmpresaEntity> listAll() throws LilfacException {
	    List<EmpresaEntity> listaEmpresas = new ArrayList<>();
	    var sentenciaSQL = new StringBuilder();

	    sentenciaSQL.append("SELECT E.id, E.nombre, E.nit, E.telefono, E.correo, E.direccion, ")
	    			.append("C.id AS ciudad_id FROM empresa E JOIN ciudad C ON E.ciudad = C.id");

	    try (var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString());
	         var resultados = sentenciaPreparada.executeQuery()) {

	        while (resultados.next()) {
	            var empresa = new EmpresaEntity();
	            empresa.setId(UtilUUID.convertirAUUID(resultados.getString("id")));
	            empresa.setNit(resultados.getString("nit"));
	            empresa.setTelefono(resultados.getString("telefono"));
	            empresa.setCorreo(resultados.getString("correo"));
	            empresa.setDireccion(resultados.getString("direccion"));

	            var ciudad = new CiudadEntity();
                ciudad.setId(UtilUUID.convertirAUUID(resultados.getString("ciudad_id")));
	            empresa.setCiudad(ciudad);

	            listaEmpresas.add(empresa);
	        }

	    } catch (SQLException exception) {
	        var mensajeUsuario = "Se ha presentado un problema tratando de consultar la información de las empresas";
	        var mensajeTecnico = "Se presentó una excepción de tipo SQLexception tratando de hacer un SELECT en la tabla Empresa";
	        throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
	    } catch (Exception exception) {
	        var mensajeUsuario = "Se ha presentado un problema INESPERADO tratando de consultar la información de las empresas";
	        var mensajeTecnico = "Excepción NO CONTROLADA al hacer SELECT en la tabla Empresa";
	        throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
	    }

	    return listaEmpresas;
	}

	
	public EmpresaEntity listById(UUID id) throws LilfacException {
		var empresaEntityRetorno=new EmpresaEntity();
		var sentenciaSQL = new StringBuilder();
		
		sentenciaSQL.append("SELECT E.id, E.nombre, E.nit, E.telefono, E.correo, E.direccion,")
					.append(" C.id AS ciudad_id FROM empresa E JOIN ciudad C ON E.ciudad = C.id WHERE E.id = ?");
		
		try(var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString())){
			
			sentenciaPreparada.setObject(1, id);
			
			try (var cursorResultados = sentenciaPreparada.executeQuery()){
				
				if (cursorResultados.next()) {
					empresaEntityRetorno.setId(UtilUUID.convertirAUUID(cursorResultados.getString("id")));
					empresaEntityRetorno.setNombre(cursorResultados.getString("nombre"));
					empresaEntityRetorno.setNit(cursorResultados.getString("nit"));
					empresaEntityRetorno.setTelefono(cursorResultados.getString("telefono"));
					empresaEntityRetorno.setCorreo(cursorResultados.getString("correo"));
					empresaEntityRetorno.setDireccion(cursorResultados.getString("direccion"));
					var ciudad = new CiudadEntity();
	                ciudad.setId(UtilUUID.convertirAUUID(cursorResultados.getString("ciudad_id")));
					empresaEntityRetorno.setCiudad(ciudad);
				}
				
			}
			
		} catch (SQLException exception) {
			var mensajeUsuario="Se ha presentado un problema tratando de consultar la información de la empresa con el identificador deseado";
			var mensajeTecnico="Se presentó una excepción de tipo SQLexception tratando de hacer un SELECT en la tabla Empresa,  para tener más detalles revise el log de errores";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de consultar la información de la empresa con el identificador deseado";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de hacer un SELECT en la tabla Empresa";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}
		
		return empresaEntityRetorno;
	}

	
	public void update(UUID id, EmpresaEntity entity) throws LilfacException {
		var sentenciaSQL = new StringBuilder();
		
		sentenciaSQL.append("UPDATE empresa SET nombre = ?, nit = ?, telefono = ?, correo = ?,")
				    .append(" direccion = ?, ciudad = ? WHERE id = ?");
		
		try(var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString())){
			
			sentenciaPreparada.setString(1, entity.getNombre());
			sentenciaPreparada.setString(2, entity.getNit());
			sentenciaPreparada.setString(3, entity.getTelefono());
			sentenciaPreparada.setString(4, entity.getCorreo());
			sentenciaPreparada.setString(5,  entity.getDireccion());
			sentenciaPreparada.setObject(6, entity.getCiudad().getId());
			sentenciaPreparada.setObject(7, id);
			sentenciaPreparada.executeUpdate();
			
		} catch (SQLException exception) {
			var mensajeUsuario="Se ha presentado un problema tratando de actualizar la información de una empresa con el identificador ingresado";
			var mensajeTecnico="Se presentó una excepción de tipo SQLexception tratando de hacer un UPDATE en la tabla Empresa,  para tener más detalles revise el log de errores";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de actualizar la información de una empresa con el identificador ingresado";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de hacer un UPDATE en la tabla Empresa";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}
		
	}

	
	public void delete(UUID id) throws LilfacException {
		var sentenciaSQL = new StringBuilder();
		
		sentenciaSQL.append("DELETE FROM empresa WHERE id = ?");
		
		try(var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString())){
			
			sentenciaPreparada.setObject(1, id);
			sentenciaPreparada.executeUpdate();
			
		} catch (SQLException exception) {
			var mensajeUsuario="Se ha presentado un problema tratando de eliminar la información de una empresa con el identificador ingresado";
			var mensajeTecnico="Se presentó una excepción de tipo SQLexception tratando de hacer un DELETE en la tabla Empresa,  para tener más detalles revise el log de errores";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de eliminar la información de una empresa con el identificador ingresado";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de hacer un DELETE en la tabla Empresa";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}
		
	}

}
