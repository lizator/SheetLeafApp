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
    private var characterSheetFragment: CharacterSheetFragment? = null


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
        characterSheetFragment = null//CharacterSheetFragment.newInstance(-1,"S1,L1,R1,R2")

        replaceFragment(gamesOverviewFragment)

        binding.bottomNavigation3.setOnItemSelectedListener { item ->

            when (item.itemId) {
                R.id.bot_game_overview -> {
                    replaceFragment(gamesOverviewFragment)
                    true
                }
                R.id.bot_character_sheet -> {
                    if (characterSheetFragment != null)
                        replaceFragment(characterSheetFragment!!)
                    else
                        Toast.makeText(this, "Der skal vælges en karakter først!", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.bot_settings -> {
                    Toast.makeText(this, "Ikke implementeret", Toast.LENGTH_SHORT).show()
                    //replaceFragment(profileOverviewFragment)
                    true
                }
                else -> false
            }

        }

        //Observer for when choosing a character
        gamesOverviewFragment.chosenCharacter.observe(this, { character ->
            if (character != null) {
                characterSheetFragment =
                    CharacterSheetFragment.newInstance(character.characterID!!, character.sheet!!)
                replaceFragment(characterSheetFragment!!)
            }
        })


    }


    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fl_wrapper, fragment)
        transaction.commit()
    }
}