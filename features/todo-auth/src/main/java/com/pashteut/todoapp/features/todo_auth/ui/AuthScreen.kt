package com.pashteut.todoapp.features.todo_auth.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pashteut.todoapp.features.todo_auth.BuildConfig
import com.pashteut.todoapp.features.todo_auth.R
import com.pashteut.todoapp.features.todo_auth.ui_logic.AuthScreenViewModel

@Composable
fun AuthScreen(
    backNavigation: () -> Unit,
    viewModel: AuthScreenViewModel,
) {
    val uiMessage by viewModel.message.collectAsStateWithLifecycle()
    AuthScreenContent(
        mainScreenNavigation = backNavigation,
        uiMessage = uiMessage,
        setAuthToken = viewModel::setToken,
        logout = viewModel::logout,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AuthScreenContent(
    mainScreenNavigation: () -> Unit,
    uiMessage: String?,
    setAuthToken: (String) -> Unit,
    logout: () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var text by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = uiMessage) {
        uiMessage?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = mainScreenNavigation) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(10.dp)
                .fillMaxSize()
        ) {
            val context = LocalContext.current
            Row {
                TextButton(onClick = {
                    context.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(BuildConfig.REQUEST_TOKEN_URL)
                        )
                    )
                }) {
                    Text(
                        stringResource(id = R.string.getToken),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                TextButton(onClick = logout) {
                    Text(
                        stringResource(id = R.string.resetToken),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            AuthInputBar(
                text = text,
                onTextChange = { text = it },
                setAuthToken = setAuthToken,
                authVisible = passwordVisible,
                onChangeAuthVisibility = { passwordVisible = !passwordVisible }
            )
        }
    }
}

@Composable
private fun AuthInputBar(
    text: String,
    onTextChange: (String) -> Unit,
    setAuthToken: (String) -> Unit,
    authVisible: Boolean,
    onChangeAuthVisibility: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = onTextChange,
            modifier = Modifier.weight(1f),
            textStyle = MaterialTheme.typography.bodyMedium,
            label = { Text(stringResource(id = R.string.token)) },
            singleLine = true,
            trailingIcon = {
                val painter =
                    if (authVisible) painterResource(id = R.drawable.visibility_icon)
                    else painterResource(id = R.drawable.visibility_off_icon)

                val description = if (authVisible) "Hide password" else "Show password"

                IconButton(onClick = onChangeAuthVisibility) {
                    Icon(painter = painter, contentDescription = description)
                }
            },
            visualTransformation =
            if (authVisible) VisualTransformation.None
            else PasswordVisualTransformation()
        )

        IconButton(
            modifier = Modifier.padding(start = 10.dp),
            onClick = { setAuthToken(text) },
        ) {
            Icon(Icons.Filled.Check, contentDescription = "Set token")
        }
    }
}