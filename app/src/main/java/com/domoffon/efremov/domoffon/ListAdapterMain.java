package com.domoffon.efremov.domoffon;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by 1 on 06.12.2017.
 */

public class ListAdapterMain  extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] names;

    ListAdapterMain(Activity context, String[] names) {
        super(context, R.layout.item_catalog, names);
        this.context = context;
        this.names = names;
    }

    // Класс для сохранения во внешний класс и для ограничения доступа
    // из потомков класса
    static class ViewHolder {
        ImageView imageView;
        TextView textView;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // ViewHolder буферизирует оценку различных полей шаблона элемента

        ViewHolder holder;
        // Очищает сущетсвующий шаблон, если параметр задан
        // Работает только если базовый шаблон для всех классов один и тот же
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.item_catalog, null, true);
            holder = new ViewHolder();
            holder.textView = (TextView) rowView.findViewById(R.id.catalog_name);
            holder.imageView = (ImageView) rowView.findViewById(R.id.catalog_img);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        holder.textView.setText(names[position]);
        // Изменение иконки для Windows и iPhone
        String s = names[position];
        if (s.startsWith("Ключи")) {
            holder.imageView.setImageResource(R.mipmap.key);
        } else if (s.startsWith("Дубликаторы")) {
            holder.imageView.setImageResource(R.mipmap.tools);
        }
        else{
            holder.imageView.setImageResource(R.mipmap.lock);
        }

        return rowView;
    }
}