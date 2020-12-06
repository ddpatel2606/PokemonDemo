package com.dixitpatel.pokemondemo.network


data class APIRequestResponseHandler<out T>(val status: AuthStatus, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T): APIRequestResponseHandler<T> = APIRequestResponseHandler(status = AuthStatus.SUCCESS, data = data, message = null)

        fun <T> error(data: T?, message: String): APIRequestResponseHandler<T> =
            APIRequestResponseHandler(status = AuthStatus.ERROR, data = data, message = message)

        fun <T> loading(data: T?): APIRequestResponseHandler<T> = APIRequestResponseHandler(status = AuthStatus.LOADING, data = data, message = null)
    }
}