package com.example.catalog.content.domain.di

import com.example.catalog.content.domain.functions.CreateDocFileInterface
import com.example.catalog.content.domain.functions.TransformBitmapImageInterface
import com.example.catalog.content.domain.interfaces.DeleteDataUseCaseInterface
import com.example.catalog.content.domain.interfaces.DeleteFileUseCaseInterface
import com.example.catalog.content.domain.interfaces.TransformImageUseCaseInterface
import com.example.catalog.content.domain.usecases.network.DownloadDataUseCase
import com.example.catalog.content.domain.usecases.network.DownloadFileUseCase
import com.example.catalog.content.domain.usecases.network.UploadDataUseCase
import com.example.catalog.content.domain.usecases.network.UploadFileUseCase
import com.example.catalog.content.domain.interfaces.DownloadDataUseCaseInterface
import com.example.catalog.content.domain.interfaces.DownloadFileUseCaseInterface
import com.example.catalog.content.domain.interfaces.GenerateAiTextUseCaseInterface
import com.example.catalog.content.domain.interfaces.UploadDataUseCaseInterface
import com.example.catalog.content.domain.interfaces.UploadFileUseCaseInterface
import com.example.catalog.content.domain.usecases.logic.CreateDocFile
import com.example.catalog.content.domain.usecases.logic.TransformImageUseCase
import com.example.catalog.content.domain.usecases.network.GenerateAiTextUseCase
import com.example.catalog.content.domain.usecases.logic.SegmentImageInterfaceUseCase
import com.example.catalog.content.domain.usecases.network.DeleteDataUseCase
import com.example.catalog.content.domain.usecases.network.DeleteFileUseCase
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
    fun bindTransformBitmapImageInterface(impl: SegmentImageInterfaceUseCase): TransformBitmapImageInterface

    @Singleton
    @Binds
    fun bindCreateDocFileInterface(impl: CreateDocFile): CreateDocFileInterface

    @Singleton
    @Binds
    fun bindDeleteDataUseCaseInterface(impl: DeleteDataUseCase): DeleteDataUseCaseInterface

    @Singleton
    @Binds
    fun bindDeleteFileUseCaseInterface(impl: DeleteFileUseCase): DeleteFileUseCaseInterface
}