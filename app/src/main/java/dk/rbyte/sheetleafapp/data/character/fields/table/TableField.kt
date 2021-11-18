package dk.rbyte.sheetleafapp.data.character.fields.table

import dk.rbyte.sheetleaf.data.character.fields.FieldTypes
import dk.rbyte.sheetleafapp.data.character.fields.DataField

data class TableField(
    override var id: String,
    override var characterID: Int,
    override var title: String,
    override var value: Any,
    override var type: FieldTypes = FieldTypes.TABLE_FIELD
) : DataField {
}