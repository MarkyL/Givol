package com.givol.utils

import bolts.Task
import bolts.TaskCompletionSource

class ObjectDelegate<T> {
    private var o: T? = null

    private var tcs: TaskCompletionSource<T>? = null

    fun takeObject(o: T) {
        this.o = o
        tcs?.let {
            it.setResult(o)
            tcs = null
        }
    }

    fun dropObject() {
        this.o = null
    }

    fun waitForObject(): Task<T> {
        if (o != null) {
            return Task.forResult(o)
        } else if (tcs == null) {
            tcs = TaskCompletionSource()
        }

        return tcs!!.task
    }
}
