package com.twentyhours.nabijam.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.widget.Button
import com.twentyhours.nabijam.R
import com.twentyhours.nabijam.adapter.AddressAdapter
import com.twentyhours.nabijam.core.Address
import com.twentyhours.nabijam.model.AddressItem
import kotlinx.android.synthetic.main.activity_create_address.*

class CreateAddressActivity : AppCompatActivity(), AddressAdapter.onViewSelectedListener {
  override fun onItemSelected(address: AddressItem?, position: Int): Boolean {
    return true
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_create_address)

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

    val generateButton = findViewById(R.id.generate) as Button
    generateButton.setOnClickListener { view ->
        if (!nickname.text.isEmpty()) {
          val address = Address.create(nickname.text.toString())
          (address_list.adapter as AddressAdapter).addAddress(AddressItem(address.nickname, address.address))
        }
    }
  }

  private fun initAdapter() {
    if (address_list.adapter == null) {
      address_list.adapter = AddressAdapter(this)
    }
  }
}
