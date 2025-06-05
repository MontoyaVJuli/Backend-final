package co.edu.co.lilfac.api;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.co.lilfac.businesslogic.facade.EmpresaFacade;
import co.edu.co.lilfac.businesslogic.facade.impl.EmpresaFacadeImpl;
import co.edu.co.lilfac.crosscutting.excepciones.LilfacException;
import co.edu.co.lilfac.dto.EmpresaDTO;
import co.edu.co.lilfac.init.LilfacApplication;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/empresas")


public class EmpresaController {
	
	 private final LilfacApplication lilfacApplication;
		
		private EmpresaFacade empresaFachada;
		
		public EmpresaController(LilfacApplication lilfacApplication) throws LilfacException {
			empresaFachada = new EmpresaFacadeImpl();
			this.lilfacApplication = lilfacApplication;
		}
		
		
		@GetMapping("/dummy")
		public EmpresaDTO getDummy() {
			return new EmpresaDTO();	
		}
		
		@GetMapping("/empresas")
		public ResponseEntity<List<EmpresaDTO> >Consultar() throws LilfacException {
			var lista = empresaFachada.consultarEmpresas();	
			return ResponseEntity.ok(lista);
		}
		
		@GetMapping("/{id}")
		public ResponseEntity<EmpresaDTO> Consultar(@PathVariable("id") UUID id) throws LilfacException {
			var empresa =  empresaFachada.consultarEmpresaPorId(id);
			return ResponseEntity.ok(empresa);
		}
		
		@PostMapping
		public ResponseEntity<String> crear(@RequestBody EmpresaDTO empresa) throws LilfacException {
			empresaFachada.registrarInformacionEmpresa(empresa);
			var mensajeExito = "La empresa " + empresa.getNombre() + " se ha registrado correctamente";
			return ResponseEntity.ok(mensajeExito);
		}
		
		@PutMapping("/{id}")
		public ResponseEntity<String> modificar(@PathVariable("id") UUID id, @RequestBody EmpresaDTO empresa) throws LilfacException {
			empresa.setId(id);
			empresaFachada.modificarEmpresaExistente(id, empresa);
			var mensajeExito = "La empresa " + empresa.getNombre() + " se ha modificado correctamente";
			return ResponseEntity.ok(mensajeExito);
		}
		

}
