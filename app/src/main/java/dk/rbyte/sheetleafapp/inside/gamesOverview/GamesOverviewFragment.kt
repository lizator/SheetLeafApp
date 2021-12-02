package dk.rbyte.sheetleafapp.inside.gamesOverview

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dk.rbyte.sheetleaf.data.character.fields.FieldTypes
import dk.rbyte.sheetleafapp.R
import dk.rbyte.sheetleafapp.data.character.CharacterDTO
import dk.rbyte.sheetleafapp.databinding.FragmentGamesOverviewBinding
import dk.rbyte.sheetleafapp.inside.gamesOverview.createCharacter.CreateCharacterActivity

private const val ARG_profileID = "profileID"

class GamesOverviewFragment : Fragment() {
    val chosenCharacter = MutableLiveData<CharacterDTO?>(null)

    private var profileID: Int? = null

    private var _binding: FragmentGamesOverviewBinding? = null
    private val binding get() = _binding!!

    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.rotate_open_gameoverview) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.rotate_close_gameoverview) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.from_bottom_gameoverview) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.to_bottom_gameoverview) }
    private var addClicked = false

    private val currentShowingCharacters = ArrayList<CharacterDTO>()

    private val gameOptions = arrayOf("Personligt")

    private val chooseCharacterAdapter = CharacterAdapter()

    private lateinit var vm : GamesOverviewViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            profileID = it.getInt(ARG_profileID)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGamesOverviewBinding.inflate(inflater, container, false)
        val root = binding.root

        vm = GamesOverviewViewModel(profileID!!)

        binding.addbtn.setOnClickListener {
            onAddButtonClicked()
        }

        binding.addcharacterbtn.setOnClickListener {
            val intent = Intent(context, CreateCharacterActivity::class.java)
            intent.putExtra(ARG_profileID, profileID)
            startActivity(intent)
        }

        binding.addgamebtn.setOnClickListener {
            Toast.makeText(context, "Ikke implementeret", Toast.LENGTH_SHORT).show()
        }

        binding.gameSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, gameOptions)

        binding.gameSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                //TODO("Load characters for selected")

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }

        //Setting up Character recycler
        val llm = LinearLayoutManager(context)
        llm.orientation = LinearLayoutManager.VERTICAL

        binding.recyclerChooseCharacter.layoutManager = llm
        binding.recyclerChooseCharacter.adapter = chooseCharacterAdapter


        //Setting up observers
        vm.charactersLiveData.observe(viewLifecycleOwner, {
            if (vm.updated && vm.charactersLiveData.value != null) {
                val size = currentShowingCharacters.size
                currentShowingCharacters.clear()
                chooseCharacterAdapter.notifyItemRangeRemoved(0, size)
                val gameChoice = binding.gameSpinner.selectedItemPosition
                for (character in vm.charactersLiveData.value!!){
                    when (gameChoice) {
                        0 -> {
                            if (character.gameID == null || character.gameID!! < 1) {
                                currentShowingCharacters.add(character)
                                chooseCharacterAdapter.notifyItemInserted(currentShowingCharacters.size-1)
                            }
                        }
                        else -> {
                            //Not implemented
                        }
                    }
                }

                vm.updated = false

            }
        })

        vm.errorLiveData.observe(viewLifecycleOwner, {
            if (vm.updated && vm.errorLiveData.value != null) {
                Toast.makeText(context, vm.errorLiveData.value.toString(), Toast.LENGTH_SHORT).show()
                vm.updated = false
                vm.errorLiveData.value = null

            }
        })


        return root
    }

    override fun onResume() {
        super.onResume()
        vm.getCharactersFromProfile()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param userID ID of logged in user.
         * @return A new instance of fragment GamesOverviewFragment.
         */

        @JvmStatic
        fun newInstance(userID: Int) =
            GamesOverviewFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_profileID, userID)
                }
            }
    }

    private fun onAddButtonClicked() {
        addClicked = !addClicked
        setAnimationFloating(addClicked)
        setVisibilityFloating(addClicked)
    }

    private fun setVisibilityFloating(clicked: Boolean) {
        if (clicked) {
            binding.addcharacterbtn.visibility = View.INVISIBLE
            binding.addgamebtn.visibility = View.INVISIBLE
        } else {
            binding.addcharacterbtn.visibility = View.VISIBLE
            binding.addgamebtn.visibility = View.VISIBLE
        }
        binding.addcharacterbtn.isClickable = clicked
        binding.addgamebtn.isClickable = clicked
    }

    private fun setAnimationFloating(clicked: Boolean) {
        if (clicked) {
            binding.addgamebtn.startAnimation(fromBottom)
            binding.addcharacterbtn.startAnimation(fromBottom)
            binding.addbtn.startAnimation(rotateOpen)
        } else {
            binding.addgamebtn.startAnimation(toBottom)
            binding.addcharacterbtn.startAnimation(toBottom)
            binding.addbtn.startAnimation(rotateClose)
        }
    }

    private inner class CharacterAdapter() :
        RecyclerView.Adapter<CharacterAdapter.ViewHolder>() {


        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val fullView = view

            val nameView = fullView.findViewById<TextView>(R.id.nameView)
            val chooseBtn = fullView.findViewById<Button>(R.id.btnChoose)

        }

        // Create new views (invoked by the layout manager)
        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            // Create a new view, which defines the UI of the list item
            val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.recycler_character, viewGroup, false)

            return ViewHolder(view)
        }

        // Replace the contents of a view (invoked by the layout manager)
        override fun onBindViewHolder(viewHolder: ViewHolder, pos: Int) {
            val character = currentShowingCharacters[viewHolder.adapterPosition]

            viewHolder.nameView.text = character.name

            viewHolder.chooseBtn.setOnClickListener {
                chosenCharacter.postValue(character)
            }

        }

        // Return the size of your dataset (invoked by the layout manager)
        override fun getItemCount() = currentShowingCharacters.size

    }

}