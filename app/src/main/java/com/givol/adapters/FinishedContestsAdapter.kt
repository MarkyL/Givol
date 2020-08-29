package com.givol.adapters

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.givol.R
import com.givol.fragments.main.ContestDiffCallback
import com.givol.model.CONTEST_WIN_STATE
import com.givol.model.FBContest
import com.givol.model.User
import com.givol.utils.GlideApp
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.finished_contest_item.view.*
import kotlinx.android.synthetic.main.horizontal_textual_data_layout.view.*


class FinishedContestsAdapter() : BaseAdapter<FBContest>() {

    constructor(listener: AdapterListener<FBContest>) : this() {
        this.listener = listener
    }

    lateinit var userFinishedContests: HashMap<String, FBContest>

    var uid: String

    init {
        setAnimationType(0)
        uid = User.me()?.uid.toString()
    }

    override fun getLayoutId(position: Int, obj: FBContest): Int {
        return R.layout.finished_contest_item
    }

    override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
        return FinishedContestItemViewHolder(view, userFinishedContests)
    }

    override fun submitList(listItems: List<FBContest>) {
        val diffResult =
            DiffUtil.calculateDiff(ContestDiffCallback(this.items, listItems))
        this.items.clear()
        this.items.addAll(listItems)
        diffResult.dispatchUpdatesTo(this)
    }
}

class FinishedContestItemViewHolder(
    override val containerView: View,
    private val userFinishedContests: HashMap<String, FBContest>) :
    RecyclerView.ViewHolder(containerView), LayoutContainer, BaseAdapter.Binder<FBContest> {

    override fun bind(data: FBContest) {
        with(containerView) {
            title.text = data.businessName
            dueDateTextualView.dataTv.text = data.times.dateEndStr

            userFinishedContests[data.contestID]?.let {
                if (!it.used) {
                    actionBtn.visibility = View.VISIBLE
                    actionBtn.setOnClickListener { containerView.callOnClick() }
                }

                when (it.contestStateEnum) {
                    CONTEST_WIN_STATE.NONE -> TODO()
                    CONTEST_WIN_STATE.WIN -> {
                        setPrizeAmountText(true, data)
                        contestStateTextView.dataTv.text = if (it.used) "מימשתי" else "זכיתי"
                    }
                    CONTEST_WIN_STATE.COLDONSENSE -> {
                        setPrizeAmountText(false, data)
                        contestStateTextView.dataTv.text = if (it.used) "מימשתי" else "קיבלתי פרס"
                    }
                }
            }
        }

        GlideApp.with(containerView).load(data.logo)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .placeholder(R.drawable.background_gray)
            .into(containerView.logoIV)
    }

    private fun setPrizeAmountText(hasWon: Boolean, data: FBContest) {
        containerView.amountTextualView.dataTv.text =
            if (hasWon)
                containerView.resources.getString(R.string.amount_data, data.prizes.primary.value)
            else
                containerView.resources.getString(R.string.amount_data, data.prizes.secondary.value)
    }
}
