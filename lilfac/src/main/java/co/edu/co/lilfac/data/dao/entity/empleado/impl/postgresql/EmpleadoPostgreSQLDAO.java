package co.edu.co.lilfac.data.dao.entity.empleado.impl.postgresql;

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
import co.edu.co.lilfac.data.dao.entity.empleado.EmpleadoDAO;
import co.edu.co.lilfac.entity.EmpleadoEntity;
import co.edu.co.lilfac.entity.CiudadEntity;
import co.edu.co.lilfac.entity.EmpresaEntity;


public class EmpleadoPostgreSQLDAO implements EmpleadoDAO {

    private final Connection conexion;

    public EmpleadoPostgreSQLDAO(Connection conexion) {
        this.conexion = conexion;
    }

    
    public void create(EmpleadoEntity entity) throws LilfacException {
        var sentenciaSQL = new StringBuilder();
        sentenciaSQL.append("INSERT INTO empleado (id, nombre, apellido, cedula, telefono, correo, direccionResidencia, ciudad, empresa) ")
                    .append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");

        try (var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString())) {
            sentenciaPreparada.setObject(1, entity.getId());
            sentenciaPreparada.setString(2, entity.getNombre());
            sentenciaPreparada.setString(3, entity.getApellido());
            sentenciaPreparada.setString(4, entity.getCedula());
            sentenciaPreparada.setString(5, entity.getTelefono());
            sentenciaPreparada.setString(6, entity.getCorreo());
            sentenciaPreparada.setString(7, entity.getDireccionResidencia());
            sentenciaPreparada.setObject(8, entity.getCiudad().getId());
            sentenciaPreparada.setObject(9, entity.getEmpresa().getId());

            sentenciaPreparada.executeUpdate();

        } catch (SQLException exception) {
            var mensajeUsuario = "Se ha presentado un problema tratando de registrar la información de un nuevo empleado.";
            var mensajeTecnico = "Error de SQL al intentar insertar un nuevo empleado.";
            throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);

        } catch (Exception exception) {
            var mensajeUsuario = "Se ha presentado un problema inesperado al registrar un nuevo empleado.";
            var mensajeTecnico = "Error inesperado durante el registro de un nuevo empleado.";
            throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
        }
    }

    
    public List<EmpleadoEntity> listByFilter(EmpleadoEntity filter) throws LilfacException {
        var listaEmpleados = new ArrayList<EmpleadoEntity>();
        var sentenciaSQL = new StringBuilder();
        sentenciaSQL.append("SELECT e.id, e.nombre, e.apellido, e.cedula, e.telefono, e.correo, ")
                    .append("e.direccionResidencia, c.id AS ciudad_id, ")
                    .append("em.id AS empresa_id ")
                    .append("FROM empleado e ")
                    .append("INNER JOIN ciudad c ON e.ciudad = c.id ")
                    .append("INNER JOIN empresa em ON e.empresa = em.id ")
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
	
	        if (!UtilTexto.getInstance().estaVacia(filter.getApellido())) {
	            sentenciaSQL.append("AND e.apellido ILIKE ? ");
	            parametros.add("%" + filter.getApellido().trim() + "%");
	        }
	
	        if (!UtilTexto.getInstance().estaVacia(filter.getCedula())) {
	            sentenciaSQL.append("AND e.cedula = ? ");
	            parametros.add(filter.getCedula().trim());
	        }
	
	        if (!UtilTexto.getInstance().estaVacia(filter.getTelefono())) {
	            sentenciaSQL.append("AND e.telefono = ? ");
	            parametros.add(filter.getTelefono().trim());
	        }
	
	        if (!UtilTexto.getInstance().estaVacia(filter.getCorreo())) {
	            sentenciaSQL.append("AND e.correo ILIKE ? ");
	            parametros.add("%" + filter.getCorreo().trim() + "%");
	        }
	
	        if (!UtilTexto.getInstance().estaVacia(filter.getDireccionResidencia())) {
	            sentenciaSQL.append("AND e.direccionResidencia ILIKE ? ");
	            parametros.add("%" + filter.getDireccionResidencia().trim() + "%");
	        }
	
	        if (!UtilObjeto.getInstance().esNulo(filter.getCiudad()) && !UtilUUID.esValorDefecto(filter.getCiudad().getId())) {
	            sentenciaSQL.append("AND e.ciudad = ? ");
	            parametros.add(filter.getCiudad().getId());
	        }
	
	        if (!UtilObjeto.getInstance().esNulo(filter.getEmpresa()) && !UtilUUID.esValorDefecto(filter.getEmpresa().getId())){
	            sentenciaSQL.append("AND e.empresa = ? ");
	            parametros.add(filter.getEmpresa().getId());
	        }
	
	        try (var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString())) {
	            for (int i = 0; i < parametros.size(); i++) {
	                sentenciaPreparada.setObject(i + 1, parametros.get(i));
	            }
	
	            try (var cursor = sentenciaPreparada.executeQuery()) {
	                while (cursor.next()) {
	                    var empleado = new EmpleadoEntity();
	                    empleado.setId(UtilUUID.convertirAUUID(cursor.getString("id")));
	                    empleado.setNombre(cursor.getString("nombre"));
	                    empleado.setApellido(cursor.getString("apellido"));
	                    empleado.setCedula(cursor.getString("cedula"));
	                    empleado.setTelefono(cursor.getString("telefono"));
	                    empleado.setCorreo(cursor.getString("correo"));
	                    empleado.setDireccionResidencia(cursor.getString("direccionResidencia"));
	
	                    var ciudad = new CiudadEntity();
	                    ciudad.setId(UtilUUID.convertirAUUID(cursor.getString("ciudad_id")));
	                    empleado.setCiudad(ciudad);
	
	                    var empresa = new EmpresaEntity();
	                    empresa.setId(UtilUUID.convertirAUUID(cursor.getString("empresa_id")));
	                    empleado.setEmpresa(empresa);
	
	                    listaEmpleados.add(empleado);
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

        return listaEmpleados;
    }


    
    public List<EmpleadoEntity> listAll() throws LilfacException {
        List<EmpleadoEntity> listaEmpleados = new ArrayList<>();
        var sentenciaSQL = new StringBuilder();
        sentenciaSQL.append("SELECT e.id, e.nombre, e.apellido, e.cedula, e.telefono, e.correo, ")
                    .append("e.direccionResidencia, c.id AS ciudad_id, ")
                    .append("em.id AS empresa_id, em.nombre AS nombre_empresa ")
                    .append("FROM empleado e ")
                    .append("INNER JOIN ciudad c ON e.ciudad = c.id ")
                    .append("INNER JOIN empresa em ON e.empresa = em.id");

        try (var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString());
             var resultados = sentenciaPreparada.executeQuery()) {

            while (resultados.next()) {
                var empleado = new EmpleadoEntity();
                empleado.setId(UtilUUID.convertirAUUID(resultados.getString("id")));
                empleado.setNombre(resultados.getString("nombre"));
                empleado.setApellido(resultados.getString("apellido"));
                empleado.setCedula(resultados.getString("cedula"));
                empleado.setTelefono(resultados.getString("telefono"));
                empleado.setCorreo(resultados.getString("correo"));
                empleado.setDireccionResidencia(resultados.getString("direccionResidencia"));

                var ciudad = new CiudadEntity();
                ciudad.setId(UtilUUID.convertirAUUID(resultados.getString("ciudad_id")));
                empleado.setCiudad(ciudad);

                var empresa = new EmpresaEntity();
                empresa.setId(UtilUUID.convertirAUUID(resultados.getString("empresa_id")));
                empleado.setEmpresa(empresa);

                listaEmpleados.add(empleado);
            }

        } catch (SQLException exception) {
            throw DataLilfacException.reportar(
                    "Se ha presentado un problema consultando todos los empleados.",
                    "SQLException ejecutando SELECT ALL en Empleado.",
                    exception
            );
        } catch (Exception exception) {
            throw DataLilfacException.reportar(
                    "Se ha presentado un problema inesperado consultando todos los empleados.",
                    "Excepción no controlada ejecutando SELECT ALL en Empleado.",
                    exception
            );
        }

        return listaEmpleados;
    }


	
	public EmpleadoEntity listById(UUID id) throws LilfacException {
		var empleadoEntityRetorno=new EmpleadoEntity();
		var sentenciaSQL = new StringBuilder();
		
		sentenciaSQL.append("SELECT id, nombre, apellido, cedula, telefono, correo,")
					.append("direccionResidencia, ciudad, empresa FROM empleado WHERE id = ?");
		
		try(var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString())){
			
			sentenciaPreparada.setObject(1, id);
			
			try (var cursorResultados = sentenciaPreparada.executeQuery()){
				
				if (cursorResultados.next()) {
					empleadoEntityRetorno.setId(UtilUUID.convertirAUUID(cursorResultados.getString("id")));
					empleadoEntityRetorno.setNombre(cursorResultados.getString("nombre"));
					empleadoEntityRetorno.setApellido(cursorResultados.getString("apellido"));
					empleadoEntityRetorno.setCedula(cursorResultados.getString("cedula"));
					empleadoEntityRetorno.setTelefono(cursorResultados.getString("telefono"));
					empleadoEntityRetorno.setCorreo(cursorResultados.getString("correo"));
					empleadoEntityRetorno.setDireccionResidencia(cursorResultados.getString("direccionResidencia"));

		            var ciudad = new CiudadEntity();
	                ciudad.setId(UtilUUID.convertirAUUID(cursorResultados.getString("ciudad")));

		            empleadoEntityRetorno.setCiudad(ciudad);
		            
		            var empresa = new EmpresaEntity();
	                empresa.setId(UtilUUID.convertirAUUID(cursorResultados.getString("empresa")));
		            empleadoEntityRetorno.setEmpresa(empresa);
				}
				
			}
			
		} catch (SQLException exception) {
			var mensajeUsuario="Se ha presentado un problema tratando de consultar la información del empleado con el identificador deseado";
			var mensajeTecnico="Se presentó una excepción de tipo SQLexception tratando de hacer un SELECT en la tabla Empleado,  para tener más detalles revise el log de errores";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de consultar la información del empleado con el identificador deseado";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de hacer un SELECT en la tabla Empleado";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}
		
		return empleadoEntityRetorno;
	}

	
	public void update(UUID id, EmpleadoEntity entity) throws LilfacException {
		var sentenciaSQL = new StringBuilder();
		
		sentenciaSQL.append("UPDATE empleado SET nombre = ?, apellido = ?, cedula = ?, telefono = ?, correo = ?, direccionResidencia = ?"
							+ ", ciudad = ?, empresa = ? WHERE id = ?");
		
		try(var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString())){
			
			sentenciaPreparada.setString(1, entity.getNombre());
			sentenciaPreparada.setString(2, entity.getApellido());
			sentenciaPreparada.setString(3, entity.getCedula());
			sentenciaPreparada.setString(4, entity.getTelefono());
			sentenciaPreparada.setString(5, entity.getCorreo());
			sentenciaPreparada.setString(6, entity.getDireccionResidencia());
			sentenciaPreparada.setObject(7, entity.getCiudad().getId());
			sentenciaPreparada.setObject(8, entity.getEmpresa().getId());
			sentenciaPreparada.setObject(9, id);
			
			sentenciaPreparada.executeUpdate();
			
		} catch (SQLException exception) {
			var mensajeUsuario="Se ha presentado un problema tratando de actualizar la información de un empleado con el identificador ingresado";
			var mensajeTecnico="Se presentó una excepción de tipo SQLexception tratando de hacer un UPDATE en la tabla Empleado,  para tener más detalles revise el log de errores";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de actualizar la información de un empleado con el identificador ingresado";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de hacer un UPDATE en la tabla Empleado";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}
		
	}

	
	public void delete(UUID id) throws LilfacException {
		var sentenciaSQL = new StringBuilder();
		
		sentenciaSQL.append("DELETE FROM empleado WHERE id = ?");
		
		try(var sentenciaPreparada = conexion.prepareStatement(sentenciaSQL.toString())){
			
			sentenciaPreparada.setObject(1, id);
			sentenciaPreparada.executeUpdate();
			
		} catch (SQLException exception) {
			var mensajeUsuario="Se ha presentado un problema tratando de eliminar la información de un empleado con el identificador ingresado";
			var mensajeTecnico="Se presentó una excepción de tipo SQLexception tratando de hacer un DELETE en la tabla Empleado,  para tener más detalles revise el log de errores";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de eliminar la información de un empleado con el identificador ingresado";
			var mensajeTecnico="Se presentó una excepción NO CONTROLADA de tipo Exception tratando de hacer un DELETE en la tabla Empleado";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}
		
	}


}
