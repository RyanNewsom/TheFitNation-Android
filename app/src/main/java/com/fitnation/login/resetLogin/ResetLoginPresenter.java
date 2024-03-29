package com.fitnation.login.resetLogin;


import android.app.ProgressDialog;
import android.support.v7.app.AlertDialog;

import com.fitnation.networking.tasks.loginTasks.TaskCallback;
import com.fitnation.networking.tasks.loginTasks.EmailResetPasswordTask;

/**
 * contains the business logic for the view
 */
public class ResetLoginPresenter implements ResetLoginContract.Presenter, TaskCallback.Presenter{
    private ResetLoginContract.View mView;

    public ResetLoginPresenter(ResetLoginContract.View view) { mView = view; }

    @Override
    public void onResetPasswordButtonPressed(final String email) {

        EmailResetPasswordTask emailResetPasswordTask = new EmailResetPasswordTask(mView.getBaseActivity(), this);
        emailResetPasswordTask.resetPasswordRequest(email);

    }

    /*-------------------------------------ResetLoginContract-------------------------------------*/

    @Override
    public void onViewReady() {
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {
    }

    /*----------------------------------------TaskCallback----------------------------------------*/

    @Override
    public void showSuccess(AlertDialog.Builder successDialog) {
        mView.showSuccess(successDialog);
    }

    @Override
    public void showProgress(ProgressDialog progressDialog) {
        mView.showProgress(progressDialog);
    }

    @Override
    public void stopProgress() {
        mView.stopProgress();
    }

    @Override
    public void showAuthError(AlertDialog.Builder errorDialog) {
        mView.showAuthError(errorDialog);
    }
}
