package com.givol.fragments.main

import androidx.recyclerview.widget.DiffUtil
import com.givol.model.FBContest
import timber.log.Timber.i

class ContestDiffCallback(private var oldList : List<FBContest>, private var newList : List<FBContest>)
    : DiffUtil.Callback() {

    init {
        i("ContestDiffCallback: created...")
    }
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].contestID == newList[newItemPosition].contestID
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return oldItem.businessID == newItem.businessID
                && newItem.participantsIdList.size == oldItem.participantsIdList.size
                && newItem.prizes == oldItem.prizes
    }
}