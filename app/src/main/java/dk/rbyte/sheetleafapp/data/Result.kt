package dk.rbyte.sheetleafapp.data

import java.lang.Exception

open class Result<T>  // hide the private constructor to limit subclass types (Success, Error)
{
    override fun toString(): String {
        if (this is Success<*>) {
            val success = this as Success<*>
            return "Success[data=" + success.data.toString() + "]"
        } else if (this is Error) {
            val error = this as Error
            return "Error[exception=" + error.error.toString() + "]"
        }
        return ""
    }

    // Success sub-class
    class Success<T>(val data: T) : Result<Any?>()

    // Error sub-class
    class Error(val error: Exception) : Result<Any?>()
}