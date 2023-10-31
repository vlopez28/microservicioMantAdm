package com.integrador.controller;


import lombok.RequiredArgsConstructor;
import lombok.var;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.integrador.domain.Monopatin;
import com.integrador.service.*;
import com.integrador.service.dto.administrador.AdministradorRequestDto;
import com.integrador.service.dto.administrador.AdministradorResponseDto;
import com.integrador.service.dto.monopatin.MonopatinRequestDto;
import com.integrador.service.dto.parada.ParadaRequestDto;
import com.integrador.domain.Administrador;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("api/administrador")
public class AdministradorController {
	
	@Autowired
    private AdministradorService administradorService;

	
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(administradorService.findById(id));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error. Administrador inexistente");
        }

    }
    @GetMapping("")
    public List<AdministradorResponseDto> findAll(){
        return this.administradorService.findAll();
    }
    
    @PostMapping("")
    public ResponseEntity<?> save (@RequestBody @Validated AdministradorRequestDto request) {
	    try {
	    	return ResponseEntity.status(HttpStatus.OK).body(administradorService.save(request));
	    }catch(Exception e) {
	    	return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Ocurrio un error, revise los campos ingresados");
	    } 
	}   
    
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Validated AdministradorRequestDto request) {
        try {
            Administrador adm = administradorService.update(id, request);
            AdministradorResponseDto response = new AdministradorResponseDto(adm);

            return ResponseEntity.status(HttpStatus.OK).body(response);
          
        } catch (Exception e) {
        	return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró el administrador con el ID proporcionado.");
        }
    }
    

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        try{
            this.administradorService.delete(id);
            return ResponseEntity.status(HttpStatus.OK).body("Se elimino correctamente administrador con el id: " + id);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error. No se pudo eliminar el administrador con id: " + id);
        }
    }
    
    //agregar monopatin
    @PostMapping("/agregarMonopatin")
    public ResponseEntity<?> agregarMonopatin (@RequestBody @Validated Monopatin request) {
	    try {
	    	return ResponseEntity.status(HttpStatus.OK).body(administradorService.agregarMonopatin(request));
	    }catch(Exception e) {
	    	return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Ocurrio un error, revise los campos ingresados");
	    } 
	}  
    
    @DeleteMapping("/eliminarMonopatin/{idMonopatin}")
    public ResponseEntity<?> quitarMonopatin(@PathVariable Long idMonopatin){
        try{
            this.administradorService.quitarMonopatin(idMonopatin);
            return ResponseEntity.status(HttpStatus.OK).body("Se elimino correctamente monopatin con el id: " + idMonopatin);
        }catch (Exception e){
        	e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error. No se pudo eliminar el monopatin con id: " + idMonopatin);
        }
    }
    
    
    //trae los monopatines con mas de tantos km
    @GetMapping("/monopatines/porKm/{cantKm}")
    public ResponseEntity<?> getMonopatinesPorKm(@PathVariable Long cantKm){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(administradorService.getMonopatinesPorKm(cantKm));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error. No se encontraron monopatines");
        }

    }
    
  //trae los monopatines con mas de tanto tiempo, sin contar pausas
    @GetMapping("/monopatines/porTiempoSinPausa/{cantKm}")
    public ResponseEntity<?> getMonopatinesPorTiempoSinPausa(@PathVariable Long cantKm){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(administradorService.getMonopatinesPorTiempoSinPausa(cantKm));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error. No se encontraron monopatines");
        }

    }
    
  //trae los monopatines con mas de tanto tiempo, sin contar pausas
    @GetMapping("/monopatines/porTiempoConPausa/{cantKm}")
    public ResponseEntity<?> getMonopatinesPorTiempoConPausa(@PathVariable Long cantKm){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(administradorService.getMonopatinesPorTiempoConPausa(cantKm));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error. No se encontraron monopatines");
        }

    }
    
    //crear un parada
    @PostMapping("/paradas/agregarParada")
    public ResponseEntity<?> agregarParada (@RequestBody @Validated ParadaRequestDto request) {
	    try {
	    	return ResponseEntity.status(HttpStatus.OK).body(administradorService.agregarParada(request));
	    }catch(Exception e) {
	    	return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Ocurrio un error, revise los campos ingresados");
	    } 
	}  
    
    
    //quitar una parada
    @DeleteMapping("/paradas/quitarParada/{idParada}")
    public ResponseEntity<?> eliminarParada (@PathVariable Long idParada){
        try{
            this.administradorService.eliminarParada(idParada);
            return ResponseEntity.status(HttpStatus.OK).body("Se elimino correctamente parada con el id: " + idParada);
        }catch (Exception e){
        	e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error. Ya se elimino la parada con id: " + idParada);
        }
    }
    
    //anular cuenta
    @PutMapping("/cuentas/anularCuenta/{idCuenta}")
    public ResponseEntity<?> anularCuenta(@PathVariable Long idCuenta) {
        try {
        	System.out.println("hola controller");
            return ResponseEntity.status(HttpStatus.OK).body(administradorService.anularCuenta(idCuenta));
          
        } catch (Exception e) {
        	return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró la cuenta con el ID proporcionado.");
        }
    }
    
    //consultar los monopatines con más de X viajes en un cierto año
    @GetMapping("/monopatines/conViajes/{cantViajes}/{anio}")
    public ResponseEntity<?> getMonopatinesConViajes(@PathVariable Long cantViajes, @PathVariable Integer anio){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(administradorService.getMonopatinesConViajes(cantViajes, anio));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error. No se encontraron monopatines");
        }

    }
}
