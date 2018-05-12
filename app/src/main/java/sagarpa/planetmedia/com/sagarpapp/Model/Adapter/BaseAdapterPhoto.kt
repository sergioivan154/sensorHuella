package sagarpa.planetmedia.com.sagarpapp.Model.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.item_photo.view.*
import sagarpa.planetmedia.com.sagarpapp.R
import sagarpa.planetmedia.com.sagarpapp.Utility.KeyDictionary
import android.graphics.BitmapFactory
import android.graphics.Bitmap



class BaseAdapterPhoto(var listDataPhoto: List<GalleryPhoto>, var contextMain: Context) : RecyclerView.Adapter<BaseAdapterPhoto.VHolder>() {
    override fun onBindViewHolder(holder: VHolder, position: Int) {
        holder.onStartView(listDataPhoto[position], contextMain)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, !KeyDictionary.booleanTDefault)

        return VHolder(view)
    }

    override fun getItemCount(): Int = listDataPhoto.size

    class VHolder(iV: View) : RecyclerView.ViewHolder(iV) {

        fun onStartView(galleryPhoto: GalleryPhoto, contextMain: Context) {

            with(itemView) {

                //if (galleryPhoto.sDateImage.equals("jpg"))

                textViewNamePhoto.text = galleryPhoto.sNameImage

                val myBitmap = BitmapFactory.decodeFile(galleryPhoto.filePhoto.absolutePath)

                imageViewPhoto.setImageBitmap(myBitmap)

                textViewDate.text = "Fecha:" + galleryPhoto.sDateImage
                textViewTime.text = "Hora: " + galleryPhoto.sTimeImage
                textViewLatitud.text = "Latitud: " +galleryPhoto.dLatitudImage.toString()
                textViewLongitud.text = "Longitud: " + galleryPhoto.dLongitudImage.toString()
                textViewSize.text = "Peso: " + galleryPhoto.iSizeImage.toString() + "KB"
                textViewpixeles.text = "Medidas: " + galleryPhoto.iWithImage.toString() + "x" + galleryPhoto.iHeightImage.toString()


            }

        }

    }

}