package com.ayaan.artignment.presentation.detail.bottomsheet.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun IdleContent(
    selectedFileName: String?,
    onSelectFile: () -> Unit,
    onUploadFile: () -> Unit,
    onDismiss: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedButton(
            onClick = onSelectFile, modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.AttachFile, contentDescription = "Select file")
            Spacer(modifier = Modifier.width(8.dp))
            Text(if (selectedFileName == null) "Select File" else "Change File")
        }

        if (selectedFileName != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Selected: $selectedFileName",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onDismiss, modifier = Modifier.weight(1f)
            ) {
                Text("Cancel")
            }

            Button(
                onClick = onUploadFile,
                enabled = selectedFileName != null,
                modifier = Modifier.weight(1f)
            ) {
                Text("Upload")
            }
        }
    }
}
