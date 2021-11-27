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
import com.ducdiep.bookshop.event.EHome
import com.ducdiep.bookshop.models.Account
import com.ducdiep.bookshop.sql.SQLHelper
import kotlinx.android.synthetic.main.fragment_login.view.*
import org.greenrobot.eventbus.EventBus
import java.util.*

class LoginFragment : Fragment() {
    var list: List<Account>? = null
    var sqlHelper: SQLHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_login, container, false)
        list = ArrayList()
        sqlHelper = SQLHelper(requireContext())
        list = sqlHelper!!.getAllAccount()
        view.btnLogin.setOnClickListener(View.OnClickListener {
            val username: String = view.edtUserName.getText().toString()
            val password: String = view.edtPasWord.getText().toString()
            if (username.length > 0 && password.length > 0) {
                var check = false
                for (x in list!!) {
                    if (x.phone.equals(username) && x.password.equals(password)) {
                        EventBus.getDefault().post(EHome())
                        val sharedPreferences =
                            context?.getSharedPreferences(SHARE_PRE_NAME, Context.MODE_PRIVATE)
                        val editor = sharedPreferences?.edit()
                        editor?.putBoolean(ACCOUNT_STATUS, true)
                        editor?.putString(ACCOUNT_FULL_NAME, x.fullName)
                        editor?.putString(ACCOUNT_PHONE, x.phone)
                        editor?.putString(ACCOUNT_ADDRESS, x.address)
                        editor?.apply()
                        check = true
                        Toast.makeText(
                            context,
                            getString(R.string.login_sucess),
                            Toast.LENGTH_SHORT
                        ).show()
                        break
                    }
                }
                if (!check) {
                    Toast.makeText(
                        context,
                        getString(R.string.check_login_fail),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(context, getString(R.string.check_null), Toast.LENGTH_SHORT).show()
            }
        })
        view.newAcc.setOnClickListener(View.OnClickListener {
            val fragment: Fragment = RegistrationFragment.newInstance()
            val fragmentManager = activity?.supportFragmentManager
            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.layout_fragment_login, fragment)
            fragmentTransaction?.addToBackStack(null)
            fragmentTransaction?.commit()
        })
        view.btnBackToHomeFragment.setOnClickListener(View.OnClickListener {
            EventBus.getDefault().post(EHome())
        })
        return view
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
            LoginFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}