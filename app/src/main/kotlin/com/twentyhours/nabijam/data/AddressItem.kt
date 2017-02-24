package com.twentyhours.nabijam.data

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by soonhyung on 12/31/16.
 */
open class AddressItem(
    @PrimaryKey open var label: String = "",
    open var address: String = "",
    open var privSigningKeyWIF: String = "",
    open var privEncryptionKeyWIF: String = "")
  : RealmObject() {
  override fun equals(other: Any?): Boolean = (label == (other as AddressItem).label)

  override fun hashCode(): Int = label.hashCode()
}