package co.edu.co.lilfac.businesslogic.businesslogic.domain;
import java.util.UUID;
import co.edu.co.lilfac.crosscutting.utilitarios.UtilObjeto;
import co.edu.co.lilfac.crosscutting.utilitarios.UtilTexto;
import co.edu.co.lilfac.crosscutting.utilitarios.UtilUUID;

public final class DepartamentoDomain {
	
	private UUID id;
	private String nombre;
	private PaisDomain pais;
	
	DepartamentoDomain() {
		setId(UtilUUID.obtenerValorDefecto());
		setNombre(UtilTexto.getInstance().obtenerValorDefecto());
		setPais(PaisDomain.obtenerValorDefecto());
	}
	
	public DepartamentoDomain(final UUID id) {
		setId(id);
		setNombre(UtilTexto.getInstance().obtenerValorDefecto());	
		setPais(PaisDomain.obtenerValorDefecto());
	}
		
	public DepartamentoDomain(final UUID id, final String nombre, final PaisDomain pais) {
		setId(id);
		setNombre(nombre);	
		setPais(pais);
	}
	
	static DepartamentoDomain obtenerValorDefecto() {
		return new DepartamentoDomain();
	}
	
	public static DepartamentoDomain obtenerValorDefecto(final DepartamentoDomain departamento ) {
		return UtilObjeto.getInstance().obtenerValorDefecto(departamento, obtenerValorDefecto());
	}

	public UUID getId() {
		return id;
	}

	private void setId(final UUID id) {
		this.id = UtilUUID.obtenerValorDefecto(id);
	}

	public String getNombre() {
		return nombre;
	}

	private void setNombre(final String nombre) {
		this.nombre = UtilTexto.getInstance().quitarEspaciosBlancoInicioFin(nombre);
	}

	public PaisDomain getPais() {
		return pais;
	}

	private void setPais(final PaisDomain pais) {
		this.pais = PaisDomain.obtenerValorDefecto(pais);
	}

	

}
