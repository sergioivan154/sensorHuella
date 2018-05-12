package sagarpa.planetmedia.com.sagarpapp.View.Activity

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.Toast
import com.planetmedia.victoria.healthkit.utils.SagarpaPreferences
import kotlinx.android.synthetic.main.activity_login.*
import sagarpa.planetmedia.com.sagarpapp.R
import sagarpa.planetmedia.com.sagarpapp.Utility.AppUtilidadesEncript
import sagarpa.planetmedia.com.sagarpapp.Utility.Biometrico.Biometrico
import sagarpa.planetmedia.com.sagarpapp.Utility.Biometrico.BiometricoResponse
import sagarpa.planetmedia.com.sagarpapp.Utility.ConexionSQLiteHelper

class LoginActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val con = ConexionSQLiteHelper(this, "sqLiteDatabase_user", null, 1)

        btnIniciarSesion.setOnClickListener({

            var email = editCorreo.text.toString()
            var password = editPassword.text.toString()


            if(email == "" || password == ""){
                Toast.makeText(this, "Debe llenar todos los campos", Toast.LENGTH_LONG).show()
            }else {
                val lUsers = con.selectUser(email)
                if(lUsers.size>0) {
                    //aqui se consulta de la base de datos
                    var emailDB = lUsers[0].email
                    var passwordDB = lUsers[0].password


                    if (email == AppUtilidadesEncript().Desencriptar(emailDB) && password == AppUtilidadesEncript().Desencriptar(passwordDB)) {


                        val intent = Intent(this, HomeActivity::class.java)
                        finish()
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "usuario no encontrado", Toast.LENGTH_LONG).show()
                    }
                }
                else{
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

    fun validar(finger1:ByteArray,finger2:ByteArray ) {

        progress.visibility = View.VISIBLE
        fingerprint_icon.visibility = View.INVISIBLE
        val uno = Base64.encodeToString(finger1, Base64.NO_WRAP)
        val dos = Base64.encodeToString(finger2, Base64.NO_WRAP)

        val bio = Biometrico(this)


        bio.compararHuellas(finger1, finger2) { respuesta, compatibilidad ->
            if(respuesta) {

                Toast.makeText(this@LoginActivity, "eres autentico al $compatibilidad%", Toast.LENGTH_LONG).show()
                val intent = Intent(this, HomeActivity::class.java)
                finish()
                startActivity(intent)

            } else{
                Toast.makeText(this@LoginActivity, "No eres el usario  $compatibilidad%", Toast.LENGTH_LONG).show()
            }

            fingerprint_icon.visibility = View.VISIBLE
            progress.visibility = View.INVISIBLE

        }



    }

}
