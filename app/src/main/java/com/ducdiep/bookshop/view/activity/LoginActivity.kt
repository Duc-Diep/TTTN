package com.ducdiep.bookshop.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ducdiep.bookshop.R
import com.ducdiep.bookshop.event.EHome
import com.ducdiep.bookshop.view.fragment.LoginFragment
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        getFragment(LoginFragment.newInstance())
    }

    fun getFragment(fragment: Fragment?) {
        try {
            supportFragmentManager.beginTransaction().replace(
                R.id.layout_fragment_login,
                fragment!!
            ).commit()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("LoginActivity.TAG", "getFragment" + e.message)
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    fun Event(eHome: EHome?) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}