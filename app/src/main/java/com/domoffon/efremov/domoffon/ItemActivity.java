package com.domoffon.efremov.domoffon;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;

public class ItemActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {
    private EditText mCounter;
    TextView mTitle;
    SharedPreferences sPref;
    TextView tw;
    private int price_ = 0;

    final String SAVED_TEXT = "saved_text";

    public static final String PRICE_NAME = "Цена 15 руб./шт.";
    public static final String COUNT_NAME = "Всего 15 шт.";
    public static final String COST_NAME = "Стоимость - ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        final SeekBar seekBar = (SeekBar)findViewById(R.id.seekBarCost);
        seekBar.setOnSeekBarChangeListener(this);

        mCounter = (EditText) findViewById(R.id.editTextCost);
        mCounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCounter.setText("");
            }

        });
        mCounter.setOnKeyListener(new View.OnKeyListener()
                                  {
                                      @Override
                                      public boolean onKey(View view, int i, KeyEvent keyEvent) {
                                          if (!mCounter.getText().toString().equals(""))
                                              seekBar.setProgress(Integer.parseInt(mCounter.getText().toString()));
                                          return false;
                                      }
                                  }
        );

        Button mPay = (Button) findViewById(R.id.button_pay);
        mPay.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        PopupScreen popupScreen = new PopupScreen();
                                        popupScreen.show(getFragmentManager(), PopupScreen.TAG);
                                    }
                                }
        );
        //mName = (TextView) findViewById(R.id.itemName);

        Intent intent = getIntent();

        String name = intent.getStringExtra("name");
        String price = intent.getStringExtra("price");
        price = price.replace(" руб.", "");
        price_ = Integer.parseInt(price.replace(" руб.", ""));
        //int foo = Integer.parseInt(price.replace(" руб.", ""));
        //seekBar.setMax(foo);
        mTitle = (TextView) findViewById(R.id.login_title);
        mTitle.setText(name);
        //mName.setText(name);
        int fruitImgResId = getResources().getIdentifier(name.toLowerCase(), "drawable", "com.efremov.domoffon.rw15");
        ImageView mImg = (ImageView) findViewById(R.id.itemImage);
        mImg.setImageResource(fruitImgResId);
        tw = (TextView) findViewById(R.id.textViewCost);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        try{
            mCounter.setText(String.valueOf(i));
            int cost = i * price_;
            tw.setText(cost + " руб.");
        }
        catch (Exception e){
            e.getMessage();
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


    public static class PopupScreen extends DialogFragment {
        public static String TAG = PopupScreen.class.getSimpleName();

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup
                container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.alert_payment, container, false);
        }
    }
}

