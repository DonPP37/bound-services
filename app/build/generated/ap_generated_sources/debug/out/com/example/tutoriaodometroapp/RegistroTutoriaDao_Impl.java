package com.example.tutoriaodometroapp;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class RegistroTutoriaDao_Impl implements RegistroTutoriaDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<RegistroTutoria> __insertionAdapterOfRegistroTutoria;

  private final SharedSQLiteStatement __preparedStmtOfBorrarTodos;

  public RegistroTutoriaDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRegistroTutoria = new EntityInsertionAdapter<RegistroTutoria>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `registros_tutoria` (`id`,`estudiante`,`procesoFormacion`,`distanciaMetros`,`latitudInicio`,`longitudInicio`,`latitudFin`,`longitudFin`,`precisionMetros`,`tiempoActualizacionSeg`,`timestamp`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final RegistroTutoria entity) {
        statement.bindLong(1, entity.id);
        if (entity.estudiante == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.estudiante);
        }
        if (entity.procesoFormacion == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.procesoFormacion);
        }
        statement.bindDouble(4, entity.distanciaMetros);
        statement.bindDouble(5, entity.latitudInicio);
        statement.bindDouble(6, entity.longitudInicio);
        statement.bindDouble(7, entity.latitudFin);
        statement.bindDouble(8, entity.longitudFin);
        statement.bindDouble(9, entity.precisionMetros);
        statement.bindLong(10, entity.tiempoActualizacionSeg);
        statement.bindLong(11, entity.timestamp);
      }
    };
    this.__preparedStmtOfBorrarTodos = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM registros_tutoria";
        return _query;
      }
    };
  }

  @Override
  public long insertar(final RegistroTutoria registro) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfRegistroTutoria.insertAndReturnId(registro);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void borrarTodos() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfBorrarTodos.acquire();
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfBorrarTodos.release(_stmt);
    }
  }

  @Override
  public List<RegistroTutoria> obtenerTodos() {
    final String _sql = "SELECT * FROM registros_tutoria ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfEstudiante = CursorUtil.getColumnIndexOrThrow(_cursor, "estudiante");
      final int _cursorIndexOfProcesoFormacion = CursorUtil.getColumnIndexOrThrow(_cursor, "procesoFormacion");
      final int _cursorIndexOfDistanciaMetros = CursorUtil.getColumnIndexOrThrow(_cursor, "distanciaMetros");
      final int _cursorIndexOfLatitudInicio = CursorUtil.getColumnIndexOrThrow(_cursor, "latitudInicio");
      final int _cursorIndexOfLongitudInicio = CursorUtil.getColumnIndexOrThrow(_cursor, "longitudInicio");
      final int _cursorIndexOfLatitudFin = CursorUtil.getColumnIndexOrThrow(_cursor, "latitudFin");
      final int _cursorIndexOfLongitudFin = CursorUtil.getColumnIndexOrThrow(_cursor, "longitudFin");
      final int _cursorIndexOfPrecisionMetros = CursorUtil.getColumnIndexOrThrow(_cursor, "precisionMetros");
      final int _cursorIndexOfTiempoActualizacionSeg = CursorUtil.getColumnIndexOrThrow(_cursor, "tiempoActualizacionSeg");
      final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
      final List<RegistroTutoria> _result = new ArrayList<RegistroTutoria>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final RegistroTutoria _item;
        _item = new RegistroTutoria();
        _item.id = _cursor.getInt(_cursorIndexOfId);
        if (_cursor.isNull(_cursorIndexOfEstudiante)) {
          _item.estudiante = null;
        } else {
          _item.estudiante = _cursor.getString(_cursorIndexOfEstudiante);
        }
        if (_cursor.isNull(_cursorIndexOfProcesoFormacion)) {
          _item.procesoFormacion = null;
        } else {
          _item.procesoFormacion = _cursor.getString(_cursorIndexOfProcesoFormacion);
        }
        _item.distanciaMetros = _cursor.getDouble(_cursorIndexOfDistanciaMetros);
        _item.latitudInicio = _cursor.getDouble(_cursorIndexOfLatitudInicio);
        _item.longitudInicio = _cursor.getDouble(_cursorIndexOfLongitudInicio);
        _item.latitudFin = _cursor.getDouble(_cursorIndexOfLatitudFin);
        _item.longitudFin = _cursor.getDouble(_cursorIndexOfLongitudFin);
        _item.precisionMetros = _cursor.getFloat(_cursorIndexOfPrecisionMetros);
        _item.tiempoActualizacionSeg = _cursor.getInt(_cursorIndexOfTiempoActualizacionSeg);
        _item.timestamp = _cursor.getLong(_cursorIndexOfTimestamp);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public double obtenerDistanciaTotalMetros() {
    final String _sql = "SELECT COALESCE(SUM(distanciaMetros), 0) FROM registros_tutoria";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final double _result;
      if (_cursor.moveToFirst()) {
        _result = _cursor.getDouble(0);
      } else {
        _result = 0.0;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public int contarRegistros() {
    final String _sql = "SELECT COUNT(*) FROM registros_tutoria";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _result;
      if (_cursor.moveToFirst()) {
        _result = _cursor.getInt(0);
      } else {
        _result = 0;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
