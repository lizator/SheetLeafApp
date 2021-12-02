package dk.rbyte.sheetleafapp.data.login

import android.content.res.Resources
import dk.rbyte.sheetleafapp.R
import dk.rbyte.sheetleafapp.data.WebServerPointer
import dk.rbyte.sheetleafapp.data.profile.UserDTO
import dk.rbyte.sheetleafapp.outside.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

class LoginRepository {


    fun login(login: LoginDTO, callback: (Result<UserDTO>) -> Unit) {

        val loginApi = LoginApi.create().login(login)

        loginApi.enqueue(object : Callback<UserDTO> {
            override fun onResponse(call: Call<UserDTO>, response: Response<UserDTO>) {
                when {
                    response.isSuccessful -> {
                        val body = response.body()
                        if (body == null) callback(Result.failure(Exception()))
                        else callback(Result.success(body))
                    }
                    else -> callback(Result.failure(Exception(getError(response.code()))))
                }
            }

            override fun onFailure(call: Call<UserDTO>, t: Throwable) {
                callback(Result.failure(t))
            }
        })


    }

    private fun getError(errorCode: Int): String {
        return when (errorCode) {
            206  -> LoginActivity.appResources.getString(R.string.common_206)
            403  -> LoginActivity.appResources.getString(R.string.login_403)
            404  -> LoginActivity.appResources.getString(R.string.login_404)
            500  -> LoginActivity.appResources.getString(R.string.common_500)
            else -> LoginActivity.appResources.getString(R.string.common_unknown_error)
        }
    }

}



interface LoginApi {
    @POST("/open/profile/authenticate")
    fun login(@Body dto: LoginDTO): Call<UserDTO>

    companion object {

        var BASE_URL = WebServerPointer.getBaseURL()

        fun create() : LoginApi {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(LoginApi::class.java)

        }
    }

}