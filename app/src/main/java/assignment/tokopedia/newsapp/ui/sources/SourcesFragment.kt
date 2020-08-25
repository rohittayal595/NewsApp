package assignment.tokopedia.newsapp.ui.sources

import android.graphics.drawable.ClipDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import assignment.tokopedia.newsapp.R
import assignment.tokopedia.newsapp.viewmodel.SourcesViewModel
import kotlinx.android.synthetic.main.news_sources_fragment.*

class SourcesFragment : Fragment() {

    private lateinit var viewModel: SourcesViewModel
    private val myAdapter by lazy { SourceListAdapter(this) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.news_sources_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SourcesViewModel::class.java)

        viewModel.getNewsSourceListObservable().observe(viewLifecycleOwner, {
            myAdapter.updateList(it.data)
        })

        main_recycler_view.adapter = myAdapter
        main_recycler_view.addItemDecoration(
            DividerItemDecoration(
                main_recycler_view.context,
                ClipDrawable.HORIZONTAL
            ).apply {
                setDrawable(
                    context?.let {
                        ContextCompat.getDrawable(it, R.drawable.recycler_view_divider)
                    }!!
                )
            })
    }

}