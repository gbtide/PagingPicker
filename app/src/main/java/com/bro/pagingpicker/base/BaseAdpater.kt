package com.bro.pagingpicker.base

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by kyunghoon on 2021-01-10
 */
typealias ItemClass = Class<out Any>

typealias ItemBinder = ItemViewBinder<Any, RecyclerView.ViewHolder>

open class BaseAdapter(
    private val viewBinders: Map<ItemClass, ItemBinder>
) : ListAdapter<Any, RecyclerView.ViewHolder>(ItemDiffCallback(viewBinders)) {

    private val viewTypeToBinders = viewBinders.mapKeys { it.value.getFeedItemType() }

    private fun getViewBinder(viewType: Int): ItemBinder = viewTypeToBinders.getValue(viewType)

    override fun getItemViewType(position: Int): Int =
        viewBinders.getValue(super.getItem(position).javaClass).getFeedItemType()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return getViewBinder(viewType).createViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        return getViewBinder(getItemViewType(position)).bindViewHolder(getItem(position), holder)
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        getViewBinder(holder.itemViewType).onViewRecycled(holder)
        super.onViewRecycled(holder)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        getViewBinder(holder.itemViewType).onViewDetachedFromWindow(holder)
        super.onViewDetachedFromWindow(holder)
    }
}


abstract class ItemViewBinder<M, in VH : RecyclerView.ViewHolder>(
    val modelClass: Class<out M>
) : DiffUtil.ItemCallback<M>() {
    abstract fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder
    abstract fun bindViewHolder(model: M, viewHolder: VH)
    abstract fun getFeedItemType(): Int

    open fun onViewRecycled(viewHolder: VH) = Unit
    open fun onViewDetachedFromWindow(viewHolder: VH) = Unit
}

internal class ItemDiffCallback(
    private val viewBinders: Map<ItemClass, ItemBinder>
) : DiffUtil.ItemCallback<Any>() {

    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
        if (oldItem::class != newItem::class) {
            return false
        }
        return viewBinders[oldItem::class.java]?.areItemsTheSame(oldItem, newItem) ?: false
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
        // We know the items are the same class because [areItemsTheSame] returned true
        return viewBinders[oldItem::class.java]?.areContentsTheSame(oldItem, newItem) ?: false
    }

}