package com.faigenbloom.famillyspandings.ui.chooser

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.faigenbloom.famillyspandings.R
import com.faigenbloom.famillyspandings.ui.theme.FamillySpandingsTheme

@Composable
fun ImageSourceChooser(
    onDismissRequest: () -> Unit,
    onGalleryChosen: () -> Unit,
    onCameraChosen: () -> Unit,
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(color = MaterialTheme.colorScheme.background),
        ) {
            Text(
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                        top = 16.dp,
                    ),
                text = stringResource(id = R.string.photo_chooser_title),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge,
            )
            Row {
                DialogButton(
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(start = 16.dp)
                        .padding(vertical = 16.dp)
                        .clickable {
                            onGalleryChosen()
                        },
                    drawableRes = R.drawable.icon_from_gallery,
                    text = stringResource(R.string.photo_chooser_gallery),

                    )
                DialogButton(
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(start = 16.dp, end = 16.dp)
                        .padding(vertical = 16.dp)
                        .clickable {
                            onCameraChosen()
                        },
                    drawableRes = R.drawable.icon_take_photo,
                    text = stringResource(R.string.photo_chooser_camera),
                )
            }
        }
    }
}

@Composable
fun DialogButton(modifier: Modifier, drawableRes: Int, text: String) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            modifier = Modifier.size(80.dp),
            painter = painterResource(id = drawableRes),
            contentDescription = "",
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = text,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DialogScreenPreview() {
    FamillySpandingsTheme {
        ImageSourceChooser(
            onDismissRequest = { },
            onGalleryChosen = {},
            onCameraChosen = {},
        )
    }
}
