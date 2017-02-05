package com.twentyhours.nabijam.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.twentyhours.nabijam.R
import com.twentyhours.nabijam.extension.inflate
import com.twentyhours.nabijam.model.AddressItem
import kotlinx.android.synthetic.main.address_item.view.*
import java.util.*

/**
 * Created by soonhyung on 12/31/16.
 */
class AddressAdapter(val listener: AddressAdapter.onViewSelectedListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
  private var items: ArrayList<AddressItem> = ArrayList()

  interface onViewSelectedListener {
    fun onItemSelected(address: AddressItem?, position: Int): Boolean
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    (holder as AddressViewHolder).bind(items[position], position)
  }

  override fun getItemCount(): Int {
    return items.size
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    return AddressViewHolder(parent)
  }

  fun addAddresses(addresses: List<AddressItem>) {
    // TODO: need to check duplication?
    items.addAll(addresses);
    notifyItemRangeChanged(0, items.size)
  }

  fun addAddress(address: AddressItem) {
    if (!items.contains(address)) {
      items.add(address)
      notifyItemInserted(items.size - 1)
    }
  }

  fun removeAddressAt(position: Int) {
    items.removeAt(position)
    notifyItemRangeRemoved(position, 1)
  }

  inner class AddressViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.address_item)) {
    fun bind(item: AddressItem, position: Int) = with(itemView) {
      nickname.text = item.label
      address.text = item.address

      super.itemView.setOnLongClickListener { listener.onItemSelected(item, position) }
    }
  }
}
