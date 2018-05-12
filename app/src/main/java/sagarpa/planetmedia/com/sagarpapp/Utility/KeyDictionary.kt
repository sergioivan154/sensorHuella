package sagarpa.planetmedia.com.sagarpapp.Utility

import android.Manifest

object KeyDictionary {

    const val stringDefault: String = ""
    const val intDefault: Int = 0
    const val doubleDefault: Double = 0.0
    const val booleanTDefault: Boolean = true
    const val longDefault: Long = 0

    const val rutaNameFolder: String = "/ruta/photo/"


    /**
     * @see
     * @Permission Permisos para la app
     */
    const val PERMISSION_REQUEST_READ_STATUS_SUCCESS = 100
    @JvmField val GROUP_PERMISSION = arrayOf<String>(
            Manifest.permission.WAKE_LOCK,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO)

}