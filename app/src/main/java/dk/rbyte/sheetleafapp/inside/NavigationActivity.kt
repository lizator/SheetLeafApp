package dk.rbyte.sheetleafapp.inside

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import dk.rbyte.sheetleafapp.R
import dk.rbyte.sheetleafapp.databinding.ActivityLoginBinding
import dk.rbyte.sheetleafapp.databinding.ActivityNavigationBinding
import dk.rbyte.sheetleafapp.inside.characterSheet.CharacterSheetFragment
import dk.rbyte.sheetleafapp.inside.gamesOverview.GamesOverviewFragment
import dk.rbyte.sheetleafapp.inside.profileOverview.ProfileOverviewFragment

class NavigationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNavigationBinding
    private lateinit var gamesOverviewFragment: GamesOverviewFragment
    private lateinit var profileOverviewFragment: ProfileOverviewFragment
    private lateinit var characterSheetFragment: CharacterSheetFragment

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityNavigationBinding.inflate(layoutInflater)
        val root =  binding.root
        setContentView(R.layout.activity_navigation)

        val userID = intent.getIntExtra("userID", -1)
        if (userID == -1) {
            Toast.makeText(applicationContext, "Der skete en fejl med brugeren", Toast.LENGTH_SHORT).show()
            finish()
        }

        gamesOverviewFragment = GamesOverviewFragment.newInstance(userID)
        profileOverviewFragment = ProfileOverviewFragment.newInstance("","")
        characterSheetFragment = CharacterSheetFragment.newInstance("","")

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fl_wrapper, gamesOverviewFragment)
            .commit()


    }
}