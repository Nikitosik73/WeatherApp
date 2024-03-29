package ru.paramonov.weatherapp.di.component

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.paramonov.weatherapp.di.annotation.ApplicationScope
import ru.paramonov.weatherapp.di.module.DataModule
import ru.paramonov.weatherapp.di.module.NetworkModule
import ru.paramonov.weatherapp.di.module.PresentationModule
import ru.paramonov.weatherapp.presentation.MainActivity

@ApplicationScope
@Component(
    modules = [
        DataModule::class,
        NetworkModule::class,
        PresentationModule::class
    ]
)
interface ApplicationComponent {

    fun inject(activity: MainActivity)

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance context: Context
        ): ApplicationComponent
    }
}
