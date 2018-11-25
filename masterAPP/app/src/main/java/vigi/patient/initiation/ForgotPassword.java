package vigi.patient.initiation;

import android.app.Activity;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

import vigi.patient.R;
import vigi.patient.utils.exceptions.FirebaseException;
import vigi.patient.utils.InternetCheck;

@SuppressWarnings("FieldCanBeLocal")
public class ForgotPassword extends AppCompatActivity implements View.OnClickListener,TextView.OnEditorActionListener,TextView.OnKeyListener{

    private static String TAG = "forgotPasswordClass";

    private ProgressBar fabProgressCircle;
    private FloatingActionButton continueBtn;
    private EditText emailText;
    private boolean error=false;
    private Toolbar myToolbar;
    private TextView internet;
    private RelativeLayout background;
    private TextInputLayout emailInput;
    private String errorCode;
    private String errorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Open layout

        setContentView(R.layout.initiation_forgot_password);

        // Objects

        continueBtn = findViewById(R.id.initiationForgotPassword_continueButton);
        emailText = findViewById(R.id.initiationForgotPassword_email);
        fabProgressCircle = findViewById(R.id.initiationForgotPassword_progressCircle);
        internet = findViewById(R.id.initiationForgotPassword_internet);
        background = findViewById(R.id.initiationForgotPassword);
        emailInput = findViewById(R.id.initiationForgotPassword_emailInputLayout);
        myToolbar = findViewById(R.id.initiationForgotPassword_toolbar);

        // Customize action bar / toolbar

        setSupportActionBar(myToolbar);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayShowTitleEnabled(false);
            supportActionBar.setHomeButtonEnabled(true);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Make continue button respond to a click and disable it at beginning

        continueBtn.setOnClickListener(this);
        continueBtn.setEnabled(false);

        // Make editText respond to enter

        emailText.addTextChangedListener(generalTextWatcher);
        emailText.setOnEditorActionListener(this);
        emailText.setOnKeyListener(this);

        emailText.hasFocus();

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event){

        // Try to continue when enter is pressed in email text

        if (actionId == EditorInfo.IME_ACTION_GO) {

            if (continueBtn.isEnabled()){

                continueBtn.performClick();

            }

            return true;
        }
        return false;

    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        // Try to continue when enter is pressed in email text from the enter button

        if (keyCode == KeyEvent.KEYCODE_ENTER) {

            switch (event.getAction()){

                case KeyEvent.ACTION_DOWN:

                    if (continueBtn.isEnabled()){

                        continueBtn.performClick();

                    }

                    break;

                case KeyEvent.ACTION_UP:

                    // nothing

                    break;

            }

            return true;

        }

        return false;
    }

    // Implement TextWatcher for removing errors and enabling continue button

    private final TextWatcher generalTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (error) {

                emailInput.setError(null);

                error=false;

            }

        }

        @Override
        public void afterTextChanged(Editable s) {

            if (emailText.getText().length()!=0){
                continueBtn.setEnabled(true);
                continueBtn.setBackgroundTintList(ForgotPassword.this.getResources().getColorStateList(R.color.colorMain));
            }
            else{

                continueBtn.setEnabled(false);
                continueBtn.setBackgroundTintList(ForgotPassword.this.getResources().getColorStateList(R.color.colorGrayLight));

            }

        }
    };

    // Implement functionalities on click

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.initiationForgotPassword_continueButton) {  // Send e-mail to user

            // Internet check

            InternetCheck checkNet = new InternetCheck();

            if (!checkNet.isInternetAvailable(ForgotPassword.this)){

                if (internet.getVisibility()!=View.INVISIBLE) return;

                else {

                    slideUp(internet);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            slideDown(internet);
                        }
                    }, 3500);

                    return;

                }

            }

            // Check if emailText is still wrong

            if(error){

                emailInput.requestFocus();

                return;

            }

            // Disable other views and start spinning loading

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            continueBtn.setImageResource(0);
            fabProgressCircle.setVisibility(View.VISIBLE);


            String email = emailText.getText().toString().trim();

            // Initialize FirebaseException auth

            FirebaseAuth authfire = FirebaseAuth.getInstance();

            authfire.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {

                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {
                        finish();
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                    } else {

                        try {

                            Exception firebaseAuthException = task.getException();

                            if (firebaseAuthException != null) {
                                errorCode = ((FirebaseAuthException) firebaseAuthException).getErrorCode();

                                FirebaseException exceptionThrowed = new FirebaseException();

                                errorText = exceptionThrowed.exceptionType(errorCode);

                                error = true;

                                emailInput.setError(errorText);
                            }

                            loadingHandling(fabProgressCircle, emailText, continueBtn, ForgotPassword.this);


                        } catch (ClassCastException e) {

                            // Internet problem

                            if (internet.getVisibility() != View.INVISIBLE) {
                                internet.setVisibility(View.VISIBLE);
                            } else {

                                slideUp(internet);
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        slideDown(internet);
                                    }
                                }, 3500);

                            }

                            loadingHandling(fabProgressCircle, emailText, continueBtn, ForgotPassword.this);

                        }

                    }
                }
            });

        }

    }

    // Stop spinning loader, enable movement

    private static void loadingHandling(View spinView, View textView, FloatingActionButton btn, Activity activity) {

        spinView.setVisibility(View.GONE);
        btn.setImageResource(R.drawable.icon_continue);
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        textView.requestFocus();

    }

    // Slide the view from below itself to the current position

    private void slideUp(View view){

        view.setVisibility(View.VISIBLE);

        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);

    }

    // Slide the view from its current position to below itself

    private void slideDown(View view){

        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                view.getHeight()); // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.INVISIBLE);
    }

    // Action when back arrow is pressed

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                return true;
        }

        return false;
    }

}


/*

    // Make activity finish if change password is successfully completed


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUESTCODE) {

            if (resultCode == RESULT_OK) {

                setResult(RESULT_OK, null);

                this.finish();
            }
        }
    }

    // send email

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... params) {

                final String from = getResources().getString(R.string.vigi_email);
                final String password = getResources().getString(R.string.vigi_password);
                final String to = emailText.getText().toString();

                if (!generalMailValidator.isEmailValid(to)) throw new Exception();

                mailSender mailClass = new mailSender();

                final boolean success = mailClass.send(from, password, to);

                if (!success) throw new NoNetworkException("INTERNET CONNECTION ERROR");

                loadingHandling(fabProgressCircle, emailText, continueBtn, ForgotPassword.this);

                // Launch change password activity

                Intent sendIntent = new Intent(ForgotPassword.this, changePassword.class);
                sendIntent.putExtra("Code", mailClass.getCode());
                startActivityForResult(sendIntent,REQUESTCODE);


                return "";
                }

        @Override
        protected void onPostExecute(String result) {

            loadingHandling(fabProgressCircle, emailText, continueBtn, ForgotPassword.this);
        }


    }

*/

