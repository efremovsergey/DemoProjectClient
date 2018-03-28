package com.domoffon.efremov.domoffon;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

class ListAdapterProfile extends ArrayAdapter<Profile> {
    private List<Profile> ProfileList = new ArrayList<Profile>();

    static class ProfileViewHolder {
        TextView ProfileName;
        AutoCompleteTextView calories;
    }

    ListAdapterProfile(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    @Override
    public void add(Profile object) {
        ProfileList.add(object);
        super.add(object);
    }

    @Override
    public int getCount() {
        return this.ProfileList.size();
    }

    @Override
    public Profile getItem(int index) {
        return this.ProfileList.get(index);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        ProfileViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            row = inflater.inflate(R.layout.item_profile, parent, false);
            viewHolder = new ProfileViewHolder();
            viewHolder.ProfileName = row.findViewById(R.id.text1);
            viewHolder.calories = row.findViewById(R.id.text2);
            row.setTag(viewHolder);
        } else {
            viewHolder = (ProfileViewHolder)row.getTag();
        }
        Profile Profile = getItem(position);
        assert Profile != null;
        viewHolder.ProfileName.setText(Profile.getProfileName());
        viewHolder.calories.setHint(Profile.getCalories());
        return row;
    }
}
