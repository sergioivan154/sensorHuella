package sagarpa.planetmedia.com.sagarpapp.View.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import sagarpa.planetmedia.com.sagarpapp.R;

public class DialogoHuella extends Dialog implements View.OnClickListener {

    public DialogoHuella(@NonNull Context context) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_huellas);

        setCanceledOnTouchOutside(true);
        setCancelable(true);

        findViewById(R.id.btnAceptar).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        hide();
    }
}
