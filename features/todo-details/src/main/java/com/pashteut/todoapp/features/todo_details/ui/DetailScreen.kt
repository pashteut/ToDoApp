package com.pashteut.todoapp.features.todo_details.ui

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pashteut.todoapp.data.model.Priority
import com.pashteut.todoapp.features.todo_details.R
import com.pashteut.todoapp.features.todo_details.ui_logic.DetailScreenViewModel
import com.pashteut.todoapp.ui_kit.ToDoAppTheme
import com.pashteut.todoapp.ui_kit.additionalColors

@Composable
fun DetailScreen(
    viewModel: DetailScreenViewModel,
    mainScreenNavigation: () -> Unit,
) {
    val priority by viewModel.priority.collectAsStateWithLifecycle()
    val text by viewModel.text.collectAsStateWithLifecycle()
    val textDeadline by viewModel.textDate.collectAsStateWithLifecycle()

    DetailScreenContent(
        mainScreenNavigation = mainScreenNavigation,
        saveItem = viewModel::saveToDoItem,
        text = text,
        onTextChanged = { changedText -> viewModel.setText(changedText) },
        priority = priority,
        setPriority = { changedPriority -> viewModel.setPriority(changedPriority) },
        textDeadline = textDeadline,
        setDeadline = { deadline -> viewModel.setDeadLineTime(deadline) },
        deleteItem = viewModel::deleteItem,
    )
}

