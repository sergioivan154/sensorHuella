package sagarpa.planetmedia.com.sagarpapp.View.Fragment

import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.planetmedia.victoria.healthkit.utils.SagarpaPreferences

import kotlinx.android.synthetic.main.fragment_galeria_huellas.view.*
import sagarpa.planetmedia.com.sagarpapp.Model.Adapter.BaseAdapterHuellas
import sagarpa.planetmedia.com.sagarpapp.Model.Adapter.Huella
import sagarpa.planetmedia.com.sagarpapp.R
import sagarpa.planetmedia.com.sagarpapp.Utility.AppUtilidadesEncript
import sagarpa.planetmedia.com.sagarpapp.Utility.ConexionSQLiteHelper
import sagarpa.planetmedia.com.sagarpapp.Utility.KeyDictionary

class GaleriaHuellasFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment

        var view = inflater.inflate(R.layout.fragment_galeria_huellas, container, false)
        val con = ConexionSQLiteHelper(context, "sqLiteDatabase_user", null)


        //aqui se deben de listar todas las huellas registradas en el telefono

        val lHuellas = con.selectHuellas()

        var adapter = BaseAdapterHuellas(lHuellas, context)

        view.rvGaleriaHuellas.adapter = adapter
        view.rvGaleriaHuellas.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, !KeyDictionary.booleanTDefault)


        return view
    }
}