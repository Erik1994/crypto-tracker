package com.example.cryptotracker.core.data.network

import com.example.cryptotracker.core.domain.util.NetworkError
import com.example.cryptotracker.core.domain.util.Result
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import io.reactivex.rxjava3.core.Single
import kotlinx.serialization.SerializationException
import retrofit2.Response
import kotlin.coroutines.cancellation.CancellationException

suspend inline fun <reified T> safeCall(
    execute: () -> HttpResponse
): Result<T, NetworkError> {
    return try {
        responseToResult(execute())
    } catch (e: UnresolvedAddressException) {
        Result.Error(NetworkError.NO_INTERNET)
    } catch (e: SerializationException) {
        Result.Error(NetworkError.SERIALIZATION)
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        Result.Error(NetworkError.UNKNOWN)
    }
}

inline fun <T: Any?, R> Single<Response<T>>.mapToResult(crossinline map: (T) -> R): Single<Result<R, NetworkError>> {
    return this.flatMap { response ->
        response.body()?.takeIf { response.isSuccessful }?.let {
            Single.just(Result.Success(data = map(it)))
        } ?: Single.just(response.code().errorStatusCodeToResult())
    }
}