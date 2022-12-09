package com.develoware.skyvape_pos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

class BasketAdapter(private val items: MutableList<BasketData>): BaseAdapter() {

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): BasketData = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View? {
        var convertView = view
        if (convertView == null) convertView = LayoutInflater.from(parent?.context).inflate(R.layout.list_basket, parent, false)

        val item: BasketData = items[position]

        return convertView
    }
}