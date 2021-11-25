package dk.rbyte.sheetleafapp.inside

import android.location.GnssAntennaInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import dk.rbyte.sheetleafapp.R
import dk.rbyte.sheetleafapp.databinding.ActivityNavigationBinding
import dk.rbyte.sheetleafapp.inside.characterSheet.CharacterSheetFragment
import dk.rbyte.sheetleafapp.inside.gamesOverview.GamesOverviewFragment
import dk.rbyte.sheetleafapp.inside.profileOverview.ProfileOverviewFragment

class NavigationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNavigationBinding
    private lateinit var gamesOverviewFragment: GamesOverviewFragment
    private lateinit var profileOverviewFragment: Fragment
    private lateinit var characterSheetFragment: CharacterSheetFragment


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityNavigationBinding.inflate(layoutInflater)
        val root =  binding.root
        setContentView(root)

        val userID = intent.getIntExtra("userID", -1)
        if (userID == -1) {
            Toast.makeText(applicationContext, "Der skete en fejl med brugeren", Toast.LENGTH_SHORT).show()
            finish()
        }

        gamesOverviewFragment = GamesOverviewFragment.newInstance(userID)
        profileOverviewFragment = Fragment()//ProfileOverviewFragment.newInstance("","")
        characterSheetFragment = CharacterSheetFragment.newInstance(-1,"S1,L1,R1,R2")

        replaceFragment(gamesOverviewFragment)

        /*binding.gameOverviewBtn.setOnClickListener {
            replaceFragment(gamesOverviewFragment)
        }

        binding.characterSheetBtn.setOnClickListener {
            replaceFragment(characterSheetFragment)
        }

        binding.profileSettingsBtn.setOnClickListener {
            Toast.makeText(applicationContext, "Not implemented!", Toast.LENGTH_SHORT).show()
        }*/



        /*binding.bottomNavigation3.setOnItemReselectedListener { item ->
            Toast.makeText(applicationContext, "Test", Toast.LENGTH_SHORT).show()

            when (item.itemId) {
                R.id.bot_game_overview -> {
                    replaceFragment(gamesOverviewFragment)
                    true
                }
                R.id.bot_character_sheet -> {
                    replaceFragment(characterSheetFragment)
                    true
                }
                R.id.bot_settings -> {
                    replaceFragment(profileOverviewFragment)
                    true
                }
                else -> false
            }
        }*/
        binding.bottomNavigation3.setOnItemSelectedListener { item ->
            Toast.makeText(applicationContext, "Test2", Toast.LENGTH_SHORT).show()

            when (item.itemId) {
                R.id.bot_game_overview -> {
                    replaceFragment(gamesOverviewFragment)
                    true
                }
                R.id.bot_character_sheet -> {
                    replaceFragment(characterSheetFragment)
                    true
                }
                R.id.bot_settings -> {
                    replaceFragment(profileOverviewFragment)
                    true
                }
                else -> false
            }

        }


    }


    private fun replaceFragment(fragment: Fragment) {
        if (fragment != null) { //TODO fix so it does not change until a character is selected
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fl_wrapper, fragment)
            transaction.commit()
        }
    }
}