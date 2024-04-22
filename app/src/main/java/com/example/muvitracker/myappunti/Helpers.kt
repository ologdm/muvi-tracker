package com.example.muvitracker.myappunti

fun main() {
    // let
    // run
    // apply
    // also
    // with

    val myClass = MyClass().also {
        it.loadData()
    }

    val y = myClass.also {
        10
    }

    val x = myClass.let {
        20
    }


    myClass.run {

    }

    myClass.apply {

    }

    with(myClass) {
        toString()
    }

    val string: String? = "ciao"

    val size = if (string != null) {
        string.length
    } else {
        0
    }




}


class MyClass {

    fun loadData() {
        val a = (1 + 2)
        val b = if (true) 1 else 2
        val c = when (true) {
            true -> 1
            false-> 2
        }
        val d = try {
            10
        } catch (ex: Throwable) {
            2
        }
    }

}


class Presenter {

    fun loadData() {

    }

}

class Fragment {

    // also
    val presenterAlso = createPresenter()?.also {
        it.loadData()
        showLoading()
    }

    // let
    val presenterLet = createPresenter()?.let {
        it.loadData()
        showLoading()
    }

    // apply
    val presenterApply = createPresenter()?.apply {
        loadData()
        showLoading()
    }

    // apply
    val presenterRun = createPresenter()?.run {
        loadData()
        showLoading()
    }

    fun showLoading() {
        val string: String? = "ciao"
        val size = string?.let {
            it.length * 10
        }

    }


    fun createPresenter(): Presenter? {
        TODO()
    }

}