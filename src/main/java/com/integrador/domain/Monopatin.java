package com.integrador.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.time.Duration;

import com.integrador.service.dto.monopatin.MonopatinRequestDto;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Transient;
@Data
@NoArgsConstructor
public class Monopatin implements Serializable {


    private GPS ubicacion;

    private String estado;

    private boolean disponible;
 
    private double kmsRecorridos;
   
    private double kmsMantenimiento;

    private double cantKmParaMant = 100;

   
    private Long tiempoUsoTotal; //en segundos
    
    private Long tiempoPausado; //en segundos
    
   
    private Long cantidadViajes;


	public Monopatin() {
	
	}
	
	public Monopatin(Monopatin request) {
        this.ubicacion = request.getUbicacion();
        this.estado = request.getEstado();
        this.disponible = request.isDisponible();
        this.kmsRecorridos = request.getKmsRecorridos();
        this.kmsMantenimiento = request.getKmsMantenimiento();
        this.tiempoUsoTotal = request.getTiempoUsoTotal();
        this.tiempoPausado = request.getTiempoPausado();
        this.cantidadViajes = request.getCantidadViajes();
    }
	
	public Monopatin(MonopatinRequestDto request) {
        this.ubicacion = request.getUbicacion();
        this.estado = request.getEstado();
        this.disponible = request.isDisponible();
        this.kmsRecorridos = request.getKmsRecorridos();
        this.kmsMantenimiento = request.getkmsMantenimiento();
        this.tiempoUsoTotal = request.getTiempoUsoTotal();
        this.tiempoPausado = request.getTiempoPausado();
        this.cantidadViajes = request.getCantidadViajes();
    }
   
	public Monopatin(GPS ubicacion, String estado, boolean disponible, double kmsRecorridos,
			double kmsMantenimiento, Long tiempoUsoTotal, Long tiempoPausado,
			Long cantidadViajes) {
		super();
	
		this.ubicacion = ubicacion;
		this.estado = estado;
		this.disponible = disponible;
		this.kmsRecorridos = kmsRecorridos;
		this.kmsMantenimiento = kmsMantenimiento;
		this.cantKmParaMant = cantKmParaMant;
		this.tiempoUsoTotal = tiempoUsoTotal;
		this.tiempoPausado = tiempoPausado;
		this.cantidadViajes = cantidadViajes;
	}



	public boolean estaEnMantenimiento() {
		return (!disponible && this.estado.equalsIgnoreCase("en mantenimiento"));
	}
    
    public boolean necesitaMantenimiento() {
    	return this.cantKmParaMant <= this.kmsMantenimiento;
    }

	public double getKmsMantenimiento() {
		return kmsMantenimiento;
	}



	public double getCantKmParaMant() {
		return cantKmParaMant;
	}



	public void setKmsMantenimiento(double kmsMant) {
		this.kmsMantenimiento = kmsMant;
	}





	 public boolean isDisponible() {
			return disponible;
		}



	public void setDisponible(boolean disponible) {
		this.disponible = disponible;
	}




	public void setUbicacion(GPS ubicacion) {
		this.ubicacion = ubicacion;
	}



	public void setEstado(String estado) {
		this.estado = estado;
	}



	public void setKmsRecorridos(double kmsRecorridos) {
		this.kmsRecorridos = kmsRecorridos;
	}



	public void setTiempoUsoTotal(Long tiempoUsoTotal) {
		this.tiempoUsoTotal = tiempoUsoTotal;
	}



	public void setTiempoPausado(Long tiempoPausado) {
		this.tiempoPausado = tiempoPausado;
	}



	public void setCantidadViajes(Long cantidadViajes) {
		this.cantidadViajes = cantidadViajes;
	}



	
	
	public Long getTiempoPausado() {
		return tiempoPausado;
	}

	public Long getCantidadViajes() {
		return cantidadViajes;
	}

	public GPS getUbicacion() {
		return ubicacion;
	}

	public String getEstado() {
		return estado;
	}

	public double getKmsRecorridos() {
		return kmsRecorridos;
	}

	public Long getTiempoUsoTotal() {
		return tiempoUsoTotal;
	}


	@Override
	public String toString() {
		return "Monopatin [ubicacion=" + ubicacion + ", estado=" + estado + ", disponible=" + disponible
				+ ", kmsRecorridos=" + kmsRecorridos + ", kmsMant=" + kmsMantenimiento + ", cantKmParaMant=" + cantKmParaMant
				+ ", tiempoUsoTotal=" + tiempoUsoTotal + ", tiempoPausado=" + tiempoPausado + ", cantidadViajes="
				+ cantidadViajes + "]";
	}
//
//    public Monopatin(Gps ubicacion, String estado, double kmsTotales, Long tiempoUsoTotales, Long tiempoPausado, Long cantidadViajes) {
//        this.ubicacion = ubicacion;
//        this.estado = estado;
//        this.kmsTotales = kmsTotales;
//        this.tiempoUsoTotales = tiempoUsoTotales;
//        this.tiempoPausado = tiempoPausado;
//        this.cantidadViajes = cantidadViajes;
//    }
}
