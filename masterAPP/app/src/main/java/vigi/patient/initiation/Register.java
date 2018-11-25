package vigi.patient.initiation;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

import java.util.Calendar;

import vigi.patient.R;
import vigi.patient.user.Main;
import vigi.patient.utils.ErrorDialog;
import vigi.patient.utils.exceptions.FirebaseException;
import vigi.patient.utils.proxy.NullProxy;

@SuppressWarnings("FieldCanBeLocal")
public class Register extends AppCompatActivity implements View.OnClickListener,View.OnFocusChangeListener,TextView.OnEditorActionListener,TextView.OnKeyListener{

    private static String TAG = "loginClass";

    private EditText passwordText;
    private EditText emailText;
    private FirebaseAuth authfire;
    private String errorText;
    private Button registerBtn;
    private ImageView spin;
    private TextView termsAndConditions;
    private ScrollView scrollView;
    private Toolbar myToolbar;
    private RadioGroup genderGroup;
    private String gender;
    private EditText birthday;
    private EditText name;
    private EditText phone;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private String errorCode = "";
    private LinearLayout background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Open layout

        setContentView(R.layout.initiation_register);

        // Objects

        emailText = findViewById(R.id.initiationRegister_email);
        passwordText = findViewById(R.id.initiationRegister_Password);
        name = findViewById(R.id.initiationRegister_name);
        phone = findViewById(R.id.initiationRegister_phoneNumber);
        registerBtn = findViewById(R.id.initiationRegister_registerButton);
        spin = findViewById(R.id.initiationRegister_spinner);
        termsAndConditions = findViewById(R.id.initiationRegister_termsAndConditions);
        scrollView = findViewById(R.id.initiationRegister_scrollview);
        genderGroup = findViewById(R.id.initiationRegister_gender);
        birthday = findViewById(R.id.initiationRegister_birthday);
        myToolbar = findViewById(R.id.initiationRegister_toolbar);
        background = findViewById(R.id.initiationRegister_background);

        //remove scrollable indicator

        scrollView.setVerticalScrollBarEnabled(false);

        // Customize action bar / toolbar

        setSupportActionBar(myToolbar);

        ActionBar actionBar = NullProxy.createProxy(getSupportActionBar());
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Make buttons and views respond to a click

        registerBtn.setOnClickListener(this);

        // Make layout respond to focus

        name.setOnFocusChangeListener(this);
        phone.setOnFocusChangeListener(this);
        emailText.setOnFocusChangeListener(this);
        passwordText.setOnFocusChangeListener(this);

        // Make editText respond to enter

        genderGroup.setOnKeyListener(this);
        phone.setOnKeyListener(this);
        passwordText.setOnEditorActionListener(this);
        passwordText.setOnKeyListener(this);

        // Setup input type of password programmatically to avoid getting bold

        passwordText.setTransformationMethod(new PasswordTransformationMethod());

        // Activate Radio Group

        genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton genderType = group.findViewById(checkedId);
                gender = genderType.getText().toString();
            }
        });

        birthday.setKeyListener(null);

        // Activate Birthday Dialog

        birthday.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(!birthday.hasFocus()){
                    return;
                }

                Calendar cal = Calendar.getInstance();

                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(Register.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth, dateSetListener,year,month,day);

                long hundred_ten_years = 1000*60*60*24*39600L;

                dialog.getDatePicker().setMinDate(cal.getTimeInMillis() - hundred_ten_years);

                dialog.getDatePicker().setMaxDate(cal.getTimeInMillis());

                Window window = NullProxy.createProxy(dialog.getWindow());
                window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                dialog.setCancelable(false);

                dialog.show();

                background.requestFocus();

            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                month = month + 1;

                String date = dayOfMonth + "/" + month + "/" + year;

                birthday.setText(date);

            }
        };

        // Set an underline to the terms and conditions

        String terms_conditions_text = "Terms and Conditions";

        SpannableString ss = new SpannableString(terms_conditions_text);


        ClickableSpan click_terms_conditions = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {

                Toast.makeText(Register.this,"Terms and Conditions are still missing.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Register.this.getResources().getColor(R.color.colorWhite));
            }
        };

        ss.setSpan(click_terms_conditions,0,terms_conditions_text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        termsAndConditions.setText(ss);
        termsAndConditions.setMovementMethod(LinkMovementMethod.getInstance());

        }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event){

            // Try to Register when enter is pressed in password text

            if (actionId == EditorInfo.IME_ACTION_GO) {

                registerBtn.performClick();

            return true;
        }
        return false;

    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        // Try to Register when enter is pressed in password text from the enter button on

        if (keyCode == KeyEvent.KEYCODE_ENTER) {

            switch (event.getAction()){

                case KeyEvent.ACTION_DOWN:

                    if (phone.hasFocus() || genderGroup.hasFocus() ){

                        break;

                    }
                    else{

                        registerBtn.performClick();

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

    // Implement click functionalities

    @Override
    public void onClick(View v) {

            if(v.getId()== registerBtn.getId()) { // Try to Log In

                // Disable movement and start spinning loader

                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                spinVisibility(spin,View.VISIBLE, registerBtn,Register.this);

                // Check if fields are filled

                if (name.getText().toString().isEmpty() || phone.getText().toString().isEmpty() || birthday.getText().toString().isEmpty()){

                    errorHandling(spin, background,registerBtn,Register.this,"Please enter a valid sign up, all fields are required.");

                    return;
                }

                // launch Register function

                registerAttempt();

            }
    }

    private void registerAttempt() {

        try {

            // Initialize FirebaseException auth

            authfire = FirebaseAuth.getInstance();

            // Get EditText Strings

            String email = emailText.getText().toString().trim();
            String password = passwordText.getText().toString().trim();

            authfire.createUserWithEmailAndPassword(email,password).addOnCompleteListener(Register.this,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                    // if task not successful

                    if (task.isSuccessful()) {

                        Intent launchUserIntent = new Intent(Register.this,Main.class);
                        launchUserIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(launchUserIntent);
                        finish();

                    } else {

                        try{

                            FirebaseAuthException firebaseAuthException = NullProxy.createProxy(((FirebaseAuthException) task.getException()));
                            errorCode = firebaseAuthException.getErrorCode();

                            FirebaseException exceptionThrowed = new FirebaseException();

                            errorText = exceptionThrowed.exceptionType(errorCode);

                            errorHandling(spin, background,registerBtn,Register.this,errorText);

                        } catch(ClassCastException e){

                            errorHandling(spin, background,registerBtn,Register.this,"Internet connection is not available.");

                        }

                    }

                }

            });

        }catch (IllegalArgumentException e){

            errorHandling(spin, background,registerBtn,Register.this,"Please enter a valid sign up, all fields are required.");

        }

    }

    // Focus handle

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        if (!hasFocus && !name.hasFocus() && !phone.hasFocus() && !passwordText.hasFocus() && !emailText.hasFocus()) {
            InputMethodManager input = NullProxy.createProxy((InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE));
            input.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }

    }

    // Spinning handling

    private static void spinVisibility(View spinView, int visibility, Button btn, Context context) {

        if (visibility == View.INVISIBLE) {
            btn.setEnabled(true); // SPINNER ONLY APPEARS ON THE FRAME LAYOUT IF "btn" IS SET TO DISABLE
            spinView.clearAnimation();
            spinView.setVisibility(visibility);
            btn.setTextColor(context.getResources().getColor(R.color.colorWhite));
        }
        else if(visibility == View.VISIBLE) {
            btn.setEnabled(false); // SPINNER ONLY APPEARS ON THE FRAME LAYOUT IF "btn" IS SET TO DISABLE
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

