package com.ducdiep.bookshop.view.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ducdiep.bookshop.R
import com.ducdiep.bookshop.adapter.CartAdapter
import com.ducdiep.bookshop.config.AccountAttribute.ACCOUNT_STATUS
import com.ducdiep.bookshop.config.AccountAttribute.SHARE_PRE_NAME
import com.ducdiep.bookshop.config.Internet
import com.ducdiep.bookshop.event.EHideToolbar
import com.ducdiep.bookshop.event.ELogin
import com.ducdiep.bookshop.event.IonClickBook
import com.ducdiep.bookshop.event.IonClickDelete
import com.ducdiep.bookshop.models.Book
import com.ducdiep.bookshop.sql.SQLHelper
import kotlinx.android.synthetic.main.fragment_cart.view.*
import org.greenrobot.eventbus.EventBus
import java.text.DateFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class CartFragment : Fragment() {
    var sqlHelper: SQLHelper? = null
    var listCart: ArrayList<Book>? = null
    var allBook:List<Book>? = null
    var adapter: CartAdapter? = null
    var pos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_cart, container, false)

        listCart = ArrayList()
        sqlHelper = SQLHelper(requireContext())

        allBook = sqlHelper!!.getAllBook()
        setLayout()

        val gridLayoutManager = GridLayoutManager(context, 3, RecyclerView.VERTICAL, false)
        view?.listBookInCart?.setAdapter(adapter)
        view?.listBookInCart?.setLayoutManager(gridLayoutManager)
        val local = Locale("vi", "VN")
        val numberFormat = NumberFormat.getInstance(local)
        val money = numberFormat.format(TotalMoney())
        if (TotalMoney() != 0.0) {
//            Toast.makeText(getContext(), getString(R.string.delete_item_cart), Toast.LENGTH_SHORT).show();
            view?.btnBuy?.setText(getString(R.string.buy) + " " + money + " vnđ")
        } else {
            view?.btnBuy?.setText(getString(R.string.nothing))
        }
        view?.btnBuy?.setOnClickListener(View.OnClickListener {
            if (Internet.checkConnection(requireContext())) {
                if (getStatus()) {
                    if (listCart!!.size > 0) {
                        onDialogOptionBuyShow()
                    } else {
                        Toast.makeText(context, getString(R.string.nothing), Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    onDialogLoginShow()
                }
            } else {
                Toast.makeText(context, getString(R.string.check_internet), Toast.LENGTH_SHORT)
                    .show()
            }
        })
        return view
    }

    fun setLayout() {
        listCart = sqlHelper!!.getAllBookInCart() as ArrayList<Book>?
        Log.d("Cart", "setLayout: $listCart")
        adapter = CartAdapter( requireContext(),listCart!!)

        adapter!!.setIonClickBook(object : IonClickBook {

            override fun onClickItem(book: Book) {
                EventBus.getDefault().post(EHideToolbar())
                val fragment: Fragment = BookItemInforFragment.newInstance(allBook as ArrayList<Book>,
                    book!!
                )
                val fragmentManager = activity!!.supportFragmentManager
                fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        })
        adapter!!.setIonClickDelete(object : IonClickDelete {
            override fun onClickItem(book: Book) {
                    onDialogShow(book)
            }

        })
    }

    private fun getPosOfBook(book: Book): Int {
        for (i in listCart!!.indices) {
            if (listCart!![i].id === book.id) {
                return i
            }
        }
        return -1
    }

    private fun TotalMoney(): Double {
        var price = 0.0
        for (x in listCart!!) {
            price += x.price
        }
        return price
    }

    private fun onDialogShow(book: Book) {
        val alertDialog = AlertDialog.Builder(context)
            .setTitle(getString(R.string.confirm))
            .setMessage(getString(R.string.delete_item_request))
            .setPositiveButton(
                getString(R.string.ok)
            ) { dialog, which ->
                Toast.makeText(context, getString(R.string.delete_success), Toast.LENGTH_SHORT)
                    .show()
                sqlHelper!!.deleteItemInCart(java.lang.String.valueOf(book.id))
                listCart?.removeAt(getPosOfBook(book))
                adapter?.notifyDataSetChanged()
                //setLayout();
            }
            .setNegativeButton(
                getString(R.string.cancel)
            ) { dialog, which -> }.create()
        alertDialog.show()
    }

    private fun onDialogLoginShow() {
        val alertDialog: AlertDialog = AlertDialog.Builder(context)
            .setTitle(getString(R.string.un_login))
            .setMessage(getString(R.string.sign_in_now))
            .setIcon(R.drawable.user)
            .setPositiveButton(getString(R.string.ok),
                DialogInterface.OnClickListener { dialog, which ->
                    EventBus.getDefault().post(ELogin())
                })
            .setNegativeButton(getString(R.string.cancel),
                DialogInterface.OnClickListener { dialog, which -> }).create()
        alertDialog.show()
    }

    private fun onDialogOptionBuyShow() {
        val booleans = booleanArrayOf(true, false, false, false)
        val strings = Arrays.asList(*resources.getStringArray(R.array.option_buy))
        val alertDialog: AlertDialog = AlertDialog.Builder(context)
            .setTitle(getString(R.string.select_option_buy)) //.setMessage("Yes or No")
            .setIcon(R.drawable.pay)
            .setSingleChoiceItems(R.array.option_buy, 0,
                DialogInterface.OnClickListener { dialog, position -> pos = position })
            .setPositiveButton(getString(R.string.ok),
                DialogInterface.OnClickListener { dialog, which ->
                    val df: DateFormat = SimpleDateFormat("HH:mm dd/MM/yyyy")
                    val date = df.format(Calendar.getInstance().time)
                    Log.d("Date", "onDialogOptionBuyShow: $date")
                    sqlHelper!!.deleteCart()
                    for (x in listCart!!) {
                        x.dateBuy = date
                        sqlHelper!!.InsertBookToHistory(x)
                    }
                    listCart!!.clear()
                    val adapter = CartAdapter(requireContext(), listCart!!)
                    view?.listBookInCart?.setAdapter(adapter)
                    view?.btnBuy?.setText(getString(R.string.nothing))
                    Log.d("LOG", which.toString())
                    Toast.makeText(
                        context,
                        "\t\t\t\t\t\t" + getString(R.string.your_choice) + " " + strings[pos],
                        Toast.LENGTH_SHORT
                    ).show()
                })
            .setNegativeButton(getString(R.string.cancel),
                DialogInterface.OnClickListener { dialog, which ->
                    Toast.makeText(
                        context,
                        getString(R.string.delete_trans),
                        Toast.LENGTH_SHORT
                    ).show()
                }).create()
        alertDialog.show()
    }

    fun getStatus(): Boolean {
        val sharedPreferences = context?.getSharedPreferences(SHARE_PRE_NAME, Context.MODE_PRIVATE)
        return sharedPreferences?.getBoolean(ACCOUNT_STATUS, false)!!
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
        fun newInstance(param1: String, param2: String) =
            CartFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}