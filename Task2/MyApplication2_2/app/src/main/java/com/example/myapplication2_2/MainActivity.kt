package com.example.myapplication2_2

import androidx.compose.ui.tooling.preview.Preview
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Task(
    val id: Int,
    val title: String,
    val isDone: Boolean = false
)


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                TaskScreen()
            }
        }
    }
}

@Composable
fun TaskScreen() {
    val tasks = remember {
        mutableStateListOf(
            Task(1, "学习 Column 和 Row", true),
            Task(2, "学习状态管理", false),
            Task(3, "完成 Compose 实验", false)
        )
    }

    var inputText by remember { mutableStateOf("") }
    var nextId by remember { mutableIntStateOf(4) }

    val doneCount = tasks.count { it.isDone }
    val totalCount = tasks.size

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "课程学习任务",
            color = Color(0xFFD32F2F),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                placeholder = { Text("请输入学习任务") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    if (inputText.isNotBlank()) {
                        tasks.add(Task(nextId++, inputText.trim()))
                        inputText = ""
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFD32F2F)
                )
            ) {
                Text("添加", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 已完成计数
        Text(
            text = "已完成：$doneCount / $totalCount",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // 任务列表
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(tasks, key = { it.id }) { task ->
                TaskItem(
                    task = task,
                    onToggle = {
                        val index = tasks.indexOf(task)
                        tasks[index] = task.copy(isDone = !task.isDone)
                    },
                    onDelete = {
                        tasks.remove(task)
                    }
                )
            }
        }
    }
}

@Composable
fun TaskItem(
    task: Task,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.isDone,
                onCheckedChange = { onToggle() }
            )
            Text(
                text = task.title,
                modifier = Modifier.weight(1f),
                textDecoration = if (task.isDone)
                    TextDecoration.LineThrough else TextDecoration.None,
                color = if (task.isDone) Color.Gray else Color.Black
            )
            TextButton(onClick = onDelete) {
                Text("删除", color = Color(0xFFD32F2F))
            }
        }
    }
}