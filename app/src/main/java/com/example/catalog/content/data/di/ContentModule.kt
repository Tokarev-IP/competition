package com.example.catalog.content.data.di

import com.example.catalog.content.data.interfaces.FirebaseStorageDownloadInterface
import com.example.catalog.content.data.interfaces.FirebaseStorageUploadInterface
import com.example.catalog.content.data.interfaces.FirestoreDownloadInterface
import com.example.catalog.content.data.interfaces.FirestoreUploadInterface
import com.example.catalog.content.data.interfaces.GeminiAiInterface
import com.example.catalog.content.data.repositories.FirebaseStorageDownloadRepository
import com.example.catalog.content.data.repositories.FirebaseStorageUploadRepository
import com.example.catalog.content.data.repositories.FirestoreDownloadRepository
import com.example.catalog.content.data.repositories.FirestoreUploadRepository
import com.example.catalog.content.data.repositories.GeminiAiRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ContentModule {

    @Singleton
    @Binds
    fun bindFirebaseStorageUploadInterface(impl: FirebaseStorageUploadRepository): FirebaseStorageUploadInterface

    @Singleton
    @Binds
    fun bindFirebaseStorageDownloadInterface(impl: FirebaseStorageDownloadRepository): FirebaseStorageDownloadInterface

    @Singleton
    @Binds
    fun bindFirestoreDownloadInterface(impl: FirestoreDownloadRepository): FirestoreDownloadInterface

    @Singleton
    @Binds
    fun bindFirestoreUploadInterface(impl: FirestoreUploadRepository): FirestoreUploadInterface

    @Singleton
    @Binds
    fun bindGeminiAiInterface(impl: GeminiAiRepository): GeminiAiInterface

}