package com.example.identifikasigulma;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class GulmaAdapter extends RecyclerView.Adapter<GulmaAdapter.ViewHolder> {
    private final List<Gulma> gulmaList;
    private final List<Gulma> gulmaListFull;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Gulma gulma);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public GulmaAdapter(List<Gulma> gulmaList) {
        this.gulmaList = gulmaList;
        gulmaListFull = new ArrayList<>(gulmaList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gulma, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Gulma currentGulma = gulmaList.get(position);
        holder.textViewNama.setText(currentGulma.getNama());
        holder.textViewNamaIlmiah.setText(currentGulma.getNamaIlmiah());
        holder.imageViewGulma.setImageResource(currentGulma.getImageResourceId());
    }

    @Override
    public int getItemCount() {
        return gulmaList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewNama;
        public TextView textViewNamaIlmiah;
        public ImageView imageViewGulma;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewNama = itemView.findViewById(R.id.text_view_nama);
            textViewNamaIlmiah = itemView.findViewById(R.id.text_view_nama_ilmiah);
            imageViewGulma = itemView.findViewById(R.id.image_view_gulma);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(gulmaList.get(position));
                        }
                    }
                }
            });
        }
    }

    public Filter getFilter() {
        return gulmaFilter;
    }

    private final Filter gulmaFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Gulma> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(gulmaListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Gulma item : gulmaListFull) {
                    if (item.getNama().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            gulmaList.clear();
            gulmaList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
