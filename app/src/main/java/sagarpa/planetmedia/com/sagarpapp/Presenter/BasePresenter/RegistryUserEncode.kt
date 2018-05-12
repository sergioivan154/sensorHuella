package sagarpa.planetmedia.com.sagarpapp.Presenter.BasePresenter

import sagarpa.planetmedia.com.sagarpapp.Model.Adapter.GalleryPhoto
import java.io.File

interface RegistryUserEncode {

    fun RegistryUserSelectedToEncode(filePhoto: File, dataGalleryPhoto: GalleryPhoto)

}