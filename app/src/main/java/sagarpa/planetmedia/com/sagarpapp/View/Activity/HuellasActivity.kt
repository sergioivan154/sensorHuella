package sagarpa.planetmedia.com.sagarpapp.View.Activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.planetmedia.victoria.healthkit.utils.SagarpaPreferences
import kotlinx.android.synthetic.main.activity_huellas.*
import sagarpa.planetmedia.com.sagarpapp.R
import sagarpa.planetmedia.com.sagarpapp.Utility.AppUtilidadesEncript
import sagarpa.planetmedia.com.sagarpapp.Utility.Memoria

class HuellasActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_huellas)

        btnRegistrar.setOnClickListener { val intent = Intent(this,HomeActivity::class.java)

            /*Estos son los datos que deben de guardarse*/
            var email = Memoria.email
            var nombre = Memoria.nombre
            var pass = Memoria.pass

            /*por ejemplo*/
            var sagarpa = SagarpaPreferences(this)
            sagarpa.setNombreSaved(nombre)
            sagarpa.setCorreoSaved(email)
            sagarpa.setPassSaved(pass)

            finish()
            startActivity(intent) }
    }
}