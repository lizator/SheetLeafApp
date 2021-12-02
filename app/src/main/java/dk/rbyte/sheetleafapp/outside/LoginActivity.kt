package dk.rbyte.sheetleafapp.outside

import android.content.Intent
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import dk.rbyte.sheetleafapp.R
import dk.rbyte.sheetleafapp.data.login.LoginDTO
import dk.rbyte.sheetleafapp.data.login.LoginRepository
import dk.rbyte.sheetleafapp.databinding.ActivityLoginBinding
import dk.rbyte.sheetleafapp.inside.NavigationActivity


class LoginActivity : AppCompatActivity() {
    companion object {
        lateinit var appResources: Resources
    }
    private lateinit var binding: ActivityLoginBinding
    private val loginVM = LoginViewModel()
    private var progressBar: ProgressBar? = null
    private var btnArr = ArrayList<View>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val root =  binding.root
        setContentView(root)
        appResources = resources

        progressBar = binding.progressBar

        binding.forgotPassword.setOnClickListener {
            Toast.makeText(applicationContext,"Surt Show!", Toast.LENGTH_SHORT).show()
        }
        btnArr.add(binding.forgotPassword)

        binding.newProfile.setOnClickListener {
            val intent = Intent(this, CreateProfileActivity::class.java)
            startActivity(intent)
        }
        btnArr.add(binding.newProfile)

        binding.loginbtn.setOnClickListener {
            val email = binding.editEmail.text.toString()
            val pass = binding.editPassword.text.toString()
            setLoading(true)
            val login = LoginDTO(email, pass)
            loginVM.login(login)
        }
        btnArr.add(binding.loginbtn)

        loginVM.loginLiveData.observe(this, {
            if (loginVM.updated) {
                var user = loginVM.loginLiveData.value
                if (user != null) {
                    Toast.makeText(
                        applicationContext, String.format(
                            resources.getString(R.string.login_success),
                            loginVM.loginLiveData.value!!.name.toString()
                        ), Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this, NavigationActivity::class.java)
                    intent.putExtra("userID", user.id)
                    startActivity(intent)
                    loginVM.updated = false
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Der skete en fejl med at modtage data",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            setLoading(false)
        })

        loginVM.errorLiveData.observe(this, {
            if (loginVM.updated && loginVM.errorLiveData.value != null) {
                Toast.makeText(this, loginVM.errorLiveData.value.toString(), Toast.LENGTH_SHORT).show()
                loginVM.updated = false
                setLoading(false)
            }
        })

    }

    private fun setLoading(b: Boolean) {
        if (progressBar != null) progressBar!!.visibility = if (b) View.VISIBLE else View.GONE
        for (view: View in btnArr) {
            view.isClickable = !b
        }
    }
}