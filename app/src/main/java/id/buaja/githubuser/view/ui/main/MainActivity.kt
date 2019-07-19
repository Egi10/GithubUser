package id.buaja.githubuser.view.ui.main

import android.content.Intent
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import id.buaja.githubuser.R
import id.buaja.githubuser.adapter.ItemsAdapter
import id.buaja.githubuser.network.model.ItemsItem
import id.buaja.githubuser.untils.EndlessRecyclerViewScrollListener
import id.buaja.githubuser.view.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.toast
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
                Handler().postDelayed({
                    Log.d("Sukses onText", p0.toString())
                    q = p0.toString()
                    loadData(q)
                }, 1000)
            }
        })

        swipeRefresh.post {
            if(q.isNotEmpty()) {
                loadData(q)
            }
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
                alert("GitHub adalah layanan penginangan web bersama untuk proyek " +
                        "pengembangan perangkat lunak yang menggunakan sistem pengontrol versi " +
                        "Git dan layanan hosting internet. Hal ini banyak digunakan untuk kode " +
                        "komputer. Wikipedia") {
                    yesButton {
                        it.dismiss()
                    }
                }.show()
                true
            }
            R.id.setting -> {
                toast("Belum Ada Menu")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun loadData(q: String?) {
        mainPresenter.getUser(q)
        itemsAdapter = ItemsAdapter(baseContext, listItems) {

        }
        val layoutManager = LinearLayoutManager(baseContext)
        rvList.layoutManager = layoutManager
        rvList.adapter = itemsAdapter
        Handler().postDelayed({
            rvList.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager) {
                override fun onLoadMore() {
                    mainPresenter.getUserNext(q, page)
                }

            })
        }, 3000)
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

    override fun onUnprocessableEntity() {
        listItems.clear()
        itemsAdapter.notifyDataSetChanged()
    }

    override fun onNextPage(q: String?) {
        q?.let {
            page = it
            Log.d("Sukses Page", page)
        }
    }

    override fun onNoPage() {
        Log.d("Page", "No Page")
        q = ""
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

    // onBack
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            alert("Apakah yain anda akan keluar ?") {
                yesButton {
                    val exit = Intent(Intent.ACTION_MAIN)
                    exit.addCategory(Intent.CATEGORY_HOME)
                    exit.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(exit)
                }

                noButton {
                    it.dismiss()
                }
            }.show()
        }
        return super.onKeyDown(keyCode, event)
    }
}
