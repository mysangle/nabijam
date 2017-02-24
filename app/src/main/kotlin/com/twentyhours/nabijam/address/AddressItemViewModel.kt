package com.twentyhours.nabijam.address

import com.twentyhours.nabijam.data.AddressItem
import com.twentyhours.nabijam.address.AddressItemNavigator
import com.twentyhours.nabijam.data.AddressRepository

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