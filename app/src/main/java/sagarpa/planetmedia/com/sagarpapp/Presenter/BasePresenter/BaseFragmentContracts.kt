package sagarpa.planetmedia.com.sagarpapp.Presenter.BasePresenter

import android.view.View

interface BaseFragmentContracts {

    fun showProgressDialog()
    fun hideProgressDialog()
    fun showDialogGeneric(title: String, message: String, codeError: String, btnAceptar: Boolean, btnCancelar: Boolean, styleDialogError: Boolean, clickListenerAceptar: View.OnClickListener, clickListenerCancelar: View.OnClickListener?)
    fun hideDialogGeneric()
    fun getVersionApi(): Boolean
    fun checkGPSEnable()

}