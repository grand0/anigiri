package tech.bnuuy.anigiri.feature.search.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Deselect
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.FilterListOff
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material3.Badge
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import tech.bnuuy.anigiri.core.designsystem.theme.Typography
import tech.bnuuy.anigiri.feature.search.R
import tech.bnuuy.anigiri.feature.search.api.data.model.Genre
import tech.bnuuy.anigiri.feature.search.data.enumeration.AgeRating
import tech.bnuuy.anigiri.feature.search.data.enumeration.ProductionStatus
import tech.bnuuy.anigiri.feature.search.data.enumeration.PublishStatus
import tech.bnuuy.anigiri.feature.search.data.enumeration.ReleaseType
import tech.bnuuy.anigiri.feature.search.data.enumeration.Season
import tech.bnuuy.anigiri.feature.search.data.enumeration.Sorting
import tech.bnuuy.anigiri.feature.search.data.model.CatalogSearchUiFilter
import kotlin.math.roundToInt

@Suppress("LongMethod")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersBottomSheet(
    initialFilter: CatalogSearchUiFilter,
    onDismiss: (CatalogSearchUiFilter) -> Unit,
    allGenres: List<Genre>? = null,
    minYear: Int? = null,
    maxYear: Int? = null,
    isLoading: Boolean = false,
) {
    val ctx = LocalContext.current
    
    val genres = remember { initialFilter.genres.toMutableStateList() }
    val types = remember { initialFilter.types.toMutableStateList() }
    val seasons = remember { initialFilter.seasons.toMutableStateList() }
    var fromYear by remember { mutableStateOf(initialFilter.fromYear) }
    var toYear by remember { mutableStateOf(initialFilter.toYear) }
    var sorting by remember { mutableStateOf(initialFilter.sorting) }
    val ageRatings = remember { initialFilter.ageRatings.toMutableStateList() }
    var publishStatus by remember { mutableStateOf(initialFilter.publishStatus) }
    var productionStatus by remember { mutableStateOf(initialFilter.productionStatus) }
    
    fun makeFilter() = initialFilter.copy(
        genres = genres.toList(),
        types = types.toList(),
        seasons = seasons.toList(),
        fromYear = fromYear,
        toYear = toYear,
        sorting = sorting,
        ageRatings = ageRatings.toList(),
        publishStatus = publishStatus,
        productionStatus = productionStatus,
    )
    
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()

    val surfaceColor = MaterialTheme.colorScheme.surfaceColorAtElevation(BottomSheetDefaults.Elevation)
    val gradient = Brush.verticalGradient(
        colors = listOf(
            surfaceColor.copy(alpha = 0.0f),
            surfaceColor.copy(alpha = 0.75f),
            surfaceColor.copy(alpha = 1.0f),
        )
    )

    ModalBottomSheet(
        onDismissRequest = {
            onDismiss(makeFilter())
        },
        sheetState = sheetState,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.BottomStart,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
            ) {
                if (isLoading) {
                    CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
                } else {
                    SingleChoiceFilterSection(
                        title = stringResource(R.string.sorting),
                        choices = Sorting.entries,
                        selected = sorting,
                        choiceLabel = { ctx.getString(it.labelResId) },
                        onSelected = { sorting = it ?: CatalogSearchUiFilter.DEFAULT_SORTING },
                        showBadge = false,
                    )
                    if (!allGenres.isNullOrEmpty()) {
                        MultipleChoiceFilterSection(
                            title = stringResource(R.string.genre),
                            choices = allGenres,
                            selected = genres,
                            choiceLabel = { it.name },
                        )
                    }
                    MultipleChoiceFilterSection(
                        title = stringResource(R.string.release_type),
                        choices = ReleaseType.entries,
                        selected = types,
                        choiceLabel = { ctx.getString(it.labelResId) },
                    )
                    MultipleChoiceFilterSection(
                        title = stringResource(R.string.season),
                        choices = Season.entries,
                        selected = seasons,
                        choiceLabel = { ctx.getString(it.labelResId) },
                    )
                    if (minYear != null && maxYear != null) {
                        RangeFilterSection(
                            title = stringResource(R.string.year),
                            baseFrom = minYear,
                            baseTo = maxYear,
                            from = fromYear ?: minYear,
                            to = toYear ?: maxYear,
                            onFromChange = { fromYear = it },
                            onToChange = { toYear = it },
                        )
                    }
                    MultipleChoiceFilterSection(
                        title = stringResource(R.string.age_rating),
                        choices = AgeRating.entries,
                        selected = ageRatings,
                        choiceLabel = { ctx.getString(it.labelResId) },
                    )
                    SingleChoiceFilterSection(
                        title = stringResource(R.string.publish_status),
                        choices = PublishStatus.entries,
                        selected = publishStatus,
                        choiceLabel = { ctx.getString(it.labelResId) },
                        onSelected = { publishStatus = it },
                    )
                    SingleChoiceFilterSection(
                        title = stringResource(R.string.production_status),
                        choices = ProductionStatus.entries,
                        selected = productionStatus,
                        choiceLabel = { ctx.getString(it.labelResId) },
                        onSelected = { productionStatus = it },
                    )
                    Spacer(Modifier.height(ButtonDefaults.MinHeight + 24.dp))
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .offset {
                        IntOffset(
                            x = 0,
                            y = -sheetState.requireOffset().toInt(),
                        )
                    }
                    .background(gradient)
                    .fillMaxWidth()
                    .padding(bottom = 12.dp, top = 24.dp),
            ) {
                OutlinedButton(
                    onClick = {
                        genres.clear()
                        types.clear()
                        seasons.clear()
                        fromYear = null
                        toYear = null
                        sorting = CatalogSearchUiFilter.DEFAULT_SORTING
                        ageRatings.clear()
                        publishStatus = null
                        productionStatus = null
                    },
                ) {
                    Icon(Icons.Default.FilterListOff, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.clear_filters))
                }
                Button(
                    onClick = {
                        scope.launch {
                            sheetState.hide()
                        }.invokeOnCompletion {
                            onDismiss(makeFilter())
                        }
                    },
                ) {
                    Icon(Icons.Default.FilterList, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.apply_filters))
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun <T> MultipleChoiceFilterSection(
    title: String,
    choices: List<T>,
    selected: MutableList<T>,
    choiceLabel: (T) -> String,
) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(title, style = Typography.titleLarge)
                if (selected.isNotEmpty()){
                    Spacer(Modifier.width(8.dp))
                    Badge {
                        Text(selected.size.toString())
                    }
                }
            }
            Row {
                IconButton(
                    onClick = {
                        selected.clear()
                        selected.addAll(choices)
                    }
                ) {
                    Icon(Icons.Default.SelectAll, contentDescription = null)
                }
                IconButton(
                    onClick = {
                        selected.clear()
                    }
                ) {
                    Icon(Icons.Default.Deselect, contentDescription = null)
                }
            }
        }
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) { 
            choices.forEach { choice ->
                FilterChip(
                    selected = selected.contains(choice),
                    onClick = {
                        if (selected.contains(choice)) {
                            selected.remove(choice)
                        } else {
                            selected.add(choice)
                        }
                    },
                    label = { Text(choiceLabel(choice)) },
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun <T> SingleChoiceFilterSection(
    title: String,
    choices: List<T>,
    selected: T?,
    choiceLabel: (T) -> String,
    onSelected: (T?) -> Unit,
    showBadge: Boolean = true,
) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(title, style = Typography.titleLarge)
                if (showBadge && selected != null) {
                    Spacer(Modifier.width(8.dp))
                    Badge()
                }
            }
            IconButton(
                onClick = { onSelected(null) },
            ) {
                Icon(Icons.Default.Deselect, contentDescription = null)
            }
        }
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            choices.forEach { choice ->
                FilterChip(
                    selected = selected == choice,
                    onClick = {
                        onSelected(if (selected == choice) null else choice)
                    },
                    label = { Text(choiceLabel(choice)) },
                )
            }
        }
    }
}

