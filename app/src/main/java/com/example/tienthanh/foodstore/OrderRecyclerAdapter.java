package com.example.tienthanh.foodstore;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class OrderRecyclerAdapter extends RecyclerView.Adapter<OrderRecyclerAdapter.MyViewHolder> {

    Context context;


    private ArrayList<OrderDetail> detailList;


    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView item,amount,price;

        public MyViewHolder(View view) {
            super(view);

            item=(TextView) view.findViewById(R.id.item);
            amount=(TextView) view.findViewById(R.id.amount);
            price=(TextView) view.findViewById(R.id.price);

        }

    }


    public OrderRecyclerAdapter(Context activityContacts, ArrayList<OrderDetail> detailList) {
        this.detailList = detailList;
        this.context = activityContacts;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler, parent, false);


        return new MyViewHolder(itemView);


    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        OrderDetail orderDetail = detailList.get(position);

        holder.item.setText(orderDetail.getFoodName());
        holder.amount.setText(String.valueOf(orderDetail.getAmount()));
        holder.price.setText(String.valueOf(orderDetail.getCost()));

    }

    @Override
    public int getItemCount() {
        return detailList.size();
    }

}
