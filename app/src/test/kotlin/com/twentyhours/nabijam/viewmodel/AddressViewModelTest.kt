package kotlin.com.twentyhours.nabijam.viewmodel

import com.twentyhours.nabijam.model.AddressItem
import com.twentyhours.nabijam.navigator.AddressNavigator
import com.twentyhours.nabijam.repository.AddressRepository
import com.twentyhours.nabijam.viewmodel.AddressViewModel
import org.junit.Before
import org.mockito.Mock
import org.mockito.MockitoAnnotations

/**
 * Created by soonhyung on 2/17/17.
 */
class AddressViewModelTest {
  private var ADDRESSES: List<AddressItem>? = null

  private var viewModel: AddressViewModel? = null

  @Mock
  private val addressRepository: AddressRepository? = null

  @Mock
  private val addressNavigator: AddressNavigator? = null

  @Before
  fun setupTasksViewModel() {
    // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
    // inject the mocks in the test the initMocks method needs to be called.
    MockitoAnnotations.initMocks(this)

    setupContext()

    // Get a reference to the class under test
    viewModel = AddressViewModel(addressRepository!!, addressNavigator!!)

  }

  private fun setupContext() {

  }
}