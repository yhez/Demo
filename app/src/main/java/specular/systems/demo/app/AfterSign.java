package specular.systems.demo.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class AfterSign extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        char c = getIntent().getCharExtra("user",'x');
        setTitle(getTitle() + " - Hello " + (c == 'U' ? "John" : "Lisa"));
        setContentView(R.layout.after_sign);
        findViewById(R.id.account).setBackgroundResource(c == 'U' ? R.drawable.jhon : R.drawable.lisa);
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus)
            Splash.hide(getWindow());
    }
    public void onClick(View v){
        Intent i = new Intent(AfterSign.this,Deposit.class);
        i.putExtra("user",getIntent().getCharExtra("user",'x'));
        startActivity(i);
    }
}
