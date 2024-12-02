package com.example.Tranquility.Screens

@Composable
fun MeditationLogScreen(viewModel: MeditationLogViewModel) {
    val meditationLogs by viewModel.meditationLogs.observeAsState(emptyList())

    LazyColumn {
        items(meditationLogs) { log ->
            MeditationLogItem(log)
        }
    }
}

@Composable
fun MeditationLogItem(log: MeditationLog) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = log.dateTime, style = MaterialTheme.typography.body1)
        Text(text = "${log.meditationTime} min", style = MaterialTheme.typography.body2)
    }
}
