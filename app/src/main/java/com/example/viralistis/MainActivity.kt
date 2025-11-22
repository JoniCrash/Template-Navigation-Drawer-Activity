package com.example.viralistis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.viralistis.ui.theme.ViralistisTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isDarkTheme by rememberSaveable { mutableStateOf(false) }
            var selectedDrawerItem by rememberSaveable { mutableStateOf("home") }
            val coroutineScope = rememberCoroutineScope()


            ViralistisTheme(darkTheme = isDarkTheme) {
                val navController = rememberNavController()
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()

                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        DrawerContent(
                            selectedItem = selectedDrawerItem,
                            onDestinationClicked = { route ->
//                                selectedDrawerItem = route
                                navController.navigate(route) {
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                                coroutineScope.launch { drawerState.close() }
                            }
                        )
                    }
                ) {
                    Scaffold(
                    topBar = {
                            TopAppBar(
                                modifier = Modifier
                                    .border(1.dp,
                                        MaterialTheme.colorScheme.primary,
                                        RoundedCornerShape(10.dp)),
                                title = { Text("Aplikasi Tema") },
                                navigationIcon = {
                                    IconButton(onClick = {
                                        scope.launch { drawerState.open() }
                                    }) {
                                        Icon(
                                            imageVector = Icons.Filled.Menu,
                                            contentDescription = "Menu"
                                        )
                                    }
                                },
                                actions = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(text = if (isDarkTheme) "Dark" else "Light")
                                        Switch(
                                            checked = isDarkTheme,
                                            onCheckedChange = { isDarkTheme = it }
                                        )
                                    }
                                }
                            )
                        }
                    ) { paddingValues ->
                        NavHost(
                            navController = navController,
                            startDestination = "home",
                            modifier = Modifier
                                .padding(paddingValues)
                        ) {
                            composable("home") { HomeScreen() }
                            composable("about") { AboutScreen() }
                            composable("settings") { SettingsScreen() }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DrawerContent(
    selectedItem: String,
    onDestinationClicked: (route: String) -> Unit
) {
    Column(
        modifier = Modifier
            .border(1.dp,
                MaterialTheme.colorScheme.primary,
                RoundedCornerShape(10.dp))
            .width(250.dp)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)

    ) {
        Text(
            text = "Menu",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        DrawerItem("home", "Home", selectedItem == "home") {
            onDestinationClicked("home")
        }
        DrawerItem("about", "About", selectedItem == "about") {
            onDestinationClicked("about")
        }
        DrawerItem("settings", "Settings", selectedItem == "settings") {
            onDestinationClicked("settings")
        }
    }
}



@Composable
fun DrawerItem(route: String, label: String, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else Color.Transparent
    val textColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
    val fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 16.dp)
    ) {
        Text(
            text = label,
            color = textColor,
            fontWeight = fontWeight,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}


@Composable
fun HomeScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Ini Halaman Home", style = MaterialTheme.typography.headlineMedium)
    }
}

@Composable
fun AboutScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Ini Halaman About", style = MaterialTheme.typography.headlineMedium)
    }
}

@Composable
fun SettingsScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Ini Halaman Settings", style = MaterialTheme.typography.headlineMedium)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewHome() {
    ViralistisTheme {
        HomeScreen()
    }
}


