package specular.systems.demo.app;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;


public class Deposit extends Activity {
    char c;
    ImageView iv;
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus)
            Splash.hide(getWindow());
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);
        iv = (ImageView) findViewById(R.id.sig);
        c = getIntent().getCharExtra("user", 'x');
        setTitle(getTitle() + " - Hello " + (c == 'U' ? "John" : "Lisa"));
        findViewById(R.id.fullscreen_content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.toast(getString(R.string.tab_to_aprove), 3, Deposit.this);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter != null && nfcAdapter.isEnabled()) {
            PendingIntent pi = PendingIntent.getActivity(this, 0,
                    new Intent(this, getClass())
                            .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0
            );
            IntentFilter tagDetected = new IntentFilter(
                    NfcAdapter.ACTION_TAG_DISCOVERED);
            IntentFilter[] filters = new IntentFilter[]{tagDetected};
            nfcAdapter.enableForegroundDispatch(this, pi, filters, null);
        }
    }

    @Override
    public void onNewIntent(Intent i) {
        if (i.getAction() != null && i.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
            Parcelable raw[] = i.getParcelableArrayExtra(
                    NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (raw == null)
                return;
            NdefMessage msg = (NdefMessage) raw[0];
            NdefRecord pvk = msg.getRecords()[0];
            if (pvk == null)
                return;
            if (c == new String(pvk.getPayload()).charAt(3)) {
                iv.animate().alpha(1).setDuration(1000).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        iv.setImageResource(R.drawable.sign);
                        ((AnimationDrawable) iv.getDrawable()).start();
                    }
                }).start();
                iv.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ((AnimationDrawable)iv.getDrawable()).stop();
                        iv.animate().alpha(0).setDuration(1000).start();
                        Toast.toast(getString(R.string.action_done), 6, Deposit.this);
                    }
                },3000);
            } else {
                Toast.toast(getString(R.string.tab_doesnt_mach), 3, this);
            }
        }
    }
}
