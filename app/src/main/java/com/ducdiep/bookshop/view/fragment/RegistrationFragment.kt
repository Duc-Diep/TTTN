package com.ducdiep.bookshop.view.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ducdiep.bookshop.R
import com.ducdiep.bookshop.models.Account
import com.ducdiep.bookshop.sql.SQLHelper
import kotlinx.android.synthetic.main.fragment_registration.view.*

class RegistrationFragment : Fragment() {
    lateinit var sqlHelper: SQLHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_registration, container, false)
        sqlHelper = SQLHelper(requireContext())
        view!!.edtRePassWord.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val password: String = view!!.edtPassWord.getText().toString()
                if (s.toString() == password) {
                    view!!.layoutedtRepassWord.setError(null)
                } else {
                    view!!.layoutedtRepassWord.setError(getString(R.string.check_equal))
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        view!!.btnBackToLogin.setOnClickListener(View.OnClickListener {
            val fragment: Fragment = LoginFragment.newInstance()
            val fragmentManager = activity?.supportFragmentManager
            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.layout_fragment_login, fragment)
            fragmentTransaction?.addToBackStack(null)
            fragmentTransaction?.commit()
        })
        view!!.btnRegister.setOnClickListener(View.OnClickListener {
            val phone: String = view!!.edtUserName.getText().toString()
            val password: String = view!!.edtPassWord.getText().toString()
            val repassword: String = view!!.edtRePassWord.getText().toString()
            val fullname: String = view!!.edtFullName.getText().toString()
            val address: String = view!!.edtAddress.getText().toString()
            if (phone.length > 0 && password.length > 0 && repassword == password && address.length > 0) {
                view!!.layoutCheckCode.setVisibility(View.VISIBLE)
                view!!.btnRegister.setText(getString(R.string.confirm))
                val code: String = view!!.edtConfirm.getText().toString()
                if (code.length > 0) {
                    val fragment: Fragment = LoginFragment.newInstance()
                    val fragmentManager = activity?.supportFragmentManager
                    val fragmentTransaction = fragmentManager?.beginTransaction()
                    fragmentTransaction?.replace(R.id.layout_fragment_login, fragment)
                    fragmentTransaction?.addToBackStack(null)
                    fragmentTransaction?.commit()
                    Toast.makeText(
                        context,
                        getString(R.string.check_sucess_true),
                        Toast.LENGTH_SHORT
                    ).show()
                    sqlHelper.InsertAccount(Account(0,phone, password, fullname, address))
                } else {
                    Toast.makeText(
                        context,
                        getString(R.string.check_confirm_code),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(context, getString(R.string.check_sucess_false), Toast.LENGTH_SHORT)
                    .show()
            }
        })
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            RegistrationFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}