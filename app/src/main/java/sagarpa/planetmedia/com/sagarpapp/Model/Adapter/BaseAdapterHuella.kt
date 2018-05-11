package sagarpa.planetmedia.com.sagarpapp.Model.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView

class BaseAdapterHuella(var listDataPhoto: List<GalleryPhoto>, var contextMain: Context) : RecyclerView.Adapter<BaseAdapterPhoto.VHolder>() {
}