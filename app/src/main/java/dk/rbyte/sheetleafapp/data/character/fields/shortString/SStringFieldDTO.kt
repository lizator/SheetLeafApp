package dk.rbyte.sheetleafapp.data.character.fields.shortString

import dk.rbyte.sheetleafapp.data.character.fields.DataField

data class SStringFieldDTO(
    override var id: String,
    override var characterID: Int,
    override var title: String,
    override var value: Any
) : DataField {
}