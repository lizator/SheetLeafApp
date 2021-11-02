package dk.rbyte.sheetleafapp.outside

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import dk.rbyte.sheetleafapp.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

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

    }
}