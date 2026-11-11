package com.example.tutoriaodometroapp;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {RegistroTutoria.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCIA;

    public abstract RegistroTutoriaDao registroTutoriaDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCIA == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCIA == null) {
                    INSTANCIA = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "odometro_tutorias.db"
                    ).fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCIA;
    }
}
