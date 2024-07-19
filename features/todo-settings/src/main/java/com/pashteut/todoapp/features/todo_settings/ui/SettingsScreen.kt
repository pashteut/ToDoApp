package com.pashteut.todoapp.features.todo_settings.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pashteut.todoapp.features.todo_settings.R
import com.pashteut.todoapp.features.todo_settings.ui_logic.SettingsScreenViewModel
import com.pashteut.todoapp.ui_kit.additionalColors
import com.pashteut.todoapp.ui_kit.theme_state.ThemeState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    backNavigation: () -> Unit,
    aboutScreenNavigation: () -> Unit,
    viewModel: SettingsScreenViewModel,
) {
    val themeState by viewModel.themeState.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.settings),
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 40.dp, top = 50.dp, end = 15.dp, bottom = 10.dp)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = backNavigation) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(10.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(MaterialTheme.colorScheme.additionalColors.surfaceVariant),
        ) {
            ThemeSettings(
                selectedThemeState = themeState,
                selectThemeState = {
                    viewModel.setThemeState(it)
                },
                modifier = Modifier.background(Color.Transparent)
            )
            HorizontalDivider()
            AboutAppBar(
                aboutScreenNavigation = aboutScreenNavigation,
            )
        }
    }
}

@Composable
fun ThemeSettings(
    selectedThemeState: ThemeState,
    selectThemeState: (ThemeState) -> Unit,
    modifier: Modifier = Modifier,
) {
    var themesVisibility by remember {
        mutableStateOf(false)
    }
    val rotationAngle by animateFloatAsState(targetValue = if (themesVisibility) 90f else 0f)
    Column(
        modifier = modifier,
    ) {
        ListItem(
            headlineContent = {
                Text(text = stringResource(id = R.string.theme))
            },
            leadingContent = {
                Icon(
                    painterResource(id = R.drawable.palette),
                    contentDescription = "theme",
                )
            },
            trailingContent = {
                IconButton(onClick = { themesVisibility = !themesVisibility }) {
                    Icon(
                        painterResource(id = R.drawable.arrow_down),
                        contentDescription = "expand",
                        modifier = Modifier.graphicsLayer { rotationZ = rotationAngle }
                    )
                }
            },
            modifier = Modifier
                .clickable { themesVisibility = !themesVisibility },
            colors = ListItemDefaults.colors(
                containerColor = Color.Transparent,
            ),
        )
        AnimatedVisibility(visible = themesVisibility) {
            Column {
                ThemeState.entries.forEach { themeState ->
                    ListItem(
                        headlineContent = {
                            Text(
                                text = when (themeState) {
                                    ThemeState.Light -> stringResource(id = R.string.light)
                                    ThemeState.Dark -> stringResource(id = R.string.dark)
                                    ThemeState.System -> stringResource(id = R.string.system)
                                }
                            )
                        },
                        leadingContent = {
                            RadioButton(
                                selected = selectedThemeState == themeState,
                                onClick = { selectThemeState(themeState) },
                            )
                        },
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .clickable { selectThemeState(themeState) },
                        colors = ListItemDefaults.colors(
                            containerColor = Color.Transparent,
                        ),
                    )
                }
            }
        }
    }
}

@Composable
fun AboutAppBar(
    aboutScreenNavigation: () -> Unit,
) {
    ListItem(
        headlineContent = {
            Text(text = stringResource(id = R.string.aboutApp))
        },
        leadingContent = {
            Icon(
                painter = painterResource(id = R.drawable.cubes),
                contentDescription = "aboutApp"
            )
        },
        colors = ListItemDefaults.colors(
            containerColor = Color.Transparent,
        ),
        modifier = Modifier
            .clickable { aboutScreenNavigation() }
    )
}