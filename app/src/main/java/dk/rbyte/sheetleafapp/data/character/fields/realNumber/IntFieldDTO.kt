package dk.rbyte.sheetleafapp.data.character.fields.realNumber

import dk.rbyte.sheetleafapp.data.character.fields.DataField

data class IntFieldDTO(
    override var id: String,
    override var characterID: Int,
    override var title: String,
    override var value: Any
) : DataField {
}