package com.domoffon.efremov.domoffon;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class ShopArrayAdapter extends ArrayAdapter<Shop> {
    private static final String TAG = "FruitArrayAdapter";
	private List<Shop> fruitList = new ArrayList<Shop>();

    static class ShopViewHolder {
        ImageView fruitImg;
        TextView fruitName;
        TextView calories;
    }

    public ShopArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

	@Override
	public void add(Shop object) {
		fruitList.add(object);
		super.add(object);
	}

    @Override
	public int getCount() {
		return this.fruitList.size();
	}

    @Override
	public Shop getItem(int index) {
		return this.fruitList.get(index);
	}

    @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
        ShopViewHolder viewHolder;
		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.item_shop, parent, false);
            viewHolder = new ShopViewHolder();
            viewHolder.fruitImg = (ImageView) row.findViewById(R.id.fruitImg);
            viewHolder.fruitName = (TextView) row.findViewById(R.id.cost);
            viewHolder.calories = (TextView) row.findViewById(R.id.calories);
            row.setTag(viewHolder);
		} else {
            viewHolder = (ShopViewHolder)row.getTag();
        }
        Shop fruit = getItem(position);
        viewHolder.fruitImg.setImageResource(fruit.getShopImg());
        viewHolder.fruitName.setText(fruit.getShopName());
        viewHolder.calories.setText(fruit.getCalories());
		return row;
	}

    public Bitmap decodeToBitmap(byte[] decodedByte) {
		return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
	}
}
