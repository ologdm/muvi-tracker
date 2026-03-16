package com.example.muvitracker.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope

// fragment extended property for scope
val Fragment.fragmentViewLifecycleScope: LifecycleCoroutineScope
    get() = this.viewLifecycleOwner.lifecycleScope