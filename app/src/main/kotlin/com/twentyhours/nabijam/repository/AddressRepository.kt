package com.twentyhours.nabijam.repository

import com.twentyhours.nabijam.model.AddressItem

/**
 * Created by soonhyung on 2/18/17.
 */
interface AddressRepository {
  fun getAddresses(): List<AddressItem>
  fun saveAddress(item: AddressItem)
  fun deleteAddress(label: String)
}