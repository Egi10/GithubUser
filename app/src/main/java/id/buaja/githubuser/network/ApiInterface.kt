package id.buaja.githubuser.network

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {
    @GET("search/users")
    fun getUser(
        @Query("q") search: String?
    ): Deferred<Response<ApiResponse>>

    @GET("search/users")
    fun getUserNext(
        @Query("q") q: String?,
        @Query("page") page: String?
    ): Deferred<Response<ApiResponse>>
}