package com.givol.adapters

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.givol.R
import com.givol.fragments.main.ContestDiffCallback
import com.givol.model.FBContest
import com.givol.utils.GlideApp
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.contest_item.view.*
import kotlinx.android.synthetic.main.textual_data_layout.view.*


class ContestsAdapter(listener: AdapterListener<FBContest>) : BaseAdapter<FBContest>(listener) {

    init {
        setAnimationType(0)
    }

    override fun getLayoutId(position: Int, obj: FBContest): Int {
        return R.layout.contest_item
    }

    override fun getViewHolder(view: View, viewType: Int): RecyclerView.ViewHolder {
        return PollSectionItemViewHolder(view)
    }

    override fun submitList(listItems: List<FBContest>) {
        val diffResult =
            DiffUtil.calculateDiff(ContestDiffCallback(this.items, listItems))
        this.items.clear()
        this.items.addAll(listItems)
        diffResult.dispatchUpdatesTo(this)
    }
}

class PollSectionItemViewHolder constructor(override val containerView: View) :
    RecyclerView.ViewHolder(containerView), LayoutContainer, BaseAdapter.Binder<FBContest> {

    override fun bind(data: FBContest) {
        with(containerView) {
            title.text = data.businessName
            participantsTextualView.dataTv.text =
                resources.getString(R.string.participants_data,
                    data.participantsIdList.size, data.minParticipants)
            dueDateTextualView.dataTv.text = "17/04/2020 21:00"//data.times.dateEndStr
            amountTextualView.dataTv.text =
                resources.getString(R.string.amount_data, data.prizes.primary.value)
        }



        GlideApp.with(containerView).load(data.logo)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .placeholder(R.drawable.background_gray)
            .into(containerView.logoIV)
    }
}