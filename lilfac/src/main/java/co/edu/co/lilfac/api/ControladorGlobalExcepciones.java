package co.edu.co.lilfac.api;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import co.edu.co.lilfac.crosscutting.excepciones.LilfacException;

@ControllerAdvice
public class ControladorGlobalExcepciones {

    @ExceptionHandler(LilfacException.class)
    public ResponseEntity<Map<String, String>> controlarLilfacException(LilfacException excepcion) {
        excepcion.printStackTrace();
        return new ResponseEntity<>(
            Map.of("mensaje", excepcion.getMensajeUsuario()),
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> controlarException(Exception excepcion) {
        excepcion.printStackTrace();
        var mensajeError = "Se ha presentado un problema INESPERADO tratando de llevar a cabo la operación deseada";
        return ResponseEntity.internalServerError().body(
            Map.of("mensaje", mensajeError)
        );
    }
}
