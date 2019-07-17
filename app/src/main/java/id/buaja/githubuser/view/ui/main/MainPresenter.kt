package id.buaja.githubuser.view.ui.main

import id.buaja.githubuser.network.base.BaseConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainPresenter(private val view: MainView) : BaseConfig() {
    fun getUser(search: String?) {
        GlobalScope.launch(Dispatchers.Main) {
            view.showLoading()
            try {
                val config = config().getUser(search)
                val response = config.await()

                when(response.code()) {
                    200 -> {
                        view.onSuccess(response.body()?.items)

                        val next = response.headers().get("Link")
                        val link = if (next.isNullOrEmpty()) {
                            ""
                        } else {
                            next
                        }
                        view.onNextPage(link)
                    }

                    422 -> {
                        view.onUnprocessableEntity(response.body()?.message)
                    }

                    400 -> {
                        view.onBadRequest(response.body()?.message)
                    }
                }
                view.hideLoading()
            } catch (e: Exception) {
                view.onError(e.message)
                view.hideLoading()
            }
        }
    }
}