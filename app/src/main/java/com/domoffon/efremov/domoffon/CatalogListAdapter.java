package com.domoffon.efremov.domoffon;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

public class CatalogListAdapter extends BaseAdapter
    {
        private Context mContext;
        private List<Catalog> mProductList;

        // статический ViewHolder класс внутри адаптера
        public static final class ViewHolder {
            View v;
            TextView txName;
            TextView txDes;
            TextView txCount;
            TextView txPrice;
        }

    public CatalogListAdapter(Context mContext, List<Catalog> mProductList) {
        this.mContext = mContext;
        this.mProductList = mProductList;
    }

    public void clear() {
        mProductList.clear();
    }

    @Override
    public int getCount() {
        return mProductList.size();
    }

    @Override
    public Object getItem(int position) {
        return mProductList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        holder.v = View.inflate(mContext, R.layout.item_catalog, null);
        holder.txName = (TextView)holder.v.findViewById(R.id.catalog_name);

        holder.txName.setText(mProductList.get(position).getName());

        holder.v.setTag(mProductList.get(position).getId());

        return holder.v;
    }
}