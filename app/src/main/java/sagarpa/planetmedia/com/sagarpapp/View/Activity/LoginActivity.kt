package sagarpa.planetmedia.com.sagarpapp.View.Activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*
import sagarpa.planetmedia.com.sagarpapp.R

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btnIniciarSesion.setOnClickListener({
            val intent = Intent(this,HomeActivity::class.java)
            finish()
            startActivity(intent)
        })

        btnRegistrar.setOnClickListener({
            val intent = Intent(this,RegistroActivity::class.java)
            finish()
            startActivity(intent)
        })
    }
}
