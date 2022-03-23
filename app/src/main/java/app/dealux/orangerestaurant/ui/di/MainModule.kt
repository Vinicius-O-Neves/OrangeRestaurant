package app.dealux.orangerestaurant.ui.di

import androidx.lifecycle.ViewModel
import app.dealux.orangerestaurant.di.ViewModelKey
import app.dealux.orangerestaurant.ui.fragments.viewmodel.FoodFragmentViewModel
import app.dealux.orangerestaurant.ui.activitys.viewmodel.SplashScreenViewModel
import app.dealux.orangerestaurant.ui.fragments.viewmodel.CartFragmentViewModel
import app.dealux.orangerestaurant.ui.fragments.viewmodel.FoodAndCategoryViewModel
import app.dealux.orangerestaurant.ui.fragments.viewmodel.SelectTableFragmentViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface MainModule {

    @Binds
    @IntoMap
    @ViewModelKey(SplashScreenViewModel::class)
    fun bindSplashScreenViewModel(viewModel: SplashScreenViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FoodAndCategoryViewModel::class)
    fun bindFoodAndCategoryViewModel(viewModel: FoodAndCategoryViewModel) : ViewModel
    
    @Binds
    @IntoMap
    @ViewModelKey(FoodFragmentViewModel::class)
    fun bindFoodFragmentViewModel(viewModel: FoodFragmentViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CartFragmentViewModel::class)
    fun bindCartFragmentViewModel(viewModel: CartFragmentViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SelectTableFragmentViewModel::class)
    fun bindSelectTableFragmentViewModel(viewModel: SelectTableFragmentViewModel) : ViewModel

}