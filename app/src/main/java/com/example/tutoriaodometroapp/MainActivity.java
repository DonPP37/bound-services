package com.example.tutoriaodometroapp;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 42;

    // Componentes del Bound Service
    private OdometerService odometerService;
    private boolean isBound = false;

    // Elementos de UI
    private TextView tvDistanciaOdometro, tvEstadoServicio, tvUbicacionActual, tvReporteConsolidado;
    private TextInputEditText etPrecisionMetros, etTiempoActualizacion, etNombreEstudiante, etProcesoFormacion;
    private Button btnActualizarConfiguracion, btnResetearOdometro, btnGuardarRegistro, btnLimpiarReportes;
    private RecyclerView recyclerReportes;
    private ReporteAdapter reporteAdapter;

    // Base de datos y Handler para actualizar UI periódicamente
    private AppDatabase db;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable runnableOdometro;

    private Location sessionStartLocation = null;

    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            OdometerService.OdometerBinder odometerBinder = (OdometerService.OdometerBinder) binder;
            odometerService = odometerBinder.getOdometer();
            isBound = true;
            tvEstadoServicio.setText("Servicio enlazado: Activo");
            tvEstadoServicio.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));

            // Sincronizar UI con la configuración del servicio
            if (odometerService != null) {
                etPrecisionMetros.setText(String.valueOf(odometerService.getMinDistance()));
                etTiempoActualizacion.setText(String.valueOf(odometerService.getMinTimeSeconds()));
                sessionStartLocation = odometerService.getStartLocation();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
            odometerService = null;
            tvEstadoServicio.setText("Servicio enlazado: Desconectado");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = AppDatabase.getInstance(this);

        initViews();
        setupRecyclerView();
        checkPermissionsAndStart();
        setupListeners();
        startPeriodicUIUpdate();
        cargarReporteConsolidado();
    }

    private void initViews() {
        tvDistanciaOdometro = findViewById(R.id.tvDistanciaOdometro);
        tvEstadoServicio = findViewById(R.id.tvEstadoServicio);
        tvUbicacionActual = findViewById(R.id.tvUbicacionActual);
        tvReporteConsolidado = findViewById(R.id.tvReporteConsolidado);

        etPrecisionMetros = findViewById(R.id.etPrecisionMetros);
        etTiempoActualizacion = findViewById(R.id.etTiempoActualizacion);
        etNombreEstudiante = findViewById(R.id.etNombreEstudiante);
        etProcesoFormacion = findViewById(R.id.etProcesoFormacion);

        btnActualizarConfiguracion = findViewById(R.id.btnActualizarConfiguracion);
        btnResetearOdometro = findViewById(R.id.btnResetearOdometro);
        btnGuardarRegistro = findViewById(R.id.btnGuardarRegistro);
        btnLimpiarReportes = findViewById(R.id.btnLimpiarReportes);
        recyclerReportes = findViewById(R.id.recyclerReportes);
    }

    private void setupRecyclerView() {
        reporteAdapter = new ReporteAdapter();
        recyclerReportes.setLayoutManager(new LinearLayoutManager(this));
        recyclerReportes.setAdapter(reporteAdapter);
    }

    private void checkPermissionsAndStart() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_REQUEST_CODE
            );
        }
    }

    private void setupListeners() {
        // Botón para actualizar en caliente las variables de configuración en el Bound Service
        btnActualizarConfiguracion.setOnClickListener(v -> {
            if (!isBound || odometerService == null) {
                Toast.makeText(this, "El servicio no está enlazado todavía", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                String precStr = etPrecisionMetros.getText() != null ? etPrecisionMetros.getText().toString().trim() : "1.0";
                String timeStr = etTiempoActualizacion.getText() != null ? etTiempoActualizacion.getText().toString().trim() : "2";

                float prec = Float.parseFloat(precStr);
                int seg = Integer.parseInt(timeStr);

                if (prec <= 0 || seg <= 0) {
                    Toast.makeText(this, "Los valores deben ser mayores a cero", Toast.LENGTH_SHORT).show();
                    return;
                }

                odometerService.aplicarConfiguracion(prec, seg);
                Toast.makeText(this, "Variables de configuración aplicadas al Bound Service", Toast.LENGTH_SHORT).show();
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Por favor ingrese valores numéricos válidos", Toast.LENGTH_SHORT).show();
            }
        });

        // Botón para reiniciar odómetro
        btnResetearOdometro.setOnClickListener(v -> {
            if (isBound && odometerService != null) {
                odometerService.resetDistance();
                sessionStartLocation = odometerService.getCurrentLocation();
                tvDistanciaOdometro.setText("0.00 m");
                Toast.makeText(this, "Odómetro reiniciado a 0 metros", Toast.LENGTH_SHORT).show();
            }
        });

        // Botón para guardar desplazamiento en Room y generar persistencia
        btnGuardarRegistro.setOnClickListener(v -> guardarDesplazamientoTutoria());

        // Botón para borrar reportes
        btnLimpiarReportes.setOnClickListener(v -> {
            Executors.newSingleThreadExecutor().execute(() -> {
                db.registroTutoriaDao().borrarTodos();
                runOnUiThread(() -> {
                    Toast.makeText(this, "Historial de desplazamientos limpiado", Toast.LENGTH_SHORT).show();
                    cargarReporteConsolidado();
                });
            });
        });
    }

    private void guardarDesplazamientoTutoria() {
        if (!isBound || odometerService == null) {
            Toast.makeText(this, "El servicio aún no se encuentra disponible", Toast.LENGTH_SHORT).show();
            return;
        }

        String estudiante = etNombreEstudiante.getText() != null ? etNombreEstudiante.getText().toString().trim() : "";
        String proceso = etProcesoFormacion.getText() != null ? etProcesoFormacion.getText().toString().trim() : "";

        if (estudiante.isEmpty() || proceso.isEmpty()) {
            Toast.makeText(this, "Complete el nombre del estudiante y proceso de tutoría", Toast.LENGTH_SHORT).show();
            return;
        }

        double distancia = odometerService.getDistance();
        Location currentLoc = odometerService.getCurrentLocation();
        Location startLoc = sessionStartLocation != null ? sessionStartLocation : odometerService.getStartLocation();

        double latIni = startLoc != null ? startLoc.getLatitude() : 0.0;
        double lonIni = startLoc != null ? startLoc.getLongitude() : 0.0;
        double latFin = currentLoc != null ? currentLoc.getLatitude() : latIni;
        double lonFin = currentLoc != null ? currentLoc.getLongitude() : lonIni;

        float precision = odometerService.getMinDistance();
        int tiempo = odometerService.getMinTimeSeconds();
        long ahora = System.currentTimeMillis();

        RegistroTutoria registro = new RegistroTutoria(
                estudiante,
                proceso,
                distancia,
                latIni,
                lonIni,
                latFin,
                lonFin,
                precision,
                tiempo,
                ahora
        );

        Executors.newSingleThreadExecutor().execute(() -> {
            db.registroTutoriaDao().insertar(registro);
            runOnUiThread(() -> {
                Toast.makeText(this, "¡Desplazamiento de tutoría guardado exitosamente!", Toast.LENGTH_LONG).show();
                odometerService.resetDistance();
                sessionStartLocation = odometerService.getCurrentLocation();
                cargarReporteConsolidado();
            });
        });
    }

    private void cargarReporteConsolidado() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<RegistroTutoria> lista = db.registroTutoriaDao().obtenerTodos();
            double totalMetros = db.registroTutoriaDao().obtenerDistanciaTotalMetros();
            int totalSesiones = db.registroTutoriaDao().contarRegistros();

            runOnUiThread(() -> {
                reporteAdapter.setRegistros(lista);
                tvReporteConsolidado.setText(String.format(
                        Locale.getDefault(),
                        "Total acumulado en procesos de formación: %.2f m (en %d registros)",
                        totalMetros, totalSesiones
                ));
            });
        });
    }

    private void startPeriodicUIUpdate() {
        runnableOdometro = new Runnable() {
            @Override
            public void run() {
                if (isBound && odometerService != null) {
                    double distance = odometerService.getDistance();
                    tvDistanciaOdometro.setText(String.format(Locale.getDefault(), "%.2f m", distance));

                    Location loc = odometerService.getCurrentLocation();
                    if (loc != null) {
                        tvUbicacionActual.setText(String.format(
                                Locale.getDefault(),
                                "Latitud: %.6f\nLongitud: %.6f\nAltitud: %.1fm | Proveedor: %s",
                                loc.getLatitude(), loc.getLongitude(), loc.getAltitude(), loc.getProvider()
                        ));
                    } else {
                        tvUbicacionActual.setText("Esperando señal de localización (GPS/Red)...");
                    }
                }
                handler.postDelayed(this, 1000); // Actualiza la UI cada segundo
            }
        };
        handler.post(runnableOdometro);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Enlazar al Bound Service
        Intent intent = new Intent(this, OdometerService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Desvincular el Bound Service al pausar la actividad
        if (isBound) {
            unbindService(connection);
            isBound = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (runnableOdometro != null) {
            handler.removeCallbacks(runnableOdometro);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (isBound && odometerService != null) {
                    odometerService.registrarActualizaciones();
                }
            } else {
                Toast.makeText(this, "Se requiere permiso de localización para calcular la distancia", Toast.LENGTH_LONG).show();
            }
        }
    }
}
