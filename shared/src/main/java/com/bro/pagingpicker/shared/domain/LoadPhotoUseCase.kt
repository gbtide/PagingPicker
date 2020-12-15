package com.bro.pagingpicker.shared.domain

import com.bro.pagingpicker.model.gallery.Image
import com.bro.pagingpicker.shared.di.IoDispatcher
import com.bro.pagingpicker.shared.result.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Created by kyunghoon on 2020-12-16
 */
class LoadPhotoUseCase @Inject constructor(
    @IoDispatcher dispatcher: CoroutineDispatcher
): FlowUseCase<Unit, LoadPhotoUseCaseResult>(dispatcher) {

    override fun execute(parameters: Unit): Flow<Result<LoadPhotoUseCaseResult>> {
        return flow {
            emit(Result.Loading)

            val listToAdd = ArrayList<Image>()
            for (i in 0..100) {
                listToAdd.add(Image("data://$i ${System.currentTimeMillis()}"))
            }
            emit(Result.Success(LoadPhotoUseCaseResult(listToAdd)))

            // if error
            // emit(Result.Error())
        }
    }

}

data class LoadPhotoUseCaseResult(
    val list: List<Image>
)