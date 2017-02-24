package com.twentyhours.nabijam.main

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import com.twentyhours.nabijam.R
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.*
import com.twentyhours.nabijam.address.AddressActivity
import java.util.*

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val mainViewPager = findViewById(R.id.main_view_pager) as ViewPager
    val pagerAdapter = MainViewPagerAdapter(supportFragmentManager)
    pagerAdapter.addFragment(FriendsFragment(), "Friends")
    pagerAdapter.addFragment(ChatsFragment(), "Chats")
    mainViewPager.adapter = pagerAdapter

    val mainTabs = findViewById(R.id.main_tabs) as TabLayout
    mainTabs.setupWithViewPager(mainViewPager)

    val fab = findViewById(R.id.fab) as FloatingActionButton
    fab.setOnClickListener { view ->
      showCreateAddressActivity()
    }
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    // Inflate the menu; this adds items to the action bar if it is present.
    menuInflater.inflate(R.menu.menu_main, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    val id = item.itemId

    if (id == R.id.action_settings) {
      return true
    }

    return super.onOptionsItemSelected(item)
  }

  fun showCreateAddressActivity() {
    startActivity(Intent(this, AddressActivity::class.java))
    overridePendingTransition(R.anim.slide_in_right, R.anim.scale_down)
  }

  class MainViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    private val mFragments = ArrayList<Fragment>()
    private val mFragmentTitles = ArrayList<String>()

    fun addFragment(fragment: Fragment, title: String) {
      mFragments.add(fragment)
      mFragmentTitles.add(title)
    }

    override fun getCount(): Int {
      return mFragments.size
    }

    override fun getItem(position: Int): Fragment {
      return mFragments[position]
    }

    override fun getPageTitle(position: Int): CharSequence {
      return mFragmentTitles[position]
    }
  }
}
