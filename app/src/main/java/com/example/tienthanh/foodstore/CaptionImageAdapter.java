package com.example.tienthanh.foodstore;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CaptionImageAdapter extends RecyclerView.Adapter<CaptionImageAdapter.ViewHolder> implements Filterable {

    private ArrayList<Info> infos;
    private ArrayList<Info> mFilteredList;

    Listener listener;

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                String charString = constraint.toString();

                if (constraint.toString().isEmpty()) {
                    mFilteredList = infos;
                } else {

                    ArrayList<Info> filteredList = new ArrayList<>();
                    for (Info info : infos) {
                        if (info.getName().toLowerCase().contains(charString) || info.getDescription().toLowerCase().contains(charString)) {
                            filteredList.add(info);
                        }
                    }
                    mFilteredList = filteredList;

                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;

            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mFilteredList = (ArrayList<Info>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    interface Listener {
        void onClick(int id);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;

        public ViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }

    public CaptionImageAdapter(ArrayList<Info> infos) {
        this.infos = infos;
        this.mFilteredList = infos;

    }

    @Override
    public CaptionImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.image_card, parent, false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(CaptionImageAdapter.ViewHolder holder, final int position) {
        CardView cardView = holder.cardView;
        ImageView imageView = (ImageView) cardView.findViewById(R.id.info_image);
        imageView.setImageBitmap(FoodStoreDatabaseHelper.loadImageFromStorage(mFilteredList.get(position).getImage()));
        imageView.setContentDescription(mFilteredList.get(position).getName());
        TextView name = (TextView) cardView.findViewById(R.id.info_name);
        name.setText(mFilteredList.get(position).getName());
        TextView description = (TextView) cardView.findViewById(R.id.info_description);
        description.setText(mFilteredList.get(position).getDescription());
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(position);
            }
        });
    }
    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }
}
