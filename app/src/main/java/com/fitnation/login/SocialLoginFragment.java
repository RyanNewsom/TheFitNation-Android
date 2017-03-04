package com.fitnation.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.fitnation.R;
import com.fitnation.base.BaseActivity;
import com.fitnation.base.BaseFragment;
import com.fitnation.navigation.NavigationActivity;
import com.stormpath.sdk.Stormpath;
import com.stormpath.sdk.StormpathCallback;
import com.stormpath.sdk.models.Account;
import com.stormpath.sdk.models.StormpathError;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SocialLoginFragment extends BaseFragment implements SocialLoginContract.View {
    private SocialLoginContract.Presenter mPresenter;

    public SocialLoginFragment() {
        // Required empty public constructor
    }

    public static SocialLoginFragment newInstance() {
        return new SocialLoginFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_social_login, container, false);
        ButterKnife.bind(this, v);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.start();
    }

    @Override
    public void onResume(){
        super.onResume();

        //check to see if stormpath already has the user logged in
        Stormpath.getAccount(new StormpathCallback<Account>() {
            @Override
            public void onSuccess(Account account) {
                Intent homeActivityIntent = new Intent(getBaseActivity(), NavigationActivity.class);
                getBaseActivity().startActivity(homeActivityIntent);
                getBaseActivity().finish();
            }

            @Override
            public void onFailure(StormpathError error) {

            }
        });
    }

    /**
     * on facebook button pressed will launch the stormpath social login for facebook
     */
    @OnClick(R.id.facebook_login_button)
    public void onFacebookLoginButtonClicked() {
        mPresenter.onFacebookLoginPressed();
    }


    @OnClick(R.id.google_login_button)
    public void onGoogleLoginButtonClicked() { mPresenter.onGoogleLoginPressed();}

    @OnClick(R.id.github_login_button)
    public void onGitHubLoginButtonClicked() { mPresenter.onGitHubLoginPressed(); }

    @OnClick(R.id.twitter_login_button)
    public void onTwitterLoginButtonClicked() { mPresenter.onTwitterLoginPressed(); }

    @Override
    public void showProgress() {
    }

    @Override
    public void showAuthError(String errorMessage) {
        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();

    }

    @Override
    public void setPresenter(SocialLoginContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }
}
