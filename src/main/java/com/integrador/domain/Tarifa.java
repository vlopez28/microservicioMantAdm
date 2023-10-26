package com.integrador.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity

public class Tarifa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int IDTarifa;

    @Column
    private double tarifaEstandar;

    @Column
    private double tarifaEspecial;

    @Column
    private Date fechaEntradaVigencia;



}
