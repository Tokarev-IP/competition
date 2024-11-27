package com.example.catalog.content.data.di

import com.example.catalog.content.data.adapters.FirebaseStorageDeleteAdapter
import com.example.catalog.content.data.adapters.FirebaseStorageDeleteAdapterInterface
import com.example.catalog.content.data.adapters.FirebaseStorageDownloadAdapter
import com.example.catalog.content.data.adapters.FirebaseStorageDownloadAdapterInterface
import com.example.catalog.content.data.adapters.FirebaseStorageUploadAdapter
import com.example.catalog.content.data.adapters.FirebaseStorageUploadAdapterInterface
import com.example.catalog.content.data.adapters.FirestoreDeleteAdapter
import com.example.catalog.content.data.adapters.FirestoreDeleteAdapterInterface
import com.example.catalog.content.data.adapters.FirestoreDownloadAdapter
import com.example.catalog.content.data.adapters.FirestoreDownloadAdapterInterface
import com.example.catalog.content.data.adapters.FirestoreUploadAdapter
import com.example.catalog.content.data.adapters.FirestoreUploadAdapterInterface
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AdapterModule {

    @Singleton
    @Binds
    fun bindFirebaseStorageDeleteAdapterInterface(impl: FirebaseStorageDeleteAdapter): FirebaseStorageDeleteAdapterInterface

    @Singleton
    @Binds
    fun bindFirebaseStorageDownloadAdapterInterface(impl: FirebaseStorageDownloadAdapter): FirebaseStorageDownloadAdapterInterface

    @Singleton
    @Binds
    fun bindFirebaseStorageUploadAdapterInterface(impl: FirebaseStorageUploadAdapter): FirebaseStorageUploadAdapterInterface

    @Singleton
    @Binds
    fun bindFirestoreDeleteAdapterInterface(impl: FirestoreDeleteAdapter): FirestoreDeleteAdapterInterface

    @Singleton
    @Binds
    fun bindFirestoreDownloadAdapterInterface(impl: FirestoreDownloadAdapter): FirestoreDownloadAdapterInterface

    @Singleton
    @Binds
    fun bindFirestoreUploadAdapterInterface(impl: FirestoreUploadAdapter): FirestoreUploadAdapterInterface

}