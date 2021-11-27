package com.ducdiep.bookshop.view.fragment

import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ducdiep.bookshop.R
import com.ducdiep.bookshop.adapter.BookAdapter
import com.ducdiep.bookshop.event.EShowToolBar
import com.ducdiep.bookshop.event.IonClickBook
import com.ducdiep.bookshop.models.Book
import kotlinx.android.synthetic.main.fragment_list_book.view.*
import org.greenrobot.eventbus.EventBus
import java.util.*
import kotlin.collections.ArrayList

class ListBookFragment : Fragment() {
    val me: Fragment = this
    var listBook: List<Book>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            listBook = it.getSerializable("list") as List<Book>?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_list_book, container, false)
        view!!.btnBackToHome.setOnClickListener(View.OnClickListener {
            EventBus.getDefault().post(EShowToolBar())
            val manager = activity?.supportFragmentManager
            val transaction = manager?.beginTransaction()
            manager?.backStackEntryCount
            transaction?.remove(me)
            transaction?.commit()
        })
        val adapter = BookAdapter(requireContext(),listBook!!)
        adapter.setIonClickBook(object : IonClickBook {
            override fun onClickItem(book: Book) {
                val fragment: Fragment = BookItemInforFragment.newInstance(listBook as ArrayList<Book>, book)
                val fragmentManager = activity!!.supportFragmentManager
                fragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        })

        val gridLayoutManager = GridLayoutManager(context, 3, RecyclerView.VERTICAL, false)
        view!!.list_book.setAdapter(adapter)
        view!!.list_book.setLayoutManager(gridLayoutManager)
        view!!.edt_search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                val templist: MutableList<Book> = ArrayList()
                for (i in listBook!!.indices) {
                    if (listBook!![i].title.toLowerCase()
                            .contains(s.toString().toLowerCase())
                    ) {
                        templist.add(listBook!![i]!!)
                    }
                }

                val adapter = BookAdapter(requireContext(), templist)
                val gridLayoutManager = GridLayoutManager(context, 3, RecyclerView.VERTICAL, false)
                view!!.list_book.setAdapter(adapter)
                view!!.list_book.setLayoutManager(gridLayoutManager)
                adapter.setIonClickBook(object : IonClickBook {
                    override fun onClickItem(book: Book) {
                        val fragment: Fragment = BookItemInforFragment.newInstance(listBook as ArrayList<Book>, book)
                        val fragmentManager = activity!!.supportFragmentManager
                        fragmentManager.beginTransaction()
                            .replace(R.id.nav_host_fragment, fragment)
                            .addToBackStack(null)
                            .commit()
                    }
                })
            }
        })
        return view
    }

    companion object {

        @JvmStatic
        fun newInstance(list: List<Book>) =
            ListBookFragment().apply {
                arguments = Bundle().apply {
                    this.putParcelableArrayList("list", list as ArrayList<out Parcelable?>?)
                }
            }
    }
    override fun onStart() {
        super.onStart()
//        EventBus.getDefault().register(context)
    }

    override fun onStop() {
        super.onStop()
//        EventBus.getDefault().unregister(context)

    }
}