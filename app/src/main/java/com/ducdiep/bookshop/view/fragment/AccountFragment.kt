package com.ducdiep.bookshop.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ducdiep.bookshop.R
import com.ducdiep.bookshop.config.AccountAttribute.ACCOUNT_ADDRESS
import com.ducdiep.bookshop.config.AccountAttribute.ACCOUNT_FULL_NAME
import com.ducdiep.bookshop.config.AccountAttribute.ACCOUNT_PHONE
import com.ducdiep.bookshop.config.AccountAttribute.ACCOUNT_STATUS
import com.ducdiep.bookshop.config.AccountAttribute.SHARE_PRE_NAME
import com.ducdiep.bookshop.config.Internet
import com.ducdiep.bookshop.event.Bus
import com.ducdiep.bookshop.event.ELogin
import kotlinx.android.synthetic.main.fragment_account.view.*
import org.greenrobot.eventbus.EventBus

class AccountFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_account, container, false)
        if (getStatus()) {
            val sharedPreferences =
                context?.getSharedPreferences(SHARE_PRE_NAME, Context.MODE_PRIVATE)
            val fullname = sharedPreferences?.getString(ACCOUNT_FULL_NAME, "")
            val phone = sharedPreferences?.getString(ACCOUNT_PHONE, "")
            val addresss = sharedPreferences?.getString(ACCOUNT_ADDRESS, "")
            //
            view!!.avatar.setVisibility(View.VISIBLE)
            view!!.acc_name.setVisibility(View.VISIBLE)
            view!!.layout_pic.setVisibility(View.VISIBLE)
            view!!.layout_address.setVisibility(View.VISIBLE)
            view!!.layout_phone.setVisibility(View.VISIBLE)
            view!!.isLogin.setVisibility(View.GONE)
            //
            view!!.acc_name.setText(fullname)
            view!!.phone_number.setText(phone)
            view!!.address.setText(addresss)
            view!!.btnLoginAndLogout.setText(getString(R.string.sign_out))
        } else {
            view!!.btnLoginAndLogout.setText(getString(R.string.sign_in))
        }
        view!!.btnLoginAndLogout.setOnClickListener(View.OnClickListener {
            if (Internet.checkConnection(requireContext())) {
                if (getStatus()) {
                    view!!.avatar.setVisibility(View.GONE)
                    view!!.layout_pic.setVisibility(View.GONE)
                    view!!.layout_address.setVisibility(View.GONE)
                    view!!.layout_phone.setVisibility(View.GONE)
                    view!!.isLogin.setVisibility(View.VISIBLE)
                    view!!.acc_name.setVisibility(View.GONE)
                    view!!.btnLoginAndLogout.setText(getString(R.string.sign_in))
                    val sharedPreferences =
                        context?.getSharedPreferences(SHARE_PRE_NAME, Context.MODE_PRIVATE)
                    val editor = sharedPreferences?.edit()
                    editor?.putBoolean(ACCOUNT_STATUS, false)
                    editor?.apply()
                    Toast.makeText(context, getString(R.string.logout_success), Toast.LENGTH_SHORT)
                        .show()
                } else {
                    EventBus.getDefault().post(ELogin())
                }
            } else {
                Toast.makeText(context, getString(R.string.check_internet), Toast.LENGTH_SHORT)
                    .show()
            }
        })
        return view
    }

    fun getStatus(): Boolean {
        val sharedPreferences = context?.getSharedPreferences(SHARE_PRE_NAME, Context.MODE_PRIVATE)
        return sharedPreferences!!.getBoolean(ACCOUNT_STATUS, false)
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(context)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(context)

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            AccountFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}