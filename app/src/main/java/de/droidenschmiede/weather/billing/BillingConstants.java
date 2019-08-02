package de.droidenschmiede.weather.billing;

/*
 * Copyright 2017 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClient.SkuType;

import java.util.Arrays;
import java.util.List;

/**
 * Static fields and methods useful for billing
 */
public final class BillingConstants {
    // SKUs for our products: the premium upgrade (non-consumable) and gas (consumable)

    private final static boolean TESTDATA = false;

    public static final String SKU_DONATE = "donate_one_dollar";
    private static final String[] IN_APP_SKUS = {SKU_DONATE};

    //Test
    public static final String SKU_TEST = "android.test.purchased";
    private static final String[] IN_APP_SKUS_TEST = {SKU_TEST};

    private BillingConstants(){}

    /**
     * Returns the list of all SKUs for the billing type specified
     */
    public static final List<String> getSkuList() {

        if(TESTDATA){
            return Arrays.asList(IN_APP_SKUS_TEST);
        }
        else{
            return Arrays.asList(IN_APP_SKUS);
        }
    }

    public static final String getSku(){

        if(TESTDATA){
            return SKU_TEST;
        }
        else{
            return SKU_DONATE;
        }
    }

}
