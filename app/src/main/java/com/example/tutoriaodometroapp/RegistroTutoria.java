package com.example.tutoriaodometroapp;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "registros_tutoria")
public class RegistroTutoria {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String estudiante;
    public String procesoFormacion;
    public double distanciaMetros;
    public double latitudInicio;
    public double longitudInicio;
    public double latitudFin;
    public double longitudFin;
    public float precisionMetros;
    public int tiempoActualizacionSeg;
    public long timestamp;

    public RegistroTutoria() {
    }

    @androidx.room.Ignore
    public RegistroTutoria(String estudiante, String procesoFormacion, double distanciaMetros,
                           double latitudInicio, double longitudInicio, double latitudFin,
                           double longitudFin, float precisionMetros, int tiempoActualizacionSeg,
                           long timestamp) {
        this.estudiante = estudiante;
        this.procesoFormacion = procesoFormacion;
        this.distanciaMetros = distanciaMetros;
        this.latitudInicio = latitudInicio;
        this.longitudInicio = longitudInicio;
        this.latitudFin = latitudFin;
        this.longitudFin = longitudFin;
        this.precisionMetros = precisionMetros;
        this.tiempoActualizacionSeg = tiempoActualizacionSeg;
        this.timestamp = timestamp;
    }
}
