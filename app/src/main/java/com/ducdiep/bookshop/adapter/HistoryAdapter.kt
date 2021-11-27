package com.ducdiep.bookshop.adapter

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ducdiep.bookshop.R
import com.ducdiep.bookshop.event.IonClickBook
import com.ducdiep.bookshop.event.IonClickDelete
import com.ducdiep.bookshop.models.Book
import com.squareup.picasso.Picasso
import java.text.NumberFormat
import java.util.*

class HistoryAdapter(var context: Context, var list: List<Book>):RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {
    var ionClickBook: IonClickBook? = null

    @JvmName("setIonClickBook1")
    fun setIonClickBook(ionClickBook: IonClickBook?) {
        this.ionClickBook = ionClickBook
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryAdapter.HistoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.item_in_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val book = list[position]
        Picasso.with(context).load(book.imageLink).into(holder.imgBook)
        holder.tvTitle.setText(book.title)
        holder.tvTitle.setEllipsize(TextUtils.TruncateAt.MARQUEE)
        holder.tvTitle.setHorizontallyScrolling(true)
        holder.tvTitle.setSelected(true)
        holder.tvTitle.setMarqueeRepeatLimit(-1)
        holder.tvTitle.setFocusable(true)
        val local = Locale("vi", "VN")
        val numberFormat = NumberFormat.getInstance(local)
        val money: String = numberFormat.format(book.price)
        holder.tvPricebook.setText(money + "đ")
        holder.layout.setOnClickListener(View.OnClickListener { ionClickBook?.onClickItem(book) })
        holder.tvDateBuy.setText(book.dateBuy)
    }



    override fun getItemCount(): Int {
        return list.size
    }
    class HistoryViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {
        var imgBook = itemView.findViewById<ImageView>(R.id.imgBookInHis)
        var tvTitle = itemView.findViewById<TextView>(R.id.tvTitleInHis)
        var tvPricebook = itemView.findViewById<TextView>(R.id.tvPriceBookInHis)
        var layout = itemView.findViewById<RelativeLayout>(R.id.layoutClickerInHis)
        var tvDateBuy = itemView.findViewById<TextView>(R.id.tvDateBuy)
    }
}