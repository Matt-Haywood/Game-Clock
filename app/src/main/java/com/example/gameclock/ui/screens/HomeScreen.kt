@file:Suppress("unused")

package com.example.gameclock.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.gameclock.R
import com.example.gameclock.model.AppTheme
import com.example.gameclock.model.ClockThemePreferences
import com.example.gameclock.ui.screens.backgrounds.HomeBackground


@Composable
fun HomeScreen(
    clockThemeList: List<ClockThemePreferences>,
    onThemeClick: (AppTheme) -> Unit
) {
    val isLandscape: Boolean =
        LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE



    Scaffold(bottomBar = {
        BannerAd()

    }) {paddingValues ->
        HomeBackground()
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.padding(10.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(10.dp)
                    )
                }


                if (isLandscape) {
                    LazyHorizontalGrid(
                        rows = GridCells.Adaptive(200.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                        contentPadding = PaddingValues(20.dp)
                    ) {
                        items(clockThemeList) { clockTheme ->
                            UiCard(clockTheme, onThemeClick = { onThemeClick(clockTheme.appTheme) })
                        }
                    }
                } else
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(160.dp),
                        //                    GridCells.Adaptive(130.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                        contentPadding = PaddingValues(10.dp)
                    ) {
                        items(clockThemeList) { clockTheme ->
                            UiCard(clockTheme, onThemeClick = { onThemeClick(clockTheme.appTheme) })
                        }
                    }
            }
        }
    }
}

@Composable
fun UiCard(
    clockTheme: ClockThemePreferences,
    onThemeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(),
        onClick = onThemeClick,
        modifier = modifier
            .size(200.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(clockTheme.thumbnail),
                contentDescription = clockTheme.appTheme.themeName,
                modifier = modifier
                    .fillMaxSize(),

                contentScale = ContentScale.Crop
            )

            Text(
                text = clockTheme.appTheme.themeName,
                modifier = Modifier.padding(
                    top = 8.dp,
                    start = 0.dp,
                    end = 0.dp,
                    bottom = 8.dp
                ),
            )
        }
    }
}

/*
@Preview(
    showSystemUi = true,
)
@Composable
fun PortraitPreview() {
    GameClockTheme(appTheme = AppTheme.Default) {
        HomeScreen(clockThemeList = ClockThemeList().loadThemes()) {}
    }

}

@Preview(
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape"
)
@Composable
fun LandscapePreview() {
    GameClockTheme(appTheme = AppTheme.Default) {
        HomeScreen(clockThemeList = ClockThemeList().loadThemes()) {}
    }

}*/
