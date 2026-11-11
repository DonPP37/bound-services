package com.example.tutoriaodometroapp;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile RegistroTutoriaDao _registroTutoriaDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `registros_tutoria` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `estudiante` TEXT, `procesoFormacion` TEXT, `distanciaMetros` REAL NOT NULL, `latitudInicio` REAL NOT NULL, `longitudInicio` REAL NOT NULL, `latitudFin` REAL NOT NULL, `longitudFin` REAL NOT NULL, `precisionMetros` REAL NOT NULL, `tiempoActualizacionSeg` INTEGER NOT NULL, `timestamp` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '1cff1355ac85ff31df26bfa64e9ac22a')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `registros_tutoria`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsRegistrosTutoria = new HashMap<String, TableInfo.Column>(11);
        _columnsRegistrosTutoria.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRegistrosTutoria.put("estudiante", new TableInfo.Column("estudiante", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRegistrosTutoria.put("procesoFormacion", new TableInfo.Column("procesoFormacion", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRegistrosTutoria.put("distanciaMetros", new TableInfo.Column("distanciaMetros", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRegistrosTutoria.put("latitudInicio", new TableInfo.Column("latitudInicio", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRegistrosTutoria.put("longitudInicio", new TableInfo.Column("longitudInicio", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRegistrosTutoria.put("latitudFin", new TableInfo.Column("latitudFin", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRegistrosTutoria.put("longitudFin", new TableInfo.Column("longitudFin", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRegistrosTutoria.put("precisionMetros", new TableInfo.Column("precisionMetros", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRegistrosTutoria.put("tiempoActualizacionSeg", new TableInfo.Column("tiempoActualizacionSeg", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRegistrosTutoria.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRegistrosTutoria = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesRegistrosTutoria = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoRegistrosTutoria = new TableInfo("registros_tutoria", _columnsRegistrosTutoria, _foreignKeysRegistrosTutoria, _indicesRegistrosTutoria);
        final TableInfo _existingRegistrosTutoria = TableInfo.read(db, "registros_tutoria");
        if (!_infoRegistrosTutoria.equals(_existingRegistrosTutoria)) {
          return new RoomOpenHelper.ValidationResult(false, "registros_tutoria(com.example.tutoriaodometroapp.RegistroTutoria).\n"
                  + " Expected:\n" + _infoRegistrosTutoria + "\n"
                  + " Found:\n" + _existingRegistrosTutoria);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "1cff1355ac85ff31df26bfa64e9ac22a", "89966192caabfe400ee9b2bde61f700e");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "registros_tutoria");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `registros_tutoria`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(RegistroTutoriaDao.class, RegistroTutoriaDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public RegistroTutoriaDao registroTutoriaDao() {
    if (_registroTutoriaDao != null) {
      return _registroTutoriaDao;
    } else {
      synchronized(this) {
        if(_registroTutoriaDao == null) {
          _registroTutoriaDao = new RegistroTutoriaDao_Impl(this);
        }
        return _registroTutoriaDao;
      }
    }
  }
}
