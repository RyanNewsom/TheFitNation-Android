package com.fitnation.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import com.fitnation.R;
import com.fitnation.base.BaseActivity;
import com.fitnation.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterFragment extends BaseFragment implements RegisterContract.View{
    private RegisterContract.Presenter mPresenter;

    @BindView(R.id.registerEmail_editText) public EditText mEmail;
    @BindView(R.id.registerPassword_editText) public EditText mPassword;
    @BindView(R.id.userName_editText) public EditText userName;

    public RegisterFragment() {
        // Required empty public constructor
    }

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        ButterKnife.bind(this, v);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.start();
    }

    @OnClick(R.id.register_button)
    public void onRegisterButtonPressed() {
        if(!mEmail.getText().toString().isEmpty()
                && !mPassword.getText().toString().isEmpty()
                && !userName.getText().toString().isEmpty()) {
            mPresenter.onRegisterCreatePressed(mEmail.getText().toString().trim(),
                    mPassword.getText().toString().trim(),
                    userName.getText().toString().trim(),
                    "en");
        }
    }

    @Override
    public void showSuccess(android.support.v7.app.AlertDialog.Builder successDialog) {
        successDialog.show();
    }

    @Override
    public void showProgress() {
    }

    @Override
    public void showAuthError(android.support.v7.app.AlertDialog.Builder errorDialog) {
        errorDialog.show();
    }

    @Override
    public void setPresenter(RegisterContract.Presenter presenter) { mPresenter = presenter; }

    @Override
    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }
}
