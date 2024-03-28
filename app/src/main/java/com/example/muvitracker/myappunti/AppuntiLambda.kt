package com.example.muvitracker.mainactivity.kotlin.boxo

/**
 * classe anonime,
 * lambda,
 * clallbacks

=> tutte hanno 3 fasi:
 * - dichiarazione
 * - implementazione
 * - invocazione (o chiamata)

 * nella callback e come nella classe anonima, ma cambia l'ordine
 *

 * esempio struttura:

var lambda0: () -> Unit = { println("ciao" ) }
(1. dichiarazione)    | (2. implementazione)

lambda0.invoke()
(3. invocazione)

 */


class Prova {

    //  ### CLASSE ANONIMA ==> LAMBDA

    // anonima
    interface Interf {
        fun go()
    }

    var interf: Interf = object : Interf {
        override fun go() {
            println("questo e una classe anonima ")
        }
    }

    // lambda
    var interfLambda: () -> Unit = {
        println("ciao")
    }


    fun Runner() {

        interf.go()

        interfLambda()
        interfLambda.invoke()
    }


    // ### ESEMPI LAMBDA

    // void
    var lambda0: () -> Unit = {
        println("ciao")
    }


    // un par (dichiarato)
    var lambda1: (Int) -> Unit = { a ->
        println(a)
    }

    // un par (senza dichiarazione)
    var lambda2: (Int) -> Unit = {
        println(it)
    }

    // un par (senza dichiarazione, non lo utilizzo)
    // -> caso click su view,
    // dentro il button.setListener{ lambda di questo tipo }
    var lambda3: (Int) -> Unit = {
        println("ciao")
    }


    // due par
    var lambda4: (Int, Int) -> Unit = { a, b ->
        val c = a + b
        println(c)
    }

    //
    private var lambda5: ((Int) -> Unit)? = {
        null
    }


    fun Runnerr() {

        lambda0.invoke()

        lambda1.invoke(5)
        lambda2.invoke(5)
        lambda3.invoke(5)

        lambda4.invoke(2, 2)

        lambda5?.invoke(3) // se lambda ha ?, anche poi la chiamata la deve usare


    }


}

class Esempi {

    // costruisco una roba simile alla view

    var view: () -> Unit = {
        println("ciao")
    }


    // ########  CALLBACKS INTERFACCIA ==> CALBACK LAMBDA
    var callbackVH: ((Int) -> Unit)? = null

    fun setCallbackVHK(call: (Int) -> Unit) {
        this.callbackVH = call
    }


}



