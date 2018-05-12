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
    private ImageView fingerRegister, fingerActual, imEstatus;
    private TextView txtMensajes, txtEstatus ;
    public Bitmap fingerRegisterBm;
    private ProgressBar progress, progressSensor;



    public DialogoHuella(@NonNull Context context) {
        super(context);



        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_huellas);

        setCanceledOnTouchOutside(true);
        setCancelable(true);

        findViewById(R.id.btnAceptar).setOnClickListener(this);
        fingerRegister = findViewById(R.id.fingerprint_register);
        fingerActual = findViewById(R.id.fingerprint_actual);
        imEstatus = findViewById(R.id.autentificacion);

       txtMensajes =  findViewById(R.id.fingerprint_description);
        txtEstatus =  findViewById(R.id.fingerprint_status);

        progress =  findViewById(R.id.progress);
        progressSensor =  findViewById(R.id.progresslector);
        fingerprint.scan(context, printHandler, updateHandler);

        findViewById(R.id.btnSensor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    fingerprint.scan(getContext(), printHandler, updateHandler);


            }
        });


    }

    public void setHuella(Bitmap fingerRegisterBm){
        fingerRegister.setImageBitmap(fingerRegisterBm);
        this.fingerRegisterBm = fingerRegisterBm;

    }

    @Override
    public void onClick(View v) {

        fingerprint.turnOffReader();
        dismiss();

    }

    Handler updateHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            int status = msg.getData().getInt("status");
            txtMensajes.setText("");
            switch (status) {
                case Status.INITIALISED:
                    txtMensajes.setText("Configurando lector");

                    break;
                case Status.SCANNER_POWERED_ON:
                    txtMensajes.setText("EL lector esta encendido");

                    progressSensor.setVisibility(View.GONE);
                    break;
                case Status.READY_TO_SCAN:
                    txtMensajes.setText("El lector esta listo para escanear");
                    break;
                case Status.FINGER_DETECTED:
                    txtMensajes.setText("Dedo detectado");
                    break;
                case Status.RECEIVING_IMAGE:
                    txtMensajes.setText("Reciviendo imagen");
                    break;
                case Status.FINGER_LIFTED:
                    txtMensajes.setText("El dedo ha dejado el sensor");
                    break;
                case Status.SCANNER_POWERED_OFF:
                    txtMensajes.setText("El sensor esta apagado, iniciar nuevamente");
                    fingerprint.scan(getContext(), printHandler, updateHandler);

                    break;
                case Status.SUCCESS:
                    txtMensajes.setText("Se capturo la huella");
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


                    txtEstatus.setText("Huella válida");
                    imEstatus.setImageResource(R.drawable.check);
                }
                else{

                    txtEstatus.setText("Huella invalida\nIntente nuevamente");
                    imEstatus.setImageResource(R.drawable.noentry);
                }
                progress.setVisibility(View.INVISIBLE);
                imEstatus.setVisibility(View.VISIBLE);

            }
        });


    }
}
