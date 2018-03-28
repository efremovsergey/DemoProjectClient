package com.domoffon.efremov.domoffon;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PaymentsActivity extends AppCompatActivity {

    TextView mTitle;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    intent = new Intent(PaymentsActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    return true;
                case R.id.navigation_dashboard:
                    return true;
                case R.id.navigation_notifications:
                    intent = new Intent(PaymentsActivity.this, StoryActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    return true;
                case R.id.navigation_user:
                    intent = new Intent(PaymentsActivity.this, ProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    return true;
            }
            return false;
        }

    };

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PaymentsActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);

        mTitle = (TextView) findViewById(R.id.login_title);
        mTitle.setText(R.string.app_name);

        Button info = (Button) findViewById(R.id.i);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PaymentsActivity.PopupScreen popupScreen = new PaymentsActivity.PopupScreen();
                popupScreen.show(getFragmentManager(), ItemActivity.PopupScreen.TAG);
            }
        });

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.removeShiftMode(navigation);
        navigation.setSelectedItemId(R.id.navigation_dashboard);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        ListView listView = (ListView) findViewById(R.id.shop_listview);
        ListAdapterPayment fruitArrayAdapter = new ListAdapterPayment(getApplicationContext(), R.layout.item_payment);
        listView.setAdapter(fruitArrayAdapter);

        List<String[]> fruitList = readData();

        /*for(String[] fruitData:fruitList ) {
            String fruitImg = fruitData[0];
            String fruitName = fruitData[1];
            String calories = fruitData[2];
            int fruitImgResId = getResources().getIdentifier(fruitImg, "drawable", "com.domoffon.efremov.domoffon");

            Payment fruit = new Payment(fruitImgResId,fruitName,calories);
            fruitArrayAdapter.add(fruit);

        }*/
    }

    public List<String[]> readData(){
        List<String[]> resultList = new ArrayList<>();

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
        resultList.add(fruit1);resultList.add(fruit1);resultList.add(fruit1);resultList.add(fruit1);resultList.add(fruit1);resultList.add(fruit1);resultList.add(fruit1);resultList.add(fruit1);resultList.add(fruit1);resultList.add(fruit1);resultList.add(fruit1);resultList.add(fruit1);resultList.add(fruit1);resultList.add(fruit1);resultList.add(fruit1);resultList.add(fruit1);resultList.add(fruit1);resultList.add(fruit1);resultList.add(fruit1);resultList.add(fruit1);resultList.add(fruit1);resultList.add(fruit1);resultList.add(fruit1);resultList.add(fruit1);resultList.add(fruit1);resultList.add(fruit1);resultList.add(fruit1);resultList.add(fruit1);resultList.add(fruit1);resultList.add(fruit1);resultList.add(fruit1);

        return resultList;
    }

    @SuppressLint("ValidFragment")
    public class PopupScreen extends DialogFragment {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.alert_payment_info, null);
            builder.setView(view);

            String button1String = "ОК";



            builder.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });
            builder.setCancelable(true);

            return builder.create();
        }
    }
}
