package id.buaja.githubuser.network

import com.google.gson.annotations.SerializedName
import id.buaja.githubuser.network.model.ItemsItem

class ApiResponse (
    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("total_count")
    val totalCount: Int? = null,

    @field:SerializedName("incomplete_results")
    val incompleteResults: Boolean? = null,

    @field:SerializedName("items")
    val items: List<ItemsItem>? = null
)