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

package com.domoffon.efremov.domoffon;

import android.content.Context;

import com.domoffon.efremov.domoffon.wizard.model.AbstractWizardModel;
import com.domoffon.efremov.domoffon.wizard.model.CustomerCityPage;
import com.domoffon.efremov.domoffon.wizard.model.CustomerInfoPage;
import com.domoffon.efremov.domoffon.wizard.model.PageList;
import com.domoffon.efremov.domoffon.wizard.model.SingleFixedChoicePage;

public class SandwichWizardModel extends AbstractWizardModel {
    SandwichWizardModel(Context context) {
        super(context);
    }

    @Override
    protected PageList onNewRootPageList() {
        CustomerCityPage ccp = new CustomerCityPage(this, "ШАГ 1. Город");
        return new PageList(


                ccp.setRequired(true),

                //new SingleFixedChoicePage(this, "Склад").setChoices
                //        (ccp.getData().getString("city"), ccp.getData().getString("city"),
                //                ccp.getData().getString("city")).setRequired(true)

                new CustomerInfoPage(this, "ШАГ 2. Ваши данные").setRequired(true),

                new SingleFixedChoicePage(this, "ШАГ 3. Адрес доставки", ccp)
                        .setChoices("Caesar", ccp.GetValue())
                        .setRequired(true)
        );
    }
}
