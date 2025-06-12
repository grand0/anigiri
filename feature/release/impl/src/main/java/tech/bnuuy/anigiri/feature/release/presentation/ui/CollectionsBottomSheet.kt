package tech.bnuuy.anigiri.feature.release.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import tech.bnuuy.anigiri.core.designsystem.theme.Typography
import tech.bnuuy.anigiri.core.network.datasource.enumeration.CollectionType
import tech.bnuuy.anigiri.feature.release.R
import tech.bnuuy.anigiri.feature.release.presentation.mapper.icon
import tech.bnuuy.anigiri.feature.release.presentation.mapper.presentationName

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionsBottomSheet(
    selectedType: CollectionType? = null,
    onSelect: (CollectionType?) -> Unit,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        LazyColumn(
            contentPadding = PaddingValues(bottom = 36.dp)
        ) {
            item {
                Text(
                    stringResource(R.string.add_to_collection),
                    style = Typography.titleLarge,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            items(CollectionType.entries, key = { it.value }) { type ->
                CollectionTypeItem(type, selectedType == type, onSelect = { onSelect(type) })
            }

            if (selectedType != null) {
                item {
                    Item(
                        icon = {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error
                            )
                        },
                        label = {
                            Text(
                                stringResource(R.string.remove_from_collection),
                                color = MaterialTheme.colorScheme.error,
                            )
                        },
                        onSelect = { onSelect(null) },
                    )
                }
            }
        }
    }
}

@Composable
private inline fun CollectionTypeItem(
    type: CollectionType,
    selected: Boolean = false,
    crossinline onSelect: () -> Unit,
) {
    Item(
        icon = {
            Icon(type.icon(), contentDescription = null)
        },
        label = {
            Text(stringResource(type.presentationName()))
        },
        selected = selected,
        onSelect = onSelect,
    )
}

@Composable
private inline fun Item(
    crossinline icon: @Composable () -> Unit,
    crossinline label: @Composable () -> Unit,
    selected: Boolean = false,
    crossinline onSelect: () -> Unit = {},
) {
    val bg = if (selected) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        Color.Transparent
    }

    Surface(
        color = bg,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable(enabled = !selected) { onSelect() }
    ) {
        Row(
            Modifier
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start),
        ) {
            icon()
            label()
        }
    }
}
