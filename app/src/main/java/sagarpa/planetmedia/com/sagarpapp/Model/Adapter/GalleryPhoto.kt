package sagarpa.planetmedia.com.sagarpapp.Model.Adapter

import sagarpa.planetmedia.com.sagarpapp.Utility.KeyDictionary
import java.io.Serializable

data class GalleryPhoto(val sNameImage: String = KeyDictionary.stringDefault,
                        val sDateImage: String = KeyDictionary.stringDefault,
                        val sTimeImage: String = KeyDictionary.stringDefault,
                        val dLatitudImage: Double = KeyDictionary.doubleDefault,
                        val dLongitudImage: Double = KeyDictionary.doubleDefault,
                        val iSizeImage: Int = KeyDictionary.intDefault,
                        val iWithImage: Int = KeyDictionary.intDefault,
                        val iHeightImage: Int = KeyDictionary.intDefault,
                        val sNameFolder: String = KeyDictionary.stringDefault) : Serializable