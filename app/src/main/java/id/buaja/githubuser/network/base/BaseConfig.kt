package id.buaja.githubuser.network.base

import id.buaja.githubuser.network.ApiConfig
import id.buaja.githubuser.network.ApiInterface

open class BaseConfig {
    fun config(): ApiInterface {
        return ApiConfig.config()
    }
}