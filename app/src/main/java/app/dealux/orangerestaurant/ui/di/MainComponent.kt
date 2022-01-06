package app.dealux.orangerestaurant.ui.di

import app.dealux.orangerestaurant.ui.foodandcategoryfragment.FoodAndCategoryFragment
import app.dealux.orangerestaurant.ui.mainactivity.MainActivity
import app.dealux.orangerestaurant.ui.splashscreen.SplashScreen
import dagger.Subcomponent

@Subcomponent(modules = [MainModule::class])
interface MainComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): MainComponent
    }

    fun inject(activity: SplashScreen)
    fun inject(activity: MainActivity)
    fun inject(fragment: FoodAndCategoryFragment)

}