package id.buaja.githubuser.view.ui.main

import com.google.gson.JsonObject
import id.buaja.githubuser.network.base.BaseConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject

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
                        val replace = if (next.isNullOrEmpty()) {
                            ""
                        } else {
                            next.replace("""[<>;]""".toRegex(), "")
                        }
                        val split = replace.split(" rel=\"next\", ")
                        val hasil = split[0]
                        val nextPage = hasil.replace("https://api.github.com/search/users?q=$search&page=", "")

                        view.onNextPage(nextPage)
                    }

                    422 -> {
                        view.onMessage(response.body()?.message)
                    }

                    400 -> {
                        view.onMessage(response.body()?.message)
                    }

                    403 -> {
                        view.onMessage(response.body()?.message)
                    }
                }
                view.hideLoading()
            } catch (e: Exception) {
                view.onError(e.message)
                view.hideLoading()
            }
        }
    }

    fun getUserNext(q: String?, page: String?) {
        GlobalScope.launch(Dispatchers.Main) {
            view.showLoading()
            try {
                val config = config().getUserNext(q, page)
                val response = config.await()

                when(response.code()) {
                    200 -> {
                        view.onSuccessNext(response.body()?.items)

                        val next = response.headers().get("Link")
                        val replace = if (next.isNullOrEmpty()) {
                            ""
                        } else {
                            next.replace("""[<>;]""".toRegex(), "")
                        }
                        val split = replace.split(" rel=\"prev\", ")
                        val hasil = split[1]
                        val hasilNext = hasil.split(" rel=\"next\", ")
                        val nextSplit = hasilNext[0]
                        val nextPage = nextSplit.replace("https://api.github.com/search/users?q=$q&page=", "")

                        view.onNextPage(nextPage)
                    }

                    422 -> {
                        view.onMessage(response.body()?.message)
                    }

                    400 -> {
                        view.onMessage(response.body()?.message)
                    }

                    403 -> {
                        response.errorBody()?.let {
                            val jsonObject = JSONObject(it.string())
                            view.onMessage(jsonObject.getString("message"))
                        }
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