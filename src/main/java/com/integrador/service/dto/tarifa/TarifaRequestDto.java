package com.integrador.service.dto.tarifa;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import lombok.Data;

@Data
@JsonIgnoreProperties( ignoreUnknown = true )
public class TarifaRequestDto {
	
	private Long id;

    private double tarifaEstandar;

    private double tarifaEspecial;

    private Date fechaEntradaVigencia;

	public Long getId() {
		return id;
	}

	public double getTarifaEstandar() {
		return tarifaEstandar;
	}

	public double getTarifaEspecial() {
		return tarifaEspecial;
	}

	public Date getFechaEntradaVigencia() {
		return fechaEntradaVigencia;
	}
    
    

}
