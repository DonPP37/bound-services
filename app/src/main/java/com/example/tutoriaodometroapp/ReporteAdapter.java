package com.example.tutoriaodometroapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReporteAdapter extends RecyclerView.Adapter<ReporteAdapter.ReporteViewHolder> {

    private List<RegistroTutoria> lista = new ArrayList<>();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());

    public void setRegistros(List<RegistroTutoria> nuevosRegistros) {
        this.lista = nuevosRegistros != null ? nuevosRegistros : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReporteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_registro_tutoria, parent, false);
        return new ReporteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReporteViewHolder holder, int position) {
        RegistroTutoria r = lista.get(position);
        holder.tvTitulo.setText(r.procesoFormacion + " (" + r.estudiante + ")");
        holder.tvDistancia.setText(String.format(Locale.getDefault(), "%.2f metros", r.distanciaMetros));
        holder.tvFecha.setText(dateFormat.format(new Date(r.timestamp)));
        holder.tvDetalles.setText(String.format(Locale.getDefault(),
                "Precisión: %.1fm | Intervalo: %ds\nInicio: [%.5f, %.5f] -> Fin: [%.5f, %.5f]",
                r.precisionMetros, r.tiempoActualizacionSeg,
                r.latitudInicio, r.longitudInicio,
                r.latitudFin, r.longitudFin));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    static class ReporteViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvDistancia, tvFecha, tvDetalles;

        public ReporteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvItemTitulo);
            tvDistancia = itemView.findViewById(R.id.tvItemDistancia);
            tvFecha = itemView.findViewById(R.id.tvItemFecha);
            tvDetalles = itemView.findViewById(R.id.tvItemDetalles);
        }
    }
}
