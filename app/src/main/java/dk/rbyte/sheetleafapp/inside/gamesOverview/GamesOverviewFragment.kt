package dk.rbyte.sheetleafapp.inside.gamesOverview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import dk.rbyte.sheetleafapp.R
import dk.rbyte.sheetleafapp.databinding.FragmentGamesOverviewBinding

private const val ARG_userID = "userID"

class GamesOverviewFragment : Fragment() {
    private var userID: Int? = null

    private var _binding: FragmentGamesOverviewBinding? = null
    private val binding get() = _binding!!

    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.rotate_open_gameoverview) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.rotate_close_gameoverview) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.from_bottom_gameoverview) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.to_bottom_gameoverview) }
    private var addClicked = false

    private val gameOptions = arrayOf("Personligt")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userID = it.getInt(ARG_userID)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGamesOverviewBinding.inflate(inflater, container, false)
        val root = binding.root
        //binding.textview.text = userID.toString()

        binding.addbtn.setOnClickListener {
            onAddButtonClicked()
        }

        binding.addcharacterbtn.setOnClickListener {
            //TODO add intent for creating character
        }

        binding.addgamebtn.setOnClickListener {
            Toast.makeText(context, "Ikke implementeret", Toast.LENGTH_SHORT).show()
        }

        binding.gameSpinner.adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_dropdown_item_1line, gameOptions)

        binding.gameSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                TODO("Load characters for selected")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Load characters for personal to start with")
            }

        }




        return root
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
                    putInt(ARG_userID, userID)
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
}