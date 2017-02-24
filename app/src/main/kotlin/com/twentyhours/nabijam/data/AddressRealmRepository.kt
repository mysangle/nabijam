package com.twentyhours.nabijam.data

import com.twentyhours.nabijam.data.AddressRepository
import com.twentyhours.nabijam.data.AddressItem
import io.realm.Realm
import io.realm.exceptions.RealmPrimaryKeyConstraintException
import java.util.*

/**
 * Created by soonhyung on 2/17/17.
 */
class AddressRealmRepository : AddressRepository {
  private var realm = Realm.getDefaultInstance()

  override fun getAddresses(): List<AddressItem> {
    val addressItems = ArrayList<AddressItem>()
    val results = realm.where(AddressItem::class.java).findAllSorted("label")
    for (item in results) {
      addressItems.add(item)
    }
    return addressItems
  }

  override fun saveAddress(item: AddressItem) {
    realm.executeTransactionAsync { realm ->
      // This will create a new object in Realm or throw an exception if the
      // object already exists (same primary key)
      try {
        realm.copyToRealm(item)
      } catch (e: RealmPrimaryKeyConstraintException) {
        // ignored
      }

      // This will update an existing object with the same primary key
      // or create a new object if an object with no primary key = 42
      // realm.copyToRealmOrUpdate(addressItem)
    }
  }

  override fun deleteAddress(label: String) {
    realm.executeTransaction { realm ->
      val result = realm.where(AddressItem::class.java).equalTo("label", label).findAll()
      result.deleteAllFromRealm()
    }
  }
}