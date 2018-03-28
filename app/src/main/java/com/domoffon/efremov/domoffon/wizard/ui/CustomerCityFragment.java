/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.domoffon.efremov.domoffon.wizard.ui;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieSyncManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.domoffon.efremov.domoffon.R;
import com.domoffon.efremov.domoffon.wizard.model.CustomerCityPage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CustomerCityFragment extends Fragment {
    private static final String ARG_KEY = "key";

    private PageFragmentCallbacks mCallbacks;
    private String mKey;
    private CustomerCityPage mPage;
    private AutoCompleteTextView mNameView;

    public static CustomerCityFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        CustomerCityFragment fragment = new CustomerCityFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public CustomerCityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        mKey = args.getString(ARG_KEY);
        mPage = (CustomerCityPage) mCallbacks.onGetPage(mKey);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_page_customer_city, container, false);
        ((TextView) rootView.findViewById(android.R.id.title)).setText(mPage.getTitle());

        mNameView = ((AutoCompleteTextView) rootView.findViewById(R.id.city));
        //Получаем список из интернета
        String[] citys =  getResources().getStringArray(R.array.city);

        mNameView.setText(mPage.getData().getString(CustomerCityPage.NAME_DATA_KEY));
        final ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1,
                citys);
        mNameView.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof PageFragmentCallbacks)) {
            throw new ClassCastException("Activity must implement PageFragmentCallbacks");
        }

        mCallbacks = (PageFragmentCallbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mNameView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                    int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(CustomerCityPage.NAME_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);

        // In a future update to the support library, this should override setUserVisibleHint
        // instead of setMenuVisibility.
        if (mNameView != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            if (!menuVisible) {
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            }
        }
    }

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
            // TODO: attempt authentication against a network service.
            //response = "Offline";
            try {
                Thread.sleep(2000);
                URL myURL = new URL(getString(R.string.server_ip) + "get_city_list");
                //HttpURLConnection urlConnection = (HttpURLConnection) myURL.openConnection();
                HttpURLConnection conn = (HttpURLConnection) myURL.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder();
                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                conn.connect();

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer resp = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    resp.append(inputLine);
                }
                in.close();

                response = resp.toString();
            } catch (InterruptedException e) {
                return false;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                response = e.getMessage();
            } catch (ProtocolException e) {
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

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            //mAuthTask = null;
            //showProgress(false);

            List<String> list;
            //Toast toast = Toast.makeText(getApplicationContext(),
             //       response, Toast.LENGTH_SHORT);
            //toast.show();
            if (response.contains("Offline")){
            //    OpenShop(false);
            }
            else{
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("Code") == 200) {
                        CookieSyncManager.getInstance().sync();
                        JSONArray arr = new JSONArray(obj.getJSONArray("CityArray"));
                        list = new ArrayList<String>();
                        for(int i = 0; i < arr.length(); i++){
                            list.add(arr.getJSONObject(i).getString("name"));
                        }
                        //OpenShop(true);
                    }
                    else if (obj.getInt("Code") >= 500) {
                        //OpenError(obj.getInt("Code"), obj.getString("Description"));
                    }
                    else {
                        //mPasswordView.setError(getString(R.string.error_incorrect_password));
                       // mPasswordView.requestFocus();
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
            //mAuthTask = null;
            //showProgress(false);
        }
    }
}
