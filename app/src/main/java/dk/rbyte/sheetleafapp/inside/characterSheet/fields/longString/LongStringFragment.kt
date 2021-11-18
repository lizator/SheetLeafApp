package dk.rbyte.sheetleafapp.inside.characterSheet.fields.longString

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.rbyte.sheetleafapp.databinding.FragmentLongStringBinding

private const val TITLE_PARAM = "title"
private const val VALUE_PARAM = "value"

class LongStringFragment : Fragment() {
    private var _binding: FragmentLongStringBinding? = null
    private val binding get() = _binding!!

    private var title: String? = null
    private var value: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(TITLE_PARAM)
            value = it.getString(VALUE_PARAM)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLongStringBinding.inflate(inflater, container, false)
        val root = binding.root

        //TODO insert value and title when UI is made

        return root
    }

    companion object {
        @JvmStatic
        fun newInstance(title: String, value: String) =
            LongStringFragment().apply {
                arguments = Bundle().apply {
                    putString(TITLE_PARAM, title)
                    putString(VALUE_PARAM, value)
                }
            }
    }

    public fun getValue(): String {
        return binding.toString() //TODO fix when UI made
    }
}