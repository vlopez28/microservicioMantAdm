package com.integrador.service;

import com.integrador.configuration.*;
import com.integrador.domain.Administrador;
import com.integrador.domain.Monopatin;
import com.integrador.service.exception.NotFoundException;
import com.integrador.repository.AdministradorRepository;
import com.integrador.service.dto.administrador.AdministradorRequestDto;
import com.integrador.service.dto.administrador.AdministradorResponseDto;
import com.integrador.service.dto.monopatin.MonopatinRequestDto;
import com.integrador.service.dto.monopatin.MonopatinResponseDto;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
    public ResponseEntity agregarMonopatin(Monopatin m) {
    	HttpHeaders headers = new HttpHeaders();
    	//MonopatinRequestDto monopatin = new MonopatinRequestDto();
    	
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


}
