package com.twentyhours.nabijam.viewmodel

import android.databinding.BaseObservable
import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import android.databinding.ObservableList
import com.twentyhours.nabijam.model.AddressItem
import com.twentyhours.nabijam.navigator.AddressNavigator
import com.twentyhours.nabijam.repository.AddressRepository
import com.twentyhours.njbm.core.AddressGenerator

/**
 * Created by soonhyung on 2/10/17.
 */
class AddressViewModel(val repository: AddressRepository, val navigator: AddressNavigator)
    : BaseObservable() {
  val label = ObservableField<String>()
  val items: ObservableList<AddressItem> = ObservableArrayList()

  fun start() {
    loadAddresses()
  }

  fun refresh() {
    loadAddresses()
  }

  private fun loadAddresses() {
    val addressItems = repository.getAddresses()
    items.clear()
    items.addAll(addressItems)
  }

  fun deleteAddress(label: String) {
    repository.deleteAddress(label)
  }

  fun onGenerateButtonClicked() {
    val input = label.get()
    for (item in items) {
      if (item.label == input) {
        // address of the same label exists.
        return
      }
    }

    // create new address
    val address = AddressGenerator.generate()
    val item = AddressItem(input, address.toBase58(), address.privSigningKey, address.privEncryptionKey)
    items.add(0, item)

    repository.saveAddress(item)
    navigator.onNewAddressGenerated()
  }
}