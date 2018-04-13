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


public class CartRecycleAdapter extends RecyclerView.Adapter<CartRecycleAdapter.MyViewHolder> {

    Context context;



    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView name,qty,cost,type;
        ImageView image, minus, plus;


        public MyViewHolder(View view) {
            super(view);

            image = view.findViewById(R.id.info_image);
            name= view.findViewById(R.id.info_name);
            qty = view.findViewById(R.id.info_qty);
            cost = view.findViewById(R.id.info_cost);
            minus = view.findViewById(R.id.minus);
            plus = view.findViewById(R.id.plus);
            type = view.findViewById(R.id.info_type);

        }

    }


    public CartRecycleAdapter(Context activityContacts) {
        this.context = activityContacts;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_detail_list, parent, false);
        return new MyViewHolder(itemView);


    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final OrderDetail orderDetail = MainActivity.cart.get(position);
        final Food food = MainActivity.foodCart.get(position);

        holder.name.setText(orderDetail.getFoodName());
        holder.qty.setText(String.valueOf(orderDetail.getAmount()));
        holder.cost.setText(String.valueOf(orderDetail.getCost()));
        holder.type.setText(String.valueOf(food.getType()));
        holder.image.setImageBitmap(FoodStoreDatabaseHelper.loadImageFromStorage(food.getImg(), 70, 70));

        final int[] number = {orderDetail.getAmount()};
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (number[0] == 0) {
                    holder.qty.setText("" + number[0]);
                }

                if (number[0] > 0) {

                    number[0] = number[0] - 1;
                    holder.qty.setText("" + number[0]);
                }
                orderDetail.setAmount(number[0]);
            }
        });
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (number[0] == food.getAmount()) {
                    holder.qty.setText("" + number[0]);
                }

                if (number[0] < food.getAmount()) {

                    number[0] = number[0] + 1;
                    holder.qty.setText("" + number[0]);
                }
                orderDetail.setAmount(number[0]);
            }
        });
    }

    @Override
    public int getItemCount() {
        return MainActivity.cart.size();
    }

}
