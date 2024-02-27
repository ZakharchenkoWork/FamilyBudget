package com.faigenbloom.familybudget.common.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionDialog(
    isShown: Boolean = false,
    isShowIcon: Boolean = true,
    title: String,
    text: String,
    onOkClick: () -> Unit,
    onCancelClick: () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    if (isShown) {
        BasicAlertDialog(onDismissRequest = onCancelClick) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.background)
                    .padding(16.dp),
            ) {
                Text(
                    modifier = Modifier
                        .padding(bottom = 8.dp),
                    text = title,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleLarge,
                )
                if (isShowIcon) {
                    ErrorAnimatedIcon(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                    )
                }
                Text(
                    modifier = Modifier
                        .padding(bottom = 8.dp),
                    text = text,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleSmall,
                )
                content()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 16.dp,
                            vertical = 8.dp,
                        ),
                    horizontalArrangement = Arrangement.End,
                ) {
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .clickable {
                                onCancelClick()
                            },
                        text = stringResource(R.string.button_cancel),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(
                        modifier = Modifier
                            .clickable {
                                onOkClick()
                            },
                        text = stringResource(R.string.button_ok),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
            }
        }
    }
}
