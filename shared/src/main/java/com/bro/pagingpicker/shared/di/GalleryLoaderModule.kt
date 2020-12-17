package com.bro.pagingpicker.shared.di

import android.content.Context
import com.bro.pagingpicker.shared.gallery.ImageCursorFactory
import com.bro.pagingpicker.shared.gallery.ImageLoader
import com.bro.pagingpicker.shared.gallery.LocalImageLoader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext

/**
 * Created by kyunghoon on 2020-12-18
 */
@InstallIn(ApplicationComponent::class)
@Module
class GalleryLoaderModule {

    @Provides
    fun provideImageLoader(@ApplicationContext context: Context): ImageLoader = LocalImageLoader(ImageCursorFactory(context))

}