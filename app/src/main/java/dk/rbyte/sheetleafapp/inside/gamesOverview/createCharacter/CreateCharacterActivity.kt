package dk.rbyte.sheetleafapp.inside.gamesOverview.createCharacter

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dk.rbyte.sheetleaf.data.character.fields.FieldTypes
import dk.rbyte.sheetleafapp.R
import dk.rbyte.sheetleafapp.data.character.fields.DataField
import dk.rbyte.sheetleafapp.databinding.ActivityCreateCharacterBinding
import java.util.*
import kotlin.collections.ArrayList

private const val ARG_profileID = "profileID"

class CreateCharacterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateCharacterBinding
    private lateinit var editRecycler: RecyclerView
    private lateinit var previewRecycler: RecyclerView
    private lateinit var dragAdapter: DragableCreationAdapter
    private lateinit var previewAdapter: CharacterFieldPreviewAdapter

    private lateinit var vm: CreateCharacterViewModel
    private val spinnerChoices = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateCharacterBinding.inflate(layoutInflater)
        val root =  binding.root
        setContentView(root)

        vm = CreateCharacterViewModel(ArrayList(), intent.getIntExtra(ARG_profileID, -1))

        //Setting up observers for when data is returned
        vm.collectionLiveData.observe(this, {collection ->
            if(vm.updated) {
                val name = collection?.character?.name.toString()
                Toast.makeText(this, "Karakteren $name er oprettet!", Toast.LENGTH_SHORT).show()
                vm.updated = false
                finish()
            }
        })

        vm.errorLiveData.observe(this, {
            if (vm.updated) {
                Toast.makeText(this, vm.errorLiveData.value.toString(), Toast.LENGTH_SHORT).show()
                vm.updated = false
            }
        })



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
            for (i in 0..vm.dataFields.size-1) {
                val vh = editRecycler.findViewHolderForAdapterPosition(i) as DragableCreationAdapter.ViewHolder
                val title = vh.titleEdit.text.toString()
                if (vm.dataFields[i].title != title) {
                    vm.dataFields[i].title = title
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
                    vm.dataFields.add(DataField("L1", -1, "", "", FieldTypes.LONG_STRING_FIELD))
                }
                1 -> { //Short text
                    vm.dataFields.add(DataField("S1", -1, "", "", FieldTypes.SHORT_STRING_FIELD))
                }
                2 -> { //Int
                    vm.dataFields.add(DataField("R1", -1, "", 0, FieldTypes.REAL_NUMBER_FIELD))
                }
            }

            dragAdapter.notifyItemInserted(vm.dataFields.size-1)
            previewAdapter.notifyItemInserted(vm.dataFields.size-1)

        }

        //saveBtn
        binding.BtnSaveCharactersheet.setOnClickListener {
            for (i in 0..vm.dataFields.size-1) {
                val vh = editRecycler.findViewHolderForAdapterPosition(i) as DragableCreationAdapter.ViewHolder
                val title = vh.titleEdit.text.toString()
                if (vm.dataFields[i].title != title) {
                    vm.dataFields[i].title = title
                    previewAdapter.notifyItemChanged(i)
                }
            }
            privateCreationAlert()
        }



        //TMP DATA
        /*vm.dataFields.addAll(arrayListOf(
            DataField("R1", -1, "", 0, FieldTypes.REAL_NUMBER_FIELD),
            DataField("L1", -1, "", "", FieldTypes.LONG_STRING_FIELD),
            DataField("S1", -1, "", "", FieldTypes.SHORT_STRING_FIELD),
            DataField("S2", -1, "", "", FieldTypes.SHORT_STRING_FIELD)
        ))*/


    }

    private fun privateCreationAlert() {
        val builder = AlertDialog.Builder(this)

        val inflater = layoutInflater
        builder.setTitle("Giv din karakter et navn!")
        val dialogLayout = inflater.inflate(R.layout.alert_create_personal_character, null)
        val editText  = dialogLayout.findViewById<EditText>(R.id.editName)
        builder.setView(dialogLayout)
        builder.setPositiveButton("OK") { _, _ ->
            val name = editText.text.toString()
            if (name == "") {
                Toast.makeText(this, "Du skal give et navn nu!", Toast.LENGTH_SHORT).show()
                privateCreationAlert()
            } else {
                vm.createPersonalCharacter(name)
            }
        }
        builder.show()
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

                Collections.swap(vm.dataFields, fromPos, toPos)
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

            val dataField = vm.dataFields[pos]

            when (dataField.type) {
                FieldTypes.REAL_NUMBER_FIELD -> {
                    viewHolder.header.text = "Tal (Maks. 9 cifre)"
                }
                FieldTypes.LONG_STRING_FIELD -> {
                    viewHolder.header.text = "Lang tekst (8192 karakterer)"
                }
                FieldTypes.SHORT_STRING_FIELD -> {
                    viewHolder.header.text = "Kort tekst (64 karakterer)"
                }
                else -> {
                    //Error
                }
            }

            viewHolder.titleEdit.setText(dataField.title)

            viewHolder.trash.setOnClickListener {
                vm.dataFields.removeAt(viewHolder.adapterPosition)
                dragAdapter.notifyItemRemoved(viewHolder.adapterPosition)
                previewAdapter.notifyItemRemoved(viewHolder.adapterPosition)
            }
        }

        // Return the size of your dataset (invoked by the layout manager)
        override fun getItemCount() = vm.dataFields.size

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
            val dataField = vm.dataFields[pos]

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
        override fun getItemCount() = vm.dataFields.size

    }
}