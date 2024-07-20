package com.mead.todoapp.task

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Add Task Screen where user can input the name and description of the task
 */
@Preview(showBackground = true)
@Composable
fun AddTaskScreen(
    innerPadding: PaddingValues = PaddingValues(0.dp), cancelButton: (() -> Unit)? = null,
    addTaskAction: ((title: String, description: String) -> Unit)? = null
) {
    var title: String by remember { mutableStateOf("") }
    var description: String by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(innerPadding)
            .padding(16.dp)
    ) {
        // Back Button
        IconButton(
            colors = IconButtonDefaults.iconButtonColors(),
            onClick = { cancelButton?.invoke() }
        ) {
            Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = null)
        }

        // The InputField of Title
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1
        )

        Spacer(modifier = Modifier.height(8.dp))

        // The InputField of Description
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description (Optional)") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Add to the database
        Button(
            onClick = { addTaskAction?.invoke(title, description) },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Add Task")
        }
    }
}
