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

package com.domoffon.efremov.domoffon.wizard.model;

import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;

import com.domoffon.efremov.domoffon.wizard.ui.CustomerInfoFragment;

import java.util.ArrayList;

/**
 * A page asking for a name and an email.
 */
public class CustomerInfoPage extends Page {
    public static final String NAME_DATA_KEY = "name";
    public static final String EMAIL_DATA_KEY = "email";
    public static final String PASSWORD_DATA_KEY = "password";
    public static final String CONFIRM_PASSWORD_DATA_KEY = "c_password";

    public CustomerInfoPage(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
    }

    @Override
    public Fragment createFragment() {
        return CustomerInfoFragment.create(getKey());
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
        dest.add(new ReviewItem("Имя", mData.getString(NAME_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("Номер", mData.getString(EMAIL_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("Пароль", mData.getString(PASSWORD_DATA_KEY), getKey(), -1));
        //dest.add(new ReviewItem("Подтверждение пароля", mData.getString(CONFIRM_PASSWORD_DATA_KEY), getKey(), -1));
    }

    @Override
    public boolean isCompleted() {
        boolean result = false;
        try{
            int length = 0;
            int length1 = 0;
            try {
                length = TextUtils.getTrimmedLength(mData.getString(PASSWORD_DATA_KEY));
                length1 = TextUtils.getTrimmedLength(mData.getString(CONFIRM_PASSWORD_DATA_KEY));
            }
            catch (Exception e){

            }
            result = !(TextUtils.isEmpty(mData.getString(NAME_DATA_KEY)) ||
                    TextUtils.isEmpty(mData.getString(EMAIL_DATA_KEY)) ||
                    length < 5 ||
                    length1 < 5 ||
                    !mData.getString(PASSWORD_DATA_KEY).equals(mData.getString(CONFIRM_PASSWORD_DATA_KEY)));
        }
        catch (Exception e){
            Log.d(e.toString(), e.getMessage());
        }
        return result;
    }
}
