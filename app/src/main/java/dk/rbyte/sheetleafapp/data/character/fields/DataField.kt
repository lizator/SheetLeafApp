package dk.rbyte.sheetleafapp.data.character.fields

import dk.rbyte.sheetleaf.data.character.fields.FieldTypes

data class DataField (
    var id: String,
    var characterID: Int,
    var title: String,
    var value: Any,
    var type: FieldTypes
)