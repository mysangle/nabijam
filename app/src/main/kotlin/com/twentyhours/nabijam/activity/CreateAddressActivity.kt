package com.twentyhours.nabijam.activity

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import com.twentyhours.nabijam.R
import com.twentyhours.nabijam.adapter.AddressAdapter
import com.twentyhours.nabijam.databinding.ActivityCreateAddressBinding
import com.twentyhours.nabijam.extension.hideKeyboard
import com.twentyhours.nabijam.model.AddressItem
import com.twentyhours.nabijam.navigator.CreateAddressNavigator
import com.twentyhours.nabijam.viewmodel.CreateAddressViewModel
import io.realm.Realm
import io.realm.exceptions.RealmPrimaryKeyConstraintException
import kotlinx.android.synthetic.main.activity_create_address.*
import kotlin.properties.Delegates


class CreateAddressActivity : AppCompatActivity(), CreateAddressNavigator {
  private var realm: Realm by Delegates.notNull()

  override fun addNewAddress(newItem: AddressItem) {
    hideKeyboard()

    (address_list.adapter as AddressAdapter).addAddress(newItem)
    address_list.layoutManager.scrollToPosition(0)
    storeAddress(newItem)
  }

  lateinit var viewModel: CreateAddressViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val binding: ActivityCreateAddressBinding =
        DataBindingUtil.setContentView(this, R.layout.activity_create_address)

    viewModel = CreateAddressViewModel(this)
    binding.viewModel = viewModel

    realm = Realm.getDefaultInstance()

    setupActionBar()

    address_list.apply {
      setHasFixedSize(true)
      val linearLayout = LinearLayoutManager(context)
      layoutManager = linearLayout
    }

    initAdapter()

    val addressItems = realm.where(AddressItem::class.java).findAllSorted("label")
    (address_list.adapter as AddressAdapter).addAddresses(addressItems)
  }

  private fun setupActionBar() {
    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    supportActionBar?.setTitle(R.string.address)
  }

  private fun initAdapter() {
    if (address_list.adapter == null) {
      address_list.adapter = AddressAdapter()
    }
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    when (item?.itemId) {
      android.R.id.home -> {
        finish()
        return true
      }
    }
    return super.onOptionsItemSelected(item)
  }

  override fun finish() {
    super.finish()
    overridePendingTransition(R.anim.scale_up, R.anim.slide_out_right)
  }

  private fun storeAddress(addressItem: AddressItem) {
    realm.executeTransactionAsync { realm ->
      // This will create a new object in Realm or throw an exception if the
      // object already exists (same primary key)
      try {
        realm.copyToRealm(addressItem)
      } catch (e: RealmPrimaryKeyConstraintException) {
        // ignored
      }

      // This will update an existing object with the same primary key
      // or create a new object if an object with no primary key = 42
//      realm.copyToRealmOrUpdate(addressItem)
    }
  }
}
