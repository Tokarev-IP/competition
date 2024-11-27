package com.example.catalog.content.data.di

import com.example.catalog.content.data.repositories.FirebaseStorageDeleteInterface
import com.example.catalog.content.data.repositories.FirebaseStorageDeleteRepository
import com.example.catalog.content.data.repositories.FirebaseStorageDownloadInterface
import com.example.catalog.content.data.repositories.FirebaseStorageDownloadRepository
import com.example.catalog.content.data.repositories.FirebaseStorageUploadInterface
import com.example.catalog.content.data.repositories.FirebaseStorageUploadRepository
import com.example.catalog.content.data.repositories.FirestoreDeleteInterface
import com.example.catalog.content.data.repositories.FirestoreDeleteRepository
import com.example.catalog.content.data.repositories.FirestoreDownloadInterface
import com.example.catalog.content.data.repositories.FirestoreDownloadRepository
import com.example.catalog.content.data.repositories.FirestoreUploadInterface
import com.example.catalog.content.data.repositories.FirestoreUploadRepository
import com.example.catalog.content.data.repositories.GeminiAiInterface
import com.example.catalog.content.data.repositories.GeminiAiRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

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

    @Singleton
    @Binds
    fun bindFirebaseStorageDeleteInterface(impl: FirebaseStorageDeleteRepository): FirebaseStorageDeleteInterface

    @Singleton
    @Binds
    fun bindFirestoreDeleteInterface(impl: FirestoreDeleteRepository): FirestoreDeleteInterface

}