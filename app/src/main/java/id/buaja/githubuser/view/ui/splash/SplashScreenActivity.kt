package id.buaja.githubuser.view.ui.splash

import android.view.WindowManager
import id.buaja.githubuser.R
import id.buaja.githubuser.view.base.BaseActivity
import id.buaja.githubuser.view.ui.main.MainActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.anko.startActivity

class SplashScreenActivity : BaseActivity() {
    override fun contentView(): Int {
        return R.layout.activity_splash_screen
    }

    override fun onCreated() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        GlobalScope.launch {
            delay(3000)

            startActivity<MainActivity>()
            finish()
        }
    }
}
