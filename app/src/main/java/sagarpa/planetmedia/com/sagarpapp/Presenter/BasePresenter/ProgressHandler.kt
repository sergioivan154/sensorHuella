package sagarpa.planetmedia.com.sagarpapp.Presenter.BasePresenter

interface ProgressHandler {

    abstract fun setTotal(tot: Int)

    abstract fun increment(inc: Int)

    abstract fun finished()

}