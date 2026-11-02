package com.example.myapplication

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.counter.CounterScreen
import com.example.myapplication.greeting.GreetingScreen
import com.example.myapplication.tvshow.ui.TvShowScreen

sealed class BottomTab(val route: String, val title: String, val icon: ImageVector) {
    object Greeting : BottomTab("greeting", "Greeting", Icons.Filled.Person)
    object Counter : BottomTab("counter", "Counter", Icons.Filled.AddCircle)
    object TVShow : BottomTab("tvshow", "TV Show", Icons.Filled.Movie)
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val tabs = listOf(
        BottomTab.Greeting,
        BottomTab.Counter,
        BottomTab.TVShow
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            NavigationBar {
                tabs.forEach { tab ->
                    val isSelected = currentDestination?.hierarchy?.any { it.route == tab.route } == true
                    NavigationBarItem(
                        icon = { Icon(tab.icon, contentDescription = tab.title) },
                        label = { Text(tab.title) },
                        selected = isSelected,
                        onClick = {
                            navController.navigate(tab.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomTab.Greeting.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomTab.Greeting.route) {
                GreetingScreen()
            }
            composable(BottomTab.Counter.route) {
                CounterScreen()
            }
            composable(BottomTab.TVShow.route) {
                TvShowScreen()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
//    MyApplicationTheme {
    MainScreen()
//    }
}