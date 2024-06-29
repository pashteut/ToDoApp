package com.pashteut.todoapp.ui

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
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
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pashteut.todoapp.R
import com.pashteut.todoapp.common.convertDateToString
import com.pashteut.todoapp.model.Priority
import com.pashteut.todoapp.model.ToDoItem
import com.pashteut.todoapp.presentator.MainScreenViewModel
import com.pashteut.todoapp.ui.theme.ToDoAppTheme
import com.pashteut.todoapp.ui.theme.additionalColors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.math.pow
import kotlin.math.roundToInt


@Composable
fun MainScreen(
    addItemNavigation: () -> Unit,
    editItemNavigation: (Long) -> Unit,
    viewModel: MainScreenViewModel,
    modifier: Modifier = Modifier,
) {
    val visibility by viewModel.doneItemsVisibility.collectAsStateWithLifecycle()
    val list by viewModel.toDoItems.collectAsStateWithLifecycle()
    val doneCount by viewModel.doneCount.collectAsStateWithLifecycle()

    MainScreenContent(
        addItemNavigation = addItemNavigation,
        editItemNavigation = editItemNavigation,
        doneItemsVisibility = visibility,
        changeVisibility = { viewModel.changeDoneItemsVisibility() },
        doneItemsCount = doneCount,
        items = list,
        deleteItem = viewModel::deleteItem,
        changeIsDone = viewModel::changeIsDone,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreenContent(
    addItemNavigation: () -> Unit,
    editItemNavigation: (Long) -> Unit,
    doneItemsVisibility: Boolean,
    changeVisibility: () -> Unit,
    doneItemsCount: Int,
    items: List<ToDoItem>,
    deleteItem: (Long) -> Unit,
    changeIsDone: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(scrollState)
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    AppBar(
                        visibility = doneItemsVisibility,
                        changeVisibility = changeVisibility,
                        doneCount = doneItemsCount,
                        scrollState = scrollState,
                    )
                },
                scrollBehavior = scrollBehavior,
                colors = largeTopAppBarColors(scrolledContainerColor = MaterialTheme.colorScheme.surface),
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = addItemNavigation,
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
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(innerPadding)
                .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(MaterialTheme.colorScheme.additionalColors.surfaceVariant),
        ) {
            items(items, key = { it.hashCode() }) { item ->
                ToDoItemElement(
                    item = item,
                    onDelete = deleteItem,
                    changeIsItemDone = changeIsDone,
                    onInfoIconClick = { editItemNavigation(item.id) },
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
                        .clickable { addItemNavigation() }
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
    changeIsItemDone: (Long) -> Unit,
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
            changeIsItemDone(item.id)
            isDoneNeedToChange = false
        }
    }

    Box {
        SwipeToDismissBox(
            state = swipeState,
            modifier = modifier
                .combinedClickable(
                    onClick = {},
                    onLongClick = { expanded = true }
                ),
            backgroundContent = {
                SwipeBackground(
                    swipeDirection = swipeState.dismissDirection,
                    itemIsDone = item.isDone
                )
            },
            content = {
                ToDoItemCard(
                    item = item,
                    onInfoIconClick = { onInfoIconClick(item.id) },
                    onCheckBoxClick = changeIsItemDone,
                )
            },
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
                onClick = { changeIsItemDone(item.id) },
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
    onInfoIconClick: () -> Unit,
    onCheckBoxClick: (Long) -> Unit,
) {

    var deadlineString by remember { mutableStateOf("") }

    LaunchedEffect(key1 = item.deadline) {
        withContext(Dispatchers.Default) {
            deadlineString = item.deadline?.convertDateToString() ?: ""
        }
    }

    ListItem(
        headlineContent = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
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
                            text = deadlineString,
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
                onCheckedChange = { onCheckBoxClick(item.id) },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.additionalColors.green,
                    uncheckedColor =
                    if (item.priority == Priority.HIGH) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.additionalColors.gray,
                    checkmarkColor = MaterialTheme.colorScheme.additionalColors.surfaceVariant,
                ),
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(15.dp)),
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.additionalColors.surfaceVariant,
        ),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeBackground(
    swipeDirection: SwipeToDismissBoxValue,
    modifier: Modifier = Modifier,
    itemIsDone: Boolean = false,
) {
    val color = when (swipeDirection) {
        SwipeToDismissBoxValue.StartToEnd -> if (itemIsDone) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.additionalColors.green
        SwipeToDismissBoxValue.EndToStart -> MaterialTheme.colorScheme.error
        SwipeToDismissBoxValue.Settled -> Color.Transparent
    }
    Row(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(15.dp))
            .background(color)
            .padding(20.dp, 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (swipeDirection == SwipeToDismissBoxValue.StartToEnd)
            Icon(
                painter = painterResource(
                    id = if (itemIsDone) R.drawable.close_icon
                    else R.drawable.check_icon
                ),
                contentDescription = "Change isDone icon",
                tint = Color.White,
            )

        Spacer(modifier = Modifier)

        if (swipeDirection == SwipeToDismissBoxValue.EndToStart)
            Icon(
                painter = painterResource(id = R.drawable.delete_icon),
                contentDescription = "Delete icon",
                tint = Color.White,
            )
    }
}

@Preview(name = "Dark Mode", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(name = "Light Mode", showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
fun MainScreenPreview() {
    ToDoAppTheme {
        MainScreenContent(
            addItemNavigation = {},
            editItemNavigation = {},
            doneItemsVisibility = false,
            changeVisibility = {},
            doneItemsCount = 0,
            items = listOf(
                ToDoItem(
                    id = 1,
                    text = "Test",
                    deadline = null,
                    priority = Priority.LOW,
                    isDone = false,
                    createdDate = 8274356,
                ),
                ToDoItem(
                    id = 1,
                    text = "Something",
                    deadline = 1797485723234,
                    priority = Priority.HIGH,
                    isDone = false,
                    createdDate = 8274356,
                ),
                ToDoItem(
                    id = 1,
                    text = "Something",
                    deadline = 1791485723234,
                    priority = Priority.DEFAULT,
                    isDone = true,
                    createdDate = 8274356,
                ),
                ToDoItem(
                    id = 1,
                    text = "Testasliufdhsadiuhfgisadguhaisdughsadiufghsdaiguhaildfughsdilfugh",
                    deadline = null,
                    priority = Priority.LOW,
                    isDone = false,
                    createdDate = 8274356,
                ),
            ),
            deleteItem = {},
            changeIsDone = {},
        )
    }
}

@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBarPreview() {
    ToDoAppTheme {
        AppBar(
            visibility = true,
            changeVisibility = {},
            doneCount = 5,
            scrollState = rememberTopAppBarState(),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ToDoItemElementPreview() {
    ToDoAppTheme {
        ToDoItemElement(
            item = ToDoItem(
                id = 1,
                text = "Test",
                deadline = null,
                priority = Priority.LOW,
                isDone = false,
                createdDate = 8274356,
            ),
            onDelete = {},
            changeIsItemDone = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ToDoItemCardPreview() {
    ToDoAppTheme {
        ToDoItemCard(
            item = ToDoItem(
                id = 1,
                text = "Test",
                deadline = 1791485723234,
                priority = Priority.DEFAULT,
                isDone = true,
                createdDate = 8274356,
            ),
            onInfoIconClick = {},
            onCheckBoxClick = {},
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun SwipeBackgroundPreview() {
    ToDoAppTheme {
        Column {
            SwipeBackground(
                swipeDirection = SwipeToDismissBoxValue.StartToEnd,
                itemIsDone = false,
                modifier = Modifier.height(70.dp),
            )
            Spacer(modifier = Modifier.height(15.dp))
            SwipeBackground(
                swipeDirection = SwipeToDismissBoxValue.StartToEnd,
                itemIsDone = true,
                modifier = Modifier.height(70.dp),
            )
            Spacer(modifier = Modifier.height(15.dp))
            SwipeBackground(
                swipeDirection = SwipeToDismissBoxValue.EndToStart,
                itemIsDone = false,
                modifier = Modifier.height(70.dp),
            )
        }
    }
}