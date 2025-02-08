package com.erazero1.bookstore.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.erazero1.bookstore.R
import com.erazero1.bookstore.presentation.fragments.BooksFragment
import com.erazero1.bookstore.presentation.fragments.CartFragment
import com.erazero1.bookstore.presentation.fragments.FavoritesFragment
import com.erazero1.bookstore.presentation.fragments.ProfileFragment
import com.erazero1.bookstore.presentation.viewmodels.HomeViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {
    private lateinit var viewModel: HomeViewModel
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var currentFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        replaceFragment(BooksFragment())

        initViews()
        initListeners()
    }

    private fun initViews(){
        bottomNavigationView = findViewById(R.id.bottom_nav_view)
    }

    private fun initListeners() {
        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_books -> {
                    currentFragment = BooksFragment()
                }
                R.id.nav_cart -> {
                    currentFragment = CartFragment()
                }
                R.id.nav_profile -> {
                    currentFragment = ProfileFragment()
                }
                R.id.nav_favorites -> {
                    currentFragment = FavoritesFragment()
                }
                else -> {

                }
            }
            replaceFragment(currentFragment)
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment).commit()
    }


    companion object {
        private const val EXTRA_USER_ID = "user_id"
        fun newIntent(context: Context, userId: String): Intent {
            val intent = Intent(context, HomeActivity::class.java)
            intent.putExtra(EXTRA_USER_ID, userId)
            return intent
        }
    }
}
