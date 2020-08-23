package com.givol.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.givol.R
import com.givol.utils.GlideApp
import kotlinx.android.synthetic.main.picture_item.view.*
import timber.log.Timber

class PicturesAdapter : PagerAdapter() {

    private var items: List<String> = ArrayList()

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun getCount(): Int {
        return items.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        Timber.i("instantiateItem at position $position")
        val v =
            LayoutInflater.from(container.context).inflate(R.layout.picture_item, container, false)

        val pictureUrl = items[position]
        GlideApp.with(container).load(pictureUrl)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .placeholder(R.drawable.background_gray)
            .into(v.imageView)
        (container as ViewPager).addView(v)
        return v
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        (container as ViewPager).removeView(obj as View)
    }

    fun setItems(pictureUrls: List<String>) {
        this.items = pictureUrls
        notifyDataSetChanged()
    }
}