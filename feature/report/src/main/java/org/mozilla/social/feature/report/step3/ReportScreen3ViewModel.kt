@file:Suppress("detekt:all")
package org.mozilla.social.feature.report.step3

import androidx.lifecycle.ViewModel

class ReportScreen3ViewModel(
    private val doneClicked: () -> Unit,
    private val closeClicked: () -> Unit,
) : ViewModel(), ReportScreen3Interactions {

    override fun onCloseClicked() {
        closeClicked()
    }

    override fun onDoneClicked() {
        doneClicked()
    }
}