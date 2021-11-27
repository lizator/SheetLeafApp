package dk.rbyte.sheetleafapp.inside.characterSheet

import android.content.res.Resources
import androidx.lifecycle.MutableLiveData
import dk.rbyte.sheetleafapp.R
import dk.rbyte.sheetleafapp.data.character.CharacterCollectionDTO
import dk.rbyte.sheetleafapp.data.character.CharacterRepository
import java.lang.Exception

class CharacterSheetViewModel() {

    private val characterRepo = CharacterRepository()

    var updated = false
    val errorLiveData = MutableLiveData<String?>(null)
    val collectionLiveData = MutableLiveData<CharacterCollectionDTO?>(null)

    fun setCharacter(characterID: Int) {
        characterRepo.getCharacter(characterID) { result ->
            if (result.isSuccess) {
                collectionLiveData.postValue(result.getOrNull())
            } else {
                var e = result.exceptionOrNull()
                if (e == null) e = Exception(Resources.getSystem().getString(R.string.exception_error))
                errorLiveData.postValue(e.message)
            }
            updated = true
        }
    }

}