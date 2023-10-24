@file:Suppress("detekt:all")
package org.mozilla.social.feature.discover

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import org.koin.androidx.compose.koinViewModel
import org.mozilla.social.common.Resource
import org.mozilla.social.core.designsystem.component.MoSoSurface
import org.mozilla.social.model.Recommendation

@Composable
internal fun DiscoverScreen(
    viewModel: DiscoverViewModel = koinViewModel()
) {
    DiscoverScreen(
        recommendations = viewModel.recommendations.collectAsState().value
    )
}

@Composable
private fun DiscoverScreen(
    recommendations: Resource<List<Recommendation>>
) {
    MoSoSurface(
        modifier = Modifier.fillMaxSize()
    ) {
        when (recommendations) {
            is Resource.Loaded -> {
                Column {
                    recommendations.data.forEach {
                        Text(text = it.title)
                    }
                }
            }
            is Resource.Loading -> {}
            is Resource.Error -> {}
        }

    }
}