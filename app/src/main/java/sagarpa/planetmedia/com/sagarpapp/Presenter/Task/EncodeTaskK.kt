package sagarpa.planetmedia.com.sagarpapp.Presenter.Task

import android.app.Activity
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import sagarpa.planetmedia.com.sagarpapp.Model.Adapter.GalleryPhoto
import sagarpa.planetmedia.com.sagarpapp.Model.Steganography.LSB2BitK
import sagarpa.planetmedia.com.sagarpapp.Model.Steganography.ReadBitPhoto
import sagarpa.planetmedia.com.sagarpapp.Presenter.BasePresenter.ProgressHandler
import java.io.IOException

class EncodeTaskK : AsyncTask<GalleryPhoto, Int, GalleryPhoto> {

    private val TAG = EncodeTaskK::class.java.name
    lateinit var activityMain: Activity

    constructor(activity: Activity) {
        this.activityMain = activity
    }

    override fun doInBackground(vararg params: GalleryPhoto?): GalleryPhoto? {

        var galleryPhotoTask: GalleryPhoto? = null

        galleryPhotoTask = params[0]

        var sMessageComplete: String = "${galleryPhotoTask?.sNameImage}," +
                "${galleryPhotoTask?.dLatitudImage}," +
                "${galleryPhotoTask?.dLongitudImage}," +
                "${galleryPhotoTask?.iHeightImage}," +
                "${galleryPhotoTask?.iWithImage}," +
                "${galleryPhotoTask?.iSizeImage}," +
                "${galleryPhotoTask?.sDateImage}," +
                "${galleryPhotoTask?.sTimeImage}"

        val pixelNeeded = LSB2BitK().numberOfPixelForMessage(sMessageComplete)

        val squareBlockNeeded = LSB2BitK().squareBlockNeeded(pixelNeeded)

        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(galleryPhotoTask?.filePhoto?.getAbsolutePath(), options)
        val sample = calculateInSampleSize(options, squareBlockNeeded * LSB2BitK().SQUARE_BLOCK, squareBlockNeeded * LSB2BitK().SQUARE_BLOCK)
        options.inJustDecodeBounds = false
        options.inSampleSize = sample

        val bitm = BitmapFactory.decodeFile(galleryPhotoTask?.filePhoto?.getAbsolutePath(), options)
        val originalHeight = bitm.height
        val originalWidth = bitm.width

        val srcList = LSB2BitK().splitImage(bitm)

//        val encodedList = ReadBitPhoto.encodeMessage(srcList, sMessageComplete, object : ProgressHandler {
//            override fun setTotal(tot: Int) {
//                //maxProgeress = tot
//                //progressDialog.setMax(maxProgeress)
//                Log.d(TAG, "Total lenght $tot")
//            }
//
//            override fun increment(inc: Int) {
//                publishProgress(inc)
//            }
//
//            override fun finished() {
//                Log.d(TAG, "Message encoding end!")
//                //progressDialog.setIndeterminate(true)
//                //progressDialog.setTitle("Merging images...")
//            }
//        })

        bitm.recycle()
        for (bitmamp in srcList)
            bitmamp.recycle()

        System.gc()
//        val srcEncoded = LSB2BitK().mergeImage(encodedList, originalHeight, originalWidth)
//        try {
//            galleryPhotoTask = ReadBitPhoto.saveGalleryPhotoItem(sMessageComplete, srcEncoded, activityMain)
//            //galleryPhotoTask.setEncoded(true)
//        } catch (e: IOException) {
//            e.printStackTrace()
//            //TODO manage exception
//        }
//
//        //free memory
//        for (bitmamp in encodedList)
//            bitmamp.recycle()


        return galleryPhotoTask


    }


    fun calculateInSampleSize(
            options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            val halfHeight = height / 2
            val halfWidth = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize > reqHeight && halfWidth / inSampleSize > reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

}