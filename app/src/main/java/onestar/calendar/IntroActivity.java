package onestar.calendar;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class IntroActivity extends Activity {
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        handler = new Handler();
        handler.postDelayed(runnable, 1300);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Intent MainActivity = new Intent(IntroActivity.this, CalendarActivity.class);
            startActivity(MainActivity);
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    };
}
