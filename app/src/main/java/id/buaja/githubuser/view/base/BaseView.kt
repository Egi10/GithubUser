package id.buaja.githubuser.view.base

interface BaseView {
    fun showLoading()
    fun onError(error: String?)
    fun hideLoading()
}