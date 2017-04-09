package com.fitnation.networking.tasks;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fitnation.Factory.FactoryContract;
import com.fitnation.Factory.VolleyErrorMessage;
import com.fitnation.base.BaseActivity;
import com.fitnation.navigation.NavigationActivity;
import com.fitnation.utils.EnvironmentManager;
import com.fitnation.utils.NetworkUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles the login request
 */

public class GetAuthTokenTask implements FactoryContract.FactoryReturn{
    private TaskContract.Presenter mPresenter;
    private BaseActivity mActivity;

    public GetAuthTokenTask(BaseActivity activity, TaskContract.Presenter presenter) {
        this.mActivity = activity;
        mPresenter = presenter;
    }

    public void requestToken(final String userName, final String password){
        RequestQueue requestQueue = Volley.newRequestQueue(mActivity);
        String endpoint = "oauth/token";
        String url = EnvironmentManager.getInstance().getCurrentEnvironment().getBaseUrl() + endpoint;

        ProgressDialog progressDialog = new ProgressDialog(mActivity);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Please wait...");
        progressDialog.setIndeterminate(true);
        mPresenter.showProgress(progressDialog);

        JsonObjectRequest jsonObjectPost = new JsonObjectRequest(Request.Method.POST,
                url, null, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response) {
                mPresenter.stopProgress();
                NetworkUtils.getInstance().storeTokens(response);

                Intent mainActivityIntent = new Intent(mActivity, NavigationActivity.class);
                mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mActivity.startActivity(mainActivityIntent);

            }
        },  new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try{
                    int code = error.networkResponse.statusCode;
                    errorResponseMessage(error);
                    mPresenter.stopProgress();
                }catch(NullPointerException nullPointer){
                    mPresenter.stopProgress();
                    noResponseError();
                }
            }
        }){
            @Override
            public byte[] getBody() {
                Map<String,String> params = new HashMap<>();
                params.put("username", userName);
                params.put("password", password);
                params.put("grant_type", "password");
                params.put("scope", "read+write");
                params.put("client_secret", "my-secret-token-to-change-in-production");
                params.put("client_id", "TheFitNationapp");
                params.put("submit", "login");

                return NetworkUtils.getInstance().convertToUrlEncodedPostBody(params).getBytes();
            }

            @Override
            public String getBodyContentType() {
                return ("application/x-www-form-urlencoded");
            }
        };

        requestQueue.add(jsonObjectPost);
        requestQueue.start();
    }

    private void errorResponseMessage(VolleyError error) {
        if(error.networkResponse.statusCode != 401) {
            VolleyErrorMessage volleyErrorMessage = new VolleyErrorMessage(error);
            mPresenter.showAuthError(volleyErrorMessage.getErrorMessage(mActivity));
        }else {
            VolleyErrorMessage volleyErrorMessage = new VolleyErrorMessage(error);
            volleyErrorMessage.getErrorMessage(mActivity);
        }
    }

    private void noResponseError(){
        AlertDialog.Builder noResponseDialog = new AlertDialog.Builder(mActivity);
        noResponseDialog.setTitle("No Response");
        noResponseDialog.setMessage("Attempted to connect to the server but did not recieve a response. Please try again");
        noResponseDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        noResponseDialog.create();
        mPresenter.showAuthError(noResponseDialog);
    }

    @Override
    public void showSuccessDialog(AlertDialog.Builder alertDialog) {
        mPresenter.showSuccess(alertDialog);
    }

    @Override
    public void showErrorDialog(AlertDialog.Builder alertDialog) {
        mPresenter.showAuthError(alertDialog);
    }
}