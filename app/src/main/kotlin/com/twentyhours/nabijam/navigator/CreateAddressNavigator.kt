package com.twentyhours.nabijam.navigator

import com.twentyhours.nabijam.model.AddressItem

/**
 * Created by soonhyung on 2/10/17.
 */
interface CreateAddressNavigator {
  fun addNewAddress(newItem: AddressItem)
}