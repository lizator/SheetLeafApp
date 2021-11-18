package dk.rbyte.sheetleafapp.inside.gamesOverview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.rbyte.sheetleafapp.R
import dk.rbyte.sheetleafapp.databinding.FragmentGamesOverviewBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_userID = "userID"

/**
 * A simple [Fragment] subclass.
 * Use the [GamesOverviewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GamesOverviewFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var userID: Int? = null

    private var _binding: FragmentGamesOverviewBinding? = null
    private val binding get() = _binding!!

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
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(userID: Int) =
            GamesOverviewFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_userID, userID)
                }
            }
    }
}