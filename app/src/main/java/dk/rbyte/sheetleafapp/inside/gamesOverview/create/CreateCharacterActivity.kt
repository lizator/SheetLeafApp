package dk.rbyte.sheetleafapp.inside.gamesOverview.create

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    private val dataFieldsDragList = ArrayList<DataField>()
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
            binding.editListView.visibility = View.GONE
            binding.previewListView.visibility = View.VISIBLE
        }

        binding.previewSwap.setOnClickListener {
            binding.editListView.visibility = View.VISIBLE
            binding.previewListView.visibility = View.GONE
        }


        //TMP DATA
        dataFieldsDragList.addAll(arrayListOf(
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

                Collections.swap(dataFieldsDragList, fromPos, toPos)
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

            val dataField = dataFieldsDragList[pos]

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
                dataFieldsDragList.removeAt(viewHolder.adapterPosition)
                dragAdapter.notifyItemRemoved(viewHolder.adapterPosition)
                previewAdapter.notifyItemRemoved(viewHolder.adapterPosition)
            }
        }

        // Return the size of your dataset (invoked by the layout manager)
        override fun getItemCount() = dataFieldsDragList.size

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
            val dataField = dataFieldsDragList[pos]

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
        override fun getItemCount() = dataFieldsDragList.size

    }
}