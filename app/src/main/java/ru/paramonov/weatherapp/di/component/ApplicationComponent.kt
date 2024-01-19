package ru.paramonov.weatherapp.di.component

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.paramonov.weatherapp.di.annotation.ApplicationScope
import ru.paramonov.weatherapp.di.module.DataModule
import ru.paramonov.weatherapp.di.module.NetworkModule

@ApplicationScope
@Component(
    modules = [
        DataModule::class,
        NetworkModule::class
    ]
)
interface ApplicationComponent {

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance context: Context
        ): ApplicationComponent
    }
}
