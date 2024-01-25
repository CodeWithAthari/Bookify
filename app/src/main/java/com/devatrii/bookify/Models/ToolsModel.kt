package com.devatrii.bookify.Models

import androidx.annotation.DrawableRes
import com.devatrii.bookify.enums.ToolsType

data class ToolsModel(
    val title: String,
    @DrawableRes
    val image: Int,
    val type: ToolsType
)
