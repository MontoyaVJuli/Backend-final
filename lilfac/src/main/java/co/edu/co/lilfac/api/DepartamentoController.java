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

import co.edu.co.lilfac.businesslogic.facade.DepartamentoFacade;
import co.edu.co.lilfac.businesslogic.facade.impl.DepartamentoFacadeImpl;
import co.edu.co.lilfac.crosscutting.excepciones.LilfacException;
import co.edu.co.lilfac.dto.DepartamentoDTO;
import co.edu.co.lilfac.init.LilfacApplication;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/departamentos")

public class DepartamentoController {
	private final LilfacApplication lilfacApplication;

	private DepartamentoFacade departamentoFachada;
		
		public DepartamentoController(LilfacApplication lilfacApplication) throws LilfacException {
			departamentoFachada = new DepartamentoFacadeImpl();
			this.lilfacApplication = lilfacApplication;
		}
		
		
		@GetMapping("/dummy")
		public DepartamentoDTO getDummy() {
			return new DepartamentoDTO();	
		}
		
		@GetMapping("/departamentos")
		public ResponseEntity<List<DepartamentoDTO> >ConsultarDepartamentos() throws LilfacException {
			var lista = departamentoFachada.consultarDepartamentos();	
			 return ResponseEntity.ok(lista);
		}
		
		@GetMapping("/filtro")
		public ResponseEntity<List<DepartamentoDTO> >Consultar() throws LilfacException {
			var lista = departamentoFachada.consultarDepartamentosFiltro(getDummy());	
			return ResponseEntity.ok(lista);
		}
		
		@GetMapping("/{id}")
		public ResponseEntity<DepartamentoDTO> Consultar(@PathVariable("id") UUID id) throws LilfacException {
			var departamento =  departamentoFachada.consultarDepartamentoPorId(id);
			return ResponseEntity.ok(departamento);
		}
		
		@PostMapping
		public ResponseEntity<String> crear(@RequestBody DepartamentoDTO departamento) throws LilfacException {
			departamentoFachada.registrarNuevoDepartamento(departamento);
			var mensajeExito = "El departamento " + departamento.getNombre() + " se ha registrado correctamente";
			return ResponseEntity.ok(mensajeExito);
		}
		
		@PutMapping("/{id}")
		public ResponseEntity<String> modificar(@PathVariable("id") UUID id, @RequestBody DepartamentoDTO departamento) throws LilfacException {
			departamento.setId(id);
			departamentoFachada.modificarDepartamentoExistente(id, departamento);
			var mensajeExito = "El departamento " + departamento.getNombre() + " se ha modificado correctamente";
			return ResponseEntity.ok(mensajeExito);
		}
		
		@DeleteMapping("/{id}")
		public ResponseEntity<String> eliminar(@PathVariable("id") UUID id) throws LilfacException {
			departamentoFachada.darBajaDefinitivamenteDepartamentoExistente(id);
			var mensajeExito = "El departamento se ha eliminado correctamente";
			return new ResponseEntity<>(mensajeExito, HttpStatus.OK);
		}
}
