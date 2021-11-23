package dk.rbyte.sheetleafapp.inside.characterSheet.fields.shortString

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.rbyte.sheetleafapp.R
import dk.rbyte.sheetleafapp.databinding.FragmentLongStringBinding
import dk.rbyte.sheetleafapp.databinding.FragmentShortStringBinding
import dk.rbyte.sheetleafapp.inside.characterSheet.fields.FieldFragment
import dk.rbyte.sheetleafapp.inside.characterSheet.fields.longString.LongStringFragment


private const val TITLE_PARAM = "title"
private const val VALUE_PARAM = "value"


class ShortStringFragment : FieldFragment() {
    private var _binding: FragmentShortStringBinding? = null
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
        _binding = FragmentShortStringBinding.inflate(inflater, container, false)
        val root = binding.root

        binding.title.text = title.toString()

        binding.value.setText(value.toString())

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

    override fun getValue(): String {
        return binding.value.text.toString()
    }
}