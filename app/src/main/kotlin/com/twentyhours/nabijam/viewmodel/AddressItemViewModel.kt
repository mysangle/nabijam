package com.twentyhours.nabijam.viewmodel

import android.view.View
import com.twentyhours.nabijam.model.AddressItem

/**
 * Created by soonhyung on 2/10/17.
 */
class AddressItemViewModel(var item: AddressItem) {
  fun label(): String {
    return item.label
  }

  fun address(): String {
    return item.address
  }

  fun onItemSelected(item: AddressItem) {
    val label = item.label
    val address = item.address
  }
}