package com.example.cobra.exposit

import android.content.Intent
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {

    private var mDrawerLayout: DrawerLayout? = null
    private var mAuth: FirebaseAuth? = null
    override fun onStart() {
        super.onStart()
        // Check if user is signed in (not-null) and update UI accordingly.
        val currentUser = mAuth!!.currentUser
        if (currentUser == null) {
            sendToStart()
        } else {
            //Start with homefragment
            val homeFragment = HomeFragment()
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.content_frame, homeFragment).commit()

        }
    }


    private fun sendToStart() {
        val startIntent = Intent(this@MainActivity, AuthenticationActivity::class.java)
        startActivity(startIntent)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
        mDrawerLayout = findViewById(R.id.drawer_layout)

        //Add toolbar to layout
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        //Add the nav drawer button
        val actionbar = supportActionBar
        actionbar!!.setDisplayHomeAsUpEnabled(true)
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu)

        /*To receive callbacks when the user taps a list item in the drawer,
         implement the OnNavigationItemSelectedListener interface and attach it to your NavigationView
          by calling setNavigationItemSelectedListener()*/

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            // set item as selected to persist highlight
            menuItem.isChecked = true
            // close drawer when item is tapped

            mDrawerLayout!!.closeDrawers()
            selectDrawerItem(menuItem)
            true
        }


        //Listen for open/close events and other state changes
        mDrawerLayout!!.addDrawerListener(
                object : DrawerLayout.DrawerListener {
                    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                        // Respond when the drawer's position changes
                    }

                    override fun onDrawerOpened(drawerView: View) {
                        // Respond when the drawer is opened
                    }

                    override fun onDrawerClosed(drawerView: View) {
                        // Respond when the drawer is closed
                    }

                    override fun onDrawerStateChanged(newState: Int) {
                        // Respond when the drawer motion state changes
                    }
                }
        )
    }

    //Open the drawer when the button is tapped
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                mDrawerLayout!!.openDrawer(GravityCompat.START)
                return true
            }
            //logout from account and go to AuthenticationActivity
            R.id.logout_item -> {
                FirebaseAuth.getInstance().signOut()
                sendToStart()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //method to switch between fragments
    fun selectDrawerItem(menuItem: MenuItem) {

        // Create a new fragment and specify the fragment to show based on nav item clicked
        var fragment: Fragment? = null
        val fragmentClass: Class<*>

        when (menuItem.itemId) {
            R.id.add_item -> fragmentClass = AddFragment::class.java
            R.id.home_item -> fragmentClass = HomeFragment::class.java
            R.id.profile_item -> fragmentClass = ProfileFragment::class.java
            R.id.logout_item -> {
                FirebaseAuth.getInstance().signOut()
                sendToStart()
                fragmentClass = HomeFragment::class.java
            }

            else -> fragmentClass = HomeFragment::class.java
        }

        try {
            fragment = fragmentClass.newInstance() as Fragment
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Insert the fragment by replacing any existing fragment
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment!!).commit()
    }

    private fun removeAllFragments(fragmentManager: FragmentManager) {
        while (fragmentManager.backStackEntryCount > 0) {
            fragmentManager.popBackStackImmediate()
        }

    }
}
