package sagarpa.planetmedia.com.sagarpapp.View.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import sagarpa.planetmedia.com.sagarpapp.Presenter.BasePresenter.BaseFragmentContracts;
import sagarpa.planetmedia.com.sagarpapp.R;

import static android.content.Context.LOCATION_SERVICE;

public class BaseFragment extends Fragment implements BaseFragmentContracts {

    protected Context context;
    protected ProgressDialog progressDialog;
    protected String titleDialog;
    protected String messageDialog;
    protected Dialog dialogConfirm;

    protected View.OnClickListener onClickListenerDismiss = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            hideDialogGeneric();
            dialogConfirm = null;
        }
    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getActivity();
        CreateProgressDialog();

    }

    @Override
    public void onStart() {
        super.onStart();

        if (!getVersionApi()) {
            checkGPSEnable();
        }

    }

    @Override
    public void onPause() {
        super.onPause();

        if ((progressDialog != null) && progressDialog.isShowing())
            hideProgressDialog();

        progressDialog = null;

    }

    @Override
    public void showProgressDialog() {

        try {
            if (progressDialog != null) {
                if (!progressDialog.isShowing()) {
                    progressDialog.show();
                }
            } else {
                Log.e("ProgressDialog", "ProgressDialog is null");
                CreateProgressDialog();
                if (!progressDialog.isShowing()) {
                    progressDialog.show();
                }
            }
        } catch (Exception ex) {
            Log.e("error dialog", ex.getMessage());
        }

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
                        buttonAceptar.setBackground(getContext().getDrawable(R.drawable.button_redondo_rojo));
                        buttonAceptar.setTextColor(getContext().getColor(R.color.coblanco));
                    } else {
                        txtTitle.setTextColor(getContext().getResources().getColor(R.color.warning_color));
                        buttonAceptar.setBackground(ContextCompat.getDrawable(context, R.drawable.button_redondo_rojo));
                        buttonAceptar.setTextColor(getContext().getResources().getColor(R.color.coblanco));
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
                float dp = 500f;
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
    public void hideProgressDialog() {

        if (progressDialog!= null && progressDialog.isShowing()) {
            progressDialog.dismiss();

        }

    }

    @Override
    public boolean getVersionApi() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return true;
        } else
            return false;
    }

    @Override
    public void checkGPSEnable() {

        LocationManager locationManager =  (LocationManager) context.getSystemService(LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

            showDialogGeneric(context.getString(R.string.txt_permission_gps_title),
                    context.getString(R.string.txt_permission_gps_message),
                    null,
                    true,
                    true,
                    false, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            checkGPSEnable();
                        }
                    });
        }

    }

    public void CreateProgressDialog(){
        progressDialog = new ProgressDialog(context);
        titleDialog = context.getString(R.string.txt_cargando);
        messageDialog = context.getString(R.string.txt_espere);
        progressDialog.setTitle(titleDialog);
        progressDialog.setMessage(messageDialog);
        progressDialog.setCancelable(false);
    }

    @Override
    public void hideDialogGeneric() {
        dialogConfirm.dismiss();
    }
}
