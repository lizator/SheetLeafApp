package dk.rbyte.sheetleafapp.outside

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.Observer
import dk.rbyte.sheetleafapp.R
import dk.rbyte.sheetleafapp.data.profile.ProfileDTO
import dk.rbyte.sheetleafapp.data.profile.UserDTO
import dk.rbyte.sheetleafapp.databinding.ActivityCreateProfileBinding

class CreateProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateProfileBinding
    private val viewModel = CreateProfileViewModel()
    private var progressBar: ProgressBar? = null
    private val btnArr = ArrayList<View>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateProfileBinding.inflate(layoutInflater)
        val root = binding.root
        setContentView(root)

        progressBar = binding.progressBar

        binding.createProfileBtn.setOnClickListener {
            val user = UserDTO()
            user.email = binding.editEmail.text.toString()
            user.name = binding.editName.text.toString()
            val profile = ProfileDTO(user, binding.editPassword.text.toString(), "") //Disguising password in ProfileDTO
            setLoading(true)
            viewModel.createProfile(profile)
        }
        btnArr.add(binding.createProfileBtn)

        viewModel.userLiveData.observe(this, Observer {
            if (viewModel.updated)
                if (viewModel.userLiveData.value != null) {
                    Toast.makeText(
                        applicationContext,
                        String.format(
                            applicationContext.getString(R.string.create_profile_success),
                            viewModel.userLiveData.value!!.email
                        ),
                        Toast.LENGTH_SHORT
                    ).show()
                    viewModel.updated = false
                    finish()

                } else {
                    Toast.makeText(applicationContext, applicationContext.getString(R.string.create_profile_data_error), Toast.LENGTH_SHORT).show()
                    viewModel.updated = false
                }
            setLoading(false)
        })

        viewModel.errorLiveData.observe(this, Observer {
            if (viewModel.updated)
                Toast.makeText(applicationContext, it.toString(), Toast.LENGTH_SHORT).show()
            setLoading(false)
        })

    }

    private fun setLoading(b: Boolean) {
        if (progressBar != null) progressBar!!.visibility = if (b) View.VISIBLE else View.GONE
        for (view: View in btnArr) {
            view.isClickable = !b
        }
    }
}