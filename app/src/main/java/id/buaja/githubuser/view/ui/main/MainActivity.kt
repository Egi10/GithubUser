package id.buaja.githubuser.view.ui.main

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import id.buaja.githubuser.R
import id.buaja.githubuser.adapter.ItemsAdapter
import id.buaja.githubuser.network.model.ItemsItem
import id.buaja.githubuser.untils.EndlessSrolling
import id.buaja.githubuser.view.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton

class MainActivity : BaseActivity(), MainView {
    private lateinit var mainPresenter: MainPresenter
    private var listItems: MutableList<ItemsItem> = mutableListOf()
    private lateinit var itemsAdapter: ItemsAdapter
    private var q: String = ""
    private var page: String = ""

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
                q = p0.toString()
                mainPresenter.getUser(p0.toString())

                rvList.addOnScrollListener(scrollData())
            }
        })

        swipeRefresh.post {
            loadData()
        }

        swipeRefresh.setOnRefreshListener {
            swipeRefresh.isRefreshing = false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.info -> {

                true
            }
            R.id.setting -> {

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun loadData() {
        itemsAdapter = ItemsAdapter(baseContext, listItems) {

        }
        rvList.layoutManager = LinearLayoutManager(baseContext)
        rvList.adapter = itemsAdapter
        GlobalScope.launch {
            delay(2000)
            rvList.addOnScrollListener(scrollData())
        }
    }

    private fun scrollData(): EndlessSrolling {
        return object : EndlessSrolling() {
            override fun onLoadMore() {
                mainPresenter.getUserNext(q, page)
            }
        }
    }

    override fun onSuccess(list: List<ItemsItem>?) {
        listItems.clear()
        list?.let {
            listItems.addAll(it)
        }
        itemsAdapter.notifyDataSetChanged()
    }

    override fun onSuccessNext(list: List<ItemsItem>?) {
        list?.let {
            listItems.addAll(it)
        }
        itemsAdapter.notifyDataSetChanged()
    }

    override fun onMessage(message: String?) {
        alert(message.toString()) {
            yesButton {
                it.dismiss()
            }
            isCancelable = false
        }.show()
    }

    override fun onNextPage(q: String?) {
        q?.let {
            page = it
            Log.d("Sukses Page", page)
        }
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
