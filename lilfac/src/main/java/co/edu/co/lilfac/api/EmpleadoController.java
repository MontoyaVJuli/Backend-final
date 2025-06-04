package co.edu.co.lilfac.api;

import java.util.List;
import java.util.UUID;
import java.util.HashMap;
import java.util.Map;


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


import co.edu.co.lilfac.businesslogic.facade.EmpleadoFacade;
import co.edu.co.lilfac.businesslogic.facade.impl.EmpleadoFacadeImpl;
import co.edu.co.lilfac.crosscutting.excepciones.LilfacException;
import co.edu.co.lilfac.dto.EmpleadoDTO;
import co.edu.co.lilfac.init.LilfacApplication;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/empleados")

public class EmpleadoController {
	
private final LilfacApplication lilfacApplication;
	
	private EmpleadoFacade empleadoFachada;
	
	public EmpleadoController(LilfacApplication lilfacApplication) throws LilfacException {
		empleadoFachada = new EmpleadoFacadeImpl();
		this.lilfacApplication = lilfacApplication;
	}
	
	
	@GetMapping("/dummy")
	public EmpleadoDTO getDummy() {
		return new EmpleadoDTO();	
	}
	
	@GetMapping
	public ResponseEntity<List<EmpleadoDTO> >Consultar() throws LilfacException {
		var lista = empleadoFachada.consultarEmpleados(getDummy());	
		return new ResponseEntity<>(lista, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<EmpleadoDTO> Consultar(@PathVariable("id") UUID id) throws LilfacException {
		var empleado =  empleadoFachada.consultarEmpleadoPorId(id);
		return new ResponseEntity<>(empleado, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<String> crear(@RequestBody EmpleadoDTO empleado) throws LilfacException {
		empleadoFachada.registrarNuevoEmpleado(empleado);
		var mensajeExito = "El empleado " + empleado.getNombre() + " se ha registrado correctamente";
		return new ResponseEntity<>(mensajeExito, HttpStatus.OK);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<String> modificar(@PathVariable("id") UUID id, @RequestBody EmpleadoDTO empleado) throws LilfacException {
		empleado.setId(id);
		empleadoFachada.modificarEmpleadoExistente(id, empleado);
		var mensajeExito = "El empleado " + empleado.getNombre() + " se ha modificado correctamente";
		return new ResponseEntity<>(mensajeExito, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> eliminar(@PathVariable("id") UUID id) throws LilfacException {
		empleadoFachada.darBajaDefinitivamenteEmpleadoExistente(id);
		var mensajeExito = "El empleado se ha eliminado correctamente";
		return new ResponseEntity<>(mensajeExito, HttpStatus.OK);
	}
}
