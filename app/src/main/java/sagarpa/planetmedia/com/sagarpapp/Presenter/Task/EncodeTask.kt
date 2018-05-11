package sagarpa.planetmedia.com.sagarpapp.Presenter.Task

import android.app.Activity
import android.os.AsyncTask
import sagarpa.planetmedia.com.sagarpapp.Model.Adapter.GalleryPhoto
import java.io.File

class EncodeTask : AsyncTask<GalleryPhoto, Int, GalleryPhoto> {

    lateinit var activityMain: Activity

    constructor(activity: Activity) {
        this.activityMain = activity
    }

    override fun doInBackground(vararg params: GalleryPhoto?): GalleryPhoto {

        var galleryPhotoTask: GalleryPhoto? = null

        if (params.size > 0) {
            galleryPhotoTask = params[0]
        }

    }


}