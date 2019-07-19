package id.buaja.githubuser.view.ui.main

import id.buaja.githubuser.network.model.ItemsItem
import id.buaja.githubuser.view.base.BaseView

interface MainView : BaseView {
    fun onSuccess(list: List<ItemsItem>?)
    fun onSuccessNext(list: List<ItemsItem>?)
    fun onMessage(message: String?)
    fun onUnprocessableEntity()
    fun onNextPage(q: String?)
    fun onNoPage()
}