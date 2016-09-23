/**
 * Copyright (C) 2016 Alvaro Bolanos Rodriguez
 */
package com.example.android.sunshine.app.wearable;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

/*
 * TODO: Create JavaDoc
 */

public class WearableSync {
    public static final String WEARABLE_MAX_TEMP_KEY = "wearable_max_temp_key";
    public static final String WEARABLE_MIN_TEMP_KEY = "wearable_min_temp_key";

    public static void syncMaxTempToWearable(String relativeUri, double maxTempValue, GoogleApiClient googleApiClient){
        PutDataMapRequest putDataMapRequest = PutDataMapRequest.create(relativeUri);
        putDataMapRequest.getDataMap().putDouble(WEARABLE_MAX_TEMP_KEY, maxTempValue);
        setDataItem(googleApiClient, putDataMapRequest);
    }

    private static void setDataItem(GoogleApiClient googleApiClient, PutDataMapRequest putDataMapReq){
        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
        PendingResult<DataApi.DataItemResult> pendingResult =
                Wearable.DataApi.putDataItem(googleApiClient, putDataReq);
    }
}
