package assignment.tokopedia.newsapp.ui.headlines

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import assignment.tokopedia.newsapp.R
import assignment.tokopedia.newsapp.model.SourceHeadline
import assignment.tokopedia.newsapp.ui.detail.EXTRA_URL


class HeadlineListAdapter(val sourceHeadlinesFragment: SourceHeadlinesFragment) :
    RecyclerView.Adapter<HeadlineListAdapter.SourceHeadlineViewHolder>() {

    private var data = listOf<SourceHeadline>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SourceHeadlineViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_second_recycler_view, parent, false) as CardView
        return SourceHeadlineViewHolder(view)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: SourceHeadlineViewHolder, position: Int) {
        holder.bind(data[position])
    }

    fun updateList(list: List<SourceHeadline>?) {
        if (list == null) return
        data = list
        notifyDataSetChanged()
    }

    fun getListOfSourceHeadlines(): List<SourceHeadline> {
        return data
    }

    inner class SourceHeadlineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val sourceHeadlineDescription: TextView =
            itemView.findViewById(R.id.source_headline_description)
        private val sourceHeadlineName: TextView = itemView.findViewById(R.id.source_headline_text)

        fun bind(item: SourceHeadline) {
            sourceHeadlineName.text = item.title
            sourceHeadlineDescription.text = item.description
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val sourceHeadline: SourceHeadline = data[position]
                    Toast.makeText(itemView.context, sourceHeadline.url, Toast.LENGTH_SHORT).show()

                    val arguments = Bundle().apply {
                        putString(EXTRA_URL, sourceHeadline.url)
                    }
                    NavHostFragment.findNavController(sourceHeadlinesFragment)
                        .navigate(R.id.news_detail_fragment, arguments)
                }
            }
        }
    }
}


