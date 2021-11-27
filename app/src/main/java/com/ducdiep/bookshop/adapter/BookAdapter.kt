package com.ducdiep.bookshop.adapter

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ducdiep.bookshop.R
import com.ducdiep.bookshop.event.IonClickBook
import com.ducdiep.bookshop.models.Book
import com.squareup.picasso.Picasso
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class BookAdapter(var context: Context, var list: List<Book>) :
    RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    var ionClickBook: IonClickBook? = null

    @JvmName("setIonClickBook1")
    fun setIonClickBook(ionClickBook: IonClickBook?) {
        this.ionClickBook = ionClickBook
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.book_item, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        //Picasso.with(context).load(book.getImageLink()).fit().centerInside().into(holder.imgBook);
        val book = list[position]
        Picasso.with(context).load(book.imageLink).into(holder.imgBook)
        holder.tvTitle.setText(book.title)
        holder.tvTitle.ellipsize = TextUtils.TruncateAt.MARQUEE
        holder.tvTitle.setHorizontallyScrolling(true)
        holder.tvTitle.isSelected = true
        holder.tvTitle.marqueeRepeatLimit = -1
        holder.tvTitle.isFocusable = true
        val local = Locale("vi", "VN")
        val numberFormat = NumberFormat.getInstance(local)
        val money: String = numberFormat.format(book.price)
        holder.tvPricebook.text = money + "đ"
        holder.layoutClick.setOnClickListener { ionClickBook!!.onClickItem(book) }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgBook = itemView.findViewById<android.widget.ImageView>(R.id.imgBook)
        var tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        var tvPricebook = itemView.findViewById<TextView>(R.id.tvPriceBook)
        var layoutClick = itemView.findViewById<RelativeLayout>(R.id.layoutClicker)
    }
}