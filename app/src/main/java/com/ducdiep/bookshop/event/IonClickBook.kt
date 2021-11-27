package com.ducdiep.bookshop.event

import com.ducdiep.bookshop.models.Book

interface IonClickBook {
    fun onClickItem(book: Book)
}