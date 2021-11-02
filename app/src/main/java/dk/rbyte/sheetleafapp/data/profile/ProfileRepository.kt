package dk.rbyte.sheetleafapp.data.profile

import android.content.res.Resources
import dk.rbyte.sheetleafapp.R
import dk.rbyte.sheetleafapp.data.WebServerPointer
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
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
        executor.execute {
            val resp = api.createProfile(profile).execute()
            when {
                resp.isSuccessful -> {
                    val body = resp.body()
                    if (body == null) callback(Result.failure(Exception(getError(206))))
                    else callback(Result.success(body))
                }
                else -> callback(Result.failure(Exception(getError(resp.code()))))
            }
        }


    }

    private fun getError(errorCode: Int): String {
        return when (errorCode) {
            206 -> Resources.getSystem().getString(R.string.common_206)
            400 -> Resources.getSystem().getString(R.string.create_profile_400)
            409 -> Resources.getSystem().getString(R.string.create_profile_409)
            500 -> Resources.getSystem().getString(R.string.common_500)
            else -> Resources.getSystem().getString(R.string.common_xxx)
        }
    }
}


interface ProfileApi {

    @POST("/open/profile/create")
    fun createProfile(@Body profile: ProfileDTO): Call<UserDTO>

    @GET("/api/user/getByID/{id}")
    fun getUser(@Path("id") id: Number)
}