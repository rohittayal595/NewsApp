package assignment.tokopedia.newsapp.ui.sources

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import assignment.tokopedia.newsapp.R
import assignment.tokopedia.newsapp.model.NewsSource
import assignment.tokopedia.newsapp.ui.headlines.EXTRA_SOURCE_ID
import assignment.tokopedia.newsapp.ui.headlines.EXTRA_SOURCE_NAME


class SourceListAdapter(val sourcesFragment: SourcesFragment) :
    RecyclerView.Adapter<SourceListAdapter.NewsSourceViewHolder>() {

    var data = listOf<NewsSource>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsSourceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_main_recycler_view, parent, false) as CardView
        return NewsSourceViewHolder(view)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: NewsSourceViewHolder, position: Int) {
        holder.bind(data[position])
    }

    fun updateList(list: List<NewsSource>?) {
        if (list == null) return
        data = list
        notifyDataSetChanged()
    }

    inner class NewsSourceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val sourceDescription: TextView =
            itemView.findViewById(R.id.news_source_description)
        private val sourceName: TextView = itemView.findViewById(R.id.news_source_text)

        fun bind(item: NewsSource) {
            sourceName.text = item.sourceName
            sourceDescription.text = item.sourceDescription
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val newsSource: NewsSource = data[position]
                    val arguments = Bundle().apply {
                        putString(EXTRA_SOURCE_ID, newsSource.sourceNameId)
                        putString(EXTRA_SOURCE_NAME, newsSource.sourceName)
                    }
                    findNavController(sourcesFragment).navigate(
                        R.id.source_headline_fragment,
                        arguments
                    )
                }
            }
        }
    }
}


