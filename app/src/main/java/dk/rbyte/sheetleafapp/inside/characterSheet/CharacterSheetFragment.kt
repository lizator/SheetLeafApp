package dk.rbyte.sheetleafapp.inside.characterSheet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dk.rbyte.sheetleaf.data.character.fields.FieldTypes
import dk.rbyte.sheetleafapp.R
import dk.rbyte.sheetleafapp.data.character.fields.DataField
import dk.rbyte.sheetleafapp.databinding.FragmentCharacterSheetBinding


private const val ARG_CHARACTER_ID = "characte_id"
private const val ARG_CHARACTER_SHEET = "character_sheet"

class CharacterSheetFragment : Fragment() {

    private var _binding: FragmentCharacterSheetBinding? = null
    private val binding get() = _binding!!

    private val adapter = CharacterFieldAdapter()

    private var characterID: Int? = null
    private var characterSheet: String? = null

    private val sheetFields = ArrayList<DataField>()

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




        val llm = LinearLayoutManager(context)
        llm.orientation = LinearLayoutManager.VERTICAL
        binding.fieldRecycler.layoutManager = llm
        binding.fieldRecycler.adapter = adapter

        initSheet()

        //adapter.notifyDataSetChanged()


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
                    sheetFields.add(DataField(sheetID, characterID!!, "Title1", "test", FieldTypes.SHORT_STRING_FIELD))

                }
                'L' -> {
                    //Long string field
                    sheetFields.add(DataField(sheetID, characterID!!, "Title2", "test2", FieldTypes.LONG_STRING_FIELD))
                }
                'R' -> {
                    //Real number field
                    sheetFields.add(DataField(sheetID, characterID!!, "Title3 $sheetID", 0, FieldTypes.REAL_NUMBER_FIELD))
                }
                else -> {
                    //Error
                }
            }
        }
    }

    inner class CharacterFieldAdapter() :
        RecyclerView.Adapter<CharacterFieldAdapter.ViewHolder>() {


        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val fullView = view

            val realNumberLayout = fullView.findViewById<View>(R.id.realNumberLayout)
            val realNumberTitle = fullView.findViewById<TextView>(R.id.realNumberTitle)
            val realNumberValue = fullView.findViewById<EditText>(R.id.realNumberValue)

            val longStringLayout = fullView.findViewById<View>(R.id.longStringLayout)
            val longStringTitle = fullView.findViewById<TextView>(R.id.longStringTitle)
            val longStringValue = fullView.findViewById<EditText>(R.id.longStringValue)

            val shortStringLayout = fullView.findViewById<View>(R.id.shortStringLayout)
            val shortStringTitle = fullView.findViewById<TextView>(R.id.shortStringTitle)
            val shortStringValue = fullView.findViewById<EditText>(R.id.shortStringValue)


        }

        // Create new views (invoked by the layout manager)
        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            // Create a new view, which defines the UI of the list item
            val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.recycler_collected_fields, viewGroup, false)

            return ViewHolder(view)
        }

        // Replace the contents of a view (invoked by the layout manager)
        override fun onBindViewHolder(viewHolder: ViewHolder, pos: Int) {

            // Get element from your dataset at this position and replace the
            // contents of the view with that element
            val dataField = sheetFields[pos]

            when (dataField.type) {
                FieldTypes.REAL_NUMBER_FIELD -> {
                    viewHolder.realNumberLayout.visibility = View.VISIBLE
                    viewHolder.realNumberTitle.text = dataField.title
                    viewHolder.realNumberValue.setText(dataField.value.toString())
                }
                FieldTypes.LONG_STRING_FIELD -> {
                    viewHolder.longStringLayout.visibility = View.VISIBLE
                    viewHolder.longStringTitle.text = dataField.title
                    viewHolder.longStringValue.setText(dataField.value.toString())
                }
                FieldTypes.SHORT_STRING_FIELD -> {
                    viewHolder.shortStringLayout.visibility = View.VISIBLE
                    viewHolder.shortStringTitle.text = dataField.title
                    viewHolder.shortStringValue.setText(dataField.value.toString())

                }
                else -> {
                    //Error
                }
            }
        }

        // Return the size of your dataset (invoked by the layout manager)
        override fun getItemCount() = sheetFields.size

    }

}