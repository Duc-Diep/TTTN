package com.ducdiep.bookshop.view.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.ducdiep.bookshop.R
import com.ducdiep.bookshop.adapter.BookAdapter
import com.ducdiep.bookshop.config.BookAttributes.BOOK_AUTHOR
import com.ducdiep.bookshop.config.BookAttributes.BOOK_CATEGOTY
import com.ducdiep.bookshop.config.BookAttributes.BOOK_DESCRIPTION
import com.ducdiep.bookshop.config.BookAttributes.BOOK_ID
import com.ducdiep.bookshop.config.BookAttributes.BOOK_IMAGELINK
import com.ducdiep.bookshop.config.BookAttributes.BOOK_PAGE
import com.ducdiep.bookshop.config.BookAttributes.BOOK_PRICE
import com.ducdiep.bookshop.config.BookAttributes.BOOK_PUBLISHER
import com.ducdiep.bookshop.config.BookAttributes.BOOK_RATESTAR
import com.ducdiep.bookshop.config.BookAttributes.BOOK_RELEASEYEAR
import com.ducdiep.bookshop.config.BookAttributes.BOOK_REVIEW
import com.ducdiep.bookshop.config.BookAttributes.BOOK_TITLE
import com.ducdiep.bookshop.config.Internet
import com.ducdiep.bookshop.event.ECloseApp
import com.ducdiep.bookshop.event.EHideToolbar
import com.ducdiep.bookshop.event.IonClickBook
import com.ducdiep.bookshop.models.Book
import com.ducdiep.bookshop.sql.SQLHelper
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_home.view.*
import org.greenrobot.eventbus.EventBus
import org.json.JSONArray
import org.json.JSONException
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {
    var url = "https://bookshopb.herokuapp.com/api/books"
    var result = ""
    var list: ArrayList<Book>? = null
    var list2:ArrayList<Book>? = null
    var list3:ArrayList<Book>? = null
    var listtemp:List<Book>? = null
    var sqlHelper: SQLHelper? = null
    var ad: ArrayList<String> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_home, container, false)
        view.moreHotBook.setVisibility(View.INVISIBLE)
        view.moreNewBook.setVisibility(View.INVISIBLE)
        view.moreOfferBook.setVisibility(View.INVISIBLE)
        if (Internet.checkConnection(requireContext())) {
            sqlHelper = SQLHelper(requireContext())
            list = ArrayList()
            listtemp = ArrayList()
            listtemp = sqlHelper!!.getAllBook()
            //set view Flipper
            ad.add("https://res.cloudinary.com/yami177/image/upload/v1598978768/Ma-giam-gia-Fahasa_kn9mcd.png")
            ad.add("https://res.cloudinary.com/yami177/image/upload/v1598978870/M%C3%A3-gi%E1%BA%A3m-gi%C3%A1-S%C3%A1ch_evuiwm.jpg")
            ad.add("https://res.cloudinary.com/yami177/image/upload/v1598978884/ma-giam-gia-sach_fbnt0s.png")
            ad.add("https://res.cloudinary.com/yami177/image/upload/v1598978884/m%C3%A3-gi%E1%BA%A3m-gi%C3%A1-vinabook-1_dfgmq2.jpg")
            ad.add("https://res.cloudinary.com/yami177/image/upload/v1598978884/ma-giam-gia-vinabook_m8cvi2.png")
            ad.add("https://res.cloudinary.com/yami177/image/upload/v1598978885/voucher_qslawk.png")
            for (i in ad.indices) {
                val imageView = ImageView(context)
                Picasso.with(context).load(ad[i]).into(imageView)
                Log.d("Imagee", "addViewFlipper: $imageView")
                imageView.scaleType = ImageView.ScaleType.FIT_XY
                view?.viewFlipper?.addView(imageView)
            }
            view?.viewFlipper?.setFlipInterval(3000)
            view?.viewFlipper?.setAutoStart(true)
            val animation_slide_in = AnimationUtils.loadAnimation(
                context, R.anim.slide_in_right
            )
            val animation_slide_out = AnimationUtils.loadAnimation(
                context, R.anim.slide_out_right
            )
            view?.viewFlipper?.setInAnimation(animation_slide_in)
            view?.viewFlipper?.setOutAnimation(animation_slide_out)


            //set click
            view.moreNewBook.setOnClickListener(View.OnClickListener {
                EventBus.getDefault().post(EHideToolbar())
                val fragment: Fragment = ListBookFragment.newInstance(list!!)
                val fragmentManager = activity?.supportFragmentManager
                fragmentManager?.beginTransaction()?.replace(R.id.nav_host_fragment, fragment)
                    ?.addToBackStack(
                        null
                    )?.commit()
            })
            view.moreHotBook.setOnClickListener(View.OnClickListener {
                EventBus.getDefault().post(EHideToolbar())
                val fragment: Fragment = ListBookFragment.newInstance(list2!!)
                val fragmentManager = activity?.supportFragmentManager
                fragmentManager?.beginTransaction()?.replace(R.id.nav_host_fragment, fragment)
                    ?.addToBackStack(
                        null
                    )?.commit()
            })
            view.moreOfferBook.setOnClickListener(View.OnClickListener {
                EventBus.getDefault().post(EHideToolbar())
                val fragment: Fragment = ListBookFragment.newInstance(list3!!)
                val fragmentManager = activity?.supportFragmentManager
                fragmentManager?.beginTransaction()?.replace(R.id.nav_host_fragment, fragment)
                    ?.addToBackStack(
                        null
                    )?.commit()
            })
            val requestQueue = Volley.newRequestQueue(context)
            val stringRequest = StringRequest(
                Request.Method.GET, url,
                { response ->
                    view.progessbar.setVisibility(View.INVISIBLE)
                    view.moreHotBook.setVisibility(View.VISIBLE)
                    view.moreNewBook.setVisibility(View.VISIBLE)
                    view.moreOfferBook.setVisibility(View.VISIBLE)
                    result = response
                    getJson()
                }
            ) { view.listHotBook.setVisibility(View.INVISIBLE) }
            requestQueue.add(stringRequest)
        } else {
            val alertDialog = AlertDialog.Builder(context)
                .setTitle(getString(R.string.confirm))
                .setMessage(getString(R.string.check_internet))
                .setPositiveButton(
                    getString(R.string.ok)
                ) { dialog, which -> EventBus.getDefault().post(ECloseApp()) }
                .create()
            alertDialog.show()
        }
        return view
    }


    private fun getJson() {
        var id: Int
        var releaseYear: Int
        var numOfPage: Int
        var numOfReview: Int
        var imageLink: String
        var title: String
        var author: String
        var publisher: String
        var description: String
        var category: String
        var price: Double
        var rateStar: Double
        try {
            val jsonArray = JSONArray(result)
            for (i in 0 until jsonArray.length()) {
                val `object` = jsonArray.getJSONObject(i)
                id = `object`.getInt(BOOK_ID)
                releaseYear = `object`.getInt(BOOK_RELEASEYEAR)
                numOfPage = `object`.getInt(BOOK_PAGE)
                price = `object`.getDouble(BOOK_PRICE)
                imageLink = `object`.getString(BOOK_IMAGELINK)
                title = `object`.getString(BOOK_TITLE)
                author = `object`.getString(BOOK_AUTHOR)
                publisher = `object`.getString(BOOK_PUBLISHER)
                numOfReview = `object`.getInt(BOOK_REVIEW)
                description = `object`.getString(BOOK_DESCRIPTION)
                category = `object`.getString(BOOK_CATEGOTY)
                rateStar = `object`.getDouble(BOOK_RATESTAR)
                val book = Book(
                    id,
                    imageLink,
                    title,
                    author,
                    publisher,
                    releaseYear,
                    numOfPage,
                    price,
                    rateStar,
                    numOfReview,
                    description,
                    category, ""
                )
                list?.add(book)
                var check = false
                for (x in listtemp!!) {
                    if (x?.id === id) {
                        check = true
                    }
                }
                if (!check) {
                    sqlHelper!!.InsertBookToAllBook(book)
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        //add list 1
        val adapter1 = BookAdapter(requireContext(), list!!)
        val layoutManager1: RecyclerView.LayoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        view?.listNewBook?.setAdapter(adapter1)
        view?.listNewBook?.setLayoutManager(layoutManager1)
        adapter1.setIonClickBook(object : IonClickBook {
            override fun onClickItem(book: Book) {
                EventBus.getDefault().post(EHideToolbar())
                val fragment: Fragment = BookItemInforFragment.newInstance(list!!, book)
                val fragmentManager = activity!!.supportFragmentManager
                fragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        })
        //add list 2
        list2 = ArrayList()
        for (i in list!!.indices.reversed()) {
            list2!!.add(list!![i])
        }
        val adapter2 = BookAdapter(requireContext(), list2!!)
        val layoutManager2: RecyclerView.LayoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        view?.listHotBook?.setAdapter(adapter2)
        view?.listHotBook?.setLayoutManager(layoutManager2)
        adapter2.setIonClickBook(object : IonClickBook {
            override fun onClickItem(book: Book) {
                EventBus.getDefault().post(EHideToolbar())
                val fragment: Fragment = BookItemInforFragment.newInstance(list!!, book)
                val fragmentManager = activity!!.supportFragmentManager
                fragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        })
        //add list3
        list3 = ArrayList()
        for (i in list!!.size / 2 downTo 0) {
            list3!!.add(list!![i])
        }
        for (i in list!!.size / 2 + 1 until list!!.size - 1) {
            list3!!.add(list!![i])
        }
        val adapter3 = BookAdapter(requireContext(), list3!!)
        val layoutManager3: RecyclerView.LayoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        view?.listOfferBook?.setAdapter(adapter3)
        view?.listOfferBook?.setLayoutManager(layoutManager3)
        adapter3.setIonClickBook(object : IonClickBook {

            override fun onClickItem(book: Book) {
                EventBus.getDefault().post(EHideToolbar())
                val fragment: Fragment = BookItemInforFragment.newInstance(list!!, book)
                val fragmentManager = activity!!.supportFragmentManager
                fragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
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