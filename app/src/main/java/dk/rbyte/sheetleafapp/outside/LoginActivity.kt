package dk.rbyte.sheetleafapp.outside

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import dk.rbyte.sheetleafapp.data.login.LoginDTO
import dk.rbyte.sheetleafapp.data.login.LoginRepository
import dk.rbyte.sheetleafapp.databinding.ActivityLoginBinding
import dk.rbyte.sheetleafapp.inside.NavigationActivity


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginVM = LoginViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val root =  binding.root
        setContentView(root)

        binding.forgotPassword.setOnClickListener {
            Toast.makeText(applicationContext,"Surt Show!", Toast.LENGTH_SHORT).show()
        }

        binding.newProfile.setOnClickListener {
            val intent = Intent(this, CreateProfileActivity::class.java)
            startActivity(intent)
        }

        binding.loginbtn.setOnClickListener {
            val email = binding.editEmail.text.toString()
            val pass = binding.editPassword.text.toString()

            val login = LoginDTO(email, pass)
            loginVM.login(login)
        }


        loginVM.loginLiveData.observe(this, {
            if (loginVM.updated && loginVM.loginLiveData.value != null) {
                Toast.makeText(applicationContext, String.format(
                    "Velkommen %s, til SheetLeaf!",
                    loginVM.loginLiveData.value!!.name.toString()
                ), Toast.LENGTH_SHORT).show()
                val intent = Intent(this, NavigationActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(applicationContext, "Der skete en fejl med at modtage data", Toast.LENGTH_SHORT).show()
            }
        })

    }
}