package com.example.gameclock.data.di

import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import com.example.gameclock.data.workManager.factory.AlarmWorkerFactory
import com.example.gameclock.data.workManager.factory.ChildWorkerFactory
import com.example.gameclock.data.workManager.factory.RescheduleAlarmWorkerFactory
import com.example.gameclock.data.workManager.factory.WrapperWorkerFactory
import com.example.gameclock.data.workManager.worker.AlarmWorker
import com.example.gameclock.data.workManager.worker.RescheduleAlarmWorker
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@MapKey
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class WorkerKey(val value: KClass<out ListenableWorker>)

@InstallIn(SingletonComponent::class)
@Module
abstract class WorkerModule {

    @Binds
    abstract fun bindWorkerFactoryModule(workerFactory: WrapperWorkerFactory): WorkerFactory

    @Binds
    @IntoMap
    @WorkerKey(AlarmWorker::class)
    abstract fun bindAlarmWorker(alarmWorkerFactory: AlarmWorkerFactory): ChildWorkerFactory

    @Binds
    @IntoMap
    @WorkerKey(RescheduleAlarmWorker::class)
    abstract fun bindRescheduleAlarmWorker(rescheduleAlarmWorkerFactory: RescheduleAlarmWorkerFactory): ChildWorkerFactory

}