@Composable
private fun DetailScreenContent(
    mainScreenNavigation: () -> Unit,
    saveItem: () -> Boolean,
    text: String,
    onTextChanged: (String) -> Unit,
    priority: Priority,
    setPriority: (Priority) -> Unit,
    textDeadline: String,
    setDeadline: (Long?) -> Unit,
    deleteItem: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            AppBar(mainScreenNavigation, saveItem = saveItem)
        }
    ) { innerPadding ->
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(10.dp)
                .verticalScroll(scrollState)
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = onTextChanged,
                textStyle = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 120.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .padding(5.dp),
            )
            PriorityBox(
                selectedPriority = priority,
                setPriority = setPriority
            )
            HorizontalDivider()
            DeadlineBox(
                deadline = textDeadline,
                setDeadline = setDeadline,
            )
            HorizontalDivider()
            Spacer(modifier = Modifier.height(5.dp))
            TextButton(
                onClick = {
                    deleteItem()
                    mainScreenNavigation()
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.delete_icon),
                        contentDescription = "delete",
                        modifier = Modifier.size(30.dp),
                        tint = MaterialTheme.colorScheme.error,
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        stringResource(id = R.string.delete),
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppBar(
    navigation: () -> Unit,
    saveItem: () -> Boolean
) {
    val context = LocalContext.current
    val text = stringResource(id = R.string.enterTheText)
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(onClick = navigation) {
                Icon(
                    painter = painterResource(id = R.drawable.close_icon),
                    contentDescription = "close"
                )
            }
        },
        actions = {
            TextButton(onClick = {
                if (saveItem())
                    navigation()
                else
                    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
            }) {
                Text(
                    stringResource(id = R.string.save),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PriorityBox(
    selectedPriority: Priority,
    setPriority: (Priority) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                expanded = true
            }
            .padding(10.dp, 7.dp)
    ) {
        Text(
            stringResource(id = R.string.priority),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 5.dp),
        )

        when (selectedPriority) {
            Priority.DEFAULT -> Text(
                stringResource(id = R.string.no),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.alpha(.5f),
            )

            Priority.LOW -> Text(
                stringResource(id = R.string.lowPriority),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.alpha(.75f),
            )

            Priority.HIGH -> Text(
                stringResource(id = R.string.highPriority),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.error,
            )
        }
        if (expanded) {
            ModalBottomSheet(
                onDismissRequest = { expanded = false },
                sheetState = sheetState,
                containerColor = MaterialTheme.colorScheme.additionalColors.surfaceVariant,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column {
                    Priority.entries.forEach { priority ->
                        ListItem(
                            headlineContent = {
                                Text(
                                    text = when (priority) {
                                        Priority.DEFAULT -> stringResource(id = R.string.no)
                                        Priority.LOW -> stringResource(id = R.string.lowPriority)
                                        Priority.HIGH -> stringResource(id = R.string.highPriority)
                                    },
                                    style = MaterialTheme.typography.headlineMedium,
                                )
                            },
                            leadingContent = {
                                if(selectedPriority == priority)
                                    Icon(
                                        imageVector = Icons.Filled.Check,
                                        contentDescription = null
                                    )
                            },
                            modifier = Modifier
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication =
                                    if (priority == Priority.HIGH)
                                        rememberRipple(color = Color.Red.copy(alpha = 0.4f))
                                    else
                                        rememberRipple()
                                ) { setPriority(priority) },
                            colors = ListItemDefaults.colors(
                                containerColor = MaterialTheme.colorScheme.additionalColors.surfaceVariant,
                            ),
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DeadlineBox(
    deadline: String = "",
    setDeadline: (Long?) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis >= System.currentTimeMillis()
            }
        }
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp, 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text(
                stringResource(id = R.string.toDoUntil),
                style = MaterialTheme.typography.titleMedium,
                fontSize = 20.sp,
            )
            AnimatedVisibility(visible = deadline != "") {
                Text(
                    deadline,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }

        Switch(
            checked = deadline != "",
            onCheckedChange = {
                showDatePicker = it
                if (!it)
                    setDeadline(null)
            },
            colors = SwitchDefaults.colors(
                uncheckedTrackColor = MaterialTheme.colorScheme.additionalColors.surfaceVariant,
                checkedThumbColor = Color.White,
            )
        )
    }
    if (showDatePicker) {
        DatePickerDialog(
            modifier = Modifier
                .fillMaxSize()
                .padding(25.dp),
            onDismissRequest = {
                showDatePicker = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        setDeadline(datePickerState.selectedDateMillis)
                        showDatePicker = false
                    }
                ) {
                    Text(
                        stringResource(id = R.string.ok),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDatePicker = false
                    }
                ) {
                    Text(
                        stringResource(id = R.string.cancel),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            },
            colors = DatePickerDefaults.colors(
                containerColor = MaterialTheme.colorScheme.additionalColors.surfaceVariant,
            ),
        )
        {
            DatePicker(
                state = datePickerState,
            )
        }
    }
}

@Preview(name = "Dark Mode", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(name = "Light Mode", showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
fun DetailScreenPreview1() {
    ToDoAppTheme {
        DetailScreenContent(
            mainScreenNavigation = {},
            saveItem = { true },
            text = "some text",
            onTextChanged = {},
            priority = Priority.LOW,
            setPriority = {},
            textDeadline = "",
            setDeadline = {},
            deleteItem = {},
        )
    }
}

@Preview(name = "Dark Mode", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(name = "Light Mode", showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
fun DetailScreenPreview2() {
    ToDoAppTheme {
        DetailScreenContent(
            mainScreenNavigation = {},
            saveItem = { true },
            text = "i need to eat",
            onTextChanged = {},
            priority = Priority.HIGH,
            setPriority = {},
            textDeadline = "07.08.2024",
            setDeadline = {},
            deleteItem = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AppBarAppBarPreview1() {
    ToDoAppTheme {
        AppBar(
            navigation = { },
            saveItem = { true },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PriorityBoxPreview1() {
    ToDoAppTheme {
        PriorityBox(
            selectedPriority = Priority.DEFAULT,
            setPriority = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PriorityBoxPreview2() {
    ToDoAppTheme {
        PriorityBox(
            selectedPriority = Priority.HIGH,
            setPriority = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DeadlineBoxPreview1() {
    ToDoAppTheme {
        DeadlineBox(
            deadline = "07.08.2024",
            setDeadline = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DeadlineBoxPreview2() {
    ToDoAppTheme {
        DeadlineBox(
            deadline = "",
            setDeadline = {},
        )
    }
}