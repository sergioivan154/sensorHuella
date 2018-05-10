package sagarpa.planetmedia.com.sagarpapp.View.Activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.planetmedia.victoria.healthkit.utils.SagarpaPreferences
import kotlinx.android.synthetic.main.activity_login.*
import sagarpa.planetmedia.com.sagarpapp.R
import sagarpa.planetmedia.com.sagarpapp.Utility.AppUtilidadesEncript

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btnIniciarSesion.setOnClickListener({

            var email = editCorreo.text.toString()
            var password = editPassword.text.toString()


            if(email == "" || password == ""){
                Toast.makeText(this, "Debe llenar todos los campos", Toast.LENGTH_LONG).show()
            }else {
                //aqui se consulta de la base de datos
                var emailDB = SagarpaPreferences(this).getCorreoSaved()
                var passwordDB = SagarpaPreferences(this).getPassSaved()

                if (email == AppUtilidadesEncript().Desencriptar(emailDB) && password == AppUtilidadesEncript().Desencriptar(passwordDB)) {


                    val intent = Intent(this, HomeActivity::class.java)
                    finish()
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "usuario no encontrado", Toast.LENGTH_LONG).show()
                }
            }


        })

        btnRegistrar.setOnClickListener({
            val intent = Intent(this,RegistroActivity::class.java)
            finish()
            startActivity(intent)
        })
    }
}
