package sagarpa.planetmedia.com.sagarpapp.View.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import asia.kanopi.fingerscan.Fingerprint;
import asia.kanopi.fingerscan.Status;
import sagarpa.planetmedia.com.sagarpapp.R;
import sagarpa.planetmedia.com.sagarpapp.Utility.Biometrico.Biometrico;
import sagarpa.planetmedia.com.sagarpapp.Utility.Biometrico.BiometricoResponse;

public class DialogoHuella extends Dialog implements View.OnClickListener {

    private Fingerprint fingerprint = new Fingerprint();
    private ImageView fingerRegister, fingerActual;
    private TextView txtMensajes, txtEstatus ;
    private Bitmap fingerRegisterBm;
    private ProgressBar progress, progressSensor;



    public DialogoHuella(@NonNull Context context, Bitmap fingerRegisterBm) {
        super(context);



        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_huellas);

        setCanceledOnTouchOutside(true);
        setCancelable(true);

        findViewById(R.id.btnAceptar).setOnClickListener(this);
        fingerRegister = findViewById(R.id.fingerprint_register);
        fingerActual = findViewById(R.id.fingerprint_actual);

       txtMensajes =  findViewById(R.id.fingerprint_description);
        txtEstatus =  findViewById(R.id.fingerprint_status);

        progress =  findViewById(R.id.progress);
        progressSensor =  findViewById(R.id.progresslector);

        fingerRegister.setImageBitmap(fingerRegisterBm);
        fingerprint.scan(context, printHandler, updateHandler);
        this.fingerRegisterBm = fingerRegisterBm;

        findViewById(R.id.btnSensor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(progressSensor.getVisibility() == View.GONE) {

                    fingerprint.scan(getContext(), printHandler, updateHandler);

                }
            }
        });


    }

    @Override
    public void onClick(View v) {

        fingerprint.turnOffReader();
        hide();

    }

    Handler updateHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            int status = msg.getData().getInt("status");
            txtMensajes.setText("");
            switch (status) {
                case Status.INITIALISED:
                    txtMensajes.setText("Setting up reader");

                    break;
                case Status.SCANNER_POWERED_ON:
                    txtMensajes.setText("Reader powered on");

                    progressSensor.setVisibility(View.GONE);
                    break;
                case Status.READY_TO_SCAN:
                    txtMensajes.setText("Ready to scan finger");
                    break;
                case Status.FINGER_DETECTED:
                    txtMensajes.setText("Finger detected");
                    break;
                case Status.RECEIVING_IMAGE:
                    txtMensajes.setText("Receiving image");
                    break;
                case Status.FINGER_LIFTED:
                    txtMensajes.setText("Finger has been lifted off reader");
                    break;
                case Status.SCANNER_POWERED_OFF:
                    txtMensajes.setText("Reader is off");
                    fingerprint.scan(getContext(), printHandler, updateHandler);

                    break;
                case Status.SUCCESS:
                    txtMensajes.setText("Fingerprint successfully captured");
                    break;
                case Status.ERROR:
                    txtMensajes.setText("Error");
                    txtMensajes.setText(msg.getData().getString("errorMessage"));
                    break;
                default:
                    txtMensajes.setText(String.valueOf(status));
                    txtMensajes.setText(msg.getData().getString("errorMessage"));
                    break;

            }
        }
    };

    Handler printHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            byte[] image;
            String errorMessage = "empty";
            int status = msg.getData().getInt("status");
            Intent intent = new Intent();
            intent.putExtra("status", status);
            if (status == Status.SUCCESS) {
                image = msg.getData().getByteArray("img");

                Bitmap bm = BitmapFactory.decodeByteArray(image, 0, image.length);
                fingerActual.setImageBitmap(bm);

                validar(fingerRegisterBm, bm);
            } else {
                errorMessage = msg.getData().getString("errorMessage");

            }

        }
    };

    public void validar(Bitmap fingerRegister, Bitmap fingerValidacion) {

        Biometrico bio = new Biometrico(getContext());

        progress.setVisibility(View.VISIBLE);


        bio.compararHuellas(fingerRegister, fingerValidacion, new BiometricoResponse() {
            @Override
            public void response(boolean respuesta, int compatibilidad) {
                if(respuesta){

                    txtEstatus.setText("eres autentico al "+ compatibilidad +":D");
                }
                else{

                    txtEstatus.setText("No eres el usario "+ compatibilidad +":'(");
                }
                progress.setVisibility(View.INVISIBLE);

            }
        });


    }
}
