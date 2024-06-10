package com.example.muvitracker.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData



/* (combina livedata1 e livedata2)
// source1 = 1, source2 = "abc" --- 1, "abc"
// source1 = 2 --- 2, "abc
// source1 = 50 --- 50, "abc"
// source2 = "hello" --- 50, "hello"

// result - valore combinato livedata3
// cambiamento - se 1 o 2 cambia, aggiorna 3
 */
fun <T1 : Any, T2 : Any, R : Any?> combineLatest(  // operator
    source1: LiveData<T1>,
    source2: LiveData<T2>,
    combiner: Function2<T1, T2, R>
): LiveData<R> {
    val mediator = MediatorLiveData<R>()

    val combinerFunction = {
        val source1Value = source1.value
        val source2Value = source2.value
        if (source1Value != null && source2Value != null) {
            mediator.value = combiner(source1Value, source2Value)
        }
    }
    mediator.addSource(source1) { combinerFunction() }
    mediator.addSource(source2) { combinerFunction() }
    return mediator
}


// (concatena i 2 valori livedata) -> mostra tra i 2 il piu recente
// source1 |  10   20         50
// source2 |           30 40
// result  |  10   20  30 40  50
fun <T : Any> concat( // operator
    source1: LiveData<T>,
    source2: LiveData<T>,
): LiveData<T> {
    val mediator = MediatorLiveData<T>()

    mediator.addSource(source1) { mediator.value = it }
    mediator.addSource(source2) { mediator.value = it }
    return mediator
    // valore mediator e quello piu recente dei 2 livedata osservati
}