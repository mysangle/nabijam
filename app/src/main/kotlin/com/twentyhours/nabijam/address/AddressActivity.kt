package com.twentyhours.nabijam.address

import android.app.AlertDialog
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.twentyhours.nabijam.R
import com.twentyhours.nabijam.address.AddressAdapter
import com.twentyhours.nabijam.databinding.ActivityAddressBinding
import com.twentyhours.nabijam.extension.hideKeyboard
import com.twentyhours.nabijam.address.AddressItemNavigator
import com.twentyhours.nabijam.address.AddressNavigator
import com.twentyhours.nabijam.data.AddressRealmRepository
import com.twentyhours.nabijam.data.AddressRepository
import com.twentyhours.nabijam.address.AddressViewModel
import kotlinx.android.synthetic.main.activity_address.*

/**
 * AddressActivity
 */
class AddressActivity : AppCompatActivity(), AddressNavigator, AddressItemNavigator {
  lateinit var viewModel: AddressViewModel
  lateinit var repository: AddressRepository

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val binding: ActivityAddressBinding =
        DataBindingUtil.setContentView(this, R.layout.activity_address)

    repository = AddressRealmRepository()
    viewModel = AddressViewModel(repository, this)
    binding.viewModel = viewModel

    setupActionBar()

    address_list.apply {
      setHasFixedSize(true)
      val linearLayout = LinearLayoutManager(context)
      layoutManager = linearLayout
    }

    initAdapter()

    viewModel.start()
  }

  private fun setupActionBar() {
    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    supportActionBar?.setTitle(R.string.address)
  }

  private fun initAdapter() {
    if (address_list.adapter == null) {
      address_list.adapter = AddressAdapter(this)
    }
  }

  override fun onSupportNavigateUp(): Boolean {
    onBackPressed()
    return true
  }

  override fun finish() {
    super.finish()
    overridePendingTransition(R.anim.scale_up, R.anim.slide_out_right)
  }

  override fun onNewAddressGenerated() {
    hideKeyboard()

    address_list.layoutManager.scrollToPosition(0)
  }

  override fun onAddressSelected() {
  }

  override fun onDeleteClicked(label: String) {
    val builder = AlertDialog.Builder(this)
    builder.setMessage(String.format(getString(R.string.delete_address), label))
        .setPositiveButton(R.string.delete) { dialog, whichButton ->
          viewModel.deleteAddress(label)
          viewModel.refresh()
        }
        .setNegativeButton(R.string.cancel) { dialog, whichButton ->

        }
    builder.create().show()
  }
}
