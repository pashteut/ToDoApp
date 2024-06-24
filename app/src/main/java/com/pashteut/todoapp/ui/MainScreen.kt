package com.pashteut.todoapp.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.largeTopAppBarColors
import androidx.compose.material3.TopAppBarState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.pashteut.todoapp.R
import com.pashteut.todoapp.model.Priority
import com.pashteut.todoapp.model.ToDoItem
import com.pashteut.todoapp.presentator.MainScreenViewModel
import com.pashteut.todoapp.ui.theme.additionalColors
import kotlinx.coroutines.delay
import kotlin.math.pow
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: MainScreenViewModel = hiltViewModel(),
) {
    val scrollState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(scrollState)
    val visibility by viewModel.doneItemsVisibility.collectAsStateWithLifecycle()
    val list by viewModel.toDoItems.collectAsStateWithLifecycle(emptyList())
    val doneCount by viewModel.doneCount.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    AppBar(
                        visibility = visibility,
                        changeVisibility = {
                            viewModel.doneItemsVisibility.value =
                                !viewModel.doneItemsVisibility.value
                        },
                        doneCount = doneCount,
                        scrollState = scrollState,
                    )
                },
                scrollBehavior = scrollBehavior,
                colors = largeTopAppBarColors(scrolledContainerColor = MaterialTheme.colorScheme.surface),
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(ScreenDetail()) },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .padding(
                        end = WindowInsets.safeDrawing.asPaddingValues()
                            .calculateEndPadding(LocalLayoutDirection.current)
                    )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.plus_icon),
                    contentDescription = "plus",
                    tint = Color.White
                )
            }

        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(innerPadding)
                .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(MaterialTheme.colorScheme.additionalColors.surfaceVariant),
        ) {
            items(list, key = { it.hashCode() }) { item ->
                ToDoItemElement(
                    item = item,
                    onDelete = viewModel::deleteItem,
                    changeIsDone = viewModel::changeIsDone,
                    onInfoIconClick = { navController.navigate(ScreenDetail(item.id)) },
                )
            }
            item {
                ListItem(
                    headlineContent = {
                        Text(
                            text = stringResource(id = R.string.newDeal),
                            fontSize = 15.sp,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(start = 20.dp)
                                .alpha(.7f),
                        )
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { navController.navigate(ScreenDetail()) }
                        .clip(RoundedCornerShape(15.dp)),
                    colors = ListItemDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.additionalColors.surfaceVariant,
                    ),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppBar(
    visibility: Boolean,
    changeVisibility: () -> Unit,
    doneCount: Int,
    modifier: Modifier = Modifier,
    scrollState: TopAppBarState,
) {
    val collapseProgress = scrollState.heightOffset / scrollState.heightOffsetLimit
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 40.dp, top = 50.dp, end = 15.dp, bottom = 10.dp),
    ) {
        Text(
            text = stringResource(id = R.string.myDeals),
            modifier = Modifier
                .offset {
                    IntOffset(
                        (collapseProgress * -20f * density).roundToInt(),
                        (collapseProgress * 8 * density).roundToInt()
                    )
                },
            style = MaterialTheme.typography.titleLarge,
        )

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(id = R.string.doneCount, doneCount),
                modifier = Modifier
                    .alpha(((1f - collapseProgress) * 0.7f).pow(2)),
                style = MaterialTheme.typography.bodyMedium,
            )
            IconButton(
                onClick = changeVisibility,
                modifier = Modifier
                    .offset {
                        IntOffset(0, (collapseProgress * -30 * density).roundToInt())
                    }
            ) {
                Icon(
                    painter =
                    if (visibility) painterResource(id = R.drawable.visibility_off_icon)
                    else painterResource(id = R.drawable.visibility_icon),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    contentDescription = "Visibility off icon",
                )
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun ToDoItemElement(
    item: ToDoItem,
    modifier: Modifier = Modifier,
    onDelete: (Long) -> Unit,
    changeIsDone: (Long) -> Unit,
    onInfoIconClick: (Long) -> Unit = {},
) {
    var isRemoved by remember { mutableStateOf(false) }
    var isDoneNeedToChange by remember { mutableStateOf(false) }
    var swipeEnabled by rememberSaveable { mutableStateOf(true) }
    val swipeState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            when (it) {
                SwipeToDismissBoxValue.EndToStart -> isRemoved = true
                SwipeToDismissBoxValue.StartToEnd -> {
                    isDoneNeedToChange = true
                }

                SwipeToDismissBoxValue.Settled -> return@rememberSwipeToDismissBoxState false
            }
            return@rememberSwipeToDismissBoxState true
        },
        positionalThreshold = { it * .3f }
    )
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = item.isDone) {
        swipeEnabled = false
        swipeState.snapTo(SwipeToDismissBoxValue.Settled)
        delay(100)
        swipeEnabled = true
    }

    LaunchedEffect(key1 = isRemoved, key2 = isDoneNeedToChange) {
        if (isRemoved) {
            delay(300)
            onDelete(item.id)
            isRemoved = false
        }
        if (isDoneNeedToChange) {
            delay(300)
            changeIsDone(item.id)
            isDoneNeedToChange = false
        }
    }

    Box {
        SwipeToDismissBox(
            state = swipeState,
            modifier = modifier
                .combinedClickable(
                    onClick = {},
                    onLongClick = {expanded = true }
                ),
            backgroundContent = {
                SwipeBackground(
                    swipeState = swipeState,
                    itemIsDone = item.isDone
                )
            },
            content = { ToDoItemCard(item = item, onInfoIconClick = { onInfoIconClick(item.id) }) },
            enableDismissFromStartToEnd = swipeEnabled,
            enableDismissFromEndToStart = swipeEnabled,
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            offset = DpOffset(x = 60.dp, y = (-30).dp),
            modifier = modifier
                .background(MaterialTheme.colorScheme.background)
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = if (item.isDone) stringResource(id = R.string.markIsNotDone)
                        else stringResource(id = R.string.markIsDone),
                        modifier = Modifier
                    )
                },
                onClick = { changeIsDone(item.id) },
                modifier = Modifier.clip(RoundedCornerShape(12.dp))
            )
            DropdownMenuItem(
                text = {
                    Text(
                        stringResource(id = R.string.delete),
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                    )
                },
                onClick = { onDelete(item.id) },
            )
        }
    }
}

