package com.twentyhours.nabijam.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import com.twentyhours.nabijam.databinding.AddressItemBinding
import com.twentyhours.nabijam.model.AddressItem
import com.twentyhours.nabijam.navigator.AddressItemNavigator
import com.twentyhours.nabijam.repository.AddressRepository
import com.twentyhours.nabijam.viewmodel.AddressItemViewModel
import java.util.*

/**
 * Created by soonhyung on 12/31/16.
 */
class AddressAdapter(val repository: AddressRepository, val navigator: AddressItemNavigator)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
  private var items: List<AddressItem> = ArrayList()

  interface onViewSelectedListener {
    fun onItemSelected(address: AddressItem, position: Int): Boolean
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    (holder as AddressViewHolder).bind(items[position])
  }

  override fun getItemCount(): Int {
    return items.count()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val layoutInflater = LayoutInflater.from(parent.context)
    val itemBinding = AddressItemBinding.inflate(layoutInflater, parent, false)
    return AddressViewHolder(itemBinding)
  }

  fun replaceData(items: List<AddressItem>) {
    setList(items)
  }

  private fun setList(items: List<AddressItem>) {
    this.items = items
    notifyDataSetChanged()
  }

  inner class AddressViewHolder(val binding: AddressItemBinding)
      : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: AddressItem) {
      val viewModel = AddressItemViewModel(repository, navigator)
      viewModel.setAddress(item)
      binding.viewModel = viewModel
      binding.executePendingBindings()
    }
  }
}
