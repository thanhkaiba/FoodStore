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

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> implements Filterable {

    private ArrayList<Order> infos;
    private ArrayList<Order> mFilteredList;
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

                    ArrayList<Order> filteredList = new ArrayList<>();
                    for (Order info : infos) {
                        if (info.getName().toLowerCase().contains(charString)) {
                            filteredList.add(info);
                        }  else if (info.getEmail().toLowerCase().contains(charString)) {
                            filteredList.add(info);
                        } else if (info.getAddress().toLowerCase().contains(charString)) {
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
                mFilteredList = (ArrayList<Order>) results.values;
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

    public OrderListAdapter(ArrayList<Order> infos) {
        this.infos = infos;
        this.mFilteredList = infos;

    }

    @Override
    public OrderListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cv;
        cv =  LayoutInflater.from(parent.getContext()).inflate(R.layout.image_card_order, parent, false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(OrderListAdapter.ViewHolder holder, final int position) {
        View view = holder.view;
        Order order = mFilteredList.get(position);
        TextView name = view.findViewById(R.id.info_name);
        TextView email = view.findViewById(R.id.info_email);
        TextView total =  view.findViewById(R.id.info_total);
        TextView date = view.findViewById(R.id.info_date);
        TextView address = view.findViewById(R.id.info_address);
        name.setText(order.getName());
        email.setText(order.getEmail());
        total.setText(String.valueOf(order.getTotal()));
        date.setText(order.getDate());
        address.setText(order.getAddress());

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
