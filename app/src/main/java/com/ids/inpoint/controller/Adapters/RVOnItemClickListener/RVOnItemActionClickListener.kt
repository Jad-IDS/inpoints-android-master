package com.ids.inpoint.controller.Adapters.RVOnItemClickListener

import android.view.View

interface RVOnItemActionClickListener<Action> {
    fun onItemActionClicked(view: View, position: Int, action: Action)
}

enum class Action {
    A, B, C, D, E, F, G
}