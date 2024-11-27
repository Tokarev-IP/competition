package com.example.catalog.content.domain.di

import com.example.catalog.content.domain.usecases.CreateCreateMenuUseCases
import com.example.catalog.content.domain.usecases.CreateMenuUseCasesInterface
import com.example.catalog.content.domain.usecases.EditDishItemUseCases
import com.example.catalog.content.domain.usecases.EditDishItemUseCasesInterface
import com.example.catalog.content.domain.usecases.EditDishListItemUseCases
import com.example.catalog.content.domain.usecases.EditDishListItemUseCasesInterface
import com.example.catalog.content.domain.usecases.EditInfoImageListUseCases
import com.example.catalog.content.domain.usecases.EditInfoImageListUseCasesInterface
import com.example.catalog.content.domain.usecases.EditMenuInfoUseCases
import com.example.catalog.content.domain.usecases.EditMenuInfoUseCasesInterface
import com.example.catalog.content.domain.usecases.EditSectionListUseCases
import com.example.catalog.content.domain.usecases.EditSectionListUseCasesInterface
import com.example.catalog.content.domain.usecases.logic.SaveMenuPdfFileUseCase
import com.example.catalog.content.domain.usecases.logic.SaveMenuPdfFileUseCaseInterface
import com.example.catalog.content.domain.usecases.logic.TransformBitmapImage
import com.example.catalog.content.domain.usecases.logic.TransformBitmapImageInterface
import com.example.catalog.content.domain.usecases.logic.TransformImageUseCase
import com.example.catalog.content.domain.usecases.logic.TransformImageUseCaseInterface
import com.example.catalog.content.domain.usecases.GenerateAiTextUseCase
import com.example.catalog.content.domain.usecases.GenerateAiTextUseCaseInterface
import com.example.catalog.content.domain.usecases.GetDataUseCases
import com.example.catalog.content.domain.usecases.GetDataUseCasesInterface
import com.example.catalog.content.domain.usecases.PdfFileUseCasesInterface
import com.example.catalog.content.domain.usecases.PdfFileUseCases
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
    fun bindGenerateAiTextUseCaseInterface(impl: GenerateAiTextUseCase): GenerateAiTextUseCaseInterface

    @Singleton
    @Binds
    fun bindTransformImageUseCaseInterface(impl: TransformImageUseCase): TransformImageUseCaseInterface

    @Singleton
    @Binds
    fun bindTransformBitmapImageInterface(impl: TransformBitmapImage): TransformBitmapImageInterface

    @Singleton
    @Binds
    fun bindSaveMenuPdfFileUseCaseInterface(impl: SaveMenuPdfFileUseCase): SaveMenuPdfFileUseCaseInterface

    @Singleton
    @Binds
    fun bindEditDishItemUseCasesInterface(impl: EditDishItemUseCases): EditDishItemUseCasesInterface

    @Singleton
    @Binds
    fun bindEditDishListItemUseCasesInterface(impl: EditDishListItemUseCases): EditDishListItemUseCasesInterface

    @Singleton
    @Binds
    fun bindGetDataUseCasesInterface(impl: GetDataUseCases): GetDataUseCasesInterface

    @Singleton
    @Binds
    fun bindCreateMenuUseCasesInterface(impl: CreateCreateMenuUseCases): CreateMenuUseCasesInterface

    @Singleton
    @Binds
    fun bindPdfFileUseCasesInterface(impl: PdfFileUseCases): PdfFileUseCasesInterface

    @Singleton
    @Binds
    fun bindEditSectionListUseCasesInterface(impl: EditSectionListUseCases): EditSectionListUseCasesInterface

    @Singleton
    @Binds
    fun bindEditInfoImageListUseCasesInterface(impl: EditInfoImageListUseCases): EditInfoImageListUseCasesInterface

    @Singleton
    @Binds
    fun bindEditMenuInfoUseCasesInterface(impl: EditMenuInfoUseCases): EditMenuInfoUseCasesInterface
}