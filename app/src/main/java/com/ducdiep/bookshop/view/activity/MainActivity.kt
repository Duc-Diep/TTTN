package com.ducdiep.bookshop.view.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.ducdiep.bookshop.R
import com.ducdiep.bookshop.event.*
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : AppCompatActivity() {
    var mAppBarConfiguration: AppBarConfiguration? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //set ToolBar
        setSupportActionBar(toolbar)
        mAppBarConfiguration = AppBarConfiguration.Builder(
            R.id.nav_home,
            R.id.nav_cart,
            R.id.nav_history,
            R.id.nav_other_product,
            R.id.nav_account,
            R.id.nav_introduce
        )
            .setDrawerLayout(drawer_layout)
            .build()
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration!!)
        NavigationUI.setupWithNavController(nav_view, navController)
    }

    //gọi cái navigation ra
    override fun onSupportNavigateUp(): Boolean {
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        return (NavigationUI.navigateUp(navController, mAppBarConfiguration!!)
                || super.onSupportNavigateUp())
    }

    @JvmName("Event1")
    @Subscribe(threadMode = ThreadMode.POSTING)
    fun Event(eHideToolBar: EHideToolbar?) {
        toolbar.setVisibility(View.GONE)
    }

    @JvmName("Event2")
    @Subscribe(threadMode = ThreadMode.POSTING)
    fun Event(eShowToolBar: EShowToolBar?) {
        toolbar.setVisibility(View.VISIBLE)
    }

    @JvmName("Event3")
    @Subscribe(threadMode = ThreadMode.POSTING)
    fun Event(eLogin: ELogin?) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    @JvmName("Event4")
    @Subscribe(threadMode = ThreadMode.POSTING)
    fun Event(eCloseApp: ECloseApp?) {
        Intent()
    }

    @JvmName("Event5")
    @Subscribe(threadMode = ThreadMode.POSTING)
    fun Event(eCall: ECall?) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:" + getString(R.string.phone_introduce))
        startActivity(intent)
    }
}