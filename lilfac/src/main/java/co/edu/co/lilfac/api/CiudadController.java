package co.edu.co.lilfac.api;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.co.lilfac.businesslogic.facade.CiudadFacade;
import co.edu.co.lilfac.businesslogic.facade.impl.CiudadFacadeImpl;
import co.edu.co.lilfac.crosscutting.excepciones.LilfacException;
import co.edu.co.lilfac.dto.CiudadDTO;
import co.edu.co.lilfac.init.LilfacApplication;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/ciudades")

public class CiudadController {
private final LilfacApplication lilfacApplication;
	
	private CiudadFacade ciudadFachada;
	
	public CiudadController(LilfacApplication lilfacApplication) throws LilfacException {
		ciudadFachada = new CiudadFacadeImpl();
		this.lilfacApplication = lilfacApplication;
	}
	
	
	@GetMapping("/dummy")
	public CiudadDTO getDummy() {
		return new CiudadDTO();	
	}
	
	@GetMapping("/ciudades")
	public ResponseEntity<List<CiudadDTO> >ConsultarCiudades() throws LilfacException {
		var lista = ciudadFachada.consultarCiudades();	
		 return ResponseEntity.ok(lista);
	}
	
	@GetMapping("/filtro")
	public ResponseEntity<List<CiudadDTO> >Consultar() throws LilfacException {
		var lista = ciudadFachada.consultarCiudadesFiltro(getDummy());	
		return ResponseEntity.ok(lista);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<CiudadDTO> Consultar(@PathVariable("id") UUID id) throws LilfacException {
		var ciudad =  ciudadFachada.consultarCiudadPorId(id);
		return ResponseEntity.ok(ciudad);
	}
	
	@PostMapping
	public ResponseEntity<String> crear(@RequestBody CiudadDTO ciudad) throws LilfacException {
		ciudadFachada.registrarNuevaCiudad(ciudad);
		var mensajeExito = "La ciudad " + ciudad.getNombre() + " se ha registrado correctamente";
		return ResponseEntity.ok(mensajeExito);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<String> modificar(@PathVariable("id") UUID id, @RequestBody CiudadDTO ciudad) throws LilfacException {
		ciudad.setId(id);
		ciudadFachada.modificarCiudadExistente(id, ciudad);
		var mensajeExito = "La ciudad " + ciudad.getNombre() + " se ha modificado correctamente";
		return ResponseEntity.ok(mensajeExito);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> eliminar(@PathVariable("id") UUID id) throws LilfacException {
		ciudadFachada.darBajaDefinitivamenteCiudadExistente(id);
		var mensajeExito = "La ciudad se ha eliminado correctamente";
		return ResponseEntity.ok(mensajeExito);
	}

}
