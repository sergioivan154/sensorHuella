package sagarpa.planetmedia.com.sagarpapp.View.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import sagarpa.planetmedia.com.sagarpapp.Presenter.BasePresenter.BaseActivityContracts;
import sagarpa.planetmedia.com.sagarpapp.R;
import sagarpa.planetmedia.com.sagarpapp.Utility.KeyDictionary;

public class BaseActivity extends AppCompatActivity implements BaseActivityContracts {

    protected Context context;
    protected ProgressDialog progressDialog;
    protected String strTitleDialog;
    protected String strMessageDialog;

    protected static boolean detectApiVersionPlus;
    protected List<String> listPermisssionsPhone;

    protected Dialog dialogConfirm;

    public View.OnClickListener onClickListenerDismiss = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            hideDialogGeneric();
            dialogConfirm = null;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;

        progressDialog = new ProgressDialog(context);
        strTitleDialog = context.getString(R.string.txt_cargando);
        strMessageDialog = context.getString(R.string.txt_espere);
        progressDialog.setTitle(strTitleDialog);
        progressDialog.setMessage(strMessageDialog);
        progressDialog.setCancelable(false);
        showProgressDialog();

    }

    @Override
    protected void onStart() {
        super.onStart();

        detectApiVersionPlus = getVersionApi();



        if (detectApiVersionPlus)
            requestPermission();

        if (!isLocationEnable())
            activeGPS();

    }

    @Override
    protected void onResume() {
        super.onResume();

        if ((progressDialog != null) && progressDialog.isShowing())
            hideProgressDialog();
    }


    @Override
    public void showDialogGeneric(@NotNull String title, @NotNull String message, @NotNull String codeError, @org.jetbrains.annotations.Nullable boolean btnAceptar, boolean btnCancelar, boolean styleDialogError, @org.jetbrains.annotations.Nullable View.OnClickListener clickListenerAceptar, @org.jetbrains.annotations.Nullable View.OnClickListener clickListenerCancelar) {

        try {
            if (dialogConfirm == null) {
                dialogConfirm = new Dialog(context);
                View contaView = ((Activity) context).getLayoutInflater().inflate(R.layout.custom_dialog_general, null);

                TextView txtTitle = contaView.findViewById(R.id.txt_title_dialog);
                TextView txtMessage = contaView.findViewById(R.id.txt_dialog);
                TextView txtCodigoError = contaView.findViewById(R.id.textViewCodigoError);
                txtTitle.setText(title);
                txtMessage.setText(message);

                Button buttonAceptar = contaView.findViewById(R.id.btn_aceptar_dialog);
                Button buttonCancelar = contaView.findViewById(R.id.btn_cancel_dialog);

                dialogConfirm.setCanceledOnTouchOutside(false);
                dialogConfirm.setCancelable(false);

                if (styleDialogError) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        txtTitle.setTextColor(context.getColor(R.color.warning_color));
                        buttonAceptar.setBackground(context.getDrawable(R.drawable.button_redondo_rojo));
                        buttonAceptar.setTextColor(context.getColor(R.color.coblanco));
                    } else {
                        txtTitle.setTextColor(context.getResources().getColor(R.color.warning_color));
                        buttonAceptar.setBackground(ContextCompat.getDrawable(context, R.drawable.button_redondo_rojo));
                        buttonAceptar.setTextColor(context.getResources().getColor(R.color.coblanco));
                    }
                }

                if (codeError != null)
                    txtCodigoError.setText(codeError);
                else
                    txtCodigoError.setVisibility(View.GONE);


                if (clickListenerAceptar == null)
                    buttonAceptar.setOnClickListener(onClickListenerDismiss);
                else
                    buttonAceptar.setOnClickListener(clickListenerAceptar);


                if (btnCancelar) {
                    if (clickListenerCancelar == null)
                        buttonCancelar.setOnClickListener(onClickListenerDismiss);
                    else
                        buttonCancelar.setOnClickListener(clickListenerCancelar);
                } else
                    buttonCancelar.setVisibility(View.GONE);



                dialogConfirm.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogConfirm.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogConfirm.setContentView(contaView);
                dialogConfirm.show();

                DisplayMetrics metrics = context.getResources().getDisplayMetrics();
                float dp = 250f;
                float fpixels = metrics.density * dp;
                int pixels = Math.round(fpixels + 0.5f);
                Window window = dialogConfirm.getWindow();
                window.setLayout(pixels, ConstraintLayout.LayoutParams.WRAP_CONTENT);
            }
        }
        catch (Exception ex){
            Log.d("APP","error en dialogo fragment: "+ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public void hideDialogGeneric() {
        dialogConfirm.dismiss();
    }

    @Override
    public void showProgressDialog() {

        try {

            Log.e("Contenido del progress", "EL progress contiene: " + progressDialog);
            if (progressDialog != null) {
                if (!progressDialog.isShowing()) {
                    progressDialog.show();
                }
            } else {
                crateProgressDialog();
            }
        }
        catch (Exception ex){
            Log.e("error dialog", ex.getMessage());
        }

    }

    @Override
    public void hideProgressDialog() {

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();

        }

    }

    @Override
    public boolean getVersionApi() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            return true;

        return false;
    }

    @Override
    public void requestPermission() {

        listPermisssionsPhone = new ArrayList<>();

        for(int contPermissions = 0; contPermissions < KeyDictionary.GROUP_PERMISSION.length; contPermissions++)
        {
            if(ContextCompat.checkSelfPermission(context, KeyDictionary.GROUP_PERMISSION[contPermissions]) != PackageManager.PERMISSION_GRANTED)
                listPermisssionsPhone.add(KeyDictionary.GROUP_PERMISSION[contPermissions]);
        }

        if (!listPermisssionsPhone.isEmpty())
            ActivityCompat.requestPermissions((Activity) context, listPermisssionsPhone.toArray(new String[listPermisssionsPhone.size()]),KeyDictionary.PERMISSION_REQUEST_READ_STATUS_SUCCESS);
        else
        {

        }

    }

    @Override
    public void crateProgressDialog() {
        progressDialog = new ProgressDialog(context);
        strTitleDialog = context.getString(R.string.txt_cargando);
        strMessageDialog = context.getString(R.string.txt_espere);
        progressDialog.setTitle(strTitleDialog);
        progressDialog.setMessage(strMessageDialog);
        progressDialog.setCancelable(false);

        Log.e("Creado Progress", "Progress creado");

        showProgressDialog();
    }

    public boolean isLocationEnable() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public void activeGPS() {

        try {

            showDialogGeneric(getResources().getString(R.string.txt_geolocalizacion_menu_dialog), getResources().getString(R.string.txt_geolocalizacion_menu_text_dialog), "permissions", true, false, true,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            finish();
                        }
                    }, null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
