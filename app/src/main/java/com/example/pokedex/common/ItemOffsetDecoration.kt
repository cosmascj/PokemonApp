package com.example.pokedex.common

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView

class ItemOffsetDecoration(private val itemOffset:Int): RecyclerView.ItemDecoration() {

    constructor(context: Context, @DimenRes itemDimenId:Int): this(context.resources.getDimensionPixelSize(itemDimenId))


    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.set(itemOffset,itemOffset,itemOffset,itemOffset)
    }
}