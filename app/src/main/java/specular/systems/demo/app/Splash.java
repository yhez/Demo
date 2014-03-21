package specular.systems.demo.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import static android.graphics.Typeface.createFromAsset;

public class Splash extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        ((TextView) findViewById(R.id.text)).setTypeface(createFromAsset(getAssets(), "OpenSans-Light.ttf"));
        ((TextView) findViewById(R.id.company)).setTypeface(createFromAsset(getAssets(), "OpenSans-Light.ttf"));
        set(findViewById(R.id.fullscreen_content));
        mHideHandler.postDelayed(splash, 6000);
    }

    Runnable splash = new Runnable() {
        @Override
        public void run() {
            finish();
            startActivity(new Intent(Splash.this, WfDemo.class));
        }
    };

    Handler mHideHandler = new Handler();
}
