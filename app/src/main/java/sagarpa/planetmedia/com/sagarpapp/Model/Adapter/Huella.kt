package sagarpa.planetmedia.com.sagarpapp.Model.Adapter

import android.graphics.Bitmap
import sagarpa.planetmedia.com.sagarpapp.Utility.KeyDictionary

data class Huella(val nombre: String = KeyDictionary.stringDefault,
                  val correo: String = KeyDictionary.stringDefault,
                  val huella: Bitmap? = null)
