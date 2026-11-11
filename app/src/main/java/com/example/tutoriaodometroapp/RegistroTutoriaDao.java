package com.example.tutoriaodometroapp;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RegistroTutoriaDao {

    @Insert
    long insertar(RegistroTutoria registro);

    @Query("SELECT * FROM registros_tutoria ORDER BY timestamp DESC")
    List<RegistroTutoria> obtenerTodos();

    @Query("SELECT COALESCE(SUM(distanciaMetros), 0) FROM registros_tutoria")
    double obtenerDistanciaTotalMetros();

    @Query("SELECT COUNT(*) FROM registros_tutoria")
    int contarRegistros();

    @Query("DELETE FROM registros_tutoria")
    void borrarTodos();
}
