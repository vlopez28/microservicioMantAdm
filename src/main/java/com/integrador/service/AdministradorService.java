package com.integrador.service;

import com.integrador.configuration.*;
import com.integrador.domain.Administrador;
import com.integrador.domain.Cuenta;
import com.integrador.domain.Monopatin;
import com.integrador.domain.Parada;
import com.integrador.service.exception.NotFoundException;
import com.integrador.repository.AdministradorRepository;
import com.integrador.service.dto.administrador.AdministradorRequestDto;
import com.integrador.service.dto.administrador.AdministradorResponseDto;
import com.integrador.service.dto.monopatin.MonopatinRequestDto;
import com.integrador.service.dto.monopatin.MonopatinResponseDto;
import com.integrador.service.dto.monopatinConViajes.MonopatinConViajesResponseDto;
import com.integrador.service.dto.parada.ParadaRequestDto;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;

@Service
public class AdministradorService {

	@Autowired
    private AdministradorRepository administradorRepository;
	@Autowired
    private  RestTemplate restTemplate;
	
    @Transactional
    public AdministradorResponseDto save (AdministradorRequestDto request){
        Administrador administrador= new Administrador(request);
        Administrador result = this.administradorRepository.save(administrador);
        return new AdministradorResponseDto(result);
    }

    @Transactional
    public List<AdministradorResponseDto> findAll(){
        return this.administradorRepository.findAll().stream().map(AdministradorResponseDto::new).toList();
    }

    @Transactional
    public AdministradorResponseDto findById(Long id){
        return this.administradorRepository.findById(id)
                .map(AdministradorResponseDto::new)
        .orElseThrow(()->new NotFoundException("Administrador",id));
    }
    
    @Transactional
    public Administrador update(Long id, AdministradorRequestDto request) {
        Administrador adm = this.administradorRepository.findById(id).orElseThrow(
                () -> new NotFoundException("ID de administrador inválido: ", id));
        
        adm.setNombreYApellido(request.getNombreYApellido());
        adm.setRol(request.getRol());
        
        return this.administradorRepository.save(adm);
    }
    
    @Transactional
    public void delete(Long id) {
    	this.administradorRepository.delete(this.administradorRepository.findById(id).orElseThrow(
                () -> new NotFoundException("ID de administrador inválido: ", id)));
    }
    
    @Transactional
    public ResponseEntity getMonopatinesPorKm(Long cantKm) {
    	HttpHeaders headers = new HttpHeaders();
    	HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
    	ResponseEntity<List<Monopatin>> response = restTemplate.exchange(
				"http://localhost:8003/api/monopatines/porKm/" + cantKm,
				HttpMethod.GET,
				requestEntity,
				new ParameterizedTypeReference<List<Monopatin>>() {}
				);	
    	headers.setContentType(MediaType.APPLICATION_JSON);
    	return response;
    }
    
    @Transactional
    public ResponseEntity getMonopatinesPorTiempoSinPausa(Long cantKm) {
    	HttpHeaders headers = new HttpHeaders();
    	HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
    	ResponseEntity<List<Monopatin>> response = restTemplate.exchange(
				"http://localhost:8003/api/monopatines/porTiempoSinPausa/" + cantKm,
				HttpMethod.GET,
				requestEntity,
				new ParameterizedTypeReference<List<Monopatin>>() {}
				);	
    	headers.setContentType(MediaType.APPLICATION_JSON);
    	return response;
    }
    
    @Transactional
    public ResponseEntity getMonopatinesPorTiempoConPausa(Long cantKm) {
    	HttpHeaders headers = new HttpHeaders();
    	HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
    	ResponseEntity<List<Monopatin>> response = restTemplate.exchange(
				"http://localhost:8003/api/monopatines/porTiempoConPausa/" + cantKm,
				HttpMethod.GET,
				requestEntity,
				new ParameterizedTypeReference<List<Monopatin>>() {}
				);	
    	headers.setContentType(MediaType.APPLICATION_JSON);
    	return response;
    }
    
