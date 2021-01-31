package com.recepyesilkaya.arabam.util

enum class State {
    LOADING,
    SUCCESS,
    ERROR
}

data class Resource<out T>(val state: State, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T): Resource<T> =
            Resource(state = State.SUCCESS, data = data, message = null)

        fun <T> error(data: T?, message: String): Resource<T> =
            Resource(state = State.ERROR, data = data, message = message)

        fun <T> loading(data: T?): Resource<T> =
            Resource(state = State.LOADING, data = data, message = null)
    }
}