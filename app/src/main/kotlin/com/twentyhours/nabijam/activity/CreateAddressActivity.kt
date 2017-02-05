package com.twentyhours.nabijam.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.Button
import com.twentyhours.nabijam.R
import com.twentyhours.nabijam.adapter.AddressAdapter
import com.twentyhours.nabijam.model.AddressItem
import com.twentyhours.njbm.core.AddressGenerator
import kotlinx.android.synthetic.main.activity_create_address.*

import io.realm.Realm;
import io.realm.exceptions.RealmPrimaryKeyConstraintException
import kotlin.properties.Delegates


class CreateAddressActivity : AppCompatActivity(), AddressAdapter.onViewSelectedListener {
  private var realm: Realm by Delegates.notNull()

  override fun onItemSelected(address: AddressItem?, position: Int): Boolean {
    return true
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_create_address)

    realm = Realm.getDefaultInstance()

    val toolbar = findViewById(R.id.toolbar) as Toolbar
    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    supportActionBar?.setTitle(R.string.address)

    address_list.apply {
      setHasFixedSize(true)
      val linearLayout = LinearLayoutManager(context)
      layoutManager = linearLayout
    }

    initAdapter()

    // fetch addresses from db
    val addressItems = realm.where(AddressItem::class.java).findAllSorted("label")
    (address_list.adapter as AddressAdapter).addAddresses(addressItems)

    val generateButton = findViewById(R.id.generate) as Button
    generateButton.setOnClickListener { view ->
        if (!nickname.text.isEmpty()) {
          // create new address
          val address = AddressGenerator.generate()
          val item = AddressItem(nickname.text.toString(), address.toBase58(), address.privSigningKey, address.privEncryptionKey)

          (address_list.adapter as AddressAdapter).addAddress(item)
          
          storeAddress(item)
        }
    }
  }

  private fun initAdapter() {
    if (address_list.adapter == null) {
      address_list.adapter = AddressAdapter(this)
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
