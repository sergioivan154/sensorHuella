package com.planetmedia.victoria.healthkit.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * @author Gorro.
 */
class SagarpaPreferences (context: Context) {

    private var preferences: SharedPreferences? = null

    private var NAME_KEY_SAGARPA_PREFERENCES = "PREFERENCIAS"
    private var CORREO = "CORREO"
    private var NOMBRE = "NOMBRE"
    private var PASSWORD = "PASSWORD"
    private var DEFAULT_STRING = ""



    init {
        preferences = context.getSharedPreferences(NAME_KEY_SAGARPA_PREFERENCES, Context.MODE_PRIVATE)

    }


    fun setNombreSaved(strNombre: String) {
        preferences!!.edit().putString(NOMBRE, strNombre).apply()
    }

    fun getNombreSaved () = preferences!!.getString(NOMBRE, DEFAULT_STRING) ?: ""



    fun setCorreoSaved(strCorreo: String) {
        preferences!!.edit().putString(CORREO, strCorreo).apply()
    }

    fun getCorreoSaved () = preferences!!.getString(CORREO, DEFAULT_STRING) ?: ""



    fun setPassSaved(strPass: String) {
        preferences!!.edit().putString(PASSWORD, strPass).apply()
    }

    fun getPassSaved () = preferences!!.getString(PASSWORD, DEFAULT_STRING) ?: ""


    fun ClearPreferents(){
        preferences?.edit()?.clear()
    }

}