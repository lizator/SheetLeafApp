package dk.rbyte.sheetleafapp.data.character.fields.line

import dk.rbyte.sheetleafapp.data.character.fields.DataField

data class LineFieldDTO(
    override var id: String,
    override var characterID: Int,
    override var title: String,
    override var value: Any
) : DataField