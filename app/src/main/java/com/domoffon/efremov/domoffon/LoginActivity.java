package com.domoffon.efremov.domoffon;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    private UserLoginTask mAuthTask = null;

    private Session session;

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressForm;
    private View mLoginFormView;
    private Button mButtonReg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = new Session(this);

        mButtonReg = (Button) findViewById(R.id.reg_button);
        mButtonReg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, StepActivity.class);
                startActivity(intent);
            }
        });
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mEmailView.addTextChangedListener(new PhoneNumberFormattingTextWatcher() {
            //we need to know if the user is erasing or inputing some new character
            private boolean backspacingFlag = false;
            //we need to block the :afterTextChanges method to be called again after we just replaced the EditText text
            private boolean editedFlag = false;
            //we need to mark the cursor position and restore it after the edition
            private int cursorComplement;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //we store the cursor local relative to the end of the string in the EditText before the edition
                cursorComplement = s.length()-mEmailView.getSelectionStart();
                //we check if the user ir inputing or erasing a character
                backspacingFlag = count > after;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // nothing to do here =D
            }

            @Override
            public void afterTextChanged(Editable s) {
                String string = s.toString();
                //what matters are the phone digits beneath the mask, so we always work with a raw string with only digits
                String phone = string.replaceAll("[^\\d]", "");

                //if the text was just edited, :afterTextChanged is called another time... so we need to verify the flag of edition
                //if the flag is false, this is a original user-typed entry. so we go on and do some magic
                if (!editedFlag) {

                    //we start verifying the worst case, many characters mask need to be added
                    //example: 999999999 <- 6+ digits already typed
                    // masked: (999) 999-999
                    if (phone.length() >= 1 && phone.substring(0, 1).contains("7"))
                    {
                        phone = phone.replace(phone.substring(0, 1), "8");
                    }
                    if (phone.length() >= 7 && !backspacingFlag) {
                        //we will edit. next call on this textWatcher will be ignored
                        editedFlag = true;
                        //here is the core. we substring the raw digits and add the mask as convenient
                        String ans = phone.substring(0, 1) + "(" +phone.substring(1, 4) + ") " + phone.substring(4,7) + "-" + phone.substring(7);
                        mEmailView.setText(ans);
                        //we deliver the cursor to its original position relative to the end of the string
                        mEmailView.setSelection(mEmailView.getText().length()-cursorComplement);

                        //we end at the most simple case, when just one character mask is needed
                        //example: 99999 <- 3+ digits already typed
                        // masked: (999) 99
                    } else if (phone.length() >= 4 && !backspacingFlag) {
                        editedFlag = true;
                        String ans = phone.substring(0, 1) + "(" +phone.substring(1, 4) + ") " + phone.substring(4);
                        mEmailView.setText(ans);
                        mEmailView.setSelection(mEmailView.getText().length()-cursorComplement);
                    }
                    // We just edited the field, ignoring this cicle of the watcher and getting ready for the next
                } else {
                    editedFlag = false;
                }
            }
        });

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.hideSoftInputFromWindow(mButtonReg.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressForm = findViewById(R.id.layout_progress);

        Toast toast = Toast.makeText(getApplicationContext(),
                session.getpass(), Toast.LENGTH_SHORT);
        toast.show();
        if (!session.getusename().isEmpty()) {
            mEmailView.setText(session.getusename());
            if (!session.getsecret().isEmpty()) {
                showProgress(true);
                CheckSessionTask mSessionTask = new CheckSessionTask(session.getusename(), session.getsecret());
                mSessionTask.execute((Void) null);
            }
        }
    }
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        mEmailView.setError(null);
        mPasswordView.setError(null);

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            DoWork(email, password);
        }
    }

    private void DoWork(String email, String password){
        showProgress(true);
        mAuthTask = new UserLoginTask(email, password);
        mAuthTask.execute((Void) null);
    }


    private boolean isPasswordValid(String password) {
        return password.length() > 6;
    }

    public void OpenError(int code, String des) {
        Intent intent = new Intent(this, ServerErrorActivity.class);
        intent.putExtra("des", des);
        intent.putExtra("code", code);

        startActivity(intent);
        this.finish();
    }

    public void OpenShop(String secret){

        if (checkInternet()) {
            session.setsecret(secret);
            session.setpass(mPasswordView.getText().toString());
            session.setusename(mEmailView.getText().toString());
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("connection", true);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("connection", false);
            startActivity(intent);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressForm.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressForm.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressForm.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    public boolean checkInternet() {

        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        // проверка подключения
        return activeNetwork != null && activeNetwork.isConnected();

    }

    @SuppressLint("StaticFieldLeak")
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private String response;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Thread.sleep(2000);
                URL myURL = new URL(getString(R.string.server_ip) + "login");
                //HttpURLConnection urlConnection = (HttpURLConnection) myURL.openConnection();
                HttpURLConnection conn = (HttpURLConnection) myURL.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("login", mEmail)
                        .appendQueryParameter("password", mPassword);
                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                conn.connect();

                java.net.CookieManager msCookieManager = new java.net.CookieManager();

                Map<String, List<String>> headerFields = conn.getHeaderFields();
                List<String> cookiesHeader = headerFields.get("Set-Cookie");

                if (cookiesHeader != null) {
                    for (String cookie : cookiesHeader) {
                        msCookieManager.getCookieStore().add(null, HttpCookie.parse(cookie).get(0));
                    }
                }

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
                return false;
            } catch (MalformedURLException | ProtocolException e) {
                e.printStackTrace();
                response = e.getMessage();
            } catch (IOException e) {
                //java.net.ConnectException: failed to connect to /192.168.1.36 (port 8080): connect failed: ENETUNREACH (Network is unreachable)
                if (e.getMessage().contains("Network is unreachable")){
                    response = "Offline";
                    return false;
                }
                e.printStackTrace();
                response = e.getMessage();
            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);


            Toast toast = Toast.makeText(getApplicationContext(),
                    response, Toast.LENGTH_SHORT);
            toast.show();
            if (response.contains("Offline")){
                OpenShop("");
            }
            else{
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("Code") == 200) {
                        OpenShop(obj.getString("SecretCode"));
                    }
                    else if (obj.getInt("Code") >= 500) {
                        OpenError(obj.getInt("Code"), obj.getString("Description"));
                    }
                    else {
                        mPasswordView.setError(getString(R.string.error_incorrect_password));
                        mPasswordView.requestFocus();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //if (success) {
            //    finish();
            //}
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class CheckSessionTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mSecret;
        private String response;

        CheckSessionTask(String email, String secret) {
            mEmail = email;
            mSecret = secret;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Thread.sleep(2000);
                URL myURL = new URL(getString(R.string.server_ip));
                //HttpURLConnection urlConnection = (HttpURLConnection) myURL.openConnection();
                HttpURLConnection conn = (HttpURLConnection) myURL.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("login", mEmail)
                        .appendQueryParameter("secret", mSecret);
                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                conn.connect();

                java.net.CookieManager msCookieManager = new java.net.CookieManager();

                Map<String, List<String>> headerFields = conn.getHeaderFields();
                List<String> cookiesHeader = headerFields.get("Set-Cookie");

                if (cookiesHeader != null) {
                    for (String cookie : cookiesHeader) {
                        msCookieManager.getCookieStore().add(null, HttpCookie.parse(cookie).get(0));
                    }
                }

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
                return false;
            } catch (MalformedURLException | ProtocolException e) {
                e.printStackTrace();
                response = e.getMessage();
            } catch (IOException e) {
                //java.net.ConnectException: failed to connect to /192.168.1.36 (port 8080): connect failed: ENETUNREACH (Network is unreachable)
                if (e.getMessage().contains("Network is unreachable")){
                    response = "Offline";
                    return false;
                }
                e.printStackTrace();
                response = e.getMessage();
            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);


            Toast toast = Toast.makeText(getApplicationContext(),
                    response, Toast.LENGTH_SHORT);
            toast.show();
            if (response.contains("Offline")){
                OpenShop("");
            }
            else{
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("Code") == 200) {
                        OpenShop(obj.getString("SecretCode"));
                    }
                    else if (obj.getInt("Code") >= 500) {
                        OpenError(obj.getInt("Code"), obj.getString("Description"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //if (success) {
            //    finish();
            //}
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

