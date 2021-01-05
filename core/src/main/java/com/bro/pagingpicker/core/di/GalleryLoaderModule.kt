package com.bro.pagingpicker.core.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

/**
 * Created by kyunghoon on 2020-12-18
 */
@InstallIn(ApplicationComponent::class)
@Module
class GalleryLoaderModule {

//    @Provides
//    fun provideImageLoader(@ApplicationContext context: Context): ImageLoader = LocalImageLoader(ImageQueryExecutor(context))

}