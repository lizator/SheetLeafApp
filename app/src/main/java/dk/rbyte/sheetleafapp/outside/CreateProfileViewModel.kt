package dk.rbyte.sheetleafapp.outside

import android.content.res.Resources
import androidx.lifecycle.MutableLiveData
import dk.rbyte.sheetleafapp.R
import dk.rbyte.sheetleafapp.data.profile.ProfileDTO
import dk.rbyte.sheetleafapp.data.profile.ProfileRepository
import dk.rbyte.sheetleafapp.data.profile.UserDTO
import java.lang.Exception


class CreateProfileViewModel {
    private val profileRepo = ProfileRepository()
    var updated = false
    val errorLiveData = MutableLiveData<String?>(null)
    val userLiveData = MutableLiveData<UserDTO?>(null)

    fun createProfile(profile: ProfileDTO) {
        profileRepo.createProfile(profile) {result: Result<UserDTO> ->
            if (result.isSuccess){
                userLiveData.postValue(result.getOrNull())
            } else {
                var e = result.exceptionOrNull()
                if (e == null) e = Exception(Resources.getSystem().getString(R.string.exception_error))
                errorLiveData.postValue(e.message)
            }
            updated = true
        }
    }

}