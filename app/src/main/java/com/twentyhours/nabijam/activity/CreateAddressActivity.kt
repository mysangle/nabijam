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
          val address = AddressGenerator.generate()
          (address_list.adapter as AddressAdapter).addAddress(AddressItem(nickname.text.toString(), address.toBase58()))
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
}
