package sagarpa.planetmedia.com.sagarpapp.View.Fragment

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import sagarpa.planetmedia.com.sagarpapp.R

class GaleriaFotoFragment:Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment

        var view = inflater.inflate(R.layout.fragment_galeria_fotos, container, false)


        return view
    }
}