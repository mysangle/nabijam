package com.twentyhours.nabijam.binding

import android.databinding.BindingAdapter
import android.support.v7.widget.RecyclerView
import com.twentyhours.nabijam.adapter.AddressAdapter
import com.twentyhours.nabijam.model.AddressItem

/**
 * Created by soonhyung on 2/17/17.
 */
class AddressRecyclerBindings {
  companion object {
    @JvmStatic @BindingAdapter("app:items")
    fun setItems(view: RecyclerView, items: List<AddressItem>) {
      (view.adapter as AddressAdapter).replaceData(items)
    }
  }
}