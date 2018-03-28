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

class ListAdapterStory extends ArrayAdapter<Story> {
    private List<Story> StoryList = new ArrayList<Story>();

    static class StoryViewHolder {
        ImageView StoryImg;
        TextView StoryName;
        TextView calories;
    }

    public ListAdapterStory(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    @Override
    public void add(Story object) {
        StoryList.add(object);
        super.add(object);
    }

    @Override
    public int getCount() {
        return this.StoryList.size();
    }

    @Override
    public Story getItem(int index) {
        return this.StoryList.get(index);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        StoryViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            row = inflater.inflate(R.layout.item_story, parent, false);
            viewHolder = new StoryViewHolder();
            viewHolder.StoryImg = row.findViewById(R.id.fruitImg);
            viewHolder.StoryName = row.findViewById(R.id.cost);
            viewHolder.calories = row.findViewById(R.id.name);
            row.setTag(viewHolder);
        } else {
            viewHolder = (StoryViewHolder)row.getTag();
        }
        Story Story = getItem(position);
        assert Story != null;
        viewHolder.StoryImg.setImageResource(Story.getStoryImg());
        viewHolder.StoryName.setText(Story.getStoryName());
        viewHolder.calories.setText(Story.getCalories());
        return row;
    }

    public Bitmap decodeToBitmap(byte[] decodedByte) {
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
