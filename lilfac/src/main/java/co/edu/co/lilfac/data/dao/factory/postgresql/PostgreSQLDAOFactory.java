package co.edu.co.lilfac.data.dao.factory.postgresql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import co.edu.co.lilfac.crosscutting.excepciones.DataLilfacException;
import co.edu.co.lilfac.crosscutting.excepciones.LilfacException;
import co.edu.co.lilfac.data.dao.entity.categoria.CategoriaDAO;
import co.edu.co.lilfac.data.dao.entity.categoria.impl.postgresql.CategoriaPostgreSQLDAO;
import co.edu.co.lilfac.data.dao.entity.categoriaproducto.CategoriaProductoDAO;
import co.edu.co.lilfac.data.dao.entity.categoriaproducto.impl.postgresql.CategoriaProductoPostgreSQLDAO;
import co.edu.co.lilfac.data.dao.entity.ciudad.CiudadDAO;
import co.edu.co.lilfac.data.dao.entity.ciudad.impl.postgresql.CiudadPostgreSQLDAO;
import co.edu.co.lilfac.data.dao.entity.cliente.ClienteDAO;
import co.edu.co.lilfac.data.dao.entity.cliente.impl.postgresql.ClientePostgreSQLDAO;
import co.edu.co.lilfac.data.dao.entity.costoadicional.CostoAdicionalDAO;
import co.edu.co.lilfac.data.dao.entity.costoadicional.impl.postgresql.CostoAdicionalPostgreSQLDAO;
import co.edu.co.lilfac.data.dao.entity.departamento.DepartamentoDAO;
import co.edu.co.lilfac.data.dao.entity.departamento.impl.postgresql.DepartamentoPostgreSQLDAO;
import co.edu.co.lilfac.data.dao.entity.empleado.EmpleadoDAO;
import co.edu.co.lilfac.data.dao.entity.empleado.impl.postgresql.EmpleadoPostgreSQLDAO;
import co.edu.co.lilfac.data.dao.entity.empresa.EmpresaDAO;
import co.edu.co.lilfac.data.dao.entity.empresa.impl.postgresql.EmpresaPostgreSQLDAO;
import co.edu.co.lilfac.data.dao.entity.entrega.EntregaDAO;
import co.edu.co.lilfac.data.dao.entity.entrega.impl.postgresql.EntregaPostgreSQLDAO;
import co.edu.co.lilfac.data.dao.entity.factura.FacturaDAO;
import co.edu.co.lilfac.data.dao.entity.factura.impl.postgresql.FacturaPostgreSQLDAO;
import co.edu.co.lilfac.data.dao.entity.historialcosto.HistorialCostoDAO;
import co.edu.co.lilfac.data.dao.entity.historialcosto.impl.postgresql.HistorialCostoPostgreSQLDAO;
import co.edu.co.lilfac.data.dao.entity.inventario.InventarioDAO;
import co.edu.co.lilfac.data.dao.entity.inventario.impl.postgresql.InventarioPostgreSQLDAO;
import co.edu.co.lilfac.data.dao.entity.pais.PaisDAO;
import co.edu.co.lilfac.data.dao.entity.pais.impl.postgresql.PaisPostgreSQLDAO;
import co.edu.co.lilfac.data.dao.entity.pedido.PedidoDAO;
import co.edu.co.lilfac.data.dao.entity.pedido.impl.postgresql.PedidoPostgreSQLDAO;
import co.edu.co.lilfac.data.dao.entity.producto.ProductoDAO;
import co.edu.co.lilfac.data.dao.entity.producto.impl.postgresql.ProductoPostgreSQLDAO;
import co.edu.co.lilfac.data.dao.entity.productopedido.ProductoPedidoDAO;
import co.edu.co.lilfac.data.dao.entity.productopedido.impl.postgresql.ProductoPedidoPostgreSQLDAO;
import co.edu.co.lilfac.data.dao.entity.recepcion.RecepcionDAO;
import co.edu.co.lilfac.data.dao.entity.recepcion.impl.postgresql.RecepcionPostgreSQLDAO;
import co.edu.co.lilfac.data.dao.factory.DAOFactory;

public class PostgreSQLDAOFactory extends DAOFactory{
	
	private Connection conexion;
	private boolean conexionEstaAbierta;
	private boolean transaccionIniciada;
	
