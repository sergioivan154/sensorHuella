package sagarpa.planetmedia.com.sagarpapp.View.Activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_registro.*
import sagarpa.planetmedia.com.sagarpapp.R

class RegistroActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        btnTomaHuella.setOnClickListener({
            val intent = Intent(this,HuellasActivity::class.java)
            finish()
            startActivity(intent)
        })

        btnTomarFotografia.setOnClickListener({

        })

    }
}