package assignment.tokopedia.newsapp.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.fragment.app.Fragment
import assignment.tokopedia.newsapp.R

const val EXTRA_URL = "url"

class NewsDetailFragment : Fragment() {

    private var mUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mUrl = it.getString(EXTRA_URL)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_news_detail, container, false)
        mUrl?.let {
            val mWebView = view.findViewById<WebView>(R.id.webview)
            mWebView.loadUrl(it)
        }
        return view
    }
}