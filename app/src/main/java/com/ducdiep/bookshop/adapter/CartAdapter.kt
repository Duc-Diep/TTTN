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

class CartAdapter(var context: Context, var list: List<Book>):RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    var ionClickDelete: IonClickDelete? = null
    var ionClickBook: IonClickBook? = null

    @JvmName("setIonClickBook1")
    fun setIonClickBook(ionClickBook: IonClickBook?) {
        this.ionClickBook = ionClickBook
    }

    @JvmName("setIonClickDelete1")
    fun setIonClickDelete(ionClickDelete: IonClickDelete?) {
        this.ionClickDelete = ionClickDelete
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapter.CartViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.item_in_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartAdapter.CartViewHolder, position: Int) {
        val book = list[position]
        //Picasso.with(context).load(book.getImageLink()).fit().centerInside().into(holder.imgBook);
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
        holder.layoutClick.setOnClickListener(View.OnClickListener { ionClickBook?.onClickItem(book) })
        holder.imgDelete.setOnClickListener { ionClickDelete?.onClickItem(book) }
    }

    override fun getItemCount(): Int {
        return list.size
    }
    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgDelete = itemView.findViewById<ImageView>(R.id.btnDeleteItemInCart)
        var imgBook = itemView.findViewById<ImageView>(R.id.imgBookInCart)
        var tvTitle = itemView.findViewById<TextView>(R.id.tvTitleInCart)
        var tvPricebook = itemView.findViewById<TextView>(R.id.tvPriceBookInCart)
        var layoutClick = itemView.findViewById<RelativeLayout>(R.id.layoutClickerInCart)
    }
}