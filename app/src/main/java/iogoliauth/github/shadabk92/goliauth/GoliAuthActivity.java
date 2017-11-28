package iogoliauth.github.shadabk92.goliauth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Arrays;

public class GoliAuthActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 2;
    private static final String TAG = "LOGIN_ACTIVITY";
    private GoogleApiClient mGoogleApiClient;

    CallbackManager callbackManager;


    AccessTokenTracker accessTokenTracker;
    ProfileTracker profileTracker;


    //Account Kit Activity Variables
    private static int APP_REQUEST_CODE = 1;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goli_auth);

        //Method to initialize Facebook Application after Facebook SDK 4.5+
        AppEventsLogger.activateApp(getApplication());


        //Google+ Sign In Button
        SignInButton signInButton = findViewById(R.id.btngplus);
        signInButton.setOnClickListener(v -> {
            switch (v.getId()) {
                case R.id.btngplus:
                    signIn();
                    break;
            }
        });

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(GoliAuthActivity.this, connectionResult -> Toast.makeText(GoliAuthActivity.this, "Please check your email address", Toast.LENGTH_SHORT).show())
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        //Facebook Login Button
        LoginButton loginButton = findViewById(R.id.btnfb);

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                LoginManager.getInstance().logInWithReadPermissions(GoliAuthActivity.this, Arrays.asList("public_profile"));


            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                // Set the access token using
                // currentAccessToken when it's loaded or set.
            }
        };
        // If the access token is available already assign it.
//        AccessToken accessToken2 = AccessToken.getCurrentAccessToken();

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(
                    Profile oldProfile,
                    Profile currentProfile) {
                // App code
                startActivity(new Intent(getApplicationContext(), GoliAuthActivity.class));
//                Toast.makeText(GoliAuthActivity.this, currentProfile.getFirstName() + " " + currentProfile.getLastName(), Toast.LENGTH_SHORT).show();
            }
        };

        //Github Login Button


        //In Pro Version
        //Firebase Buttons
        //Firebase Login with Email

        //Firebase login with OTP

        //Firebase Login with Facebook

        //Firebase Login with Github

        //Firebase Login with  Google+

        //Firebase Login with Anonymous User

        //WhatsApp Business Auth


    }

    /**
     * Google+ SignIn Method
     */
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    //OnActivityResult for Google+ and Facebook Logins

    /**
     * onActivityResult for all Authentications
     *
     * @param requestCode Request codes from AccountKit and Google+ Logins
     * @param resultCode  ResultCode from AccountKit and Google+ Logins
     * @param data        User Data from all Authentication accounts
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == APP_REQUEST_CODE) {
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            if (loginResult.getError() != null) {
                String toastMessage = loginResult.getError().getErrorType().getMessage();
                Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();

            } else if (loginResult.getAccessToken() != null) {
                launchAccountActivity();
            }
        }


        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);


        }


    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

//            Toast.makeText(this, acct.getDisplayName(), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), GoliAuthActivity.class));


//            mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }

    /**
     * Any message after Login
     *
     * @param b
     */
    private void updateUI(boolean b) {


//        Toast.makeText(this, "Welcome to google + signed in user", Toast.LENGTH_SHORT).show();

    }

    //AccountKit Email and Mobile OTP Login Methods

    /**
     * Login method for AccountKit
     *
     * @param loginType
     */
    public void onLogin(final LoginType loginType) {
        //Create intent for Account Kit Activity
        final Intent intent = new Intent(this, AccountKitActivity.class);

        //Configure Login Type and Response Code
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        loginType,
                        AccountKitActivity.ResponseType.TOKEN
                );

        final AccountKitConfiguration configuration = configurationBuilder.build();

        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION, configuration);
        startActivityForResult(intent, APP_REQUEST_CODE);

    }

    /**
     * AccountKit Phone OTP Login
     *
     * @param view
     */
    public void onPhoneLogin(View view) {
        onLogin(LoginType.PHONE);
    }

    /**
     * AccountKit Email Login
     *
     * @param view
     */
    public void onEmailLogin(View view) {
        onLogin(LoginType.EMAIL);
    }

    /**
     * Launch Activity if AccountKit is already logged in
     */
    public void launchAccountActivity() {
        startActivity(new Intent(getApplicationContext(), GoliAuthActivity.class));
        finish();
    }


    /**
     * Firebase Email Login
     * @param view
     */
    public void onFirebaseEmailLogin(View view){

    }

    /**
     * Firebase Mobile Login
     * @param view
     */
    public void onFirebaseOTPLogin(View view){

    }


    /**
     * Common onDestroy method for all Authentications
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();

    }


}
