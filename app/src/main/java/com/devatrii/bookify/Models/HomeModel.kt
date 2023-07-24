package com.devatrii.bookify.Models

import com.devatrii.bookify.Adapters.LAYOUT_HOME


data class HomeModel(
    val catTitle:String?=null,
    val booksList:ArrayList<BooksModel>?=null,

    val bod:BooksModel?=null,
    val LAYOUT_TYPE:Int = LAYOUT_HOME
)
