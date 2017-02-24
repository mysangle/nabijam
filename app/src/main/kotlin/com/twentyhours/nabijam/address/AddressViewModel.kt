package com.twentyhours.nabijam.address

import android.databinding.BaseObservable
import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import android.databinding.ObservableList
import com.twentyhours.nabijam.data.AddressItem
import com.twentyhours.nabijam.address.AddressNavigator
import com.twentyhours.nabijam.data.AddressRepository
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

  fun addNewAddress(label: String) {
    if (label.isEmpty()) {
      // empty label is not accepted
      return
    }
    for (item in items) {
      if (item.label === label) {
        // address of the same label exists.
        return
      }
    }

    // create new random address
    val address = AddressGenerator.generate()
    val item = AddressItem(label, address.toBase58(), address.privSigningKey, address.privEncryptionKey)
    items.add(0, item)

    repository.saveAddress(item)
    navigator.onNewAddressGenerated()
  }

  fun deleteAddress(label: String) {
    repository.deleteAddress(label)
  }

  fun onGenerateButtonClicked() {
    addNewAddress(label.get())
  }
}