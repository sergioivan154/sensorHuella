package sagarpa.planetmedia.com.sagarpapp.Model.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.item_huella.view.*
import kotlinx.android.synthetic.main.item_photo.view.*
import sagarpa.planetmedia.com.sagarpapp.R
import sagarpa.planetmedia.com.sagarpapp.Utility.KeyDictionary

class BaseAdapterHuellas(var listDataPhoto: List<Huella>, var contextMain: Context) : RecyclerView.Adapter<BaseAdapterHuellas.VHolder>() {
    override fun onBindViewHolder(holder: VHolder, position: Int) {
        holder?.onStartView(listDataPhoto[position], contextMain)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_huella, parent, !KeyDictionary.booleanTDefault)

        return VHolder(view)
    }

    override fun getItemCount(): Int = listDataPhoto.size

    class VHolder(iV: View) : RecyclerView.ViewHolder(iV) {

        fun onStartView(huella: Huella, contextMain: Context) {

            with(itemView) {

                itemView.textNombre.text = huella.nombre
                itemView.textEmail.text = huella.correo
            }

        }

    }

}