    @Transactional
    public ResponseEntity agregarMonopatin(Monopatin m) {
    	HttpHeaders headers = new HttpHeaders();
    	Monopatin monopatin = new Monopatin (m);
    	HttpEntity<Monopatin> requestEntity = new HttpEntity<>(monopatin, headers);
		System.out.println(requestEntity);
        ResponseEntity<String> response = restTemplate.exchange(
				"http://localhost:8003/api/monopatines",
				HttpMethod.POST,
				requestEntity,
				String.class
				);	

        if(response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(HttpStatus.OK).body("Monopatin creado con éxito");

        }
        
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No agrego el monopatin");

    }
    
    
    @Transactional
    public ResponseEntity quitarMonopatin(Long idMonopatin) {
    	HttpHeaders headers = new HttpHeaders();
    	HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
		
        ResponseEntity<String> response = restTemplate.exchange(
				"http://localhost:8003/api/monopatines/" + idMonopatin,
				HttpMethod.DELETE,
				requestEntity,
				String.class
				);		
        System.out.println(response);
        
        if(response != null) {
            return ResponseEntity.status(HttpStatus.OK).body("Monopatin eliminado con éxito");

        }
        
    	return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontro el monopatin");
    	
    }

    @Transactional
    public ResponseEntity getMonopatinesConViajes(Long cantViajes, Integer anio) {
    	System.out.println("hola service"+cantViajes+ anio);

    	HttpHeaders headers = new HttpHeaders();
    	HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
    	ResponseEntity<List<Monopatin>> response = restTemplate.exchange(
				"http://localhost:8003/api/monopatines/conViajes/" + cantViajes + "/" + anio,
				HttpMethod.GET,
				requestEntity,
				new ParameterizedTypeReference<List<Monopatin>>() {}
				);	
    	System.out.println(response);

    	headers.setContentType(MediaType.APPLICATION_JSON);
    	return response;
    }
    
    //agregar parada
    @Transactional
    public ResponseEntity agregarParada(ParadaRequestDto p) {
        HttpHeaders headers = new HttpHeaders();

        Parada parada = new Parada(p);
        HttpEntity<Parada> requestEntity = new HttpEntity<>(parada, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:8003/api/paradas",
                HttpMethod.POST,
                requestEntity,
                String.class
        );
        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(HttpStatus.OK).body("Parada registrada con exito");

        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("no se pudo registrar con exito");
    }
   

    @Transactional
    public ResponseEntity eliminarParada(Long idParada) {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:8003/api/paradas/" + idParada,
                HttpMethod.DELETE,
                requestEntity,
                String.class
        );
        if(response != null) {
            return ResponseEntity.status(HttpStatus.OK).body("Parada eliminada con éxito");

        } else {
        	return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La parada ya fue eliminada");
        }
        
        
    	//return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontro la parada");
    }

    //anular una cuenta
    public ResponseEntity anularCuenta(Long cuentaId) {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<Cuenta> response = restTemplate.exchange(
                "http://localhost:8080/api/cuentas/" + cuentaId,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<Cuenta>() {
                }
        );
        System.out.println(response);
        if (response.getStatusCode().is2xxSuccessful()) {
        	
            Cuenta c = response.getBody();
            
            if(c.isDisponible() == false) {
            	return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("La cuenta ya fue anulada");
            }
            c.setDisponible(false);
            HttpEntity<Cuenta> requestEntity2 = new HttpEntity<>(c, headers);
            ResponseEntity<Cuenta> response2 = restTemplate.exchange(
                    "http://localhost:8080/api/cuentas/" + cuentaId,
                    HttpMethod.PUT,
                    requestEntity2,

                    new ParameterizedTypeReference<Cuenta>() {
                    }
            );
            return response2;
        } else {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error, no se encontro cuenta registrada");
        }

    }


}
