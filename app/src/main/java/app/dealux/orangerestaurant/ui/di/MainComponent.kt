package app.dealux.orangerestaurant.ui.di

import app.dealux.orangerestaurant.ui.fragments.view.FoodFragment
import app.dealux.orangerestaurant.ui.activitys.view.MainActivity
import app.dealux.orangerestaurant.ui.activitys.view.SplashScreen
import app.dealux.orangerestaurant.ui.fragments.view.CartFragment
import app.dealux.orangerestaurant.ui.fragments.view.FoodAndCategoryFragment
import app.dealux.orangerestaurant.ui.fragments.view.SelectTableFragment
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
    fun inject(fragment: FoodFragment)
    fun inject(fragment: CartFragment)
    fun inject(fragment: SelectTableFragment)

}