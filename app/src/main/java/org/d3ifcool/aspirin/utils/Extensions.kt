package org.d3ifcool.aspirin.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T> LiveData<T>.observeOnce(owner: LifecycleOwner, observer: (T) -> Unit) {
    var firstObservation = true

    observe(owner, object : Observer<T> {
        override fun onChanged(value: T) {
            if (firstObservation) {
                firstObservation = false
            } else {
                removeObserver(this)
                observer(value)
            }
        }
    })
}

