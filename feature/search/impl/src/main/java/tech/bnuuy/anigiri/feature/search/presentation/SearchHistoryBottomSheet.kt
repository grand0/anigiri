package tech.bnuuy.anigiri.feature.search.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import tech.bnuuy.anigiri.core.designsystem.theme.Typography
import tech.bnuuy.anigiri.feature.search.R
import tech.bnuuy.anigiri.feature.search.api.data.model.SearchQuery

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchHistoryBottomSheet(
    queries: List<SearchQuery>,
    onDismiss: () -> Unit,
    onQueryClick: (SearchQuery) -> Unit,
    onClearClick: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
    ) {
        Box(
            Modifier.padding(bottom = 36.dp) 
        ) {
            LazyColumn {
                item {
                    SearchHistoryHeader(
                        onClearClick = onClearClick,
                    )
                }

                if (queries.isEmpty()) { 
                    item {
                        Box(
                            Modifier
                                .height(56.dp)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                stringResource(R.string.history_is_empty),
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                } else {
                    items(queries) { query ->
                        SearchHistoryItem(
                            query = query,
                            onClick = { onQueryClick(query) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SearchHistoryHeader(
    onClearClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(stringResource(R.string.history), style = Typography.titleLarge)
        IconButton(
            onClick = onClearClick,
        ) {
            Icon(Icons.Default.Delete, contentDescription = null)
        }
    }
}

@Composable
fun SearchHistoryItem(
    query: SearchQuery,
    onClick: () -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(Icons.Default.Search, contentDescription = null)
        Spacer(Modifier.width(12.dp))
        Text(query.query)
    }
}
