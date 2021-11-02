package dk.rbyte.sheetleafapp.outside

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import dk.rbyte.sheetleafapp.R
import dk.rbyte.sheetleafapp.data.profile.ProfileDTO
import dk.rbyte.sheetleafapp.data.profile.UserDTO
import dk.rbyte.sheetleafapp.databinding.ActivityCreateProfileBinding

class CreateProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateProfileBinding
    private val viewModel = CreateProfileViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateProfileBinding.inflate(layoutInflater)
        val root = binding.root
        setContentView(root)

        binding.createProfileBtn.setOnClickListener {
            val user = UserDTO()
            user.email = binding.editEmail.text.toString()
            user.name = binding.editName.text.toString()
            val profile = ProfileDTO(user, binding.editPassword.text.toString(), "") //Disguising password in ProfileDTO
            viewModel.createProfile(profile)
        }

        viewModel.userLiveData.observe(this, Observer {
            if (viewModel.updated) {
                Toast.makeText(
                    applicationContext,
                    String.format(
                        Resources.getSystem().getString(R.string.create_profile_success),
                    ),
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.updated = false
                finish()
            }
        })

        viewModel.errorLiveData.observe(this, Observer {
            if (viewModel.updated)
                Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show()
        })

    }
}