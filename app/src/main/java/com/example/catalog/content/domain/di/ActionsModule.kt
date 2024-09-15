package com.example.catalog.content.domain.di

import com.example.catalog.content.presentation.viewmodel.actions.EditDishItemActions
import com.example.catalog.content.presentation.viewmodel.actions.EditDishItemActionsInterface
import com.example.catalog.content.presentation.viewmodel.actions.EditDishListActions
import com.example.catalog.content.presentation.viewmodel.actions.EditDishListActionsInterface
import com.example.catalog.content.presentation.viewmodel.actions.EditInfoImageListActions
import com.example.catalog.content.presentation.viewmodel.actions.EditInfoImageListActionsInterface
import com.example.catalog.content.presentation.viewmodel.actions.EditMenuInfoActions
import com.example.catalog.content.presentation.viewmodel.actions.EditMenuInfoActionsInterface
import com.example.catalog.content.presentation.viewmodel.actions.EditSectionListActions
import com.example.catalog.content.presentation.viewmodel.actions.EditSectionListActionsInterface
import com.example.catalog.content.presentation.viewmodel.actions.GetDataActions
import com.example.catalog.content.presentation.viewmodel.actions.GetDataActionsInterface
import com.example.catalog.content.presentation.viewmodel.actions.MenuActions
import com.example.catalog.content.presentation.viewmodel.actions.MenuActionsInterface
import com.example.catalog.content.presentation.viewmodel.actions.PdfFileActions
import com.example.catalog.content.presentation.viewmodel.actions.PdfFileActionsInterface
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ActionsModule {

    @Singleton
    @Binds
    fun bindEditDishItemActionsInterface(impl: EditDishItemActions): EditDishItemActionsInterface

    @Singleton
    @Binds
    fun bindEditDishListActionsInterface(impl: EditDishListActions): EditDishListActionsInterface

    @Singleton
    @Binds
    fun bindGetDataActionsInterface(impl: GetDataActions): GetDataActionsInterface

    @Singleton
    @Binds
    fun bindMenuActionsInterface(impl: MenuActions): MenuActionsInterface

    @Singleton
    @Binds
    fun bindPdfFileActionsInterface(impl: PdfFileActions): PdfFileActionsInterface

    @Singleton
    @Binds
    fun bindEditSectionListActionsInterface(impl: EditSectionListActions): EditSectionListActionsInterface

    @Singleton
    @Binds
    fun bindEditInfoImageListActionsInterface(impl: EditInfoImageListActions): EditInfoImageListActionsInterface

    @Singleton
    @Binds
    fun bindEditMenuInfoActionsInterface(impl: EditMenuInfoActions): EditMenuInfoActionsInterface
}