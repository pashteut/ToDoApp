package com.pashteut.todoapp.features.todo_about

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import com.pashteut.todoapp.ui_kit.isDarkTheme
import com.yandex.div.core.Div2Context
import com.yandex.div.core.DivConfiguration
import com.yandex.div.picasso.PicassoDivImageLoader

@Composable
fun AboutScreen(
    backNavigation: () -> Unit,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val assetReader = AssetReader(context)
    val activityContext = when (context) {
        is Activity -> context
        else -> throw IllegalStateException("Context must be an Activity")
    }

    AndroidView(
        factory = {
            val divJson = assetReader.read("aboutApp.json")
            val templatesJson = divJson.optJSONObject("templates")
            val cardJson = divJson.getJSONObject("card")

            val divContext = Div2Context(
                baseContext = activityContext,
                configuration = createDivConfiguration(context, backNavigation),
                lifecycleOwner = lifecycleOwner
            )
            val view = Div2ViewFactory(divContext, templatesJson).createView(cardJson)
            view.setVariable("isDarkTheme", isDarkTheme.toString())
            return@AndroidView view
        },
        modifier = Modifier
            .fillMaxSize()
    )
}

private fun createDivConfiguration(context: Context, action:()-> Unit): DivConfiguration {
    return DivConfiguration.Builder(PicassoDivImageLoader(context))
        .actionHandler(MyDivActionHandler(action))
        .visualErrorsEnabled(true)
        .build()
}
