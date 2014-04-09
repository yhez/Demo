package specular.systems.demo.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import static android.graphics.Typeface.createFromAsset;

public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        ((TextView) findViewById(R.id.text)).setTypeface(createFromAsset(getAssets(), "OpenSans-Light.ttf"));
        ((TextView) findViewById(R.id.company)).setTypeface(createFromAsset(getAssets(), "OpenSans-Light.ttf"));
        mHideHandler.postDelayed(splash, 6000);
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus)
            hide(getWindow());
    }
    Runnable splash = new Runnable() {
        @Override
        public void run() {
            finish();
            startActivity(new Intent(Splash.this, WfDemo.class));
        }
    };

    Handler mHideHandler = new Handler();
    public static void hide(Window w){
        w.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
}
