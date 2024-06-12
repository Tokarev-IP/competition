package com.example.catalog.login.domain.di

import com.example.catalog.login.data.AuthRepository
import com.example.catalog.login.data.interfaces.AuthRepositoryInterface
import com.example.catalog.login.domain.interfaces.PhoneAuthUseCaseInterface
import com.example.catalog.login.domain.usecases.PhoneAuthUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface LoginModule {

    @Singleton
    @Binds
    fun bindAuthRepositoryInterface(impl: AuthRepository): AuthRepositoryInterface

    @Singleton
    @Binds
    fun bindAuthUseCaseInterface(impl: PhoneAuthUseCase): PhoneAuthUseCaseInterface

}