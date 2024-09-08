package com.example.catalog.content.domain.di

import com.example.catalog.content.domain.usecases.logic.SaveMenuPdfFileUseCase
import com.example.catalog.content.domain.usecases.logic.SaveMenuPdfFileUseCaseInterface
import com.example.catalog.content.domain.usecases.logic.TransformBitmapImageUseCase
import com.example.catalog.content.domain.usecases.logic.TransformBitmapImageUseCaseInterface
import com.example.catalog.content.domain.usecases.logic.TransformImageUseCase
import com.example.catalog.content.domain.usecases.logic.TransformImageUseCaseInterface
import com.example.catalog.content.domain.usecases.network.DeleteDataUseCase
import com.example.catalog.content.domain.usecases.network.DeleteDataUseCaseInterface
import com.example.catalog.content.domain.usecases.network.DeleteFileUseCase
import com.example.catalog.content.domain.usecases.network.DeleteFileUseCaseInterface
import com.example.catalog.content.domain.usecases.network.DownloadDataUseCase
import com.example.catalog.content.domain.usecases.network.DownloadDataUseCaseInterface
import com.example.catalog.content.domain.usecases.network.DownloadFileUseCase
import com.example.catalog.content.domain.usecases.network.DownloadFileUseCaseInterface
import com.example.catalog.content.domain.usecases.network.GenerateAiTextUseCase
import com.example.catalog.content.domain.usecases.network.GenerateAiTextUseCaseInterface
import com.example.catalog.content.domain.usecases.network.UploadDataUseCase
import com.example.catalog.content.domain.usecases.network.UploadDataUseCaseInterface
import com.example.catalog.content.domain.usecases.network.UploadFileUseCase
import com.example.catalog.content.domain.usecases.network.UploadFileUseCaseInterface
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

    @Singleton
    @Binds
    fun bindTransformBitmapImageUseCaseInterface(impl: TransformBitmapImageUseCase): TransformBitmapImageUseCaseInterface

    @Singleton
    @Binds
    fun bindDeleteDataUseCaseInterface(impl: DeleteDataUseCase): DeleteDataUseCaseInterface

    @Singleton
    @Binds
    fun bindDeleteFileUseCaseInterface(impl: DeleteFileUseCase): DeleteFileUseCaseInterface

    @Singleton
    @Binds
    fun bindSaveMenuPdfFileUseCaseInterface(impl: SaveMenuPdfFileUseCase): SaveMenuPdfFileUseCaseInterface
}