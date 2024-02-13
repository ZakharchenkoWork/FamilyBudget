package com.faigenbloom.familybudget.common.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.faigenbloom.familybudget.R

@Composable
fun DateSwitcherBar(
    title: String,
    onDateMoved: (isRight: Boolean) -> Unit,
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            modifier = Modifier
                .size(32.dp)
                .aspectRatio(1f)
                .clickable {
                    onDateMoved(false)
                },
            painter = painterResource(id = R.drawable.icon_arrow_back),
            contentDescription = null,
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.secondaryContainer,
        )
        Image(
            modifier = Modifier
                .size(32.dp)
                .aspectRatio(1f)
                .clickable {
                    onDateMoved(true)
                },
            painter = painterResource(id = R.drawable.icon_arrow_next),
            contentDescription = null,
        )
    }
}
