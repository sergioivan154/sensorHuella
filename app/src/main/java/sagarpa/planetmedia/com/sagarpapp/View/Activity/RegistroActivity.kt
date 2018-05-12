package sagarpa.planetmedia.com.sagarpapp.View.Activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_registro.*
import sagarpa.planetmedia.com.sagarpapp.Model.Adapter.GalleryPhoto
import sagarpa.planetmedia.com.sagarpapp.Model.Adapter.MobiStegoItem
import sagarpa.planetmedia.com.sagarpapp.Presenter.BasePresenter.RegistryUserEncode
import sagarpa.planetmedia.com.sagarpapp.Presenter.GPU
import sagarpa.planetmedia.com.sagarpapp.Presenter.Task.EncodeTask
import sagarpa.planetmedia.com.sagarpapp.Presenter.Task.EncodeTaskK
import sagarpa.planetmedia.com.sagarpapp.R
import sagarpa.planetmedia.com.sagarpapp.Utility.AppUtilidadesEncript
import sagarpa.planetmedia.com.sagarpapp.Utility.Constants
import sagarpa.planetmedia.com.sagarpapp.Utility.Memoria
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class RegistroActivity: BaseActivity(), RegistryUserEncode, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {



    private val TAKE_PHOTO_REQUEST = 101
    private var mCurrentPhotoPath: String = ""
    private var filExample: File? = null
    @Volatile
    var TEXTURE_SIZE_GL10 = 0
    @Volatile
    var TEXTURE_SIZE_GL20 = 0
    private var mGoogleApiClient: GoogleApiClient? = null
    protected var mLocationRequest: LocationRequest? = null
    protected var mLocationSettingsRequest: LocationSettingsRequest? = null
    val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 1000
    val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2
    var RQS_GooglePlayServices = 0
    protected var mCurrentLocation: Location? = null
    private var cont = 0
    protected var mRequestingLocationUpdates: Boolean? = null
    var sizeImage: Long = 0
    var imageHeight: Int = 0
    var imageWidth: Int = 0

    var galleryImage: GalleryPhoto? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        init()

        btnTomaHuella.setOnClickListener({
            //val intent = Intent(this,HuellasActivity::class.java)
            //finish()
            //startActivity(intent)


            var calendar = Calendar.getInstance()
            var simpledate = SimpleDateFormat("dd-MM-yyyy")
            var simpleTime = SimpleDateFormat("HH:mm:ss")
            var dateFinal = simpledate.format(calendar.time)
            var timeFinal = simpleTime.format(calendar.time)

            Log.e("Example", "El contenido: dateFinal: " + dateFinal +
                    " TimeFinal: " + timeFinal + " Longitud: " +
                    mCurrentLocation?.longitude + " Latitud: " +
                    mCurrentLocation?.latitude + " sizeImage: " + sizeImage +
            " imageHeight: " + imageHeight + " imageWidth: " + imageWidth )


            if (filExample != null && editCorreo.text.toString() != null &&
                    editCorreo.text.toString() != "" &&
                    editPassword.text.toString() != null &&
                    editPassword.text.toString() != "" &&
                    editText.text.toString() != null &&
                    editText.text.toString() != "" &&(editPassword.text.toString() == editPasswordC.text.toString())) {

                galleryImage = GalleryPhoto(filExample!!, editText.text.toString(), dateFinal, timeFinal, mCurrentLocation?.latitude!!, mCurrentLocation?.longitude!!,
                        sizeImage, imageWidth, imageHeight)


                var sMessageComplete: String = "${galleryImage?.sNameImage}," +
                        "${galleryImage?.sDateImage}," +
                        "${galleryImage?.sTimeImage}," +
                        "${galleryImage?.dLatitudImage}," +
                        "${galleryImage?.dLongitudImage}," +
                        "${galleryImage?.iSizeImage}," +
                        "${galleryImage?.iWithImage}," +
                        "${galleryImage?.iHeightImage}"

                showProgressDialog()
                val encriptar = AppUtilidadesEncript()
                Memoria.nombre = encriptar.Encriptar(editText.text.toString())
                Memoria.email = encriptar.Encriptar(editCorreo.text.toString())
                Memoria.pass = encriptar.Encriptar(editPassword.text.toString())

                val task = EncodeTask(this)
                val item2 = MobiStegoItem(sMessageComplete, filExample, Constants.NO_NAME, false, null)
                task.execute(item2)
            } else {
                showDialogGeneric("Incompleto", "Ingreso los datos faltantes", "", true, false ,false , onClickListenerDismiss, null)
            }



        })

        btnTomarFotografia.setOnClickListener({
            validatePermissions()
        })

    }


    override fun onStart() {
        super.onStart()


    }

    override fun onResume() {
        super.onResume()


        if (!isLocationEnable())
            activeGPS()
        else
            startGoogleApiLocation()
    }


    override fun onStop() {
        super.onStop()

        mGoogleApiClient?.disconnect()

    }

    private fun init() {

        if (TEXTURE_SIZE_GL20 == -1 && TEXTURE_SIZE_GL10 == -1) {
            val gpu = GPU(this)
            gpu.loadOpenGLGles20Info(object : GPU.OnCompleteCallback<GPU.OpenGLGles20Info> {
                override fun onComplete(result: GPU.OpenGLGles20Info) {
                    TEXTURE_SIZE_GL20 = result.GL_MAX_TEXTURE_SIZE
                }
            })
            gpu.loadOpenGLGles10Info(object : GPU.OnCompleteCallback<GPU.OpenGLGles10Info> {
                override fun onComplete(result: GPU.OpenGLGles10Info) {
                    TEXTURE_SIZE_GL10 = result.GL_MAX_TEXTURE_SIZE
                }
            })

        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == TAKE_PHOTO_REQUEST) {
            processCapturedPhoto()
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun validatePermissions() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object: PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                        launchCamera()
                    }

                    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?,
                                                                    token: PermissionToken?) {
                        AlertDialog.Builder(this@RegistroActivity)
                                .setTitle(R.string.storage_permission_rationale_title)
                                .setMessage(R.string.storage_permition_rationale_message)
                                .setNegativeButton(android.R.string.cancel,
                                        { dialog, _ ->
                                            dialog.dismiss()
                                            token?.cancelPermissionRequest()
                                        })
                                .setPositiveButton(android.R.string.ok,
                                        { dialog, _ ->
                                            dialog.dismiss()
                                            token?.continuePermissionRequest()
                                        })
                                .setOnDismissListener({ token?.cancelPermissionRequest() })
                                .show()
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                        Snackbar.make(mainContainer!!,
                                R.string.storage_permission_denied_message,
                                Snackbar.LENGTH_LONG)
                                .show()
                    }
                })
                .check()
    }

    private fun launchCamera() {
        val values = ContentValues(1)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
        val fileUri = contentResolver
                .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        values)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if(intent.resolveActivity(packageManager) != null) {
            mCurrentPhotoPath = fileUri.toString()
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                    or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            startActivityForResult(intent, TAKE_PHOTO_REQUEST)
        }
    }

    private fun processCapturedPhoto() {
        val cursor = contentResolver.query(Uri.parse(mCurrentPhotoPath),
                Array(1) {android.provider.MediaStore.Images.ImageColumns.DATA},
                null, null, null)
        cursor.moveToFirst()
        val photoPath = cursor.getString(0)
        cursor.close()
        val file = File(photoPath)
        val uri = Uri.fromFile(file)

        val height = resources.getDimensionPixelSize(R.dimen.photo_height)
        val width = resources.getDimensionPixelSize(R.dimen.photo_width)

        //val task = EncodeTaskK(this)

        sizeImage = file.length()/1024
        var options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(File(uri.path).absolutePath, options)
        imageHeight = options.outHeight
        imageWidth = options.outWidth


        //val file_size = Integer.parseInt(String.valueOf(file.length() / 1024))

        filExample = file
        initLocationApiGoogle()



        //task.execute(item)

        val request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(ResizeOptions(width, height))
                .build()
        val controller = Fresco.newDraweeControllerBuilder()
                .setOldController(imgvPhoto?.controller)
                .setImageRequest(request)
                .build()
        imgvPhoto?.controller = controller
    }

    override fun RegistryUserSelectedToEncode(filePhoto: File, dataGalleryPhoto: GalleryPhoto) {



    }


    override fun onConnected(p0: Bundle?) {
        System.out.println("Connected to GoogleApiClient");
        goAndDetectLocation();
    }

    override fun onConnectionSuspended(p0: Int) {
        System.out.println("Connection suspended");
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        System.out.println("Connection failed: ConnectionResult.getErrorCode() = " + p0.getErrorCode());
        startGoogleApiLocation()
    }

    private fun startGoogleApiLocation() {
        val googleAPI = GoogleApiAvailability.getInstance()
        val resultCode = googleAPI.isGooglePlayServicesAvailable(this)
        if (resultCode == ConnectionResult.SUCCESS) {
            mGoogleApiClient?.connect()

        } else {
            googleAPI.getErrorDialog(this, resultCode, RQS_GooglePlayServices)
            finish()
        }
    }

    private fun initLocationApiGoogle() {
        buildGoogleApiClient()
        createLocationRequest()
        buildLocationSettingsRequest()
    }



    @Synchronized fun buildGoogleApiClient() {
        Log.i("locationGoogle", "Building GoogleApiClient")
        mGoogleApiClient = GoogleApiClient.Builder(this).
                addConnectionCallbacks(this).
                addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build()
    }

    fun createLocationRequest() {
        mLocationRequest = LocationRequest()
        mLocationRequest?.interval = UPDATE_INTERVAL_IN_MILLISECONDS
        mLocationRequest?.fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
        mLocationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    protected fun buildLocationSettingsRequest() {
        var builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest)
        mLocationSettingsRequest = builder.build()
    }

    @SuppressLint("MissingPermission")
    fun goAndDetectLocation() {

        LocationServices.FusedLocationApi?.requestLocationUpdates(mGoogleApiClient, mLocationRequest, object : LocationListener {
            override fun onLocationChanged(p0: Location?) {

                if (p0?.accuracy!! <= 100) {
                    mCurrentLocation = p0
                    if (cont === 0)
                        //Snackbar.make(findViewById(R.id.constraintLayoutsnackbar), resources.getText(R.string.txt_geolocalizacion), Snackbar.LENGTH_LONG).show()

                    cont++
                }



                System.out.println("----------->Accuracy: " + p0.getAccuracy() + " - LOCATION: " + p0.getLatitude() + " , " + p0.getLongitude())

            }

        })?.setResultCallback { object : ResultCallback<Status> {
            override fun onResult(p0: Status) {

                mRequestingLocationUpdates = true

            }

        } }


//        LocationServices.FusedLocationApi?.requestLocationUpdates(mGoogleApiClient, mLocationRequest, object : LocationListener {
//            fun onLocationChanged(location: Location) {
//                if (location.getAccuracy() <= 100) {
//
//                    mCurrentLocation = location
//
//                    if (cont === 0)
//                        Snackbar.make(findViewById(R.id.constraintLayoutsnackbar), resources.getText(R.string.txt_geolocalizacion), Snackbar.LENGTH_LONG).show()
//
//                    cont++
//                }
//
//                System.out.println("----------->Accuracy: " + location.getAccuracy() + " - LOCATION: " + location.getLatitude() + " , " + location.getLongitude())
//            }
//        }).setResultCallback(object : ResultCallback<Status>() {
//            fun onResult(status: Status) {
//                mRequestingLocationUpdates = true
//            }
//        })
    }


}