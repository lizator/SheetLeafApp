package dk.rbyte.sheetleafapp.data.character

import dk.rbyte.sheetleafapp.data.character.fields.DataField

data class CharacterCollectionDTO (var character:CharacterDTO, var fields: ArrayList<DataField>)