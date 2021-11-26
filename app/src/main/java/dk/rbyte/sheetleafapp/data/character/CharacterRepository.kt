package dk.rbyte.sheetleafapp.data.character

import dk.rbyte.sheetleafapp.R
import dk.rbyte.sheetleafapp.data.WebServerPointer
import dk.rbyte.sheetleafapp.outside.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

class CharacterRepository {

    fun createCharacter(collection: CharacterCollectionDTO, callback: (Result<CharacterCollectionDTO>) -> Unit) {

        val characterAPI = CharacterApi.create().createCharacter(collection)

        characterAPI.enqueue(object : Callback<CharacterCollectionDTO> {
            override fun onResponse(call: Call<CharacterCollectionDTO>, response: Response<CharacterCollectionDTO>) {
                when {
                    response.isSuccessful -> {
                        val body = response.body()
                        if (body == null) callback(Result.failure(Exception()))
                        else callback(Result.success(body))
                    }
                    else -> callback(Result.failure(Exception(getError(response.code()))))
                }
            }

            override fun onFailure(call: Call<CharacterCollectionDTO>, t: Throwable) {
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



interface CharacterApi {
    @POST("/api/character/create")
    fun createCharacter(@Body dto: CharacterCollectionDTO): Call<CharacterCollectionDTO>

    companion object {

        var BASE_URL = WebServerPointer.getBaseURL()

        fun create() : CharacterApi {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(CharacterApi::class.java)

        }
    }

}