package sagarpa.planetmedia.com.sagarpapp.Presenter.Task;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import sagarpa.planetmedia.com.sagarpapp.Model.Adapter.MobiStegoItem;
import sagarpa.planetmedia.com.sagarpapp.Model.Steganography.LSB2bit;
import sagarpa.planetmedia.com.sagarpapp.Model.Steganography.ReadBitPhoto;
import sagarpa.planetmedia.com.sagarpapp.View.Activity.HuellasActivity;

public class EncodeTask extends AsyncTask<MobiStegoItem, Integer, MobiStegoItem> {

    private static String TAG = EncodeTaskK.class.getName();

    private int maxProgeress;

    private Activity activity;

    //private ProgressDialog progressDialog;

    public EncodeTask(Activity act) {
        super();
        this.activity = act;
    }


    @Override
    protected MobiStegoItem doInBackground(MobiStegoItem... params) {
        MobiStegoItem result = null;
        maxProgeress = 0;
        if (params.length > 0) {
            MobiStegoItem mobistego = params[0];
            //Encrypt
            if (!ReadBitPhoto.isEmpty(mobistego.getPassword())) {
                try {
                    String ecnrypted64 = ReadBitPhoto.encrypt(mobistego.getMessage(), mobistego.getPassword());
                    mobistego.setMessage(ecnrypted64);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            int pixelNeeded = LSB2bit.numberOfPixelForMessage(mobistego.getMessage());
            int squareBlockNeeded = ReadBitPhoto.squareBlockNeeded(pixelNeeded);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(mobistego.getBitmap().getAbsolutePath(), options);
            int sample = calculateInSampleSize(options, squareBlockNeeded * ReadBitPhoto.SQUARE_BLOCK, squareBlockNeeded * ReadBitPhoto.SQUARE_BLOCK);
            options.inJustDecodeBounds = false;
            options.inSampleSize = sample;

            Bitmap bitm = BitmapFactory.decodeFile(mobistego.getBitmap().getAbsolutePath(), options);
            int originalHeight = bitm.getHeight();
            int originalWidth = bitm.getWidth();

            List<Bitmap> srcList = ReadBitPhoto.splitImage(bitm);


            List<Bitmap> encodedList = LSB2bit.encodeMessage(srcList, mobistego.getMessage(), new LSB2bit.ProgressHandler() {
                @Override
                public void setTotal(int tot) {
                    maxProgeress = tot;
                    //progressDialog.setMax(maxProgeress);
                    Log.d(TAG, "Total lenght " + tot);
                }

                @Override
                public void increment(int inc) {
                    publishProgress(inc);
                }

                @Override
                public void finished() {
                    Log.d(TAG, "Message encoding end!");
                    //progressDialog.setIndeterminate(true);
                    //progressDialog.setTitle("Merging images...");
                }
            });
            //free memory
            bitm.recycle();
            for (Bitmap bitmamp : srcList)
                bitmamp.recycle();

            System.gc();
            Bitmap srcEncoded = ReadBitPhoto.mergeImage(encodedList, originalHeight, originalWidth);
            try {
                result = ReadBitPhoto.saveMobiStegoItem(mobistego.getMessage(), srcEncoded, activity);
                result.setEncoded(true);
            } catch (IOException e) {
                e.printStackTrace();
                //TODO manage exception
            }
            //free memory
            for (Bitmap bitmamp : encodedList)
                bitmamp.recycle();


        }
        return result;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {

        super.onProgressUpdate(values);
        //progressDialog.show();
        //progressDialog.incrementProgressBy(values[0]);
        Log.d(TAG, "Progress " + values[0]);

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //progressDialog = new ProgressDialog(activity);
        //progressDialog.setMessage(activity.getString(R.string.loading));
        //progressDialog.setTitle(activity.getString(R.string.encoding));
        //progressDialog.setIndeterminate(false);
        //progressDialog.setCancelable(false);

        Log.e(TAG, "onPreExecute");

    }

    @Override
    protected void onPostExecute(MobiStegoItem mobiStegoItem) {
        super.onPostExecute(mobiStegoItem);
        //progressDialog.dismiss();

        //Bundle args = new Bundle();
        //MainFragment mainFragment = new MainFragment();
        //mainFragment.setArguments(args);
        ReadBitPhoto.listMobistegoItem(activity);

        Intent intent = new Intent(activity, HuellasActivity.class);
        activity.finish();
        activity.startActivity(intent);


        Log.e(TAG, "onPostExecute");

        //FragmentTransaction tx = activity.getFragmentManager().beginTransaction();
        //tx.replace(R.id.listFragment, mainFragment, Constants.CONTAINER);

        //tx.commit();
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
