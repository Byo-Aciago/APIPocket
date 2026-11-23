package com.example.apipocket.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apipocket.R;
import com.example.apipocket.model.earthquake.EarthquakeFeature;
import com.example.apipocket.model.earthquake.EarthquakeProperties;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EarthquakeAdapter extends RecyclerView.Adapter<EarthquakeAdapter.EarthquakeViewHolder> {

    private final List<EarthquakeFeature> earthquakeList;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault());

    public EarthquakeAdapter(List<EarthquakeFeature> earthquakeList) {
        this.earthquakeList = earthquakeList;
    }

    @NonNull
    @Override
    public EarthquakeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_earthquake, parent, false);
        return new EarthquakeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EarthquakeViewHolder holder, int position) {
        EarthquakeFeature feature = earthquakeList.get(position);
        if (feature != null && feature.getProperties() != null) {
            EarthquakeProperties props = feature.getProperties();
            double mag = props.getMagnitude();
            holder.tvMagnitude.setText(String.format(Locale.getDefault(), "%.1f", mag));

            int bgResId;
            if (mag < 5.0) {
                bgResId = R.drawable.bg_magnitude_blue;
            } else if (mag < 7.0) {
                bgResId = R.drawable.bg_magnitude_amber;
            } else {
                bgResId = R.drawable.bg_magnitude_red;
            }
            holder.tvMagnitude.setBackgroundResource(bgResId);

            holder.tvPlace.setText(props.getPlace() != null ? props.getPlace() : "Ubicación desconocida");
            holder.tvTime.setText(dateFormat.format(new Date(props.getTime())));
        }
    }

    @Override
    public int getItemCount() {
        return earthquakeList != null ? earthquakeList.size() : 0;
    }

    public static class EarthquakeViewHolder extends RecyclerView.ViewHolder {
        TextView tvMagnitude;
        TextView tvPlace;
        TextView tvTime;

        public EarthquakeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMagnitude = itemView.findViewById(R.id.tvMagnitude);
            tvPlace = itemView.findViewById(R.id.tvPlace);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }
}
