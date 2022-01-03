package app.dealux.orangerestaurant.ui.splashscreen.di

import dagger.Subcomponent

@Subcomponent(modules = [MainModule::class])
interface MainComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): MainComponent
    }

    fun inject(activity: app.dealux.orangerestaurant.ui.splashscreen.SplashScreen)

}