@Composable
private fun RangeFilterSection(
    title: String,
    baseFrom: Int,
    baseTo: Int,
    from: Int,
    to: Int,
    onFromChange: (Int) -> Unit,
    onToChange: (Int) -> Unit,
) {
    val basePosition = baseFrom.toFloat()..baseTo.toFloat()
    var sliderPosition by remember { mutableStateOf(from.toFloat()..to.toFloat()) }
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(title, style = Typography.titleLarge)
                if (sliderPosition != basePosition) {
                    Spacer(Modifier.width(8.dp))
                    Badge()
                }
            }
            IconButton(
                onClick = {
                    sliderPosition = basePosition
                    onFromChange(sliderPosition.start.roundToInt())
                    onToChange(sliderPosition.endInclusive.roundToInt())
                },
            ) {
                Icon(Icons.Default.Deselect, contentDescription = null)
            }
        }
        RangeSlider(
            value = sliderPosition,
            valueRange = basePosition,
            steps = baseTo - baseFrom - 1,
            onValueChange = {
                sliderPosition = it
            },
            onValueChangeFinished = {
                onFromChange(sliderPosition.start.roundToInt())
                onToChange(sliderPosition.endInclusive.roundToInt())
            },
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(sliderPosition.start.roundToInt().toString(), style = Typography.labelLarge)
            Text(sliderPosition.endInclusive.roundToInt().toString(), style = Typography.labelLarge)
        }
    }
}
