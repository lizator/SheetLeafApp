package dk.rbyte.sheetleafapp.data.character.fields.realNumber

import dk.rbyte.sheetleaf.data.character.fields.FieldTypes
import dk.rbyte.sheetleafapp.data.character.fields.DataField

data class IntFieldDTO(
    override var id: String,
    override var characterID: Int,
    override var title: String,
    override var value: Any,
    override var type: FieldTypes = FieldTypes.REAL_NUMBER_FIELD
) : DataField {
}