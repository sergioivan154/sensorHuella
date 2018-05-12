package sagarpa.planetmedia.com.sagarpapp.Presenter.Task;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.List;

import sagarpa.planetmedia.com.sagarpapp.Model.Adapter.MobiStegoItem;
import sagarpa.planetmedia.com.sagarpapp.Model.Steganography.LSB2bit;
import sagarpa.planetmedia.com.sagarpapp.Model.Steganography.ReadBitPhoto;

public class DecodeTask extends AsyncTask<File, Void, MobiStegoItem> {
   
    private final static String TAG=DecodeTask.class.getName();
    private Activity activity;
    private ProgressDialog progressDialog;
    private boolean isMobistegoImage;
    
    public DecodeTask(Activity m){
        super();
        this.activity=m;
        isMobistegoImage=false;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //progressDialog = new ProgressDialog(activity); //Here I get an error: The constructor ProgressDialog(PFragment) is undefined
        //progressDialog.setMessage(activity.getString(R.string.loading));
        //progressDialog.setTitle(activity.getString(R.string.decoding));
        //progressDialog.setIndeterminate(true);
        //progressDialog.setCancelable(false);
        
    }
    
    @Override
    protected MobiStegoItem doInBackground(File... params) {
        MobiStegoItem result=null;
        publishProgress();
        Bitmap bitmap = BitmapFactory.decodeFile(params[0].getAbsolutePath());
        if (bitmap == null)
            return result;
        List<Bitmap> srcEncodedList = ReadBitPhoto.splitImage(bitmap);
        String decoded = LSB2bit.decodeMessage(srcEncodedList);
        for(Bitmap bitm:srcEncodedList)
            bitm.recycle();
        if(!ReadBitPhoto.isEmpty(decoded)) {
            try {
                isMobistegoImage=true;
                result = ReadBitPhoto.saveMobiStegoItem(decoded, bitmap,activity);
            } catch (IOException e) {
                Log.e(TAG, "Error", e);
                //e.printStackTrace();
            }
        }
        
        return result;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(MobiStegoItem mobiStegoItem) {
        super.onPostExecute(mobiStegoItem);
        progressDialog.dismiss();
        if(!isMobistegoImage)
        {

            Log.e(TAG, "Entro en if onPostExecute");

            //AlertNotMobistegoFragment compose = new AlertNotMobistegoFragment();
            Bundle args = new Bundle();

            //compose.setArguments(args);
            FragmentTransaction transaction = activity.getFragmentManager().beginTransaction();

            transaction.addToBackStack(null);
            //compose.show(transaction, "dialog");
        } else {

            Log.e(TAG, "Entro en else onPostExecute");

            //((MainFragment.OnMainFragment) activity).onMainFragmentGridItemSelected(mobiStegoItem);
        }
    }
}
