package vigi.patient.initiation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

import vigi.patient.R;
import vigi.patient.user.Main;
import vigi.patient.utils.ErrorDialog;
import vigi.patient.utils.exceptions.FirebaseException;
import vigi.patient.utils.proxy.NullProxy;

@SuppressWarnings("FieldCanBeLocal")
public class Login extends AppCompatActivity implements View.OnClickListener,View.OnFocusChangeListener,TextView.OnEditorActionListener,TextView.OnKeyListener{

    private static String TAG = "loginClass";

    private EditText passwordText;
    private EditText emailText;
    private FirebaseAuth authfire;
    private String errorText;
    private Button loginBtn;
    private TextView signUpBtn;
    private TextView lostPassBtn;
    private ImageView spin;
    private ScrollView scrollView;
    private LinearLayout background;
    private android.support.v7.widget.Toolbar myToolbar;
    private String errorCode = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Open layout

        setContentView(R.layout.initiation_login);

        // Objects

        emailText = findViewById(R.id.initiationLogin_email);
        passwordText = findViewById(R.id.initiationLogin_password);
        loginBtn = findViewById(R.id.initiationLogin_loginButton);
        signUpBtn = findViewById(R.id.initiationLogin_signUpButton);
        lostPassBtn = findViewById(R.id.initiationLogin_forgotPassButton);
        spin = findViewById(R.id.initiationLogin_spinner);
        scrollView = findViewById(R.id.initiationLogin_scrollView);
        myToolbar = findViewById(R.id.initiationLogin_toolbar);
        background = findViewById(R.id.initiationLogin_background);
        myToolbar = findViewById(R.id.initiationLogin_toolbar);

        //remove scrollable indicator

        scrollView.setVerticalScrollBarEnabled(false);

        // Make buttons and views respond to a click

        signUpBtn.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
        lostPassBtn.setOnClickListener(this);

        // Customize action bar / toolbar

        setSupportActionBar(myToolbar);

        ActionBar actionBar = NullProxy.createProxy(getSupportActionBar());
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Make layout respond to focus

        emailText.setOnFocusChangeListener(this);
        passwordText.setOnFocusChangeListener(this);

        // Setup input type of password programmatically to avoid getting bold

        passwordText.setTransformationMethod(new PasswordTransformationMethod());

        // Make password respond to enter

        passwordText.setOnEditorActionListener(this);
        passwordText.setOnKeyListener(this);

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event){

            // Try to Log In when enter is pressed in password text

            if (actionId == EditorInfo.IME_ACTION_GO) {

                loginBtn.performClick();

            return true;
        }
        return false;

    }


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        // Try to Log In when enter is pressed in password text from the enter button

        if (keyCode == KeyEvent.KEYCODE_ENTER) {

            switch (event.getAction()){

                case KeyEvent.ACTION_DOWN:

                    loginBtn.performClick();

                    break;

                case KeyEvent.ACTION_UP:

                    // nothing

                    break;

            }

            return true;

        }

        return false;
    }

    // Implement click functionalities

    @Override
    public void onClick(View v) {

            if(v.getId()== signUpBtn.getId()) { // Sign up


                Intent configureTypeUserIntent = new Intent(Login.this,Register.class);
                startActivity(configureTypeUserIntent);
                finish();
                }

            else if(v.getId()== lostPassBtn.getId()) { // Forgot password

                Intent forgotpasswordIntent = new Intent(Login.this,ForgotPassword.class);
                startActivity(forgotpasswordIntent);
            }

            else if(v.getId()== loginBtn.getId()) { // Log in

                // Disable movement and start spinning loader

                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                spinVisibility(spin,View.VISIBLE, loginBtn,Login.this);

                // launch Login function

                loginAttempt();

            }
    }


    private void loginAttempt() {

        try {

            // Initialize FirebaseException auth

            authfire = FirebaseAuth.getInstance();

            // Get editText strings

            String email = emailText.getText().toString().trim();
            String password = passwordText.getText().toString().trim();

            authfire.signInWithEmailAndPassword(email, password).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {

                        Intent launchUserIntent = new Intent(Login.this,Main.class);
                        launchUserIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(launchUserIntent);
                        finish();

                    } else { // if task not successful

                        try{

                            FirebaseAuthException firebaseAuthException = NullProxy.createProxy(((FirebaseAuthException) task.getException()));
                            errorCode = firebaseAuthException.getErrorCode();

                            FirebaseException exceptionThrowed = new FirebaseException();

                            errorText = exceptionThrowed.exceptionType(errorCode);

                            errorHandling(spin,background,loginBtn,Login.this,errorText);


                        }catch(ClassCastException e){

                            errorHandling(spin,background,loginBtn,Login.this,"Internet connection is not available.");

                        }

                    }

                }

            });

        }catch (IllegalArgumentException e){

            errorHandling(spin,background,loginBtn,Login.this,"Please enter a valid sign in, all fields are required.");

        }

    }

    // Focus handle

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        if (!hasFocus && !passwordText.hasFocus() && !emailText.hasFocus()) {
            InputMethodManager input = NullProxy.createProxy((InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE));
            input.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    // Spinning handling

    private static void spinVisibility(View spinView, int visibility, Button btn, Context context) {

        if (visibility == View.INVISIBLE) {
            btn.setEnabled(true);
            spinView.clearAnimation();
            spinView.setVisibility(visibility);
            btn.setTextColor(context.getResources().getColor(R.color.colorWhite));
        }
        else if(visibility == View.VISIBLE) {
            btn.setEnabled(false);
            btn.setTextColor(context.getResources().getColor(R.color.transparent));
            spinView.setVisibility(visibility);
            spinView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.rotate_indefinitely) );
        }
    }

    // Stop spinning loader, enable movement and launch error dialog

    private static void errorHandling(View spinView, View backgroundView, Button btn, Activity activity, String text) {
        backgroundView.performClick();
        spinVisibility(spinView, View.INVISIBLE, btn , activity);
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        ErrorDialog alert = new ErrorDialog();
        alert.showDialog(activity, text);
    }

    // Action when back arrow is pressed

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_OK);
                finish();
                overridePendingTransition(R.anim.not_movable,R.anim.slide_down);
                return true;
        }

        return false;
    }

    // Action when back navigation button is pressed

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
        overridePendingTransition(R.anim.not_movable, R.anim.slide_down);
    }

}

