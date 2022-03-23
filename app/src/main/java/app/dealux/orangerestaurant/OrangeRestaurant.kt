package app.dealux.orangerestaurant

import android.app.Application
import app.dealux.orangerestaurant.di.ApplicationComponent
import app.dealux.orangerestaurant.di.DaggerApplicationComponent

class OrangeRestaurant : Application() {

    lateinit var appComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerApplicationComponent.factory().create(this)

    }

}