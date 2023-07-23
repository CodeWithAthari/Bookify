package com.devatrii.bookify.Models

import com.devatrii.bookify.Adapters.LAYOUT_HOME
import java.io.Serializable


data class HomeModel(
    val catTitle: String?=null,
    val booksList: ArrayList<BooksModel>?=null,
    // Item BOD
    val bod: BooksModel?=null,
    val LAYOUT_TYPE: Int = LAYOUT_HOME
):Serializable
