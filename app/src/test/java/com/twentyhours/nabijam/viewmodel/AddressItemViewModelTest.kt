package com.twentyhours.nabijam.viewmodel

import com.twentyhours.nabijam.model.AddressItem
import com.twentyhours.nabijam.navigator.AddressItemNavigator
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import kotlin.test.assertEquals

/**
 * Created by soonhyung on 2/19/17.
 */
class AddressItemViewModelTest {
  private val TITLE1 = "Label"
  private val ADDRESS = "Address"
  private val item = AddressItem(TITLE1, ADDRESS)
  lateinit var viewModel: AddressItemViewModel

  @Mock
  lateinit var navigator: AddressItemNavigator

  @Before
  fun setupTasksViewModel() {
    // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
    // inject the mocks in the test the initMocks method needs to be called.
    MockitoAnnotations.initMocks(this)

    // Get a reference to the class under test
    viewModel = AddressItemViewModel(navigator)
  }

  @Test
  fun setAddress() {
    viewModel.setAddress(item)

    assertEquals(TITLE1, viewModel.label())
    assertEquals(ADDRESS, viewModel.address())
  }

  @Test
  fun clickOnAddress() {
    viewModel.setAddress(item)

    // When clicking address item
    viewModel.onAddressSelected()
    // Then the navigator is called
    Mockito.verify(navigator).onAddressSelected()
  }

  @Test
  fun clickOnDeleteButton() {
    viewModel.setAddress(item)

    // When clicking delete button
    viewModel.onDeleteClicked()
    // Then the navigator is called
    Mockito.verify(navigator).onDeleteClicked(item.label)
  }
}