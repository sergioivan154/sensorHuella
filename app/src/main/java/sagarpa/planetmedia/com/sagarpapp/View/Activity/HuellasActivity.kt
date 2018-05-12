package sagarpa.planetmedia.com.sagarpapp.View.Activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_huellas.*
import sagarpa.planetmedia.com.sagarpapp.R

class HuellasActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_huellas)

        btnRegistrar.setOnClickListener { val intent = Intent(this,HomeActivity::class.java)
            finish()
            startActivity(intent) }
    }
}