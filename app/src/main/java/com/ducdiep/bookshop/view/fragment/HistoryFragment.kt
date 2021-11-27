package com.ducdiep.bookshop.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ducdiep.bookshop.R
import com.ducdiep.bookshop.adapter.HistoryAdapter
import com.ducdiep.bookshop.event.IonClickBook
import com.ducdiep.bookshop.models.Book
import com.ducdiep.bookshop.sql.SQLHelper
import kotlinx.android.synthetic.main.fragment_history.view.*
import org.greenrobot.eventbus.EventBus
import java.util.*
import kotlin.collections.ArrayList

class HistoryFragment : Fragment() {
    var sqlHelper: SQLHelper? = null
    var listHistory: ArrayList<Book>? = null
    var allBook:ArrayList<Book>? = null
    var adapter: HistoryAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_history, container, false)
        listHistory = ArrayList()
        sqlHelper = SQLHelper(requireContext())
        listHistory = sqlHelper!!.getAllBookInHistory() as ArrayList<Book>?
        allBook = sqlHelper!!.getAllBook() as ArrayList<Book>?
        adapter = HistoryAdapter(requireContext(),listHistory!!)
        adapter!!.setIonClickBook(object : IonClickBook {


            override fun onClickItem(book: Book) {
                val fragment: Fragment = BookItemInforFragment.newInstance(allBook!!, book)
                val fragmentManager = activity!!.supportFragmentManager
                fragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        })
        val gridLayoutManager = GridLayoutManager(context, 3, RecyclerView.VERTICAL, false)
        view!!.list_history.setAdapter(adapter)
        view!!.list_history.setLayoutManager(gridLayoutManager)
        view!!.btnDeleteHistory.setOnClickListener(View.OnClickListener {
            Toast.makeText(context, getString(R.string.delete_success), Toast.LENGTH_SHORT)
            sqlHelper!!.deleteHistory()
            listHistory!!.clear()
            adapter!!.notifyDataSetChanged()
        })
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HistoryFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(context)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(context)

    }
}