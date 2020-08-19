package com.givol.widgets

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import com.givol.R
import com.givol.core.AbstractAction
import kotlinx.android.synthetic.main.givol_toolbar.view.*
import java.lang.ref.WeakReference
import java.util.*

class GivolToolbar : Toolbar {

    private var weakListener: WeakReference<ActionListener>? = null
    private var menuDrawables: MutableList<Drawable>? = null

    var menuItemsAlpha = 1f
        set(alpha) {
            field = alpha
            for (menuDrawable in menuDrawables!!) {
                menuDrawable.alpha = (alpha * 255).toInt()
            }
        }

    interface ActionListener {
        fun onActionSelected(action: AbstractAction): Boolean
    }

    constructor(context: Context) : super(context) {
        initialize(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initialize(context)
        initAttrs(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initialize(context)
        initAttrs(attrs)
    }

    private fun initialize(context: Context) {
        menuDrawables = ArrayList()
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.givol_toolbar, this, true)
    }

    private fun initAttrs(attrs: AttributeSet) {
        val toolbarTypedArray =
            context.obtainStyledAttributes(attrs, R.styleable.GivolToolbar)

        val titleContentDescription =
            toolbarTypedArray.getString(R.styleable.GivolToolbar_titleContentDescription)
        titleTextView.contentDescription = titleContentDescription
    }

    fun setTitle(title: String) {
        titleTextView.text = title
    }

    fun setTitleContentDescription(contentDescription: String) {
        titleTextView.contentDescription = contentDescription
    }

    override fun setTitle(@StringRes resId: Int) {
        titleTextView.setText(resId)
    }

    fun setTextSize(size: Float) {
        titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
    }

    fun setTitleLeftDrawable(drawableResource: Int, padding: Int) {
        setTitleDrawable(drawableResource, LEFT_DRAWABLE, padding)
    }

    fun setTitleRightDrawable(drawableResource: Int, padding: Int) {
        setTitleDrawable(drawableResource, RIGHT_DRAWABLE, padding)
    }

    private fun setTitleDrawable(drawableResource: Int, direction: Int, padding: Int) {
        when (direction) {
            RIGHT_DRAWABLE -> titleTextView!!.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                drawableResource,
                0
            )
            LEFT_DRAWABLE -> titleTextView!!.setCompoundDrawablesWithIntrinsicBounds(
                drawableResource,
                0,
                0,
                0
            )
            else -> titleTextView!!.setCompoundDrawablesWithIntrinsicBounds(
                drawableResource,
                0,
                0,
                0
            )
        }
        titleTextView!!.compoundDrawablePadding = padding
    }

    override fun setTitleTextColor(@ColorInt color: Int) {
        titleTextView!!.setTextColor(color)
    }

    fun addActions(actions: Array<AbstractAction>, listener: ActionListener) {
        weakListener = WeakReference(listener)
        for (action in actions) {
            if (action.isNavigation) {
                addNavigationAction(action)
            } else {
                addAction(action)
            }
        }
    }

    private fun addNavigationAction(action: AbstractAction) {
        setNavigationIcon(action.iconResId)
        setNavigationContentDescription(action.titleResId)
        setNavigationOnClickListener { callListener(action) }
        menuDrawables?.add(navigationIcon!!)
    }

    private fun addAction(action: AbstractAction) {
        val menuItem = menu.add(action.titleResId)
            .setIcon(action.iconResId)
            .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            .setOnMenuItemClickListener { _ -> callListener(action) }
        menuDrawables!!.add(menuItem.icon)
    }

    fun callListener(action: AbstractAction): Boolean {
        val listener = weakListener!!.get()
        return listener != null && listener.onActionSelected(action)

    }

    fun findActionPosition(action: AbstractAction): Int? {
        val size = getMenu().size()
        for (i in 0 until size) {
            val item = menu.getItem(i)
            if (context.getString(action.titleResId) == item.title) {
                return i
            }
        }

        return null
    }

    fun replaceAction(pos: Int, action: AbstractAction) {
        val menuItem = menu.getItem(pos)
        menuDrawables!!.remove(menuItem.icon)
        menuItem.setTitle(action.titleResId)
        menuItem.setIcon(action.iconResId)
        menuDrawables!!.add(menuItem.icon)
        menuItem.setOnMenuItemClickListener { callListener(action) }
    }

    fun removeNavigationAction() {
        navigationIcon = null
        navigationContentDescription = null
        setNavigationOnClickListener(null)
        menuDrawables!!.remove(navigationIcon)
    }

    fun removeAllActions() {
        menu.clear()
        removeNavigationAction()
    }

    fun removeActions(id: Int) {
        menu.removeItem(id)
    }

    fun resetMenuItemsAlpha() {
        ObjectAnimator.ofFloat(this, "menuItemsAlpha", 1f).setDuration(500).start()
    }

    fun getActionItemId(action: AbstractAction): Int? {
        val position = findActionPosition(action)
        return if (position != null) {
            menu.getItem(position).itemId
        } else null
    }

    companion object {
        private const val LEFT_DRAWABLE = 1
        private const val RIGHT_DRAWABLE = 2
    }
}

