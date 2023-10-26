package com.integrador.service;

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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;



@Service
public class MantenimientoService {

	@Autowired
    private MantenimientoRepository mantenimientoRepository;
//	@Autowired
//    private  RestTemplate restTemplate;
//	@Autowired
//    private AppConfig restTemplateConfig;
//
    @Transactional
    public MantenimientoResponseDto save(MantenimientoRequestDto request){
    	System.out.println(request.getIdMonopatin());
        Mantenimiento mantenimiento= new Mantenimiento(request);
        System.out.println("hola"+mantenimiento);
        Mantenimiento result = this.mantenimientoRepository.save(mantenimiento);
        return new MantenimientoResponseDto(result);
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
