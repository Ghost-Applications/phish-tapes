package never.ending.splendor.app.ui

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import never.ending.splendor.R
import java.lang.ref.WeakReference

class ShowPagerAdapter(
    rootView: View
) : PagerAdapter() {

    private val _rootView = WeakReference(rootView)

    override fun instantiateItem(collection: ViewGroup, position: Int): View {
        val rootView = requireNotNull(_rootView.get())


        val view: View = when (position) {
            0 -> rootView.findViewById(R.id.tracks)
            1 -> rootView.findViewById(R.id.setlist)
            2 -> rootView.findViewById(R.id.reviews)
            3 -> rootView.findViewById(R.id.tapernotes)
            else -> error("Unknown position")
        }

        collection.addView(view)
        return view
    }

    override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
        collection.removeView(view as View)
    }

    override fun getCount(): Int {
        return 4
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun getPageTitle(position: Int): CharSequence = when (position) {
        0 -> "Tracks"
        1 -> "Setlist"
        2 -> "Reviews"
        3 -> "Taper Notes"
        else -> error("Unknown position")
    }
}
