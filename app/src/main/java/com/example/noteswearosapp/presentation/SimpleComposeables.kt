package com.example.noteswearosapp.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Message
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.AppCard
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.example.noteswearosapp.R
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.compose.layout.AppScaffold
import com.google.android.horologist.compose.layout.ScalingLazyColumn
import com.google.android.horologist.compose.layout.ScalingLazyColumnDefaults
import com.google.android.horologist.compose.layout.ScreenScaffold
import com.google.android.horologist.compose.layout.rememberResponsiveColumnState


@OptIn(ExperimentalHorologistApi::class)
@Composable
fun CustomWearApp() {
//    val listState = rememberLazyListState()
    val listState = rememberResponsiveColumnState(
        contentPadding = ScalingLazyColumnDefaults.padding(
            first = ScalingLazyColumnDefaults.ItemType.SingleButton,
            last = ScalingLazyColumnDefaults.ItemType.Chip,
        ),
    )

    /* *************************** Part 4: Wear OS Scaffold *************************** */

    AppScaffold {
        ScreenScaffold(
            scrollState = listState,
        ) {

        }
    }

    // Modifiers used by our Wear composables.
    val contentModifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 8.dp)
    val iconModifier = Modifier
        .size(24.dp)
        .wrapContentSize(align = Alignment.Center)


//    LazyColumn(
//        modifier = Modifier.fillMaxSize(),
//        contentPadding = PaddingValues(
//            top = 32.dp,
//            start = 8.dp,
//            end = 8.dp,
//            bottom = 32.dp,
//        ),
//        verticalArrangement = Arrangement.Center,
//        state = listState,
//    )
    /* *************************** Part 3: ScalingLazyColumn *************************** */
    // TODO: Swap a ScalingLazyColumn (Wear's version of LazyColumn)
    ScalingLazyColumn(
        modifier = Modifier.fillMaxSize(),
        columnState = listState,
    ) {
        // TODO: Remove item; for beginning only.
//        item { StartOnlyTextComposables() }

        /* ******************* Part 1: Simple composables ******************* */
        item {
            ButtonExample(contentModifier, iconModifier) {
                // receive voice

            }
        }
        item { TextExample(contentModifier) }
        item { CardExample(contentModifier, iconModifier) }

        /* ********************* Part 2: Wear unique composables ********************* */
        item { ChipExample(contentModifier, iconModifier) }
        item { ToggleChipExample(contentModifier) }
    }
}



@Composable
fun ButtonExample(modifier: Modifier, iconModifier: Modifier,buttonClick: () -> Unit){
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        // Button
        Button(
            modifier = Modifier.size(ButtonDefaults.LargeButtonSize),
            onClick = buttonClick
            ,
        ) {
            Icon(
                imageVector = Icons.Rounded.Phone,
                contentDescription = "triggers phone action",
                modifier = iconModifier
            )
        }
    }
}

@Composable
fun TextExample(modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colors.primary,
        text = stringResource(R.string.device_shape)
    )
}


@Composable
fun CardExample(
    modifier: Modifier = Modifier,
    iconModifier: Modifier = Modifier
) {
    AppCard(
        modifier = modifier,
        appImage = {
            Icon(
                imageVector = Icons.Rounded.Message,
                contentDescription = "triggers open message action",
                modifier = iconModifier
            )
        },
        appName = { Text("Messages") },
        time = { Text("12m") },
        title = { Text("Kim Green") },
        onClick = { /* ... */ }
    ) {
        Text("On my way!")
    }
}