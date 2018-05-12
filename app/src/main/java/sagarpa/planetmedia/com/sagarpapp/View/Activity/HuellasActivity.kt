package sagarpa.planetmedia.com.sagarpapp.View.Activity

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import android.widget.Toast
import com.planetmedia.victoria.healthkit.utils.SagarpaPreferences
import kotlinx.android.synthetic.main.activity_huellas.*
import sagarpa.planetmedia.com.sagarpapp.R
import sagarpa.planetmedia.com.sagarpapp.Utility.AppUtilidadesEncript
import sagarpa.planetmedia.com.sagarpapp.Utility.ConexionSQLiteHelper
import sagarpa.planetmedia.com.sagarpapp.Utility.Memoria
import sagarpa.planetmedia.com.sagarpapp.Utility.Utilities

import asia.kanopi.fingerscan.Fingerprint
import asia.kanopi.fingerscan.Status

class HuellasActivity: AppCompatActivity() {

    private var fingerprint: Fingerprint? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_huellas)

        btnRegistrar.setOnClickListener {

            if (Memoria.huella != "") {
                registrarUser()
            }else{
                Toast.makeText(this, "Debe capturar su huella", Toast.LENGTH_LONG).show()
            }
        }

        fingerprint = Fingerprint()
    }

    private fun registrarUser() {
        val con = ConexionSQLiteHelper(this, "sqLiteDatabase_user", null)
        val db = con.writableDatabase
        val values = ContentValues()

        values.put(Utilities.CampName, Memoria.nombre)
        values.put(Utilities.CampEmail, Memoria.email)
        values.put(Utilities.CampPassword, Memoria.pass)
        values.put(Utilities.CampHuella, Memoria.huella)

        val Resultante = db.insert(Utilities.TABLEuser, Utilities.TABLEuser, values)
        //Toast.makeText(getApplicationContext, Resultante, Toast.LENGTH_SHORT).show()
        Toast.makeText(this,"id Registro"+ Resultante, Toast.LENGTH_SHORT).show()

        val intent = Intent(this, HomeActivity::class.java)
        finish()
        startActivity(intent)

    }

    override fun onStart() {
        fingerprint?.scan(this, printHandler, updateHandler)
        super.onStart()
    }

    override fun onStop() {
        fingerprint?.turnOffReader()
        super.onStop()
    }

    fun reboot(){

            fingerprint_description.setText("Reader is off")
            fingerprint?.scan(this@HuellasActivity, printHandler, updateHandler)


    }

    internal var updateHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            val status = msg.data.getInt("status")
            fingerprint_description.setText("")
            when (status) {
                Status.INITIALISED -> fingerprint_description.setText("Setting up reader")
                Status.SCANNER_POWERED_ON -> fingerprint_description.setText("Reader powered on")
                Status.READY_TO_SCAN -> fingerprint_description.setText("Ready to scan finger")
                Status.FINGER_DETECTED -> fingerprint_description.setText("Finger detected")
                Status.RECEIVING_IMAGE -> fingerprint_description.setText("Receiving image")
                Status.FINGER_LIFTED -> fingerprint_description.setText("Finger has been lifted off reader")
                Status.SCANNER_POWERED_OFF -> reboot()
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


                var log = ""
                for (key in msg.data.keySet()) {
                    log += "\n" + key + " : " + msg.data.get(key)
                }


                val bm = BitmapFactory.decodeByteArray(image, 0, image.size)
                fingerprint_icon.setImageBitmap(bm)
                Memoria.huella = AppUtilidadesEncript().Encriptar(Base64.encodeToString(image, Base64.NO_WRAP))

/*                intent.putExtra("img", image)

                intent.putExtra("msgs", log)*/

            } else {
                errorMessage = msg.data.getString("errorMessage")
              //  intent.putExtra("errorMessage", errorMessage)
            }
           /* setResult(Activity.RESULT_OK, intent)
            finish()*/
        }
    }

}