@Composable
private fun ToDoItemCard(
    item: ToDoItem,
    modifier: Modifier = Modifier,
    onInfoIconClick: () -> Unit = {},
) {
    ListItem(
        headlineContent = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxSize()
                    .offset(x = (-12).dp),
            ) {
                when (item.priority) {
                    Priority.HIGH ->
                        Icon(
                            painter = painterResource(id = R.drawable.priority_high_icon),
                            contentDescription = "Priority high icon",
                            tint = Color.Red,
                            modifier = Modifier.size(20.dp),
                        )

                    Priority.DEFAULT -> {}
                    Priority.LOW ->
                        Icon(
                            painter = painterResource(id = R.drawable.priority_low_icon),
                            contentDescription = "Priority low icon",
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.additionalColors.gray,
                        )
                }

                if (item.priority != Priority.DEFAULT) Spacer(modifier = Modifier.width(7.dp))

                Column(
                    verticalArrangement = Arrangement.SpaceAround
                ) {
                    Text(
                        text = item.text,
                        fontSize = 18.sp,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .alpha(if (item.isDone) 0.5F else 1F),
                        textDecoration = if (item.isDone) TextDecoration.LineThrough else null,
                    )
                    if (item.deadline != null)
                        Text(
                            text = item.deadline.convertDateToString(),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.additionalColors.gray,
                        )
                }
            }
        },
        trailingContent = {
            IconButton(
                onClick = onInfoIconClick,
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = MaterialTheme.colorScheme.additionalColors.gray,
                    disabledContentColor = MaterialTheme.colorScheme.additionalColors.gray,
                ),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.info_icon),
                    contentDescription = "Delete icon",
                )
            }
        },
        leadingContent = {
            Checkbox(
                checked = item.isDone,
                onCheckedChange = {},
                enabled = false,
                colors = CheckboxDefaults.colors(
                    disabledCheckedColor = MaterialTheme.colorScheme.additionalColors.green,
                    disabledUncheckedColor =
                    if (item.priority == Priority.HIGH) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.additionalColors.gray,
                    checkmarkColor = MaterialTheme.colorScheme.additionalColors.surfaceVariant,
                ),
            )
        },
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(15.dp)),
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.additionalColors.surfaceVariant,
        ),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeBackground(
    swipeState: SwipeToDismissBoxState,
    itemIsDone: Boolean = false
) {
    val color = when (swipeState.dismissDirection) {
        SwipeToDismissBoxValue.StartToEnd -> if (itemIsDone) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.additionalColors.green
        SwipeToDismissBoxValue.EndToStart -> MaterialTheme.colorScheme.error
        SwipeToDismissBoxValue.Settled -> Color.Transparent
    }
    Row(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(15.dp))
            .background(color)
            .padding(20.dp, 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (swipeState.dismissDirection == SwipeToDismissBoxValue.StartToEnd)
            Icon(
                painter = painterResource(
                    id = if (itemIsDone) R.drawable.close_icon
                    else R.drawable.check_icon
                ),
                contentDescription = "Change isDone icon",
                tint = Color.White,
            )

        Spacer(modifier = Modifier)

        if (swipeState.dismissDirection == SwipeToDismissBoxValue.EndToStart)
            Icon(
                painter = painterResource(id = R.drawable.delete_icon),
                contentDescription = "Delete icon",
                tint = Color.White,
            )
    }
}