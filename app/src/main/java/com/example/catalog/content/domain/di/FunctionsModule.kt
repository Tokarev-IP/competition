package com.example.catalog.content.domain.di

import com.example.catalog.content.domain.functions.CreateMenuPdfFileInterface
import com.example.catalog.content.domain.functions.TransformImageInterface
import com.example.catalog.content.domain.usecases.logic.CreateDocFile
import com.example.catalog.content.domain.usecases.logic.CreateDocFileInterface
import com.example.catalog.content.domain.usecases.logic.DishListFunctions
import com.example.catalog.content.domain.usecases.logic.DishListFunctionsInterface
import com.example.catalog.content.domain.usecases.logic.SaveMenuPdfFileUseCase
import com.example.catalog.content.domain.usecases.logic.TransformImageUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface FunctionsModule {

    @Singleton
    @Binds
    fun bindCreateMenuPdfFileInterface(impl: SaveMenuPdfFileUseCase): CreateMenuPdfFileInterface

    @Singleton
    @Binds
    fun bindTransformImageInterface(impl: TransformImageUseCase): TransformImageInterface

    @Singleton
    @Binds
    fun bindCreateDocFileInterface(impl: CreateDocFile): CreateDocFileInterface

    @Singleton
    @Binds
    fun bindDishListFunctionInterface(impl: DishListFunctions): DishListFunctionsInterface
}