package sagarpa.planetmedia.com.sagarpapp.Model.Adapter

import android.graphics.Bitmap
import sagarpa.planetmedia.com.sagarpapp.Utility.KeyDictionary
import java.io.File
import java.io.Serializable

data class GalleryPhoto(val filePhoto: File,
                        val sNameImage: String = KeyDictionary.stringDefault,
                        val sDateImage: String = KeyDictionary.stringDefault,
                        val sTimeImage: String = KeyDictionary.stringDefault,
                        val dLatitudImage: Double = KeyDictionary.doubleDefault,
                        val dLongitudImage: Double = KeyDictionary.doubleDefault,
                        val iSizeImage: Long = KeyDictionary.longDefault,
                        val iWithImage: Int = KeyDictionary.intDefault,
                        val iHeightImage: Int = KeyDictionary.intDefault) : Serializable