	public PostgreSQLDAOFactory () throws LilfacException {
		abrirConexion();
		conexionEstaAbierta = false;
		transaccionIniciada = false;
	}

	public void abrirConexion() throws LilfacException {
        String url = "jdbc:postgresql://localhost:5432/postgresdoo";
        String usuario = "julianamontoya";
        String contrasena = "jules123";

        try {
            conexion = DriverManager.getConnection(url, usuario, contrasena);
            conexionEstaAbierta = true;
        } catch (SQLException exception) {
            var mensajeUsuario ="Se ha presentado un problema tratando de establecer la conexión con la fuente de datos.";
            var mensajeTecnico ="Se ha presentado una excepción de tipo SQLexception tratando de estbelecer la conexión con la base de datos POSGRESQL.";
            
            throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
        } catch (Exception exception) {
            var mensajeUsuario ="Se ha presentado un problema INESPERADO tratando de establecer la conexión con la fuente de datos.";
            var mensajeTecnico ="Se ha presentado una excepción NO CONTROLADA de tipo SQLexception tratando de estbelecer la conexión con la base de datos POSGRESQL.";
            
            throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
        }
	}


	@Override
	public void iniciarTransaccion() throws LilfacException {
		try {
			transaccionIniciada=true;
			conexion.setAutoCommit(false);	
			asegurarConexionAbierta();
		} catch (LilfacException exception) {
			throw exception;
		} catch (SQLException exception) {
			var mensajeUsuario="Se ha presentado un problema tratando de iniciar la transacción con la fuente de información para llevar a cabo la operación deseada";
			var mensajeTecnico="Sepresentó una excepción de tipo SQLexception tratando de iniciar la transacción con la base de datos,  para tener más detalles revise el log de errores";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		} catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de iniciar la transacción con la fuente de información para llevar a cabo la operación deseada";
			var mensajeTecnico="Sepresentó una excepción NO CONTROLADA tratando de iniciar la transacción con la base de datos,  para tener más detalles revise el log de errores";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}
		
	}

	@Override
	public void confirmarTransaccion() throws LilfacException {
		try {
			asegurarConexionAbierta();
			asegurarTransaccionIniciada();
			conexion.commit();
		} catch (LilfacException exception) {
			throw exception;
		} catch (SQLException exception) {
			var mensajeUsuario="Se ha presentado un problema tratando de confirmar la transacción realizada contra la fuente de información";
			var mensajeTecnico="Sepresentó una excepción de tipo SQLexception tratando de realizar COMMIT a la transacción contra la base de datos,  para tener más detalles revise el log de errores";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		} catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de confirmar la transacción realizada contra la fuente de información";
			var mensajeTecnico="Sepresentó una excepción NO CONTROLADA tratando de realizar COMMIT a la transacción contra la base de datos,  para tener más detalles revise el log de errores";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}
		
	}

	@Override
	public void cancelarTransaccion() throws LilfacException {
		try {
			asegurarConexionAbierta();
			asegurarTransaccionIniciada();
			conexion.rollback();
		} catch (LilfacException exception) {
			throw exception;
		} catch (SQLException exception) {
			var mensajeUsuario="Se ha presentado un problema tratando de cancelar la transacción realizada contra la fuente de información";
			var mensajeTecnico="Sepresentó una excepción de tipo SQLexception tratando de realizar ROLLBACK a la transacción contra la base de datos,  para tener más detalles revise el log de errores";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		} catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de cancelar la transacción realizada contra la fuente de información";
			var mensajeTecnico="Sepresentó una excepción NO CONTROLADA tratando de realizar ROLLBACK a la transacción contra la base de datos,  para tener más detalles revise el log de errores";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}
		
	}

	@Override
	public void cerrarConexion() throws LilfacException {
		try {
			asegurarConexionAbierta();
			conexion.close();
		} catch (LilfacException exception) {
			throw exception;
		} catch (SQLException exception) {
			var mensajeUsuario="Se ha presentado un problema tratando de cerrar la conexión contra la fuente de información";
			var mensajeTecnico="Sepresentó una excepción de tipo SQLexception tratando de cerrar la conexión contra la base de datos,  para tener más detalles revise el log de errores";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		} catch (Exception exception) {
			var mensajeUsuario="Se ha presentado un problema INESPERADO tratando de cerrar la conexión contra la fuente de información";
			var mensajeTecnico="Sepresentó una excepción NO CONTROLADA tratando de cerrar la conexión contra la base de datos,  para tener más detalles revise el log de errores";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico, exception);
		}
		
	}
	
	private void asegurarTransaccionIniciada() throws LilfacException{
		if(!transaccionIniciada) {
			var mensajeUsuario="Se presentó un problema gestionando la transacción con la fuente de datos";
			var mensajeTecnico="Se intentó gestionar (COMMIT/ROLLBACK) una transacción que no fue previamente iniciada";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico);
		}
	}
	
	private void asegurarConexionAbierta() throws LilfacException{
		if(!conexionEstaAbierta) {
			var mensajeUsuario="Se ha presentado un problema tratando de llevar a cabo la operación deseada con una conexión cerrada";
			var mensajeTecnico="Se intentó llevar a cabo una operación con una conexión a la base de datos cerrada";
			
			throw DataLilfacException.reportar(mensajeUsuario, mensajeTecnico);
		}
	}

	@Override
	public CategoriaDAO getCategoriaDAO() throws LilfacException {
		asegurarConexionAbierta();
		return new CategoriaPostgreSQLDAO(conexion);
	}

	@Override
	public CategoriaProductoDAO getCategoriaProductoDAO() throws LilfacException {
		asegurarConexionAbierta();
		return new CategoriaProductoPostgreSQLDAO(conexion);
	}

	@Override
	public CiudadDAO getCiudadDAO() throws LilfacException {
		asegurarConexionAbierta();
		return new CiudadPostgreSQLDAO(conexion);
	}

	@Override
	public ClienteDAO getClienteDAO() throws LilfacException {
		asegurarConexionAbierta();
		return new ClientePostgreSQLDAO(conexion);
	}

	@Override
	public CostoAdicionalDAO getCostoAdicionalDAO() throws LilfacException {
		asegurarConexionAbierta();
		return new CostoAdicionalPostgreSQLDAO(conexion);
	}

	@Override
	public DepartamentoDAO getDepartamentoDAO() throws LilfacException {
		asegurarConexionAbierta();
		return new DepartamentoPostgreSQLDAO(conexion);
	}

	@Override
	public EmpleadoDAO getEmpleadoDAO() throws LilfacException {
		asegurarConexionAbierta();
		return new EmpleadoPostgreSQLDAO(conexion);
	}

	@Override
	public EmpresaDAO getEmpresaDAO() throws LilfacException {
		asegurarConexionAbierta();
		return new EmpresaPostgreSQLDAO(conexion);
	}

	@Override
	public EntregaDAO getEntregaDAO() throws LilfacException {
		asegurarConexionAbierta();
		return new EntregaPostgreSQLDAO(conexion);
	}

	@Override
	public FacturaDAO getFacturaDAO() throws LilfacException {
		asegurarConexionAbierta();
		return new FacturaPostgreSQLDAO(conexion);
	}

	@Override
	public HistorialCostoDAO getHistorialCostoDAO() throws LilfacException {
		asegurarConexionAbierta();
		return new HistorialCostoPostgreSQLDAO(conexion);
	}

	@Override
	public InventarioDAO getInventarioDAO() throws LilfacException {
		asegurarConexionAbierta();
		return new InventarioPostgreSQLDAO(conexion);
	}

	@Override
	public PaisDAO getPaisDAO() throws LilfacException {
		asegurarConexionAbierta();
		return new PaisPostgreSQLDAO(conexion);
	}

	@Override
	public PedidoDAO getPedidoDAO() throws LilfacException {
		asegurarConexionAbierta();
		return new PedidoPostgreSQLDAO(conexion);
	}

	@Override
	public ProductoDAO getProductoDAO() throws LilfacException {
		asegurarConexionAbierta();
		return new ProductoPostgreSQLDAO(conexion);
	}

	@Override
	public ProductoPedidoDAO getProductoPedidoDAO() throws LilfacException {
		asegurarConexionAbierta();
		return new ProductoPedidoPostgreSQLDAO(conexion);
	}

	@Override
	public RecepcionDAO getRecepcionDAO() throws LilfacException {
		asegurarConexionAbierta();
		return new RecepcionPostgreSQLDAO(conexion);
	}

}
