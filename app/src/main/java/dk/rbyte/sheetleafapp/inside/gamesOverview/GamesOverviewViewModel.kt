package dk.rbyte.sheetleafapp.inside.gamesOverview

import android.content.res.Resources
import androidx.lifecycle.MutableLiveData
import dk.rbyte.sheetleafapp.R
import dk.rbyte.sheetleafapp.data.character.CharacterDTO
import dk.rbyte.sheetleafapp.data.character.CharacterRepository
import java.lang.Exception

class GamesOverviewViewModel (var profileID: Int) {

    private val characterRepo = CharacterRepository()

    var updated = false
    val errorLiveData = MutableLiveData<String?>(null)
    val charactersLiveData = MutableLiveData<ArrayList<CharacterDTO>?>(null)

    fun setProfile(id: Int) {
        this.profileID = id
    }

    fun getCharactersFromProfile() {
        characterRepo.getCharactersFromProfile(profileID) { result ->
            if (result.isSuccess) {
                charactersLiveData.postValue(result.getOrNull())
            } else {
                var e = result.exceptionOrNull()
                if (e == null) e = Exception(Resources.getSystem().getString(R.string.exception_error))
                errorLiveData.postValue(e.message)
            }
            updated = true
        }
    }


}