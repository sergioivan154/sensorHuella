package sagarpa.planetmedia.com.sagarpapp.Presenter.BasePresenter

import android.view.View

interface BaseActivityContracts {

    fun showProgressDialog()
    fun hideProgressDialog()
    fun getVersionApi(): Boolean
    fun requestPermission()
    fun crateProgressDialog()
    fun showDialogGeneric(title: String, message: String, codeError: String, btnAceptar: Boolean, btnCancelar: Boolean, styleDialogError: Boolean, clickListenerAceptar: View.OnClickListener, clickListenerCancelar: View.OnClickListener?)
    fun hideDialogGeneric()

}