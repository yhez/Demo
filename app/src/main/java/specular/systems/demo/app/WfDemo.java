package specular.systems.demo.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


public class WfDemo extends Activity {

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus)
            Splash.hide(getWindow());
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wf_demo);
    }
    @Override
    public void onResume() {
        super.onResume();
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter != null && nfcAdapter.isEnabled()) {
            PendingIntent pi = PendingIntent.getActivity(this, 0,
                    new Intent(this, getClass())
                            .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
            IntentFilter tagDetected = new IntentFilter(
                    NfcAdapter.ACTION_TAG_DISCOVERED);
            IntentFilter[] filters = new IntentFilter[]{tagDetected};
            nfcAdapter.enableForegroundDispatch(this, pi, filters, null);
        }
    }
    char nfcContent;

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
            nfcContent = new String(pvk.getPayload()).charAt(3);
            keyDialog kd = (keyDialog)getFragmentManager().findFragmentByTag("gg");
            if(nfcContent=='U'||nfcContent=='Z'){
                if(kd==null){
                    new keyDialog().show(getFragmentManager(),"gg");
                }
            }else{
                if(kd!=null&&kd.getDialog()!=null)
                    kd.getDialog().cancel();
                Toast.toast(getString(R.string.no_valid_user), 2, this);
            }
        }
    }
    public void signOn(View v) {
        Toast.toast(getString(R.string.sign_no_nfc), 2, this);
    }

    public class keyDialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(WfDemo.this);
            final LockPatternView lpv = new LockPatternView(getActivity());
            lpv.setOnPatternListener(new LockPatternView.OnPatternListener() {
                @Override
                public void onPatternStart() {

                }

                @Override
                public void onPatternCleared() {

                }

                @Override
                public void onPatternCellAdded(List<LockPatternView.Cell> pattern) {

                }

                @Override
                public void onPatternDetected(List<LockPatternView.Cell> pattern) {
                    if(checkDetails(pattern)){
                        WfDemo.this.finish();
                        Intent i = new Intent(WfDemo.this,AfterSign.class);
                        i.putExtra("user",nfcContent);
                        startActivity(i);
                    }else {
                        notCorrect(lpv);
                    }
                }
            });
            builder.setView(lpv);
            return builder.create();
        }

        void notCorrect(final LockPatternView lpv) {
            lpv.setDisplayMode(LockPatternView.DisplayMode.Wrong);
            lpv.invalidate();
            lpv.postDelayed(new Runnable() {
                @Override
                public void run() {
                    lpv.clearPattern();
                }
            }, 2000);
        }
        boolean checkDetails(List<LockPatternView.Cell> pattern){
            if (pattern.size() != 10) {
                return false;
            }
            ArrayList<LockPatternView.Cell> password = new ArrayList<LockPatternView.Cell>(10);
            if(nfcContent=='Z') {
                //Z
                password.add(LockPatternView.Cell.of(0, 0));
                password.add(LockPatternView.Cell.of(0, 1));
                password.add(LockPatternView.Cell.of(0, 2));
                password.add(LockPatternView.Cell.of(0, 3));
                password.add(LockPatternView.Cell.of(1, 2));
                password.add(LockPatternView.Cell.of(2, 1));
                password.add(LockPatternView.Cell.of(3, 0));
                password.add(LockPatternView.Cell.of(3, 1));
                password.add(LockPatternView.Cell.of(3, 2));
                password.add(LockPatternView.Cell.of(3, 3));
            }else {
                //U
                password.add(LockPatternView.Cell.of(0, 0));
                password.add(LockPatternView.Cell.of(1, 0));
                password.add(LockPatternView.Cell.of(2, 0));
                password.add(LockPatternView.Cell.of(3, 0));
                password.add(LockPatternView.Cell.of(3, 1));
                password.add(LockPatternView.Cell.of(3, 2));
                password.add(LockPatternView.Cell.of(3, 3));
                password.add(LockPatternView.Cell.of(2, 3));
                password.add(LockPatternView.Cell.of(1, 3));
                password.add(LockPatternView.Cell.of(0, 3));
            }
            for (int a = 0; a < password.size(); a++) {
                if (!password.get(a).equals(pattern.get(a))) {
                    return false;
                }
            }
            return true;
        }
    }

}
