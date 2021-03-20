package com.mostafasadati.weathernow

data class Resource<out T>(val status: Status, val data: T?) {

    companion object {
        fun <T> success(data: T): Resource<T> =
            Resource(status = Status.SUCCESS, data = data,)

        fun <T> error(data: T?): Resource<T> =
            Resource(status = Status.ERROR, data = data)

        fun <T> loading(data: T?): Resource<T> =
            Resource(status = Status.LOADING, data = data)

        fun <T> loadingDbNull(data: T?): Resource<T> =
            Resource(status = Status.LOADING_DB_NULL, data = data)

        fun <T> loadingDbFull(data: T?): Resource<T> =
            Resource(status = Status.LOADING_DB_FULL, data = data)
    }
}