package dk.rbyte.sheetleafapp.inside.gamesOverview.createCharacter

import android.app.AlertDialog
import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import dk.rbyte.sheetleaf.data.character.fields.FieldTypes
import dk.rbyte.sheetleafapp.R
import dk.rbyte.sheetleafapp.data.character.CharacterCollectionDTO
import dk.rbyte.sheetleafapp.data.character.CharacterDTO
import dk.rbyte.sheetleafapp.data.character.CharacterRepository
import dk.rbyte.sheetleafapp.data.character.fields.DataField
import dk.rbyte.sheetleafapp.data.profile.UserDTO
import java.lang.Exception

class CreateCharacterViewModel (var dataFields: ArrayList<DataField>, val profileID: Int){

    private val characterRepo = CharacterRepository()

    var updated = false
    val errorLiveData = MutableLiveData<String?>(null)
    val collectionLiveData = MutableLiveData<CharacterCollectionDTO?>(null)

    fun createPersonalCharacter(name: String){
        var longStringCount = 0
        var shortStringCount = 0
        var realNumberCount = 0

        var sheet = ""

        for (dataField in dataFields) {
            if (sheet != "") sheet += ","

            when (dataField.type) {
                FieldTypes.LONG_STRING_FIELD -> {
                    longStringCount += 1
                    dataField.id = "L$longStringCount"
                }
                FieldTypes.SHORT_STRING_FIELD -> {
                    shortStringCount += 1
                    dataField.id = "S$shortStringCount"
                }
                FieldTypes.REAL_NUMBER_FIELD -> {
                    realNumberCount += 1
                    dataField.id = "R$realNumberCount"
                }
                else -> {
                    //Not implemented
                }
            }
            sheet += dataField.id
        }

        val collection = CharacterCollectionDTO(
            CharacterDTO(null, name, profileID, null, sheet),
            dataFields
        )

        characterRepo.createCharacter(collection) { result ->
            if (result.isSuccess) {
                collectionLiveData.postValue(result.getOrNull())
            } else {
                var e = result.exceptionOrNull()
                if (e == null) e =
                    Exception(Resources.getSystem().getString(R.string.exception_error))
                errorLiveData.postValue(e.message)
            }
            updated = true
        }


    }


}