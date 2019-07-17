package id.buaja.githubuser.view.ui.main

import id.buaja.githubuser.network.model.ItemsItem
import id.buaja.githubuser.view.base.BaseView

interface MainView : BaseView {
    fun onSuccess(list: List<ItemsItem>?)
    fun onUnprocessableEntity(message: String?)
    fun onBadRequest(message: String?)
    fun onNextPage(link: String?)
}