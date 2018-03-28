package com.domoffon.efremov.domoffon;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends ListActivity
        implements SwipeRefreshLayout.OnRefreshListener{

    Context context;
    private SwipeRefreshLayout mSwipeRefresh;

    private boolean isOnline = true;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_dashboard:
                    intent = new Intent(MainActivity.this, PaymentsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    return true;
                case R.id.navigation_notifications:
                    intent = new Intent(MainActivity.this, StoryActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    return true;
                case R.id.navigation_user:
                    intent = new Intent(MainActivity.this, ProfileActivity.class);
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
        setContentView(R.layout.activity_main);

        TextView mTitle = (TextView) findViewById(R.id.login_title);
        mTitle.setText(R.string.app_name);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.removeShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        try {
            Bundle bundle = getIntent().getExtras();
            assert bundle != null;
            boolean test = bundle.getBoolean("connection");
            if (test){
                setTitle("Каталог");
                navigation.setVisibility(View.VISIBLE);
            }
            else {
                setTitle("Оффлайн режим");
                isOnline = false;
                navigation.setVisibility(View.INVISIBLE);
            }
        }
        catch(Exception ignored) {}

        ListView listView = (ListView)findViewById(R.id.content);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                intent.putExtra("code", id);
                startActivity(intent);
            }
        });

        final String[] values = new String[] {
                "Ключи"   , "Дубликаторы", "Замки"
        };

        listView.setAdapter(new ListAdapterMain(this, values));

        //Refresh
        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefresh.setOnRefreshListener(this);


        mSwipeRefresh.setColorSchemeResources(android.R.color.primary_text_dark_nodisable,
                android.R.color.primary_text_dark,
                android.R.color.primary_text_light,
                android.R.color.primary_text_light_nodisable);
        mSwipeRefresh.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        if (isOnline)
            new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Отменяем анимацию обновления
                   mSwipeRefresh.setRefreshing(false);
            }
            }, 4000);
    }

    @Override
    public void onBackPressed() {
        MainActivity.PopupScreen popupScreen = new MainActivity.PopupScreen();
        popupScreen.show(getFragmentManager(), ItemActivity.PopupScreen.TAG);

    }

    public void Exit(){
        LogOutTask mLogOutTask = new LogOutTask();
        mLogOutTask.execute((Void) null);
    }

    @SuppressLint("ValidFragment")
    public class PopupScreen extends DialogFragment {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.alert_back, null);
            builder.setView(view);

            String button1String = "Да";
            String button2String = "Нет";



            builder.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Exit();
                }
            });
            builder.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });
            builder.setCancelable(true);

            return builder.create();
        }
    }

    public void CloseSession(){
        //TODO: delete password
        Session session = new Session(MainActivity.this);
        session.clean();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);

        try {
            this.finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class LogOutTask extends AsyncTask<Void, Void, Boolean> {
        public boolean isEnd = false;

        private String response;

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Thread.sleep(100);
                URL myURL = new URL(getString(R.string.server_ip) + "log_out");
                //HttpURLConnection urlConnection = (HttpURLConnection) myURL.openConnection();
                HttpURLConnection conn = (HttpURLConnection) myURL.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                conn.connect();

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder resp = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    resp.append(inputLine);
                }
                in.close();

                response = resp.toString();
            } catch (InterruptedException e) {
                response = e.getMessage();
                return false;
            } catch (IOException e) {
                //java.net.ConnectException: failed to connect to /192.168.1.36 (port 8080): connect failed: ENETUNREACH (Network is unreachable)
                e.printStackTrace();
                response = e.getMessage();
            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {


            Toast toast = Toast.makeText(getApplicationContext(),
                    response, Toast.LENGTH_SHORT);
            toast.show();
            CloseSession();
            isEnd = true;
        }
    }
}
