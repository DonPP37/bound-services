package com.example.tutoriaodometroapp;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

public class OdometerService extends Service {

    private static final String TAG = "OdometerService";

    private final IBinder binder = new OdometerBinder();

    private LocationListener listener;
    private LocationManager locManager;

    private static double distanceInMeters = 0.0;
    private static Location lastLocation = null;
    private static Location currentLocation = null;
    private static Location startLocation = null;

    // Variables de configuración configurables desde la UI
    private float minDistance = 1.0f; // Precisión del movimiento en metros
    private long minTimeMs = 2000L;    // Tiempo de actualización en milisegundos (2 segundos por defecto)

    public class OdometerBinder extends Binder {
        public OdometerService getOdometer() {
            return OdometerService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        iniciarLocationListener();
    }

    private void iniciarLocationListener() {
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                if (startLocation == null) {
                    startLocation = new Location(location);
                }

                if (lastLocation == null) {
                    lastLocation = new Location(location);
                } else {
                    double step = location.distanceTo(lastLocation);
                    distanceInMeters += step;
                    lastLocation = new Location(location);
                }
                currentLocation = new Location(location);
                Log.d(TAG, "Ubicación actualizada: " + location.getLatitude() + ", " + location.getLongitude() + " - Distancia acumulada: " + distanceInMeters + " m");
            }

            @Override
            public void onProviderDisabled(@NonNull String arg0) {
            }

            @Override
            public void onProviderEnabled(@NonNull String arg0) {
            }

            @Override
            public void onStatusChanged(String arg0, int arg1, Bundle bundle) {
            }
        };

        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        registrarActualizaciones();
    }

    public void registrarActualizaciones() {
        if (locManager == null || listener == null) return;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            try {
                locManager.removeUpdates(listener);

                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                String provider = locManager.getBestProvider(criteria, true);

                if (provider != null) {
                    locManager.requestLocationUpdates(provider, minTimeMs, minDistance, listener);
                } else if (locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTimeMs, minDistance, listener);
                } else if (locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTimeMs, minDistance, listener);
                }

                // Obtener última ubicación conocida si existe
                Location lastKnown = null;
                if (provider != null) {
                    lastKnown = locManager.getLastKnownLocation(provider);
                }
                if (lastKnown == null && locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    lastKnown = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }
                if (lastKnown == null && locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    lastKnown = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }

                if (lastKnown != null && currentLocation == null) {
                    currentLocation = new Location(lastKnown);
                    if (startLocation == null) {
                        startLocation = new Location(lastKnown);
                    }
                    if (lastLocation == null) {
                        lastLocation = new Location(lastKnown);
                    }
                }
            } catch (SecurityException ex) {
                Log.e(TAG, "Permiso denegado al solicitar updates", ex);
            }
        }
    }

    /**
     * Actualiza la configuración en caliente solicitada por el usuario en la interfaz:
     * - Precisión del movimiento en metros (minDistance)
     * - Tiempo de actualización en segundos (minTimeSeconds)
     */
    public void aplicarConfiguracion(float precisionMetros, int tiempoSegundos) {
        this.minDistance = Math.max(0.1f, precisionMetros);
        this.minTimeMs = Math.max(500L, tiempoSegundos * 1000L);
        registrarActualizaciones();
    }

    public double getDistance() {
        return distanceInMeters;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public Location getStartLocation() {
        return startLocation;
    }

    public float getMinDistance() {
        return minDistance;
    }

    public int getMinTimeSeconds() {
        return (int) (minTimeMs / 1000);
    }

    public void resetDistance() {
        distanceInMeters = 0.0;
        lastLocation = currentLocation != null ? new Location(currentLocation) : null;
        startLocation = currentLocation != null ? new Location(currentLocation) : null;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locManager != null && listener != null) {
            try {
                locManager.removeUpdates(listener);
            } catch (SecurityException ex) {
                Log.e(TAG, "Error removiendo location updates", ex);
            }
        }
    }
}
