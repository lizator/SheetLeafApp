package dk.rbyte.sheetleafapp.inside.characterSheet.fields.realNumber

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.rbyte.sheetleafapp.databinding.FragmentLongStringBinding
import dk.rbyte.sheetleafapp.databinding.FragmentRealNumberBinding
import dk.rbyte.sheetleafapp.inside.characterSheet.fields.FieldFragment
import java.lang.NumberFormatException

private const val TITLE_PARAM = "title"
private const val VALUE_PARAM = "value"

class RealNumberFragment : FieldFragment() {
    private var _binding: FragmentRealNumberBinding? = null
    private val binding get() = _binding!!

    private var title: String? = null
    private var value: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(TITLE_PARAM)
            value = it.getInt(VALUE_PARAM)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRealNumberBinding.inflate(inflater, container, false)
        val root = binding.root

        binding.title.text = title.toString()

        binding.value.setText(value.toString())

        return root
    }

    companion object {
        @JvmStatic
        fun newInstance(title: String, value: Int) =
            RealNumberFragment().apply {
                arguments = Bundle().apply {
                    putString(TITLE_PARAM, title)
                    putInt(VALUE_PARAM, value)
                }
            }
    }

    override fun getValue(): Int {
        var v = 0
        try {
            val x = binding.value.text.toString()
            v = x.toInt()
        } catch (e: NumberFormatException) {
            e.printStackTrace()
            v = -1
        }
        return v
    }
}