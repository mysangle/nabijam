package com.twentyhours.nabijam.viewmodel

import com.twentyhours.nabijam.model.AddressItem
import com.twentyhours.nabijam.navigator.AddressNavigator
import com.twentyhours.nabijam.repository.AddressRepository
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

/**
 * Created by soonhyung on 2/17/17.
 */
class AddressViewModelTest {
  private val TITLE1 = "Label1"
  private val TITLE4 = "Label4"
  private var ADDRESSES: List<AddressItem>? = null

  private var viewModel: AddressViewModel? = null

  @Mock
  lateinit var repository: AddressRepository

  @Mock
  lateinit var navigator: AddressNavigator

  @Before
  fun setupTasksViewModel() {
    // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
    // inject the mocks in the test the initMocks method needs to be called.
    MockitoAnnotations.initMocks(this)

    ADDRESSES = arrayListOf(AddressItem(TITLE1, "Address1"),
        AddressItem("Label2", "Address2"),
        AddressItem("Label3", "Address3"))
    `when`(repository.getAddresses()).thenReturn(ADDRESSES)

    // Get a reference to the class under test
    viewModel = AddressViewModel(repository, navigator)
  }

  @Test
  fun loadAddressesFromRepository() {
    // when loading of addresses is requested
    viewModel?.start()
    // then data loaded
    assertFalse(viewModel?.items?.isEmpty()!!)
    assertTrue(viewModel?.items?.size === 3)
  }

  @Test
  fun clickOnGenerateButtonShowsAddAddressUi() {
    // When adding a new task
    viewModel?.addNewAddress(TITLE4)
    // Then the navigator is called
    verify(navigator).onNewAddressGenerated()
  }

  @Test
  fun addNewAddress() {
    viewModel?.start()

    // when adding new label
    viewModel?.addNewAddress(TITLE4)
    // then it is added
    assertTrue(viewModel?.items?.size === 4)
  }

  @Test
  fun addDuplicateAddress() {
    viewModel?.start()

    // when adding label exists
    viewModel?.addNewAddress(TITLE1)
    // then it is not added
    assertTrue(viewModel?.items?.size === 3)
  }

  @Test
  fun saveAddress() {
    // when adding label
    viewModel?.addNewAddress(TITLE1)
    // verify the repository is called
    verify<AddressRepository>(repository).saveAddress(any())
  }

  @Test
  fun deleteAddress() {
    // when deleted
    viewModel?.deleteAddress(TITLE1)
    // verify the repository is called
    verify(repository).deleteAddress(TITLE1)
  }

  private fun <T> any(): T {
    Mockito.any<T>()
    return uninitialized()
  }

  private fun <T> uninitialized(): T = null as T
}