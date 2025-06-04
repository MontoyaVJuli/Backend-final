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

import co.edu.co.lilfac.businesslogic.facade.ClienteFacade;
import co.edu.co.lilfac.businesslogic.facade.impl.ClienteFacadeImpl;
import co.edu.co.lilfac.crosscutting.excepciones.LilfacException;
import co.edu.co.lilfac.dto.ClienteDTO;
import co.edu.co.lilfac.init.LilfacApplication;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/clientes")
public class ClienteController {

    private final LilfacApplication lilfacApplication;
	
	private ClienteFacade clienteFachada;
	
	public ClienteController(LilfacApplication lilfacApplication) throws LilfacException {
		clienteFachada = new ClienteFacadeImpl();
		this.lilfacApplication = lilfacApplication;
	}
	
	
	@GetMapping("/dummy")
	public ClienteDTO getDummy() {
		return new ClienteDTO();	
	}
	
	@GetMapping
	public ResponseEntity<List<ClienteDTO> >Consultar() throws LilfacException {
		var lista = clienteFachada.consultarClientes(getDummy());	
		return new ResponseEntity<>(lista, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ClienteDTO> Consultar(@PathVariable("id") UUID id) throws LilfacException {
		var cliente =  clienteFachada.consultarClientePorId(id);
		return new ResponseEntity<>(cliente, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<String> crear(@RequestBody ClienteDTO cliente) throws LilfacException {
		clienteFachada.registrarNuevoCliente(cliente);
		var mensajeExito = "El cliente " + cliente.getNombre() + " se ha registrado correctamente";
		return new ResponseEntity<>(mensajeExito, HttpStatus.OK);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<String> modificar(@PathVariable("id") UUID id, @RequestBody ClienteDTO cliente) throws LilfacException {
		cliente.setId(id);
		clienteFachada.modificarClienteExistente(id, cliente);
		var mensajeExito = "El cliente " + cliente.getNombre() + " se ha modificado correctamente";
		return new ResponseEntity<>(mensajeExito, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> eliminar(@PathVariable("id") UUID id) throws LilfacException {
		clienteFachada.darBajaDefinitivamenteClienteExistente(id);
		var mensajeExito = "El cliente se ha eliminado correctamente";
		return new ResponseEntity<>(mensajeExito, HttpStatus.OK);
	}

}
