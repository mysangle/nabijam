package com.twentyhours.nabijam.viewmodel

import android.databinding.ObservableField
import android.view.View
import com.twentyhours.nabijam.model.AddressItem
import com.twentyhours.nabijam.navigator.CreateAddressNavigator
import com.twentyhours.njbm.core.AddressGenerator

/**
 * Created by soonhyung on 2/10/17.
 */
class CreateAddressViewModel(val navigator: CreateAddressNavigator) {
  val label = ObservableField<String>()

  fun onGenerateClicked(view: View) {
    val address = AddressGenerator.generate()
    val item = AddressItem(label.get(), address.toBase58(), address.privSigningKey, address.privEncryptionKey)
    navigator.addNewAddress(item)
  }
}