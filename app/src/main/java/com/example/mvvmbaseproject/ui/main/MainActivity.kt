package com.example.mvvmbaseproject.ui.main

import android.app.SearchManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.mvvmbaseproject.R
import com.example.mvvmbaseproject.Utility.Utils
import com.example.mvvmbaseproject.ui.base.BaseActivity


class MainActivity : BaseActivity<MainViewModel>(),
    SearchedMovieFragment.OnFragmentInteractionListener {
    override fun onFragmentInteraction(uri: Uri) {

    }

    private var mMainViewModel: MainViewModel? = null
    private val mToolbar: Toolbar? = null
    private var mSearchString=""
    private var fragment:Fragment?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            fragment=MainFragment.newInstance()
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment as MainFragment)
                .commitNow()
        }


    }


    override fun getViewModel(): MainViewModel {
        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        return mMainViewModel as MainViewModel
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.dashboard, menu)

        val searchItem = menu?.findItem(R.id.action_search)

        val searchManager =
            this@MainActivity.getSystemService(Context.SEARCH_SERVICE) as SearchManager

        var searchView: SearchView? = null
        if (searchItem != null) {
            searchView = searchItem.actionView as SearchView
        }
        searchView?.setSearchableInfo(searchManager.getSearchableInfo(this@MainActivity.componentName))
        val queryTextListener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                mSearchString = newText
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                mSearchString = query
                Utils.hideSoftKeyboard(this@MainActivity)
                (fragment as MainFragment).updateSearchKey(mSearchString)

                return true
            }
        }

        searchView?.setOnQueryTextListener(queryTextListener)
        return super.onCreateOptionsMenu(menu)

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var id=item.itemId
        when(id){
            R.id.action_searched_movie->{
                replaceSearchedFragment()
                return true
            }
        }
        return false

    }


    private fun replaceSearchedFragment(){
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.container, SearchedMovieFragment(), "NewFragmentTag")
        ft.commit()
        ft.addToBackStack(null);
    }





}
