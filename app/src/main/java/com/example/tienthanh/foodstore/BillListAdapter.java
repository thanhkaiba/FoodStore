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

public class BillListAdapter extends RecyclerView.Adapter<BillListAdapter.ViewHolder> implements Filterable {

    private ArrayList<Bill> infos;
    private ArrayList<Bill> mFilteredList;
    Listener listener;


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                String charString = constraint.toString().toLowerCase();

                if (constraint.toString().isEmpty()) {
                    mFilteredList = infos;
                } else {

                    ArrayList<Bill> filteredList = new ArrayList<>();
                    for (Bill info : infos) {
                        if (info.getUserName().toLowerCase().contains(charString)) {
                            filteredList.add(info);
                        } else if (String.valueOf(info.getTotal()).contains(charString)) {
                            filteredList.add(info);
                        } else if (info.getDate().toString().contains(charString)) {
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
                mFilteredList = (ArrayList<Bill>) results.values;
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

        private View view;

        public ViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    public BillListAdapter(ArrayList<Bill> infos) {
        this.infos = infos;
        this.mFilteredList = infos;

    }

    @Override
    public BillListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cv;
        cv =  LayoutInflater.from(parent.getContext()).inflate(R.layout.image_card_order, parent, false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(BillListAdapter.ViewHolder holder, final int position) {
        View view = holder.view;
        Bill bill = mFilteredList.get(position);
        TextView name = view.findViewById(R.id.info_name);
        TextView email = view.findViewById(R.id.info_email);
        TextView total =  view.findViewById(R.id.info_total);
        TextView date = view.findViewById(R.id.info_date);
        TextView address = view.findViewById(R.id.info_address);
        name.setText("ID: " + String.valueOf(bill.getId()));
        email.setText(bill.getUserName());
        total.setText(String.valueOf(bill.getTotal()));
        date.setText(bill.getDate());
        StringBuilder text = new StringBuilder();
        if (bill.getType() == Bill.SELL) {
            text.append("Salesman / Saleswoman ID: ");
        } else {
            text.append("Storekeeper ID: ");
        }
        text.append(bill.getUserID());
        address.setText(text);

        view.setOnClickListener(new View.OnClickListener() {
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
