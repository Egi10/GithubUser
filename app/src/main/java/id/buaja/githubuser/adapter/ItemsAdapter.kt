package id.buaja.githubuser.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import id.buaja.githubuser.R
import id.buaja.githubuser.network.model.ItemsItem
import kotlinx.android.synthetic.main.layout_list_item.view.*

class ItemsAdapter(private val context: Context, private val item: List<ItemsItem>, private val listener: (ItemsItem) -> Unit)
    : RecyclerView.Adapter<ItemsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_list_item, parent, false))

    override fun getItemCount(): Int {
        return item.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(item[position], listener)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        @SuppressLint("CheckResult")
        fun bindItem(itemsItem: ItemsItem, listener: (ItemsItem) -> Unit) {
            itemView.tvName.text = itemsItem.login
            itemView.tvUrl.text = itemsItem.url

            Picasso.get()
                .load(itemsItem.avatarUrl)
                .placeholder(R.drawable.no_image)
                .into(itemView.ivImage)

            itemView.setOnClickListener {
                listener(itemsItem)
            }
        }
    }
}