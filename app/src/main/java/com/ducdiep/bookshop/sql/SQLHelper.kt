package com.ducdiep.bookshop.sql

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.ducdiep.bookshop.models.Book
import com.ducdiep.bookshop.config.BookAttributes
import com.ducdiep.bookshop.config.BookAttributes.BOOK_AUTHOR
import com.ducdiep.bookshop.config.BookAttributes.BOOK_CATEGORY
import com.ducdiep.bookshop.config.BookAttributes.BOOK_DATEBUY
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
import com.ducdiep.bookshop.models.Account
import java.util.*

const val DB_NAME = "BookStore.db"
const val DB_VERSION = 1
@SuppressLint("Range")
class SQLHelper(context: Context):SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    val DB_TABLE_ALL_BOOK = "AllBooks"
    val DB_TABLE_CART = "Cart"
    val DB_TABLE_HISTORY = "History"
    val DB_TABLE_ACCOUNT = "Account"

    lateinit var sqLiteDatabase: SQLiteDatabase
    lateinit var contentValues: ContentValues

    override fun onCreate(db: SQLiteDatabase?) {
        //Create table acc

        //Create table acc
        val queryCreateTableAccount = "CREATE TABLE " + DB_TABLE_ACCOUNT + "(" +
                "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "phone Text," +
                "password Text," +
                "fullname Text," +
                "address Text)"
        db!!.execSQL(queryCreateTableAccount)
        //Create table all books
        //Create table all books
        val queryCreateTableAllBook = "CREATE TABLE " + DB_TABLE_ALL_BOOK + "(" +
                "id INTEGER NOT NULL PRIMARY KEY," +
                "imageLink Text," +
                "title Text," +
                "author Text," +
                "publisher Text," +
                "releaseYear INTEGER," +
                "numOfPage INTEGER," +
                "price INTEGER," +
                "rateStar INTEGER," +
                "numOfReview INTEGER," +
                "description Text," +
                "category Text)"
        db!!.execSQL(queryCreateTableAllBook)
        //create table Cart
        //create table Cart
        val queryCreateTableCart = "CREATE TABLE " + DB_TABLE_CART + "(" +
                "id INTEGER NOT NULL PRIMARY KEY," +
                "imageLink Text," +
                "title Text," +
                "author Text," +
                "publisher Text," +
                "releaseYear INTEGER," +
                "numOfPage INTEGER," +
                "price INTEGER," +
                "rateStar INTEGER," +
                "numOfReview INTEGER," +
                "description Text," +
                "category Text)"
        db!!.execSQL(queryCreateTableCart)
        //create table HISTORY
        //create table HISTORY
        val queryCreateTableHistory = "CREATE TABLE " + DB_TABLE_HISTORY + "(" +
                "id INTEGER NOT NULL PRIMARY KEY," +
                "imageLink Text," +
                "title Text," +
                "author Text," +
                "publisher Text," +
                "releaseYear INTEGER," +
                "numOfPage INTEGER," +
                "price INTEGER," +
                "rateStar INTEGER," +
                "numOfReview INTEGER," +
                "description Text," +
                "category Text," +
                "dateBuy Text)"
        db!!.execSQL(queryCreateTableHistory)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion != newVersion) {
            db!!.execSQL("DROP TABLE IF EXISTS $DB_NAME")
            onCreate(db)
        }
    }


    //Query Table AllBook
    fun InsertBookToAllBook(book: Book) {
        sqLiteDatabase = writableDatabase
        contentValues = ContentValues()
        contentValues.put(BOOK_ID, book.id)
        contentValues.put(BOOK_IMAGELINK, book.imageLink)
        contentValues.put(BOOK_TITLE, book.title)
        contentValues.put(BOOK_AUTHOR, book.author)
        contentValues.put(BOOK_PUBLISHER, book.publisher)
        contentValues.put(BOOK_RELEASEYEAR, book.releaseYear)
        contentValues.put(BOOK_PAGE, book.numOfPage)
        contentValues.put(BOOK_PRICE, book.price)
        contentValues.put(BOOK_RATESTAR, book.rateStar)
        contentValues.put(BOOK_REVIEW, book.numOfReview)
        contentValues.put(BOOK_DESCRIPTION, book.description)
        contentValues.put(BOOK_CATEGORY, book.category)
        sqLiteDatabase.insert(DB_TABLE_ALL_BOOK, null, contentValues)
    }


    fun getAllBook(): List<Book>? {
        val list: MutableList<Book> = ArrayList<Book>()
        sqLiteDatabase = readableDatabase
        val cursor = sqLiteDatabase.query(
            false, DB_TABLE_ALL_BOOK,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        )
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex(BOOK_ID))
            val imageLink = cursor.getString(cursor.getColumnIndex(BOOK_IMAGELINK))
            val title = cursor.getString(cursor.getColumnIndex(BOOK_TITLE))
            val author = cursor.getString(cursor.getColumnIndex(BOOK_AUTHOR))
            val publisher = cursor.getString(cursor.getColumnIndex(BOOK_PUBLISHER))
            val releaseYear = cursor.getInt(cursor.getColumnIndex(BOOK_RELEASEYEAR))
            val numOfPage = cursor.getInt(cursor.getColumnIndex(BOOK_PAGE))
            val price = cursor.getDouble(cursor.getColumnIndex(BOOK_PRICE))
            val rateStar = cursor.getDouble(cursor.getColumnIndex(BOOK_RATESTAR))
            val numOfReview = cursor.getInt(cursor.getColumnIndex(BOOK_REVIEW))
            val description = cursor.getString(cursor.getColumnIndex(BOOK_DESCRIPTION))
            val category = cursor.getString(cursor.getColumnIndex(BOOK_CATEGORY))
            list.add(
                Book(
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
                    category,""
                )
            )
        }
        return list
    }

    //Query Table Cart
    fun InsertBookToCart(book: Book) {
        sqLiteDatabase = writableDatabase
        contentValues = ContentValues()
        contentValues.put(BOOK_ID, book.id)
        contentValues.put(BOOK_IMAGELINK, book.imageLink)
        contentValues.put(BOOK_TITLE, book.title)
        contentValues.put(BOOK_AUTHOR, book.author)
        contentValues.put(BOOK_PUBLISHER, book.publisher)
        contentValues.put(BOOK_RELEASEYEAR, book.releaseYear)
        contentValues.put(BOOK_PAGE, book.numOfPage)
        contentValues.put(BOOK_PRICE, book.price)
        contentValues.put(BOOK_RATESTAR, book.rateStar)
        contentValues.put(BOOK_REVIEW, book.numOfReview)
        contentValues.put(BOOK_DESCRIPTION, book.description)
        contentValues.put(BOOK_CATEGORY, book.category)
        sqLiteDatabase.insert(DB_TABLE_CART, null, contentValues)
    }

    fun getAllBookInCart(): List<Book>? {
        val list: MutableList<Book> = ArrayList<Book>()
        sqLiteDatabase = readableDatabase
        val cursor = sqLiteDatabase.query(
            false, DB_TABLE_CART,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        )
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex(BOOK_ID))
            val imageLink = cursor.getString(cursor.getColumnIndex(BOOK_IMAGELINK))
            val title = cursor.getString(cursor.getColumnIndex(BOOK_TITLE))
            val author = cursor.getString(cursor.getColumnIndex(BOOK_AUTHOR))
            val publisher = cursor.getString(cursor.getColumnIndex(BOOK_PUBLISHER))
            val releaseYear = cursor.getInt(cursor.getColumnIndex(BOOK_RELEASEYEAR))
            val numOfPage = cursor.getInt(cursor.getColumnIndex(BOOK_PAGE))
            val price = cursor.getDouble(cursor.getColumnIndex(BOOK_PRICE))
            val rateStar = cursor.getDouble(cursor.getColumnIndex(BOOK_RATESTAR))
            val numOfReview = cursor.getInt(cursor.getColumnIndex(BOOK_REVIEW))
            val description = cursor.getString(cursor.getColumnIndex(BOOK_DESCRIPTION))
            val category = cursor.getString(cursor.getColumnIndex(BOOK_CATEGORY))
            list.add(
                Book(
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
                    category,""
                )
            )
        }
        return list
    }

    fun deleteCart(): Int {
        sqLiteDatabase = writableDatabase
        return sqLiteDatabase.delete(DB_TABLE_CART, null, null)
    }

    fun deleteItemInCart(id: String): Int {
        sqLiteDatabase = writableDatabase
        return sqLiteDatabase.delete(DB_TABLE_CART, "id=?", arrayOf(id))
    }

    //Query table History
    fun InsertBookToHistory(book: Book) {
        sqLiteDatabase = writableDatabase
        contentValues = ContentValues()
        contentValues.put(BOOK_ID, book.id)
        contentValues.put(BOOK_IMAGELINK, book.imageLink)
        contentValues.put(BOOK_TITLE, book.title)
        contentValues.put(BOOK_AUTHOR, book.author)
        contentValues.put(BOOK_PUBLISHER, book.publisher)
        contentValues.put(BOOK_RELEASEYEAR, book.releaseYear)
        contentValues.put(BOOK_PAGE, book.numOfPage)
        contentValues.put(BOOK_PRICE, book.price)
        contentValues.put(BOOK_RATESTAR, book.rateStar)
        contentValues.put(BOOK_REVIEW, book.numOfReview)
        contentValues.put(BOOK_DESCRIPTION, book.description)
        contentValues.put(BOOK_CATEGORY, book.category)
        contentValues.put(BOOK_DATEBUY, book.dateBuy)
        sqLiteDatabase.insert(DB_TABLE_HISTORY, null, contentValues)
    }

    fun getAllBookInHistory(): List<Book>? {
        val list: MutableList<Book> = ArrayList<Book>()
        sqLiteDatabase = readableDatabase
        val cursor = sqLiteDatabase.query(
            false, DB_TABLE_HISTORY,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        )
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex(BOOK_ID))
            val imageLink = cursor.getString(cursor.getColumnIndex(BOOK_IMAGELINK))
            val title = cursor.getString(cursor.getColumnIndex(BOOK_TITLE))
            val author = cursor.getString(cursor.getColumnIndex(BOOK_AUTHOR))
            val publisher = cursor.getString(cursor.getColumnIndex(BOOK_PUBLISHER))
            val releaseYear = cursor.getInt(cursor.getColumnIndex(BOOK_RELEASEYEAR))
            val numOfPage = cursor.getInt(cursor.getColumnIndex(BOOK_PAGE))
            val price = cursor.getDouble(cursor.getColumnIndex(BOOK_PRICE))
            val rateStar = cursor.getDouble(cursor.getColumnIndex(BOOK_RATESTAR))
            val numOfReview = cursor.getInt(cursor.getColumnIndex(BOOK_REVIEW))
            val description = cursor.getString(cursor.getColumnIndex(BOOK_DESCRIPTION))
            val category = cursor.getString(cursor.getColumnIndex(BOOK_CATEGORY))
            val dateBuy = cursor.getString(cursor.getColumnIndex(BOOK_DATEBUY))
            list.add(
                Book(
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
                    category,
                    dateBuy
                )
            )
        }
        return list
    }

    fun deleteHistory(): Int {
        sqLiteDatabase = writableDatabase
        return sqLiteDatabase.delete(DB_TABLE_HISTORY, null, null)
    }

    //query table Account
    fun InsertAccount(account: Account) {
        sqLiteDatabase = writableDatabase
        contentValues = ContentValues()
        contentValues.put("phone", account.phone)
        contentValues.put("password", account.password)
        contentValues.put("fullname", account.fullName)
        contentValues.put("address", account.address)
        sqLiteDatabase.insert(DB_TABLE_ACCOUNT, null, contentValues)
    }

    fun getAllAccount(): List<Account>? {
        val list: MutableList<Account> = ArrayList<Account>()
        sqLiteDatabase = readableDatabase
        val cursor = sqLiteDatabase.query(
            false, DB_TABLE_ACCOUNT,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        )
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex("id"))
            val phone = cursor.getString(cursor.getColumnIndex("phone"))
            val password = cursor.getString(cursor.getColumnIndex("password"))
            val fullname = cursor.getString(cursor.getColumnIndex("fullname"))
            val address = cursor.getString(cursor.getColumnIndex("address"))
            list.add(Account(id, phone, password, fullname, address))
        }
        return list
    }


}