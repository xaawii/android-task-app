package com.example.taskapp.task.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowCircleLeft
import androidx.compose.material.icons.rounded.ArrowCircleRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.taskapp.ui.theme.Greyed
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@Composable
fun SingleRowCalendarWithHorizontalScroll(
    selectedDate: LocalDate,
    yearMonth: YearMonth,
    onDateChange: (LocalDate) -> Unit,
    generateDaysInMonth: (YearMonth) -> List<LocalDate>,
    calculateScrollOffset: (LazyListState) -> Int,
    previousMonth: () -> Unit,
    nextMonth: () -> Unit,
) {


    // generate days in a specific month
    val days = generateDaysInMonth(yearMonth)

    val listState = rememberLazyListState()


    val selectedIndex = days.indexOf(selectedDate)

    // When selecting a day, lazy list will scroll to the item position and calculate the offset
    // to center the selected number on screen
    LaunchedEffect(selectedIndex) {
        if (selectedIndex != -1) {
            listState.animateScrollToItem(
                index = selectedIndex,
                scrollOffset = -calculateScrollOffset(listState)
            )
        }
    }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Month and year
        Column(modifier = Modifier
            .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = yearMonth.format(DateTimeFormatter.ofPattern("yyyy")),
                style = MaterialTheme.typography.titleSmall
            )
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                IconButton(onClick = previousMonth) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowCircleLeft,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
                Text(
                    text = yearMonth.format(DateTimeFormatter.ofPattern("MMMM")),
                    style = MaterialTheme.typography.titleLarge
                )
                IconButton(onClick = nextMonth) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowCircleRight,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }

        // LazyRow with horizontal scroll for the days
        LazyRow(
            state = listState,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(days) { day ->
                val isSelected = selectedDate == day

                DayItem(
                    date = day,
                    isSelected = isSelected,
                    onClick = { onDateChange(day) }
                )
            }
        }
    }
}


@Composable
fun DayItem(date: LocalDate, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (isSelected) MaterialTheme.colorScheme.secondary else Color.Transparent
    val textColor = if (isSelected) Color.White else Greyed

    Column(
        modifier = Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(100.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Text(
            text = date.dayOfMonth.toString(),
            color = textColor,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
}