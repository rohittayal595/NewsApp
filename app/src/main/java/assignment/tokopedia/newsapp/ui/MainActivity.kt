package assignment.tokopedia.newsapp.ui

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import assignment.tokopedia.newsapp.R
import assignment.tokopedia.newsapp.viewmodel.SharedViewModel


class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    lateinit var menu: Menu
    private lateinit var searchView: SearchView
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        this.menu = menu
        menuInflater.inflate(R.menu.main_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
        }
        searchView.setOnQueryTextListener(this)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.search) {
            onSearchRequested()
            return true
        }
        return false
    }

    fun setSearchVisibility(setVisible: Boolean) {
        menu.findItem(R.id.search).collapseActionView()
        searchView.isIconified = true
        searchView.onActionViewCollapsed()
        menu.findItem(R.id.search).isVisible = setVisible
    }

    override fun onSearchRequested(): Boolean {
        val mi: MenuItem = menu.findItem(R.id.search)
        if (mi.isActionViewExpanded) {
            mi.collapseActionView()
        } else {
            mi.expandActionView()
        }
        return super.onSearchRequested()
    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        sharedViewModel.searchUpdated(p0 ?: "")
        return true
    }

    override fun onBackPressed() {
        if (!searchView.isIconified) {
            searchView.isIconified = true
        } else {
            super.onBackPressed()
        }
    }

}