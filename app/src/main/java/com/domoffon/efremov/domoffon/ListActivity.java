package com.domoffon.efremov.domoffon;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private ListView listView;
    TextView mTitle;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    intent = new Intent(ListActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    return true;
                case R.id.navigation_dashboard:
                    intent = new Intent(ListActivity.this, PaymentsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    return true;
                case R.id.navigation_notifications:
                    intent = new Intent(ListActivity.this, StoryActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    return true;
                case R.id.navigation_user:
                    intent = new Intent(ListActivity.this, ProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Intent intent = getIntent();
        listView = (ListView) findViewById(R.id.listView);
        ShopArrayAdapter shopArrayAdapter = new ShopArrayAdapter(getApplicationContext(), R.layout.item_shop);
        listView.setAdapter(shopArrayAdapter);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.removeShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        long code;
        long defaultValue = 0;
        code = intent.getLongExtra("code", defaultValue);
        List<String[]> fruitList = readData(code);
        mTitle = (TextView) findViewById(R.id.login_title);
        if (code == 0) {
            mTitle.setText("Ключи");
        } else if (code == 1) {
            mTitle.setText("Дубликаторы");
        } else if (code == 2) {
            mTitle.setText("Замки");
        }
        for (String[] fruitData : fruitList) {
            String fruitName = fruitData[1];
            String calories = fruitData[2];
            int fruitImgResId = getResources().getIdentifier("no_photo", "mipmap", "com.domoffon.efremov.domoffon");
            //try{
            //    fruitImgResId = getResources().getIdentifier(fruitImg, "drawable", "com.domoffon.efremov.domoffon");
            //}
            //catch (Exception e){}

            Shop fruit = new Shop(fruitImgResId, fruitName, calories);
            shopArrayAdapter.add(fruit);

        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedSweet = listView.getItemAtPosition(i).toString();


                TextView textView = listView.findViewById(R.id.cost);
                String text = textView.getText().toString();

                TextView textView1 = (TextView) listView.findViewById(R.id.calories);
                String text1 = textView1.getText().toString();

                Intent intent = new Intent(ListActivity.this, ItemActivity.class);
                intent.putExtra("name", text);
                intent.putExtra("price", text1);
                startActivity(intent);
            }
        });

    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    public List<String[]> readData(long code) {
        List<String[]> resultList = new ArrayList<>();

        if (code == 0) {
            String[] fruit7 = new String[3];
            fruit7[0] = "key1";
            fruit7[1] = "Key1";
            fruit7[2] = "60 руб.";
            resultList.add(fruit7);

            String[] fruit1 = new String[3];
            fruit1[0] = "key2";
            fruit1[1] = "Key2";
            fruit1[2] = "50 руб.";
            resultList.add(fruit1);

            String[] fruit23 = new String[3];
            fruit1[0] = "key3";
            fruit1[1] = "Key3";
            fruit1[2] = "65 руб.";
            resultList.add(fruit23);
            String[] key1 = new String[3];
            fruit1[0] = "RW 1990";
            fruit1[1] = "RW 1990";
            fruit1[2] = "15 руб.";
            resultList.add(key1);

            String[] key2 = new String[3];
            fruit1[0] = "Proxy T5H";
            fruit1[1] = "Proxy T5H";
            fruit1[2] = "15 руб.";
            resultList.add(key2);

            String[] key3 = new String[3];
            fruit1[0] = "RW-15";
            fruit1[1] = "RW-15";
            fruit1[2] = "41 руб.";
            resultList.add(key3);

            resultList.add(fruit1);
            resultList.add(fruit1);
            resultList.add(fruit1);
            resultList.add(fruit1);
            resultList.add(fruit1);
            resultList.add(fruit1);
            resultList.add(fruit1);
            resultList.add(fruit1);
            resultList.add(fruit1);
            resultList.add(fruit1);
            resultList.add(fruit1);
            resultList.add(fruit1);
            resultList.add(fruit1);
            resultList.add(fruit1);
            resultList.add(fruit1);
            resultList.add(fruit1);
            resultList.add(fruit1);
            resultList.add(fruit1);
            resultList.add(fruit1);
            resultList.add(fruit1);
            resultList.add(fruit1);
            resultList.add(fruit1);
            resultList.add(fruit1);
            resultList.add(fruit1);
            resultList.add(fruit1);
            resultList.add(fruit1);
            resultList.add(fruit1);
            resultList.add(fruit1);
            resultList.add(fruit1);

            return resultList;
        }
        if (code == 1) {
            String[] fruit3 = new String[3];
            fruit3[0] = "banana";
            fruit3[1] = "Lock1";
            fruit3[2] = "89 Calories";
            resultList.add(fruit3);

            String[] fruit4 = new String[3];
            fruit4[0] = "apple";
            fruit4[1] = "Lock2";
            fruit4[2] = "52 Calories";
            resultList.add(fruit4);

            String[] fruit10 = new String[3];
            fruit10[0] = "kiwi";
            fruit10[1] = "Lock3";
            fruit10[2] = "61 Calories";
            resultList.add(fruit10);
            return resultList;
        }
        if (code == 2) {
            String[] fruit5 = new String[3];
            fruit5[0] = "pear";
            fruit5[1] = "Pear";
            fruit5[2] = "57 Calories";
            resultList.add(fruit5);


            String[] fruit2 = new String[3];
            fruit2[0] = "strawberry";
            fruit2[1] = "Strawberry";
            fruit2[2] = "33 Calories";
            resultList.add(fruit2);

            String[] fruit6 = new String[3];
            fruit6[0] = "lemon";
            fruit6[1] = "Lemon";
            fruit6[2] = "29 Calories";
            resultList.add(fruit6);

            String[] fruit8 = new String[3];
            fruit8[0] = "peach";
            fruit8[1] = "Peach";
            fruit8[2] = "39 Calories";
            resultList.add(fruit8);

            String[] fruit9 = new String[3];
            fruit9[0] = "apricot";
            fruit9[1] = "Apricot";
            fruit9[2] = "48 Calories";
            resultList.add(fruit9);

            String[] fruit11 = new String[3];
            fruit11[0] = "mango";
            fruit11[1] = "Mango";
            fruit11[2] = "60 Calories";
            resultList.add(fruit11);
            return resultList;
        }

        return null;
    }
}
