package sagarpa.planetmedia.com.sagarpapp.Utility.Biometrico;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Biometrico {

    private FingerPrint m_finger1 = new FingerPrint();
    private FingerPrint m_finger2 = new FingerPrint();

    private double finger1[] = new double[m_finger1.FP_TEMPLATE_MAX_SIZE];
    private double finger2[] = new double[m_finger2.FP_TEMPLATE_MAX_SIZE];

    Context context;


    public Biometrico(Context context){
        this.context = context;

    }

    public void compararHuellas(byte[] huella1, byte[] huella2, BiometricoResponse biometricoResponse){

        Bitmap imgHuella1 = BitmapFactory.decodeByteArray(huella1, 0, huella1.length);
        Bitmap imgHuella2 =  BitmapFactory.decodeByteArray(huella2, 0, huella2.length);
        m_finger1.setFingerPrintImage(imgHuella1);
        m_finger2.setFingerPrintImage(imgHuella2);
        finger1=m_finger1.getFingerPrintTemplate();
        finger2=m_finger2.getFingerPrintTemplate();

        try {

            int compatibilidad = m_finger1.Match(finger1, finger2, 65, false);
            boolean matches = compatibilidad >= 40;

            biometricoResponse.response(matches, compatibilidad);


        }
        catch (Exception ex){
            String exe= ex.getMessage();
        }
    }


}
