package com.example.catalog.content.domain.di

import com.example.catalog.content.domain.interfaces.TransformImageUseCaseInterface
import com.example.catalog.content.domain.usecases.DownloadDataUseCase
import com.example.catalog.content.domain.usecases.DownloadFileUseCase
import com.example.catalog.content.domain.usecases.UploadDataUseCase
import com.example.catalog.content.domain.usecases.UploadFileUseCase
import com.example.catalog.content.domain.interfaces.DownloadDataUseCaseInterface
import com.example.catalog.content.domain.interfaces.DownloadFileUseCaseInterface
import com.example.catalog.content.domain.interfaces.GenerateAiTextUseCaseInterface
import com.example.catalog.content.domain.interfaces.UploadDataUseCaseInterface
import com.example.catalog.content.domain.interfaces.UploadFileUseCaseInterface
import com.example.catalog.content.domain.usecases.TransformImageUseCase
import com.example.catalog.content.domain.usecases.GenerateAiTextUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface UseCasesModule {

    @Singleton
    @Binds
    fun bindDownloadDataUseCaseInterface(impl: DownloadDataUseCase): DownloadDataUseCaseInterface

    @Singleton
    @Binds
    fun bindDownloadFileUseCaseInterface(impl: DownloadFileUseCase): DownloadFileUseCaseInterface

    @Singleton
    @Binds
    fun bindUploadDataUseCAseInterface(impl: UploadDataUseCase): UploadDataUseCaseInterface

    @Singleton
    @Binds
    fun bindUploadFileUseCaseInterface(impl: UploadFileUseCase): UploadFileUseCaseInterface

    @Singleton
    @Binds
    fun bindGenerateAiTextUseCaseInterface(impl: GenerateAiTextUseCase): GenerateAiTextUseCaseInterface

    @Singleton
    @Binds
    fun bindTransformImageUseCaseInterface(impl: TransformImageUseCase): TransformImageUseCaseInterface
}