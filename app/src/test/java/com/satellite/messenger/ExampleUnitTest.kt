package com.satellite.messenger

import com.satellite.messenger.database.Repository
import com.satellite.messenger.ui.exit.ExitViewModel
import com.satellite.messenger.ui.feedback.FeedbackViewModel
import com.satellite.messenger.ui.infosat.InfoSatViewModel
import com.satellite.messenger.ui.login.LoginViewModel
import com.satellite.messenger.ui.messages.MessagesViewModel
import com.satellite.messenger.ui.phone.PhonesViewModel
import com.satellite.messenger.utils.state.Satellite
import com.satellite.messenger.utils.state.ServerMessage
import com.satellite.messenger.utils.state.UserModel
import kotlinx.coroutines.*
import org.junit.Assert
import org.junit.Test

class ExampleUnitTest {

    // test ExitViewModel
    @Test
    fun testExitViewModel()  {
        val viewModel = ExitViewModel()
        Assert.assertEquals(true, viewModel is ExitViewModel)
    }

    // test FeedBackViewModel
    @Test
    fun testFeedBackViewModel() {
        val viewModel = FeedbackViewModel()
        Assert.assertEquals(false, viewModel.sendFeedback("", ""))
    }

    // test InfoSatViewModel
    @Test
    fun testInfoSatViewModel() {
        val viewModel = InfoSatViewModel()

        GlobalScope.launch {
            viewModel.getItem()
        }

        if (viewModel.items.value.isNullOrEmpty()) {
            Assert.assertEquals(false, Satellite() != viewModel.item)
        }

    }

    // test LoginViewModel
    @Test
    fun testLoginViewModel() {
        val viewModel = LoginViewModel(Repository())
        viewModel.getModel("Email-test", "Link-test")
        Assert.assertEquals(true, viewModel.modelIn == UserModel("Email-test", "Link-test"))
    }

    // test MessageViewModel
    @Test
    fun testMessageViewModel() {
        val viewModel = MessagesViewModel(Repository())
        Assert.assertEquals(false, viewModel.controlMessage("", ""))
        Assert.assertEquals(true, viewModel.returnLastMessage() == ServerMessage())
    }

    // test PhonesViewModel
    @Test
    fun testPhonesViewModel() {
        val viewModel = PhonesViewModel()
        viewModel.getListPhone()
        val size = viewModel.itemPhone?.value?.size ?: 0
        var result = false
        if (size > 0)
            result = true
        Assert.assertEquals(false, result)
    }
}