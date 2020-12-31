package com.bro.pagingpicker.core.di

import android.content.Context
import com.bro.pagingpicker.core.gallery.ImageQueryExecutor
import com.bro.pagingpicker.core.gallery.ImageLoader
import com.bro.pagingpicker.core.gallery.LocalImageLoader
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

//    @Provides
//    fun provideImageLoader(@ApplicationContext context: Context): ImageLoader = LocalImageLoader(ImageQueryExecutor(context))

}