package assignment.tokopedia.newsapp.ui.headlines

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
import assignment.tokopedia.newsapp.model.NewsSource
import assignment.tokopedia.newsapp.ui.MainActivity
import assignment.tokopedia.newsapp.viewmodel.HeadlinesViewModel
import assignment.tokopedia.newsapp.viewmodel.SharedViewModel
import kotlinx.android.synthetic.main.source_headlines_fragment.*

const val EXTRA_SOURCE_ID = "NEWS_SOURCE_ID"
const val EXTRA_SOURCE_NAME = "NEWS_SOURCE_NAME"

class SourceHeadlinesFragment : Fragment() {

    private lateinit var viewModel: HeadlinesViewModel
    private lateinit var sharedViewModel: SharedViewModel

    private val myAdapter by lazy { HeadlineListAdapter(this) }
    private val source: NewsSource = NewsSource()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.source_headlines_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).setSearchVisibility(true)
        arguments?.let {
            source.sourceNameId = it.getString(EXTRA_SOURCE_ID) ?: ""
            source.sourceName = it.getString(EXTRA_SOURCE_NAME) ?: ""
        }
    }

    override fun onDestroyView() {
        (activity as MainActivity).setSearchVisibility(false)
        super.onDestroyView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HeadlinesViewModel::class.java)

        viewModel.getSourceHeadlineListObservable().observe(viewLifecycleOwner, {
            myAdapter.updateList(it.data)
        })

        viewModel.getSearchHeadlineListObservable().observe(viewLifecycleOwner, {
            myAdapter.updateList(it)
        })

        headlines_recycler_view.adapter = myAdapter
        headlines_recycler_view.addItemDecoration(
            DividerItemDecoration(
                headlines_recycler_view.context,
                ClipDrawable.HORIZONTAL
            ).apply {
                setDrawable(
                    context?.let {
                        ContextCompat.getDrawable(it, R.drawable.recycler_view_divider)
                    }!!
                )
            })

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        sharedViewModel.getSearchStringObservable()
            .observe(viewLifecycleOwner, {
                viewModel.searchString(it)
            })

        viewModel.fetchNewsHeadlines(source.sourceNameId)
    }


}