package id.buaja.githubuser.view.ui.main

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import id.buaja.githubuser.R
import id.buaja.githubuser.adapter.ItemsAdapter
import id.buaja.githubuser.network.model.ItemsItem
import id.buaja.githubuser.view.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), MainView {
    private lateinit var mainPresenter: MainPresenter
    private var listItems: MutableList<ItemsItem> = mutableListOf()
    private lateinit var itemsAdapter: ItemsAdapter

    override fun contentView(): Int {
        return R.layout.activity_main
    }

    override fun onCreated() {
        setSupportActionBar(toolbar)
        title = "Github User"

        mainPresenter = MainPresenter(this)

        etAutoComplite.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                Log.d("Sukses After", p0.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.d("Sukses before", p0.toString())
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.d("Sukses onText", p0.toString())
                mainPresenter.getUser(p0.toString())
            }
        })

        swipeRefresh.post {
            loadData()
        }

        swipeRefresh.setOnRefreshListener {
            swipeRefresh.isRefreshing = false
        }
    }

    private fun loadData() {
        itemsAdapter = ItemsAdapter(baseContext, listItems) {

        }
        rvList.layoutManager = LinearLayoutManager(baseContext)
        rvList.adapter = itemsAdapter
    }

    override fun onSuccess(list: List<ItemsItem>?) {
        listItems.clear()
        list?.let {
            listItems.addAll(it)
        }
        itemsAdapter.notifyDataSetChanged()
    }

    override fun onUnprocessableEntity(message: String?) {
        listItems.clear()
        itemsAdapter.notifyDataSetChanged()
    }

    override fun onBadRequest(message: String?) {

    }

    override fun onNextPage(link: String?) {
        Log.d("Sukses", link.toString())
    }

    override fun showLoading() {
        swipeRefresh.isRefreshing = true
    }

    override fun onError(error: String?) {
        Log.d("Error", error.toString())
    }

    override fun hideLoading() {
        swipeRefresh.isRefreshing = false
    }
}
