package sagarpa.planetmedia.com.sagarpapp.View.Activity

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Base64
import android.view.View
import android.widget.Toast
import asia.kanopi.fingerscan.Fingerprint
import asia.kanopi.fingerscan.Status
import com.planetmedia.victoria.healthkit.utils.SagarpaPreferences


import kotlinx.android.synthetic.main.activity_login.*
import sagarpa.planetmedia.com.sagarpapp.Model.Adapter.Huella
import sagarpa.planetmedia.com.sagarpapp.R
import sagarpa.planetmedia.com.sagarpapp.Utility.AppUtilidadesEncript
import sagarpa.planetmedia.com.sagarpapp.Utility.Biometrico.Biometrico
import sagarpa.planetmedia.com.sagarpapp.Utility.Biometrico.BiometricoResponse
import sagarpa.planetmedia.com.sagarpapp.Utility.ConexionSQLiteHelper
import sagarpa.planetmedia.com.sagarpapp.Utility.Memoria

class LoginActivity : AppCompatActivity() {

    private val fingerprint = Fingerprint()
    var lHuellas: List<Huella>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val con = ConexionSQLiteHelper(this, "sqLiteDatabase_user", null)

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

        //validar huellas

        lHuellas = con.selectHuellas()

        btnSensor.setOnClickListener({

            fingerprint?.scan(this, printHandler, updateHandler)

        })
    }

    fun validar(finger1:Bitmap?,finger2:Bitmap ) {


        if(finger1 != null) {

            progress.visibility = View.VISIBLE
           // fingerprint_icon.visibility = View.INVISIBLE
            fingerprint_status.visibility = View.GONE

            val bio = Biometrico(this)


            bio.compararHuellas(finger1, finger2) { respuesta, compatibilidad ->
                if (respuesta) {

                    fingerprint_status.setImageResource(R.drawable.check)
                   // Toast.makeText(this@LoginActivity, "eres autentico al $compatibilidad%", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, HomeActivity::class.java)
                    finish()
                    startActivity(intent)

                } else {
                    fingerprint_status.setImageResource(R.drawable.noentry)
                    fingerprint?.scan(this, printHandler, updateHandler)
                    //Toast.makeText(this@LoginActivity, "No eres el usario  $compatibilidad%", Toast.LENGTH_LONG).show()
                }

                //fingerprint_icon.visibility = View.VISIBLE
                progress.visibility = View.INVISIBLE
                fingerprint_status.visibility = View.VISIBLE

            }
        }



    }


    override fun onStart() {
        fingerprint?.scan(this, printHandler, updateHandler)
        super.onStart()
    }

    override fun onStop() {
        fingerprint?.turnOffReader()
        super.onStop()
    }


    internal var updateHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            val status = msg.data.getInt("status")
            fingerprint_description.setText("")
            when (status) {
                Status.INITIALISED -> fingerprint_description.setText("Setting up reader")
                Status.SCANNER_POWERED_ON ->{
                    fingerprint_description.setText("Reader powered on")
                    progress.visibility = View.GONE

                }
                Status.READY_TO_SCAN -> fingerprint_description.setText("Ready to scan finger")
                Status.FINGER_DETECTED -> {
                    fingerprint_description.setText("Finger detected")
                    progress.visibility  = View.VISIBLE
                    fingerprint_icon.visibility = View.GONE
                }
                Status.RECEIVING_IMAGE -> fingerprint_description.setText("Receiving image")
                Status.FINGER_LIFTED -> fingerprint_description.setText("Finger has been lifted off reader")
                Status.SCANNER_POWERED_OFF -> fingerprint_description.setText("Reader is off")
                Status.SUCCESS -> fingerprint_description.setText("Fingerprint successfully captured")
                Status.ERROR -> {
                    fingerprint_description.setText("Error")
                    fingerprint_description.setText(msg.data.getString("errorMessage"))
                }
                else -> {
                    fingerprint_description.setText(status.toString())
                    fingerprint_description.setText(msg.data.getString("errorMessage"))
                }
            }
        }
    }

    internal var printHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            val image: ByteArray?
            var errorMessage: String? = "empty"
            val status = msg.data.getInt("status")
            val intent = Intent()
            intent.putExtra("status", status)
            if (status == Status.SUCCESS) {
                image = msg.data.getByteArray("img")


                fingerprint?.turnOffReader()

                fingerprint_icon.visibility = View.VISIBLE
                progress.visibility = View.GONE
                val bm = BitmapFactory.decodeByteArray(image, 0, image.size)
                fingerprint_icon.setImageBitmap(bm)

                lHuellas?.forEach {
                    validar(it.huella, bm)
                }

            } else {
                errorMessage = msg.data.getString("errorMessage")
                //  intent.putExtra("errorMessage", errorMessage)
            }
            /* setResult(Activity.RESULT_OK, intent)
             finish()*/
        }
    }

}
