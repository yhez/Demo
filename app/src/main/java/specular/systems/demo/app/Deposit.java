package specular.systems.demo.app;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.ImageView;
import android.widget.Toast;


public class Deposit extends BaseActivity {
    char c;
    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);
        iv = (ImageView) findViewById(R.id.sig);
        c = getIntent().getCharExtra("user", 'x');
        setTitle(getTitle() + " - Hello " + (c == 'U' ? "John" : "Lisa"));
        set(findViewById(R.id.fullscreen_content));
        Toast.makeText(this, "Tab NFC to verify action", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(Deposit.this,"Action successfully completed",Toast.LENGTH_LONG).show();
                    }
                },6000);
            } else {
                Toast.makeText(this, "The NFC doesn't match", Toast.LENGTH_LONG).show();
            }
        }
    }
}
