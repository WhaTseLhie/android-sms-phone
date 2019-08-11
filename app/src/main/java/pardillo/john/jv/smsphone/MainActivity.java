package pardillo.john.jv.smsphone;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final static int REQUEST_SMS = 100;

    private Intent sendSMS;
    private EditText txtPhone, txtMessage;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.txtPhone = this.findViewById(R.id.editText);
        this.txtMessage = this.findViewById(R.id.editText2);
        this.btnSubmit = this.findViewById(R.id.button);

        this.btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String phone = this.txtPhone.getText().toString();
        String message = this.txtMessage.getText().toString();
        sendMessage(phone, message);
    }

    public void sendMessage(String phone, String message) {
        sendSMS = new Intent(Intent.ACTION_VIEW);
        sendSMS.putExtra("address", phone);
        sendSMS.putExtra("sms_body", message);
        sendSMS.setType("vnd.android-dir/mms-sms");

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{
                        Manifest.permission.SEND_SMS}, REQUEST_SMS);
            }
        } else {
            this.startActivity(sendSMS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case REQUEST_SMS:
                if(ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                    final Dialog dialog = new Dialog(this);
                    dialog.setContentView(R.layout.layout_permission);

                    TextView t1 = dialog.findViewById(R.id.textView);
                    TextView t2 = dialog.findViewById(R.id.textView2);
                    t1.setText("Request Permissions");
                    t2.setText("Please allow permissions if you want this application to perform the task.");

                    Button dialogButton = dialog.findViewById(R.id.button);
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                } else {
                    this.startActivity(sendSMS);
                }

                break;
        }
    }
}
