package app.dealux.orangerestaurant.ui.splashscreen.di

import androidx.lifecycle.ViewModel
import app.dealux.orangerestaurant.di.ViewModelKey
import app.dealux.orangerestaurant.ui.splashscreen.SplashScreenViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface MainModule {

    @Binds
    @IntoMap
    @ViewModelKey(SplashScreenViewModel::class)
    fun bindSplashScreenViewModel (viewModel: SplashScreenViewModel): ViewModel

}