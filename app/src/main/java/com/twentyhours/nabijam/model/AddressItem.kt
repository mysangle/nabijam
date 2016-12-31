package com.twentyhours.nabijam.model

/**
 * Created by soonhyung on 12/31/16.
 */
data class AddressItem(val nickname: String, val address: String) {
  override fun equals(other: Any?): Boolean = (nickname == (other as AddressItem).nickname)

  override fun hashCode(): Int = nickname.hashCode()
}