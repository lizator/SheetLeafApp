package dk.rbyte.sheetleafapp.inside.characterSheet

import android.content.res.Resources
import androidx.lifecycle.MutableLiveData
import dk.rbyte.sheetleafapp.R
import dk.rbyte.sheetleafapp.data.character.CharacterCollectionDTO
import dk.rbyte.sheetleafapp.data.character.CharacterDTO
import dk.rbyte.sheetleafapp.data.character.CharacterRepository
import dk.rbyte.sheetleafapp.data.character.fields.DataField
import java.lang.Exception

class CharacterSheetViewModel() {

    private val characterRepo = CharacterRepository()

    var updated = false
    val errorLiveData = MutableLiveData<String?>(null)
    val collectionLiveData = MutableLiveData<CharacterCollectionDTO?>(null)

    var characterID: Int? = null
    var characterSheet: String? = null
    val sheetFields = ArrayList<DataField>()

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

    fun updateCharacter(character: CharacterDTO) {
        val col = CharacterCollectionDTO(
            character,
            sheetFields
        )

        characterRepo.updateCharacter(col) {result ->
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