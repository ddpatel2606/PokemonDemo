package com.dixitpatel.pokemondemo.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber


abstract class CommonAdapter<T>(data: ArrayList<T?>) : RecyclerView.Adapter<CommonAdapter.CommonViewHolder>()
{
    private var data = data
    private val TAG = CommonAdapter::class.java.simpleName
    private var moreDataAvailable = false
    private var isLoading = false
    private var loadMoreListener: OnLoadMoreListener? = null

    open fun CommonAdapter(arrItem: ArrayList<T?>) {
        data = arrItem
        setHasStableIds(false)
    }

    open fun CommonAdapter(
        arrItem: ArrayList<T?>,
        hasStableIds: Boolean) {
        data = arrItem
        setHasStableIds(hasStableIds)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder {

        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            viewType,
            parent,
            false
        )
        return CommonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommonViewHolder, position: Int) {
        try {
            if (position >= itemCount - 1 && moreDataAvailable && !isLoading && loadMoreListener != null) {
                isLoading = true
                loadMoreListener!!.onLoadMore()
            }
            onBind(holder, holder.adapterPosition)
            onBind(holder, holder.adapterPosition, getItem(holder.adapterPosition))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    open fun isMoreDataAvailable(): Boolean {
        return moreDataAvailable
    }

    open fun isLoading(): Boolean {
        return isLoading
    }

    open fun setMoreDataAvailable(moreDataAvailable: Boolean) {
        this.moreDataAvailable = moreDataAvailable
    }

    open fun addItemNotifyAll(item: T) {
        data.add(item)
        notifyDataSetChanged()
    }

    open fun removeItemNotifyAll(item: T) {
        data.remove(item)
        notifyDataSetChanged()
    }

    open fun addItem(item: T) {
        try {
            data.add(item)
            Timber.d("additem start")
            notifyItemRangeInserted(data.size - 1, data.size)
        } catch (e: Exception) {
            Timber.d("additem start ex")
            notifyItemInserted(data.size - 1)
        }
        Timber.d("additem start")
    }

    interface OnLoadMoreListener {
        fun onLoadMore()
    }

    open fun setLoadMoreListener(loadMoreListener: OnLoadMoreListener?) {
        this.loadMoreListener = loadMoreListener
    }

    override fun getItemCount(): Int {
        return data.size
    }


    open fun getItem(pos: Int): T? {
        return data[pos]
    }

    open fun removeItem(pos: Int) {
        data.removeAt(pos)
        notifyItemRemoved(pos)
    }

    open fun removeItem(item: T) {
        val pos = data.indexOf(item)
        data.remove(item)
        notifyItemRemoved(pos)
    }

    open fun isContain(item: T): Boolean {
        return data.indexOf(item) > -1
    }

    open fun removeItemRange(item: T) {
        val pos = data.indexOf(item)
        data.remove(item)
        try {
            Timber.d("removeRange start")
            notifyItemRangeRemoved(pos, data.size)
        } catch (e: Exception) {
            e.printStackTrace()
            Timber.d("removeRange start ex")
            notifyItemRemoved(pos)
        }
        Timber.d("removeRange start end")
    }

    open fun updateData(data: ArrayList<T?>) {
        this.data = data
        notifyDataSetChanged()
    }


    open fun startLoadMore() {
        data.add(null)
        notifyItemInserted(data.size - 1)
    }

    open fun startLoadPrevious() {
        data.add(0, null)
        notifyItemInserted(data.size - 1)
    }

    open fun stopLoadPrevious(trending: ArrayList<T>) {
        data.removeAt(0)
        data.addAll(0, trending)
        notifyDataSetChanged()
        isLoading = false
    }

    open fun stopLoadMore(trending: ArrayList<T?>) {
        data.removeAt(data.size - 1)
        data.addAll(trending)
        notifyDataSetChanged()
        isLoading = false
    }


    open fun getData(): ArrayList<T?> {
        return data
    }

    class CommonViewHolder(var binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root)

    open fun onBind(holder: CommonViewHolder, position: Int, item: T?) {}

    abstract fun onBind(holder: CommonViewHolder?, position: Int)

}