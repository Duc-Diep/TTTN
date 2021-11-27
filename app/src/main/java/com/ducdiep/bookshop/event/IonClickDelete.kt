package com.ducdiep.bookshop.event

import com.ducdiep.bookshop.models.Book

interface IonClickDelete {
    fun onClickItem(book: Book)
}