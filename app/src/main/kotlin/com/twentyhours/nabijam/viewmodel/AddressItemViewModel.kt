package com.twentyhours.nabijam.viewmodel

import com.twentyhours.nabijam.model.AddressItem
import com.twentyhours.nabijam.navigator.AddressItemNavigator
import com.twentyhours.nabijam.repository.AddressRepository

/**
 * Created by soonhyung on 2/10/17.
 */
class AddressItemViewModel(val navigator: AddressItemNavigator) {
  private var item: AddressItem? = null

  fun setAddress(item: AddressItem?) {
    this.item = item
  }

  fun onAddressSelected() {
    navigator.onAddressSelected()
  }

  fun onDeleteClicked() {
    item?.let {
      navigator.onDeleteClicked(it.label)
    }
  }

  fun label(): String {
    return item?.label ?: ""
  }

  fun address(): String {
    return item?.address ?: ""
  }
}