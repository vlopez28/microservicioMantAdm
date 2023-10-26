package com.integrador.service;

import com.integrador.configuration.*;
import com.integrador.domain.Administrador;
import com.integrador.domain.Monopatin;
import com.integrador.service.exception.NotFoundException;
import com.integrador.repository.AdministradorRepository;
import com.integrador.service.dto.administrador.AdministradorRequestDto;
import com.integrador.service.dto.administrador.AdministradorResponseDto;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdministradorService {

	@Autowired
    private AdministradorRepository administradorRepository;
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



}
