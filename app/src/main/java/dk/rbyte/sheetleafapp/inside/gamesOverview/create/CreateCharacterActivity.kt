package dk.rbyte.sheetleafapp.inside.gamesOverview.create

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dk.rbyte.sheetleafapp.R
import dk.rbyte.sheetleafapp.databinding.ActivityCreateCharacterBinding
import dk.rbyte.sheetleafapp.databinding.ActivityLoginBinding

class CreateCharacterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateCharacterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateCharacterBinding.inflate(layoutInflater)
        val root =  binding.root
        setContentView(root)



    }
}