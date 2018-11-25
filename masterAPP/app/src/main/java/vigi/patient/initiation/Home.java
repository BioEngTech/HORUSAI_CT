package vigi.patient.initiation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.transition.ChangeBounds;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import vigi.patient.R;

@SuppressWarnings("FieldCanBeLocal")
public class Home extends AppCompatActivity {

    private static final int REQUESTCODE = 1;

    private ViewPager slideView;
    private LinearLayout dotLayout;
    private SliderAdapter sliderAdapt;
    private TextView[] dots;
    private Button loginBtn;
    private Button registerBtn;
    private Animation downToUp;
    private Animation rightToLeft;
    private ProgressBar circleView;
    private ValueAnimator animator;
    private LinearLayout background;
    private LinearLayout buttonsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Open layout

        setContentView(R.layout.initiation_home);

        // Objects

        slideView = findViewById(R.id.initiationHome_slideView);
        dotLayout = findViewById(R.id.initiationHome_dotLayout);
        loginBtn = findViewById(R.id.initiationHome_loginButton);
        registerBtn = findViewById(R.id.initiationHome_registerButton);
        circleView = findViewById(R.id.initiationHome_progressCircle);
        background = findViewById(R.id.initiationHome);
        buttonsLayout = findViewById(R.id.initiationHome_buttonsLayout);

        sliderAdapt = new SliderAdapter(Home.this);

        slideView.setAdapter(sliderAdapt);

        slideView.addOnPageChangeListener(viewListener);

        addDotsIndicator(0);

        // Set up animations

        ChangeBounds bounds = new ChangeBounds();
        bounds.setDuration(800);
        getWindow().setSharedElementEnterTransition(bounds);

        downToUp = AnimationUtils.loadAnimation(this,R.anim.slide_up);
        downToUp.setDuration(800);

        rightToLeft = AnimationUtils.loadAnimation(this,R.anim.slide_in_left);
        rightToLeft.setDuration(1000);

        buttonsLayout.setAnimation(downToUp);
        dotLayout.setAnimation(downToUp);
        slideView.setAnimation(rightToLeft);

        animator = ValueAnimator.ofInt(0, circleView.getMax());
        animator.setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation){
                circleView.setProgress((Integer)animation.getAnimatedValue());
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {});
        animator.start();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent loginIntent = new Intent(Home.this,Login.class);
                startActivityForResult(loginIntent,REQUESTCODE);
                overridePendingTransition(R.anim.slide_up,R.anim.not_movable);
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent registerIntent = new Intent(Home.this,Register.class);
                startActivityForResult(registerIntent,REQUESTCODE);
                overridePendingTransition(R.anim.slide_up,R.anim.not_movable);
            }
        });

        background.requestFocus();

    }

    private void addDotsIndicator(int position){

        dots = new TextView[3];
        dotLayout.removeAllViews();

        for(int i = 0; i< dots.length; i++){

            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(24);
            dots[i].setTextColor(getResources().getColor(R.color.colorBlueSoft));

            dotLayout.addView(dots[i]);

        }

        if(dots.length > 0){

            dots[position].setTextColor(getResources().getColor(R.color.colorMain));
        }

    }

    private ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            addDotsIndicator(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUESTCODE) {

            if (resultCode == RESULT_OK) {

                animator.start();
            }
        }
    }
}
