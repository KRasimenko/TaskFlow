package com.practicum.taskmanager

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.practicum.taskmanager.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private val mainDestinations = setOf(
        R.id.homeFragment,
        R.id.scheduleFragment,
        R.id.favoritesFragment,
        R.id.notificationsFragment,
        R.id.profileFragment,
    )

    private val authDestinations = setOf(
        R.id.loginFragment,
        R.id.signUpFragment,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        val navHost = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHost.navController

        val app = application as TaskManagerApp
        if (app.repository.isLoggedIn() && navController.currentDestination?.id == R.id.loginFragment) {
            navController.navigate(R.id.action_login_to_home)
        }

        binding.bottomNav.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            val id = destination.id
            val isMain = id in mainDestinations
            val isAuth = id in authDestinations

            if (isMain && !app.repository.isLoggedIn()) {
                navController.navigate(R.id.loginFragment)
                return@addOnDestinationChangedListener
            }

            binding.bottomNav.visibility = if (isMain) View.VISIBLE else View.GONE
            binding.fabAdd.visibility = if (id == R.id.homeFragment) View.VISIBLE else View.GONE

            if (isAuth) {
                binding.bottomNav.menu.setGroupCheckable(0, true, false)
            }
        }

        binding.fabAdd.setOnClickListener {
            navController.navigate(
                R.id.taskDetailFragment,
                Bundle().apply { putString("taskId", "new") },
            )
        }
    }
}
