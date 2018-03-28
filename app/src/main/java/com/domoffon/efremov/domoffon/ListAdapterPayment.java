package com.domoffon.efremov.domoffon;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1 on 10.12.2017.
 */

class ListAdapterPayment extends ArrayAdapter<Payment> {
    private List<Payment> PaymentList = new ArrayList<Payment>();

    static class PaymentViewHolder {
        ImageView PaymentImg;
        TextView PaymentName;
        TextView calories;
    }

    public ListAdapterPayment(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    @Override
    public void add(Payment object) {
        PaymentList.add(object);
        super.add(object);
    }

    @Override
    public int getCount() {
        return this.PaymentList.size();
    }

    @Override
    public Payment getItem(int index) {
        return this.PaymentList.get(index);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        PaymentViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            row = inflater.inflate(R.layout.item_payment, parent, false);
            viewHolder = new PaymentViewHolder();
            viewHolder.PaymentImg = row.findViewById(R.id.fruitImg);
            viewHolder.PaymentName = row.findViewById(R.id.cost);
            viewHolder.calories = row.findViewById(R.id.name);
            row.setTag(viewHolder);
        } else {
            viewHolder = (PaymentViewHolder)row.getTag();
        }
        Payment Payment = getItem(position);
        assert Payment != null;
        viewHolder.PaymentImg.setImageResource(Payment.getPaymentImg());
        viewHolder.PaymentName.setText(Payment.getPaymentName());
        viewHolder.calories.setText(Payment.getCalories());
        return row;
    }

}
