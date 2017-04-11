package com.fitnation.networking.tasks;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fitnation.Factory.FactoryCallback;
import com.fitnation.Factory.VolleyErrorMessage;
import com.fitnation.base.BaseActivity;
import com.fitnation.utils.EnvironmentManager;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles the register request
 */

public class RegisterUserTask implements FactoryCallback.FactoryReturn{
    private BaseActivity mActivity;
    private TaskCallback.Presenter mPresenter;

    public RegisterUserTask(BaseActivity activity, TaskCallback.Presenter presenter) {
        this.mActivity = activity;
        this.mPresenter = presenter;
    }

    public void requestRegistration(final String email, final String password, final String userName,
                                    final String language){
        RequestQueue requestQueue = Volley.newRequestQueue(mActivity);
        String endpoint = "api/register";
        String url = EnvironmentManager.getInstance().getCurrentEnvironment().getBaseUrl() + endpoint;

        ProgressDialog progressDialog = new ProgressDialog(mActivity);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Please wait...");
        progressDialog.setIndeterminate(true);
        mPresenter.showProgress(progressDialog);

        StringRequest jsonObjectPost = new StringRequest(Request.Method.POST, url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                mPresenter.stopProgress();
                handleJsonResponse();

            }
        },  new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mPresenter.stopProgress();
                errorResponseMessage(error);
            }
        }
        ){
            @Override
            public byte[] getBody() {
                Map<String, String> map = new HashMap<>();
                map.put("email", email);
                map.put("langKey", language);
                map.put("login", userName);
                map.put("password", password);

                JSONObject jsonObject = new JSONObject(map);
                return jsonObject.toString().getBytes();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                return params;
            }

        };

        jsonObjectPost.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1));
        requestQueue.add(jsonObjectPost);
        requestQueue.start();
    }

    private void errorResponseMessage(VolleyError error) {
        VolleyErrorMessage volleyErrorMessage = new VolleyErrorMessage(mActivity, this);
        volleyErrorMessage.getErrorMessage(error);
    }

    /**
     * returns the successful registration alert dialog which informs user to activate email
     */
    private void handleJsonResponse(){
        android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(mActivity);
        alertDialog.setTitle("Success!");
        alertDialog.setMessage("Welcome to the Fit Nation! To complete the regestration process please confirm your email");
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.create();
        mPresenter.showSuccess(alertDialog);
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
