package specular.systems.demo.app;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import static android.graphics.Typeface.createFromAsset;


public class ToastCostume extends Activity {
    View v;
    @Override
    public void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.toast);
        v = findViewById(R.id.text);
        ((TextView)v).setTypeface(createFromAsset(getAssets(), "OpenSans-Light.ttf"));
        v.animate().setDuration(500).alpha(1).start();
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                synchronized (this) {
                    try {
                        wait(6000);
                    } catch (Exception ignore) {
                        ignore.printStackTrace();
                    }
                }
                return null;
            }
            @Override
            protected void onPostExecute(Void i){
                v.animate().setDuration(500).alpha(0).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }).start();
            }
        }.execute();
    }
}
