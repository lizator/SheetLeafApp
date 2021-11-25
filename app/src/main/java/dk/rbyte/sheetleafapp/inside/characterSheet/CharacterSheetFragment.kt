package dk.rbyte.sheetleafapp.inside.characterSheet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.rbyte.sheetleafapp.databinding.FragmentCharacterSheetBinding
import dk.rbyte.sheetleafapp.inside.characterSheet.fields.FieldFragment
import dk.rbyte.sheetleafapp.inside.characterSheet.fields.longString.LongStringFragment
import dk.rbyte.sheetleafapp.inside.characterSheet.fields.realNumber.RealNumberFragment
import dk.rbyte.sheetleafapp.inside.characterSheet.fields.shortString.ShortStringFragment


private const val ARG_CHARACTER_ID = "characte_id"
private const val ARG_CHARACTER_SHEET = "character_sheet"

class CharacterSheetFragment : Fragment() {

    private var _binding: FragmentCharacterSheetBinding? = null
    private val binding get() = _binding!!


    private var characterID: Int? = null
    private var characterSheet: String? = null

    private val sheetFields = ArrayList<FieldFragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            characterID = it.getInt(ARG_CHARACTER_ID)
            characterSheet = it.getString(ARG_CHARACTER_SHEET)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCharacterSheetBinding.inflate(inflater, container, false)


        initSheet()



        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(characterID: Int, characterSheet: String) =
            CharacterSheetFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_CHARACTER_ID, characterID)
                    putString(ARG_CHARACTER_SHEET, characterSheet)
                }
            }
    }

    private fun initSheet() {
        val sheetIDs = characterSheet!!.split(",")
        sheetFields.clear()
        for (sheetID in sheetIDs) {
            when (sheetID[0]) {
                'S' -> {
                    //Short string field
                    sheetFields.add(ShortStringFragment.newInstance("Title", ""))

                }
                'L' -> {
                    //Long string field
                    sheetFields.add(LongStringFragment.newInstance("Title", ""))
                }
                'R' -> {
                    //Real number field
                    sheetFields.add(RealNumberFragment.newInstance("Title", 0))
                }
                else -> {
                    //Error
                }
            }
        }
    }
}