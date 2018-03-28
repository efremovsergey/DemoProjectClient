package com.domoffon.efremov.domoffon;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    TextView mTitle;
    ListView lw;

    private HashMap<String, String> mInfo = new HashMap<String, String>();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    intent = new Intent(ProfileActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    return true;
                case R.id.navigation_dashboard:
                    intent = new Intent(ProfileActivity.this, PaymentsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    return true;
                case R.id.navigation_notifications:
                    intent = new Intent(ProfileActivity.this, StoryActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    return true;
                case R.id.navigation_user:
                    return true;
            }
            return false;
        }

    };

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mTitle = (TextView) findViewById(R.id.login_title);
        mTitle.setText(R.string.app_name);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.removeShiftMode(navigation);
        navigation.setSelectedItemId(R.id.navigation_user);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        lw = (ListView) findViewById(R.id.lw);

        ListAdapterProfile fruitArrayAdapter = new ListAdapterProfile(getApplicationContext(), R.layout.item_profile);
        lw.setAdapter(fruitArrayAdapter);


        lw.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        List<String[]> fruitList = readData();

        for(String[] fruitData:fruitList ) {
            String fruitImg = fruitData[0];
            String fruitName = fruitData[1];

            Profile fruit = new Profile(fruitImg,fruitName);
            fruitArrayAdapter.add(fruit);

        }
    }

    public List<String[]> readData(){
        List<String[]> resultList = new ArrayList<>();

        String[] fruit7 = new String[2];
        fruit7[0] = "Имя";
        fruit7[1] = "Имя";
        resultList.add(fruit7);

        String[] fruit1 = new String[2];
        fruit1[0] = "Пароль";
        fruit1[1] = "Пароль";
        resultList.add(fruit1);

        String[] fruit23 = new String[2];
        fruit23[0] = "Город";
        fruit23[1] = "Й-О";
        resultList.add(fruit23);

        String[] fruit233 = new String[2];
        fruit233[0] = "Адрес склада";
        fruit233[1] = "Й-О";
        resultList.add(fruit233);

        return resultList;
    }
}
