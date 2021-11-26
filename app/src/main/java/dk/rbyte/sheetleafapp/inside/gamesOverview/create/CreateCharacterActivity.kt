package dk.rbyte.sheetleafapp.inside.gamesOverview.create

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dk.rbyte.sheetleaf.data.character.fields.FieldTypes
import dk.rbyte.sheetleafapp.R
import dk.rbyte.sheetleafapp.data.character.fields.DataField
import dk.rbyte.sheetleafapp.databinding.ActivityCreateCharacterBinding
import java.util.*
import kotlin.collections.ArrayList

class CreateCharacterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateCharacterBinding
    private val dataFields = ArrayList<DataField>()
    private lateinit var editRecycler: RecyclerView
    private lateinit var previewRecycler: RecyclerView
    private lateinit var dragAdapter: DragableCreationAdapter
    private lateinit var previewAdapter: CharacterFieldPreviewAdapter

    private val spinnerChoices = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateCharacterBinding.inflate(layoutInflater)
        val root =  binding.root
        setContentView(root)


        //Setting up adapters and recycleviews
        editRecycler = binding.recyclerEditCharacterSheet
        previewRecycler = binding.recyclerPreviewCharacterSheet

        dragAdapter = DragableCreationAdapter()
        previewAdapter = CharacterFieldPreviewAdapter()



        editRecycler.adapter = dragAdapter
        previewRecycler.adapter = previewAdapter

        val llm = LinearLayoutManager(applicationContext)
        llm.orientation = LinearLayoutManager.VERTICAL

        val llm2 = LinearLayoutManager(applicationContext)
        llm2.orientation = LinearLayoutManager.VERTICAL

        editRecycler.layoutManager = llm
        previewRecycler.layoutManager = llm2

        //Setting up drag feature
        val swiper = dragAdapter.SwipeGesture()
        val touchHelper = ItemTouchHelper(swiper)
        touchHelper.attachToRecyclerView(editRecycler)

        //Setting op switch Btns
        binding.editSwap.setOnClickListener {
            //Saving titles in dataFields
            for (i in 0..dataFields.size-1) {
                val vh = editRecycler.findViewHolderForAdapterPosition(i) as DragableCreationAdapter.ViewHolder
                val title = vh.titleEdit.text.toString()
                if (dataFields[i].title != title) {
                    dataFields[i].title = title
                    previewAdapter.notifyItemChanged(i)
                }
            }
            binding.editListView.visibility = View.GONE
            binding.previewListView.visibility = View.VISIBLE
        }

        binding.previewSwap.setOnClickListener {
            binding.editListView.visibility = View.VISIBLE
            binding.previewListView.visibility = View.GONE
        }

        //Setting up spinner

        //setting up spinner
        ArrayAdapter.createFromResource(applicationContext,
            R.array.character_creation_insert_array,
            android.R.layout.simple_dropdown_item_1line
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            binding.spinnerEditCharactersheet.adapter = adapter
        }

        //Insert btn setup
        binding.BtnInsertCharactersheet.setOnClickListener {
            val choice = binding.spinnerEditCharactersheet.selectedItemPosition

            when (choice) {
                0 -> { //Long text
                    dataFields.add(DataField("L1", -1, "", "", FieldTypes.LONG_STRING_FIELD))
                }
                1 -> { //Short text
                    dataFields.add(DataField("S1", -1, "", "", FieldTypes.SHORT_STRING_FIELD))
                }
                2 -> { //Int
                    dataFields.add(DataField("R1", -1, "", 0, FieldTypes.REAL_NUMBER_FIELD))
                }
            }

            dragAdapter.notifyItemInserted(dataFields.size-1)
            previewAdapter.notifyItemInserted(dataFields.size-1)

        }



        //TMP DATA
        dataFields.addAll(arrayListOf(
            DataField("R1", -1, "", 0, FieldTypes.REAL_NUMBER_FIELD),
            DataField("L1", -1, "", "", FieldTypes.LONG_STRING_FIELD),
            DataField("S1", -1, "", "", FieldTypes.SHORT_STRING_FIELD),
            DataField("S2", -1, "", "", FieldTypes.SHORT_STRING_FIELD)
        ))


    }



    private inner class DragableCreationAdapter() :
        RecyclerView.Adapter<DragableCreationAdapter.ViewHolder>() {


        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val fullView = view

            val trash = fullView.findViewById<ImageView>(R.id.trash_icon)
            val header = fullView.findViewById<TextView>(R.id.header)
            val titleEdit = fullView.findViewById<EditText>(R.id.title_edit)

        }

        inner class SwipeGesture: ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END, 0) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val fromPos = viewHolder.adapterPosition
                val toPos = target.adapterPosition

                Collections.swap(dataFields, fromPos, toPos)
                editRecycler.adapter?.notifyItemMoved(fromPos,toPos)
                previewRecycler.adapter?.notifyItemMoved(fromPos,toPos)

                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            }
        }

        // Create new views (invoked by the layout manager)
        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            // Create a new view, which defines the UI of the list item
            val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.recycler_create_charactersheet_field_line, viewGroup, false)

            return ViewHolder(view)
        }


        override fun onBindViewHolder(viewHolder: ViewHolder, pos: Int) {

            val dataField = dataFields[pos]

            when (dataField.type) {
                FieldTypes.REAL_NUMBER_FIELD -> {
                    viewHolder.header.text = "Number (9 decibles)"
                }
                FieldTypes.LONG_STRING_FIELD -> {
                    viewHolder.header.text = "Long Text (8192 characters)"
                }
                FieldTypes.SHORT_STRING_FIELD -> {
                    viewHolder.header.text = "Short Text (64 characters)"
                }
                else -> {
                    //Error
                }
            }

            viewHolder.titleEdit.setText(dataField.title)

            viewHolder.trash.setOnClickListener {
                dataFields.removeAt(viewHolder.adapterPosition)
                dragAdapter.notifyItemRemoved(viewHolder.adapterPosition)
                previewAdapter.notifyItemRemoved(viewHolder.adapterPosition)
            }
        }

        // Return the size of your dataset (invoked by the layout manager)
        override fun getItemCount() = dataFields.size

    }


    private inner class CharacterFieldPreviewAdapter() :
        RecyclerView.Adapter<CharacterFieldPreviewAdapter.ViewHolder>() {


        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val fullView = view

            val realNumberLayout = fullView.findViewById<View>(R.id.realNumberLayout)
            val realNumberTitle = fullView.findViewById<TextView>(R.id.realNumberTitle)

            val longStringLayout = fullView.findViewById<View>(R.id.longStringLayout)
            val longStringTitle = fullView.findViewById<TextView>(R.id.longStringTitle)

            val shortStringLayout = fullView.findViewById<View>(R.id.shortStringLayout)
            val shortStringTitle = fullView.findViewById<TextView>(R.id.shortStringTitle)


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
            val dataField = dataFields[pos]

            viewHolder.realNumberLayout.visibility = View.GONE
            viewHolder.longStringLayout.visibility = View.GONE
            viewHolder.shortStringLayout.visibility = View.GONE

            when (dataField.type) {
                FieldTypes.REAL_NUMBER_FIELD -> {
                    viewHolder.realNumberLayout.visibility = View.VISIBLE
                    viewHolder.realNumberTitle.text = dataField.title
                }
                FieldTypes.LONG_STRING_FIELD -> {
                    viewHolder.longStringLayout.visibility = View.VISIBLE
                    viewHolder.longStringTitle.text = dataField.title
                }
                FieldTypes.SHORT_STRING_FIELD -> {
                    viewHolder.shortStringLayout.visibility = View.VISIBLE
                    viewHolder.shortStringTitle.text = dataField.title

                }
                else -> {
                    //Error
                }
            }
        }

        // Return the size of your dataset (invoked by the layout manager)
        override fun getItemCount() = dataFields.size

    }
}