package com.example.presentation

import android.content.Context


/**
 * Provides a global access point to the Application Context.
 *
 * ⚠️ This object intentionally holds a static reference to a Context.
 * To avoid memory leaks, ONLY the Application Context must be assigned
 * via [init]. Never pass an Activity, Fragment, or View context.
 *
 * This is a pragmatic solution used to simplify access to resources
 * (e.g. strings, dimensions) in places where passing a Context would
 * be verbose or impractical (e.g. UI defaults like MovieDefaults).
 *
 * Usage:
 * - Must be initialized once in Application.onCreate():
 *     AppContextProvider.init(this)
 *
 * - Safe usage:
 *     val title = AppContextProvider.context.getString(R.string.untitled)
 *
 * Limitations:
 * - Introduces a global state (Service Locator pattern)
 * - Not ideal for unit testing (harder to mock)
 * - Should NOT be used for business logic, data layer, or navigation
 *
 * Recommended usage scope:
 * - UI layer only (presentation)
 * - Access to Android resources
 *
 * Suppressed warning:
 * - "StaticFieldLeak" is suppressed because we explicitly store
 *   applicationContext, which has the same lifecycle as the app
 *   and does not cause memory leaks.
 */
@Suppress("StaticFieldLeak")
object AppContextProvider {
    lateinit var context: Context
        private set

    fun init(context: Context) {
        this.context = context.applicationContext
    }
}