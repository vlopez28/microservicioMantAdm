package com.integrador.service;

import org.springframework.http.HttpHeaders;
import com.integrador.configuration.*;
import com.integrador.domain.Mantenimiento;
import com.integrador.domain.Monopatin;
import com.integrador.repository.MantenimientoRepository;
import com.integrador.service.dto.*;
import com.integrador.service.dto.mantenimiento.MantenimientoRequestDto;
import com.integrador.service.dto.mantenimiento.MantenimientoResponseDto;

import lombok.Data;
import lombok.NoArgsConstructor;
import com.integrador.service.exception.*;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;



@Service
public class MantenimientoService {

	@Autowired
    private MantenimientoRepository mantenimientoRepository;
	@Autowired
    private  RestTemplate restTemplate;
	
	 @Transactional
	    public MantenimientoResponseDto save(MantenimientoRequestDto request){
	    	Mantenimiento mantenimiento= new Mantenimiento(request);
	        Mantenimiento result = this.mantenimientoRepository.save(mantenimiento);
	        return new MantenimientoResponseDto(result);
	    }

	 private void agregarMantenimiento(Long idMonopatin) {
		//seteo el nuevo mant, con ese monopatin validado
		Mantenimiento mant = new Mantenimiento();
	    mant.setMonopatinId(idMonopatin);
	    mant.setFechaHoraInicioMantenimiento(new Date());
	    mant.setFechaHoraFinalizacionMantenimiento(null);
	    mant.setReparado(false);
	    
	    this.mantenimientoRepository.save(mant);
	 }
	 
    @Transactional
    public ResponseEntity agregarMonopatinAMantenimiento(Long idMonopatin){
    	//pido por rest el monopatin con ese id   	
    	HttpHeaders headers = new HttpHeaders();
        HttpEntity<	Void> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<Monopatin> response = restTemplate.exchange(
				"http://localhost:8003/api/monopatines/" + idMonopatin,
				HttpMethod.GET,
				requestEntity,
				
				new ParameterizedTypeReference<Monopatin>() {}
				);		System.out.println("hola3");
		//si la respuest esta ok
	
		if(response.getStatusCode().is2xxSuccessful()) {
			//creo un monopatin

			Monopatin m = response.getBody();
			//chequeo que si necesitaMantenimiento
			System.out.println("mon" + m);
			if(m.getEstado().equalsIgnoreCase("en mantenimiento")) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El monopatin ya esta en mantenimiento");
			}
			if(m.necesitaMantenimiento()) {
		    	System.out.println("necesita mant");

				//lo pongo no disponible y el estado en mantenimiento
				m.setDisponible(false);
				m.setEstado("en mantenimiento");
				HttpEntity<Monopatin> requestEntity2 = new HttpEntity<>(m,headers);
		        System.out.println("hola2");
				ResponseEntity<Monopatin> response2 = restTemplate.exchange(
						"http://localhost:8003/api/monopatines/" + idMonopatin,
						HttpMethod.PUT,
						requestEntity2,
						
						new ParameterizedTypeReference<Monopatin>() {}
				);
				return response2;
			}else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El monopatin no necesita mantenimiento");
			} 
			
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró el monopatin para setear a mantenimiento");
    }

    @Transactional
    public List<MantenimientoResponseDto> findAll(){
        return this.mantenimientoRepository.findAll().stream().map(MantenimientoResponseDto::new).toList();
    }

    @Transactional
    public MantenimientoResponseDto findById(Long id){
        return this.mantenimientoRepository.findById(id)
                .map(MantenimientoResponseDto::new)
                .orElseThrow(()->new NotFoundException("Mantenimiento", id));
    }
    
    @Transactional
    public void delete(Long id) {
    	this.mantenimientoRepository.delete(this.mantenimientoRepository.findById(id).orElseThrow(
                () -> new NotFoundException("ID de mantenimiento invalido:",id)));
    }

    @Transactional
    public Mantenimiento update(Long id, MantenimientoRequestDto request) {
        Mantenimiento mant = this.mantenimientoRepository.findById(id).orElseThrow(
                () -> new NotFoundException("ID de monopatin inválido: ", id));
        
        
        mant.setFechaHoraFinalizacionMantenimiento(request.getFechaHoraFinalizacionMantenimiento());
        mant.setFechaHoraInicioMantenimiento(request.getFechaHoraInicioMantenimiento());
        mant.setMonopatinId(request.getIdMonopatin());
        mant.setReparado(request.isReparado());
        return this.mantenimientoRepository.save(mant);
    }

//
//
//    public ResponseEntity setMonopatinMantenimiento(Integer monopatinId)
//    HttpHeaders headers = new HttpHeaders();
//    HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
//

    //varificar si necesita mantenimiento
    //realizar mantenimiento
}
