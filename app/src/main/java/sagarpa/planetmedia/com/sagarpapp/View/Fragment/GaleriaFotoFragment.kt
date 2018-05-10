package sagarpa.planetmedia.com.sagarpapp.View.Fragment

import android.app.Fragment
import android.media.ExifInterface
import android.media.RemoteController
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import sagarpa.planetmedia.com.sagarpapp.Model.Adapter.BaseAdapterPhoto
import sagarpa.planetmedia.com.sagarpapp.Model.Adapter.GalleryPhoto
import sagarpa.planetmedia.com.sagarpapp.R
import sagarpa.planetmedia.com.sagarpapp.Utility.KeyDictionary
import java.io.File
import java.nio.charset.Charset
import android.graphics.Bitmap
import android.widget.Toast
import java.io.FileOutputStream
import java.util.*


class GaleriaFotoFragment:Fragment() {

    lateinit var rvGalleryPhoto: RecyclerView
    lateinit var listPhoto: MutableList<GalleryPhoto>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment

        var view = inflater.inflate(R.layout.fragment_galeria_fotos, container, !KeyDictionary.booleanTDefault)

        rvGalleryPhoto = view?.findViewById(R.id.rvGaleriaFotografica)!!

        rvGalleryPhoto.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, !KeyDictionary.booleanTDefault)

        listPhoto = mutableListOf<GalleryPhoto>()

        SearchPhotoStorage()

        /*listPhoto.add(GalleryPhoto("Imagen1","","",0.0, 0.0, 0, 0, 0, ""))
        listPhoto.add(GalleryPhoto("Imagen2","","",0.0, 0.0, 0, 0, 0, ""))
        listPhoto.add(GalleryPhoto("Imagen3","","",0.0, 0.0, 0, 0, 0, ""))
        listPhoto.add(GalleryPhoto("Imagen3","","",0.0, 0.0, 0, 0, 0, ""))
        listPhoto.add(GalleryPhoto("Imagen4","","",0.0, 0.0, 0, 0, 0, ""))*/



        return view
    }

    protected fun SearchPhotoStorage() {

        var myFolder = Environment.getExternalStorageDirectory().toString() + "/descaga"
        var fi = File(myFolder)
        if (!fi.exists()) {
            if (!fi.mkdir()) {
                Toast.makeText(context, myFolder + " can´t b created", Toast.LENGTH_LONG).show()
            } else
                Toast.makeText(context, myFolder + " can be created.", Toast.LENGTH_SHORT).show();
        }else
            Toast.makeText(context, myFolder +" already exits.", Toast.LENGTH_SHORT).show();

        var fileRuta = File(Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera/")

        Log.e("Log", Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera/" )

        val files = fileRuta.listFiles()

        for (file in files) {

            if (file.isFile)
            {

                val selectedImage = Uri.fromFile(file)
                val fileExtension: String = MimeTypeMap.getFileExtensionFromUrl(selectedImage.toString())
                val mimeTypeMap = MimeTypeMap.getSingleton().getExtensionFromMimeType(fileExtension)

                listPhoto.add(GalleryPhoto(file.name, fileExtension, selectedImage.authority, file.freeSpace.toDouble(), 0.0, 0, 0, 0,file.path))

            }

            /*var exifInterface = ExifInterface(file.inputStream())



            file.canonicalFile.canonicalFile.
            file.bufferedReader(Charset.defaultCharset(), DEFAULT_BUFFER_SIZE).
            file.canonicalFile.nameWithoutExtension
            println("Extension " + file.extension)
            file.isFile*/

        }

        rvGalleryPhoto.adapter = BaseAdapterPhoto(listPhoto, context)

    }

}