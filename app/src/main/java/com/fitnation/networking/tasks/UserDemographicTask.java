package com.fitnation.networking.tasks;

import android.nfc.Tag;
import android.support.design.widget.TabLayout;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fitnation.networking.AuthToken;
import com.fitnation.networking.UserLogins;
import com.fitnation.model.UserDemographic;
import com.fitnation.networking.JsonParser;
import com.fitnation.profile.callbacks.UserDemographicsCallback;
import com.fitnation.utils.EnvironmentManager;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;

/**
 * Created by Jeremy on 4/9/2017.
 */

public class UserDemographicTask extends NetworkTask{

    private final String TAG = getClass().getSimpleName().toString();

    public UserDemographicTask(String authToken, RequestQueue queue) {
        super(authToken, queue);
    }

    public void getUserDemographicById(final UserDemographicsCallback callback){

        String resourceRoute = "users/user-demographic/";
        String url = EnvironmentManager.getInstance().
                getCurrentEnvironment().getApiUrl() + resourceRoute;

        //USERDEMOGRAPHIC
        JsonObjectRequest jsonRequestUserDemo =
                new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, response.toString());
                        UserDemographic userdemo = JsonParser.convertJsonStringToPojo(response.toString(), UserDemographic.class);
                        Log.i(TAG, "Updating User Login, User Id, Userdemographic Id " + userdemo.getUserLogin() + " " + userdemo.getUserId().toString() + " " + userdemo.getId().toString());
                        UserLogins.setUserLogin(userdemo.getUserLogin());
                        UserLogins.setUserDemographicId(String.valueOf(userdemo.getId()));
                        UserLogins.setUserId(userdemo.getUserId().toString());
                        callback.onSuccess(userdemo);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            callback.onFailure(String.valueOf(error.networkResponse.statusCode));
                        } catch (NullPointerException ex) {
                            Log.d(TAG, ex.toString());
                        }
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {

                        HashMap<String, String> params = new HashMap<String, String>();
                        params.put("Content-Type", "application/json");
                        params.put("Accept", "application/json");
                        params.put("Authorization", "Bearer "+mAuthToken);
                        return params;
                    }
                };

        mRequestQueue.add(jsonRequestUserDemo);
    }

    public void putUserDemographicData(UserDemographic userdemo, final UserDemographicsCallback callback){
        //save data to web
        String resourceRoute = "user-demographics";
        String url = EnvironmentManager.getInstance().getCurrentEnvironment()
                .getApiUrl()+resourceRoute;
        final String authToken = AuthToken.getInstance().getAccessToken();

        String jString = JsonParser.convertPojoToJsonString(userdemo);
        JSONObject udjObj;
        try {
            udjObj = new JSONObject(jString);
            Log.i(TAG, udjObj.toString());
        } catch (org.json.JSONException e) {
            Log.d(TAG, "Failed to convert User Demographic to JSON String");
            udjObj = new JSONObject();
        }

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.PUT, url, udjObj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                UserDemographic ud = JsonParser.convertJsonStringToPojo(response.toString(), UserDemographic.class);
                Log.i("PUT", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("PUT", error.toString());
                try{
                    callback.onFailure(error.toString() + " " + error.networkResponse.statusCode);
                } catch (NullPointerException ex){
                    Log.d(TAG, "Volley error respsonse is null " + ex.toString());
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                HashMap<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer "+authToken);
                return params;
            }
        };

        Log.d("JSON REQUEST", jsonRequest.toString());
        mRequestQueue.add(jsonRequest);
    }


}
