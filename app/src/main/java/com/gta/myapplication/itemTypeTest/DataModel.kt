package com.gta.myapplication.itemTypeTest

sealed class ListItem {
    abstract val id: Long
}

data class ExpandableItem(
    override val id: Long,
    val title: String,
    val content: String
) : ListItem()

data class GridItem(
    override val id: Long,
    val title: String,
    val description: String
) : ListItem()