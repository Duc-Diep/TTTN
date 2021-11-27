package com.ducdiep.bookshop.view.fragment

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.ducdiep.bookshop.R
import com.ducdiep.bookshop.adapter.BookAdapter
import com.ducdiep.bookshop.config.BookAttributes.BOOK_AUTHOR
import com.ducdiep.bookshop.config.BookAttributes.BOOK_CATEGOTY
import com.ducdiep.bookshop.config.BookAttributes.BOOK_DESCRIPTION
import com.ducdiep.bookshop.config.BookAttributes.BOOK_IMAGELINK
import com.ducdiep.bookshop.config.BookAttributes.BOOK_PAGE
import com.ducdiep.bookshop.config.BookAttributes.BOOK_PRICE
import com.ducdiep.bookshop.config.BookAttributes.BOOK_PUBLISHER
import com.ducdiep.bookshop.config.BookAttributes.BOOK_RATESTAR
import com.ducdiep.bookshop.config.BookAttributes.BOOK_RELEASEYEAR
import com.ducdiep.bookshop.config.BookAttributes.BOOK_REVIEW
import com.ducdiep.bookshop.config.BookAttributes.BOOK_TITLE
import com.ducdiep.bookshop.event.EShowToolBar
import com.ducdiep.bookshop.event.IonClickBook
import com.ducdiep.bookshop.models.Book
import com.ducdiep.bookshop.sql.SQLHelper
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_book_item_infor.view.*
import org.greenrobot.eventbus.EventBus
import org.json.JSONException
import org.json.JSONObject
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class BookItemInforFragment : Fragment() {
    lateinit var book: Book
    lateinit var listBook: List<Book>
    var similarBook:ArrayList<Book>? = null
    var me: Fragment = this
    var sqlHelper: SQLHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            book = it.getSerializable("book") as Book
            listBook = it.getParcelableArrayList("list")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_book_item_infor, container, false)
        sqlHelper = SQLHelper(requireContext())

        //set atribute
        //Picasso.with(getContext()).load(book.getImageLink()).fit().centerInside().into(binding.imgBook);
        Picasso.with(context).load(book.imageLink).into(view!!.imgBook)
        view!!.tvBookName.setText(book.title)
        view!!.tvAuthor.setText("Tác giả: " + book.author)
        view!!.tvNumberOfPage.setText(
            java.lang.String.valueOf(book.numOfPage)
                .toString() + " " + getString(R.string.page)
        )
        val local = Locale("vi", "VN")
        val numberFormat = NumberFormat.getInstance(local)
        val money: String = numberFormat.format(book.price)
        view!!.tvPrice.setText("$money đ")
        view!!.tvDescrition.setText(book.description)
        if (book.numOfReview > 0) {
            val decimalFormat = DecimalFormat("#.#")
            val star: Double = book.rateStar / book.numOfReview
            view!!.tvstarOfBook.setText(decimalFormat.format(star))
        } else {
            view!!.tvstarOfBook.setText("0")
        }

        view!!.tvCategory.setText(book.category)
        view!!.tvNumOfReview.setText(
            book.numOfReview.toString() + " " + getString(R.string.numOfEvaluate)
        )
        view!!.btnBackToListBook.setOnClickListener(View.OnClickListener {
            EventBus.getDefault().post(EShowToolBar())
            val manager = activity?.supportFragmentManager
            val transaction = manager?.beginTransaction()
            manager?.backStackEntryCount
            transaction?.remove(me)
            transaction?.commit()
            //                Fragment fragment = ListBookFragment.newInstance(bookList);
            //                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            //                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //                fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
            //                fragmentTransaction.addToBackStack(null);
            //                fragmentTransaction.commit();
        })
        //set adater
        similarBook = ArrayList()
        for (x in listBook) {
            if (x.category.equals(book.category) && !x.title
                    .equals(book.title)
            ) {
                similarBook!!.add(x)
            }
        }
        if (similarBook!!.size > 0) {
            val adapter = BookAdapter(requireContext(), similarBook!!)
            val layoutManager2: RecyclerView.LayoutManager =
                LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            view!!.similar_book.setAdapter(adapter)
            view!!.similar_book.setLayoutManager(layoutManager2)
            adapter.setIonClickBook(object : IonClickBook {
                override fun onClickItem(book: Book) {
                    val fragment: Fragment =
                        BookItemInforFragment.newInstance(listBook as ArrayList<Book>, book!!)
                    val fragmentManager = activity!!.supportFragmentManager
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()
                }
            })
        } else {
        }
        view!!.btnEvaluate.setOnClickListener(View.OnClickListener {
            val requestQueue = Volley.newRequestQueue(context)
            val jsonObject = JSONObject()
            try {
                jsonObject.put(BOOK_IMAGELINK, book.imageLink)
                jsonObject.put(BOOK_TITLE, book.title)
                jsonObject.put(BOOK_AUTHOR, book.author)
                jsonObject.put(BOOK_PUBLISHER, book.publisher)
                jsonObject.put(BOOK_RELEASEYEAR, book.releaseYear)
                jsonObject.put(BOOK_PAGE, book.numOfPage)
                jsonObject.put(BOOK_PRICE, book.price)
                jsonObject.put(BOOK_DESCRIPTION, book.description)
                jsonObject.put(BOOK_CATEGOTY, book.category)
                jsonObject.put(BOOK_RATESTAR, book.rateStar + view!!.rateStar.getRating())
                jsonObject.put(BOOK_REVIEW, book.numOfReview + 1)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            val mrequestbody = jsonObject.toString()
            val url = "https://bookshopb.herokuapp.com/api/books/" + book.id

            val stringRequest: StringRequest = object : StringRequest(
                Method.PATCH, url,
                Response.Listener {
                    val decimalFormat = DecimalFormat("#.#")
                    val star: Double =
                        (book.rateStar + view!!.rateStar.getRating()) / (book.numOfReview + 1)
                    view!!.tvstarOfBook.setText(decimalFormat.format(star).toString())
                    view!!.tvNumOfReview.setText(
                        (book.numOfReview + 1).toString() + " " + getString(
                            R.string.numOfEvaluate
                        )
                    )
                    Toast.makeText(
                        context,
                        getString(R.string.your_evaluate) + " " + view!!.rateStar.getRating() + " " + getString(
                            R.string.star
                        ),
                        Toast.LENGTH_SHORT
                    ).show()
                },
                Response.ErrorListener {
                    Toast.makeText(
                        context,
                        getString(R.string.evaluate_eror),
                        Toast.LENGTH_SHORT
                    ).show()
                }) {
                //xu li du lieu cho body
                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8"
                }

                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray? {
                    return try {
                        if (mrequestbody == null) {
                            null
                        } else {
                            mrequestbody.toByteArray(charset("utf-8"))
                        }
                    } catch (e: Exception) {
                        e.message
                        null
                    }
                }
            }
            requestQueue.add(stringRequest)


            //double star = (book.getRateStar()+1)/(book.getNumOfReview()+1);
            //binding.tvstarOfBook.setText(String.valueOf(star));
        })
        view!!.btnAddToCart.setOnClickListener(View.OnClickListener {
            sqlHelper!!.InsertBookToCart(book!!)
            Toast.makeText(context, getString(R.string.add_to_cart_sucess), Toast.LENGTH_SHORT)
                .show()
        })
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(bookList: ArrayList<Book>, book: Book) =
            BookItemInforFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("book", book)
                    putParcelableArrayList(
                        "list",
                        bookList as ArrayList<out Parcelable?>
                    )
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