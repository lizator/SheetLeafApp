package dk.rbyte.sheetleafapp.data.profile

import android.content.res.Resources
import dk.rbyte.sheetleafapp.R
import dk.rbyte.sheetleafapp.data.WebServerPointer
import dk.rbyte.sheetleafapp.outside.LoginActivity
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.concurrent.Executor

class ProfileRepository {
    val api = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(WebServerPointer.getBaseURL())
        .build()
        .create<ProfileApi>()
    private val executor = Executor {
        it.run()
    }


    fun createProfile(profile: ProfileDTO, callback: (Result<UserDTO>) -> Unit) {

        val profileApi = ProfileApi.create().createProfile(profile)

        profileApi.enqueue(object : Callback<UserDTO> {
            override fun onResponse(call: Call<UserDTO>, response: Response<UserDTO>) {
                when {
                    response.isSuccessful -> {
                        val body = response.body()
                        if (body == null) callback(Result.failure(Exception(getError(206))))
                        else callback(Result.success(body))
                    }
                    else ->callback(Result.failure(Exception(getError(response.code()))))
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
            400  -> LoginActivity.appResources.getString(R.string.create_profile_400)
            409  -> LoginActivity.appResources.getString(R.string.create_profile_409)
            500  -> LoginActivity.appResources.getString(R.string.common_500)
            else -> LoginActivity.appResources.getString(R.string.common_unknown_error)
        }
    }
}


interface ProfileApi {
    @GET("/api/user/getByID/1")
    fun getTest(): Call<UserDTO>

    @POST("/open/profile/create")
    fun createProfile(@Body profile: ProfileDTO): Call<UserDTO>

    @GET("/api/user/getByID/{id}")
    fun getUser(@Path("id") id: Number): Call<UserDTO>


    companion object {

        var BASE_URL = WebServerPointer.getBaseURL()

        fun create() : ProfileApi {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(ProfileApi::class.java)

        }
    }

}