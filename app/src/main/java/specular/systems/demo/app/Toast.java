package specular.systems.demo.app;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

import static android.graphics.Typeface.createFromAsset;


public class Toast extends Activity {
    TextView v;
    static long tTime;
    static String msg;
    @Override
    public void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.toast);
        v = (TextView)findViewById(R.id.text);
        v.setText(msg);
        v.setTypeface(createFromAsset(getAssets(), "OpenSans-Light.ttf"));
        v.animate().setDuration(500).alpha(1).start();
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                synchronized (this) {
                    try {
                        wait(tTime);
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
    @Override
    public boolean onTouchEvent(MotionEvent event){
        finish();
        return super.onTouchEvent(event);
    }
    public static void toast(String message,int time,Activity a){
        tTime = time*1000;
        msg = message;
        Intent i = new Intent(a,Toast.class);
        a.startActivity(i);
    }
}
