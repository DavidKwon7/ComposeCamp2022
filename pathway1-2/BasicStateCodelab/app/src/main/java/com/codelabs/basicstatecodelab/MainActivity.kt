package com.codelabs.basicstatecodelab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.codelabs.basicstatecodelab.ui.theme.BasicStateCodelabTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BasicStateCodelabTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    WellnessScreen()
                }
            }
        }
    }
}

data class WellnessTask(val id: Int, val label: String)

@Composable
fun WellnessScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        StatefulCounter()
        WellnessTasksList()
    }
}

private fun getWellnessTasks() = List(30) { i -> WellnessTask(i, "Task # $i")}

@Composable
fun WellnessTasksList(
    modifier: Modifier = Modifier,
    list: List<WellnessTask> = remember { getWellnessTasks() }
) {
    LazyColumn(modifier = modifier) {
        items(list) { task ->
            WellnessTaskItem(taskName = task.label)
        }
    }
}

@Composable
fun WellnessTaskItem(taskName: String, modifier: Modifier = Modifier) {
    var checkedState by remember { mutableStateOf(false) }

    WellnessTaskItem(
        taskName = taskName,
        checked = checkedState,
        onCheckedChange = { newValue -> checkedState = newValue },
        onClose = {}, // we will implement this later!
        modifier = modifier,
    )
}

@Composable
fun WellnessTaskItem(
    taskName: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(modifier = Modifier
            .weight(1f)
            .padding(start = 16.dp),
            text = taskName
        )
        Checkbox(checked = checked, onCheckedChange = onCheckedChange)
        IconButton(onClick = { onClose }) {
            Icon(imageVector = Icons.Filled.Close, contentDescription = "Close")
        }
    }
}

@Composable
fun StatefulCounter() {
    var waterCounter by remember { mutableStateOf(0) }
    var juiceCounter by remember { mutableStateOf(0) }

    StatelessCounter(count = waterCounter, onIncrement = { waterCounter++ })
    StatelessCounter(count = juiceCounter, onIncrement = { juiceCounter++ })
}

// stateless로 만들어서 stateful로 hoisting
@Composable
fun StatefulCounterTest(modifier: Modifier = Modifier) {
    var count by rememberSaveable { mutableStateOf(0) }
    StatelessCounter(count = count, onIncrement = { count++ }, modifier)
}

@Composable
fun StatelessCounter(count: Int, onIncrement:() -> Unit, modifier: Modifier =Modifier) {
    Column(modifier = modifier.padding(16.dp)) {
        if (count > 0) {
            Text(text = "You've had $count glasses")
        }
        Button(onClick =  onIncrement,
            Modifier.padding(top = 8.dp),
            enabled = count <10
        ) {
            Text(text = "Add One")
        }
    }
}

/**
 * 상태가 변경될 때 : recomposition 실행(상태 변경)
 * 이는 상태가 변경될 때 Compose에 화면을 다시 그려야 한다고(recomposition)
 * 알리지 않았기 때문
 */
@Composable
fun WaterCounter(
    modifier: Modifier = Modifier
) {
    Column(modifier = Modifier.padding(16.dp)) {
        var count by remember { mutableStateOf(0) }
        if (count>0) {
            var showTask by remember { mutableStateOf(true) }
            if (showTask) {
                WellnessTaskItem(
                    taskName = "Have you taken your 15min walk today?",
                    onClose = { showTask = false })
            }
            Text(text = "You've had $count glasses")
        }
        Row(Modifier.padding(top = 8.dp)) {
            Button(onClick = { count++ }, enabled = count<10) {
                Text(text = "Add One")
            }
            Button(onClick = { count =0 }, Modifier.padding(start = 8.dp)) {
                Text(text = "Clear water count")
            }
        }
    }
}

@Composable
fun WellnessTaskItem(
    taskName: String,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier, verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp),
            text = taskName)
        IconButton(onClick = onClose) {
            Icon(imageVector = Icons.Filled.Close, contentDescription = "Close")
        }
    }
}



@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BasicStateCodelabTheme {
        //
    }
}