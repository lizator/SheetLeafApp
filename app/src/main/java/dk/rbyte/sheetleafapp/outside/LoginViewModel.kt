package dk.rbyte.sheetleafapp.outside

import android.content.res.Resources
import androidx.lifecycle.MutableLiveData
import dk.rbyte.sheetleafapp.R
import dk.rbyte.sheetleafapp.data.login.LoginDTO
import dk.rbyte.sheetleafapp.data.login.LoginRepository
import dk.rbyte.sheetleafapp.data.profile.ProfileDTO
import dk.rbyte.sheetleafapp.data.profile.UserDTO
import java.lang.Exception

class LoginViewModel {
    private val loginRepo = LoginRepository();
    var updated = false
    val errorLiveData = MutableLiveData<String?>(null)
    val loginLiveData = MutableLiveData<UserDTO?>(null)

    fun login(login: LoginDTO) {
        loginRepo.login(login) {result: Result<UserDTO> ->
            if (result.isSuccess){
                loginLiveData.postValue(result.getOrNull())
            } else {
                var e = result.exceptionOrNull()
                if (e == null) e = Exception(Resources.getSystem().getString(R.string.exception_error))
                errorLiveData.postValue(e.message)
            }
            updated = true
        }
    }
}