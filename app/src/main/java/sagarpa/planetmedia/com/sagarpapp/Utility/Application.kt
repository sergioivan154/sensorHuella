package sagarpa.planetmedia.com.sagarpapp.Utility

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco

/**
 * Created by JC on 17/07/2017.
 */
class Application : Application() {



    override fun onCreate() {
        super.onCreate()

        Fresco.initialize(this)
    }
}
