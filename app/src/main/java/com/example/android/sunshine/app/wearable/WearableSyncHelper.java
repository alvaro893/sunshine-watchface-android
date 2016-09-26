package com.example.android.sunshine.app.wearable;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.sunshine.app.Utility;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

public class WearableSyncHelper implements DataApi.DataListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private static final String WEARABLE_MAX_TEMP_KEY = "max_temp";
    private static final String WEARABLE_MIN_TEMP_KEY = "min_temp";
    private static final String WEARABLE_WEATHER_ID = "weather_id";
    private static final String RELATIVE_URL = "/sunface";

    private static final String TAG = WearableSyncHelper.class.getSimpleName();

    private GoogleApiClient mGoogleApiClient;
    private Context mContext;


    public WearableSyncHelper(Context mContext) {
        this.mContext = mContext;
        init();
    }

    public void init() {
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Wearable.API)
                .build();
        mGoogleApiClient.connect();
    }

    public void startConnection() {
        Log.d(TAG, "is connected already: " + mGoogleApiClient.isConnected());
        if(!mGoogleApiClient.isConnected()){
            mGoogleApiClient.connect();
        }
    }

    public void stopConnection(){
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "connected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended: " + i);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed: " + connectionResult);
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {
            Log.d(TAG, "onDataChanged: " + dataEventBuffer);
    }

    public void syncWeatherInfo(double high, double low, int weatherId) {
        String formatedHigh = Utility.formatTemperature(mContext, high);
        String formatedLow = Utility.formatTemperature(mContext, low);


        PutDataMapRequest putDataMapRequest = PutDataMapRequest.create(RELATIVE_URL);
        putDataMapRequest.getDataMap().putString(WEARABLE_MAX_TEMP_KEY, formatedHigh);
        putDataMapRequest.getDataMap().putString(WEARABLE_MIN_TEMP_KEY, formatedLow);
        putDataMapRequest.getDataMap().putInt(WEARABLE_WEATHER_ID, weatherId);
        putDataMapRequest.getDataMap().putInt("vvvv", (int) System.currentTimeMillis());

        PutDataRequest dataRequest = putDataMapRequest.asPutDataRequest();
        dataRequest.setUrgent();

        PendingResult<DataApi.DataItemResult> pendingResult =
                Wearable.DataApi.putDataItem(mGoogleApiClient, dataRequest);

        pendingResult.setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
            @Override
            public void onResult(@NonNull DataApi.DataItemResult dataItemResult) {
                Log.d(TAG, "Data item result success: " + dataItemResult.getStatus().isSuccess());
            }
        });


    }
}
