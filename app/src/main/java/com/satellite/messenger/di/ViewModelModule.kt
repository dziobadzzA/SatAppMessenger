package com.satellite.messenger.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.satellite.messenger.pager.fragment.image.ImageViewModel
import com.satellite.messenger.pager.fragment.info.InfoPagerViewModel
import com.satellite.messenger.pager.fragment.text.TextViewModel
import com.satellite.messenger.pager.fragment.video.VideoViewModel
import com.satellite.messenger.ui.exit.ExitViewModel
import com.satellite.messenger.ui.feedback.FeedbackViewModel
import com.satellite.messenger.ui.infosat.InfoSatViewModel
import com.satellite.messenger.ui.login.LoginViewModel
import com.satellite.messenger.ui.messages.MessagesViewModel
import com.satellite.messenger.ui.phone.PhonesViewModel
import com.satellite.messenger.ui.profile.ProfileViewModel
import com.satellite.messenger.ui.settings.SettingsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindsViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(InfoSatViewModel::class)
    abstract fun infoSatViewModel(viewModel: InfoSatViewModel):ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MessagesViewModel::class)
    abstract fun messagesViewModel(viewModel: MessagesViewModel):ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PhonesViewModel::class)
    abstract fun phonesViewModel(viewModel: PhonesViewModel):ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    abstract fun settingsViewModel(viewModel: SettingsViewModel):ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FeedbackViewModel::class)
    abstract fun feedbackViewModel(viewModel: FeedbackViewModel):ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    abstract fun profileViewModel(viewModel: ProfileViewModel):ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun loginViewModel(viewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ExitViewModel::class)
    abstract fun exitViewModel(viewModel: ExitViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ImageViewModel::class)
    abstract fun imageViewModel(viewModel: ImageViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TextViewModel::class)
    abstract fun textViewModel(viewModel: TextViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(VideoViewModel::class)
    abstract fun videoViewModel(viewModel: VideoViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(InfoPagerViewModel::class)
    abstract fun infoViewModel(viewModel: InfoPagerViewModel): ViewModel
}