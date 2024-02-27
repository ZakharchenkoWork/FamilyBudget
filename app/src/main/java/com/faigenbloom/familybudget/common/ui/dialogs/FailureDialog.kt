@file:OptIn(ExperimentalMaterial3Api::class)

package com.faigenbloom.familybudget.common.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.faigenbloom.familybudget.R
import com.faigenbloom.familybudget.common.ui.ErrorAnimatedIcon

@Composable
fun FailureDialog(
    isShown: Boolean = false,
    text: String,
    onHideDialog: () -> Unit,
) {
    if (isShown) {
        BasicAlertDialog(onDismissRequest = onHideDialog) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.background)
                    .padding(16.dp),
            ) {
                Text(
                    modifier = Modifier
                        .padding(bottom = 8.dp),
                    text = stringResource(R.string.error_title),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleLarge,
                )
                ErrorAnimatedIcon(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                )
                Text(
                    modifier = Modifier
                        .padding(bottom = 8.dp),
                    text = text,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleSmall,
                )
            }
        }
    }
}

