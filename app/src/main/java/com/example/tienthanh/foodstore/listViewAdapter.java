package com.example.tienthanh.foodstore;

import java.util.ArrayList;
import java.util.HashMap;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class listViewAdapter extends BaseAdapter
{


    public ArrayList<OrderDetail> list;
    Activity activity;

    public listViewAdapter(Activity activity, ArrayList<OrderDetail> list) {
        super();
        this.activity = activity;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder {
        TextView txtFirst;
        TextView txtSecond;
        TextView txtThird;
        TextView txtFourth;

        public ViewHolder(TextView txtFirst, TextView txtSecond, TextView txtThird, TextView txtFourth) {
            this.txtFirst = txtFirst;
            this.txtSecond = txtSecond;
            this.txtThird = txtThird;
            this.txtFourth = txtFourth;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater =  activity.getLayoutInflater();

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.listview_row, null);
            holder = new ViewHolder((TextView)convertView.findViewById(R.id.FirstText),
                    (TextView)convertView.findViewById(R.id.SecondText),
                    (TextView)convertView.findViewById(R.id.ThirdText),
                    (TextView)convertView.findViewById(R.id.FourthText));

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        OrderDetail orderDetail = list.get(position);
        holder.txtFirst.setText(String.valueOf(position + 1));
        holder.txtSecond.setText(orderDetail.getFoodName());
        holder.txtThird.setText(String.valueOf(orderDetail.getAmount()));
        holder.txtFourth.setText(String.valueOf(orderDetail.getCost()));

        return convertView;
    }

}