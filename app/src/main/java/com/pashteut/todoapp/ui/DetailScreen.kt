package com.pashteut.todoapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.pashteut.todoapp.R
import com.pashteut.todoapp.ScreenMain
import com.pashteut.todoapp.model.Priority
import com.pashteut.todoapp.presentator.DetailScreenViewModel
import com.pashteut.todoapp.ui.theme.additionalColors
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale


@Composable
fun DetailScreen(
    id: Long,
    navController: NavController
) {
    val viewModel = hiltViewModel<DetailScreenViewModel>()
    val priority by viewModel.priority.collectAsState()
    val text by viewModel.text.collectAsState()
    LaunchedEffect(key1 = id) {
        viewModel.setPickedItem(id)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppBar(navController) {
                viewModel.saveToDoItem()
                navController.popBackStack()
            }
        }
    ) { innerPadding ->
        val scrollState = rememberScrollState()
        val deadline by viewModel.deadLineTime.collectAsState()
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(10.dp)
                .verticalScroll(scrollState)
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = { viewModel.text.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 120.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .padding(5.dp),
            )
            PriorityBox(priority) {
                viewModel.priority.value = it
            }
            HorizontalDivider()
            DeadlineBox(deadline) {
                viewModel.deadLineTime.value = it
            }
            HorizontalDivider()
            Spacer(modifier = Modifier.height(5.dp))
            TextButton(
                onClick = {
                    if (viewModel.deleteItem())
                        navController.navigate(ScreenMain)
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
fun AppBar(
    navController: NavController,
    saveItem: () -> Unit
) {
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.close_icon),
                    contentDescription = "close"
                )
            }
        },
        actions = {
            TextButton(onClick = {
                saveItem()
                navController.navigate(ScreenMain)
            }) {
                Text(
                    stringResource(id = R.string.save),
                    fontSize = 20.sp
                )
            }
        }
    )
}

@Composable
fun PriorityBox(
    priority: Priority,
    setPriority: (Priority) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                expanded = true
            }
            .padding(10.dp, 5.dp)
    ) {
        Text(
            stringResource(id = R.string.priority),
            fontSize = 20.sp,
            modifier = Modifier
        )

        when (priority) {
            Priority.DEFAULT -> Text(
                stringResource(id = R.string.no),
                fontSize = 15.sp,
                modifier = Modifier.alpha(.5f),
            )

            Priority.LOW -> Text(
                stringResource(id = R.string.lowPriority),
                fontSize = 15.sp,
                modifier = Modifier.alpha(.75f),
            )

            Priority.HIGH -> Text(
                stringResource(id = R.string.highPriority),
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.error,
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainer)
        ) {
            DropdownMenuItem(
                text = { Text(stringResource(id = R.string.no), modifier = Modifier.alpha(.5f)) },
                onClick = {
                    setPriority(Priority.DEFAULT)
                    expanded = false
                },
            )
            DropdownMenuItem(
                text = {
                    Text(
                        stringResource(id = R.string.lowPriority),
                        modifier = Modifier.alpha(.8f)
                    )
                },
                onClick = {
                    setPriority(Priority.LOW)
                    expanded = false
                },
            )
            DropdownMenuItem(
                text = {
                    Text(
                        stringResource(id = R.string.highPriority),
                        color = MaterialTheme.colorScheme.error
                    )
                },
                onClick = {
                    setPriority(Priority.HIGH)
                    expanded = false
                },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeadlineBox(
    deadline: Long? = null,
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
                fontSize = 20.sp,
            )
            if (deadline != null) {
                Text(
                    deadline.convertDateToString(),
                    fontSize = 15.sp,
                    modifier = Modifier,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }

        Switch(
            checked = deadline != null,
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
                    Text(stringResource(id = R.string.ok))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDatePicker = false
                    }
                ) {
                    Text(stringResource(id = R.string.cancel))
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

fun Long.convertDateToString(): String {
    val selectedDate = LocalDate.ofEpochDay(this / (24 * 60 * 60 * 1000))
    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.getDefault())
    return selectedDate.format(formatter)
}
