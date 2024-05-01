package com.rendox.grocerygenius.feature.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement.SpaceEvenly
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rendox.grocerygenius.R
import com.rendox.grocerygenius.model.DarkThemeConfig
import com.rendox.grocerygenius.model.GroceryGeniusColorScheme
import com.rendox.grocerygenius.model.GroceryList
import com.rendox.grocerygenius.model.UserPreferences
import com.rendox.grocerygenius.ui.components.CustomIconSetting
import com.rendox.grocerygenius.ui.components.DropDownMenuToggleIcon
import com.rendox.grocerygenius.ui.components.LazyDropdownMenu
import com.rendox.grocerygenius.ui.components.TonalDataInput
import com.rendox.grocerygenius.ui.components.collapsing_toolbar.CollapsingToolbar
import com.rendox.grocerygenius.ui.components.collapsing_toolbar.CollapsingToolbarScaffoldScrollableState
import com.rendox.grocerygenius.ui.components.collapsing_toolbar.scroll_behavior.CollapsingToolbarNestedScrollConnection
import com.rendox.grocerygenius.ui.components.collapsing_toolbar.scroll_behavior.rememberExitUntilCollapsedToolbarState
import com.rendox.grocerygenius.ui.theme.GroceryGeniusTheme
import com.rendox.grocerygenius.ui.theme.TopAppBarMediumHeight
import com.rendox.grocerygenius.ui.theme.TopAppBarSmallHeight
import com.rendox.grocerygenius.ui.theme.deriveColorScheme

@Composable
fun SettingsRoute(
    viewModel: SettingsScreenViewModel = hiltViewModel(),
    navigateBack: () -> Unit = {},
) {
    val screenState by viewModel.screenStateFlow.collectAsStateWithLifecycle()
    SettingsScreen(
        modifier = Modifier.fillMaxSize(),
        screenState = screenState,
        onIntent = viewModel::onIntent,
        navigateBack = navigateBack,
    )
}

@Composable
private fun SettingsScreen(
    modifier: Modifier = Modifier,
    screenState: SettingsScreenState,
    onIntent: (SettingsScreenIntent) -> Unit,
    navigateBack: () -> Unit,
) {
    var isThemeDropdownExpanded by remember { mutableStateOf(false) }
    var isDefaultListDropdownExpanded by remember { mutableStateOf(false) }

    val toolbarHeightRange = with(LocalDensity.current) {
        TopAppBarSmallHeight.roundToPx()..TopAppBarMediumHeight.roundToPx()
    }
    val toolbarState = rememberExitUntilCollapsedToolbarState(toolbarHeightRange)
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val scrollState: CollapsingToolbarScaffoldScrollableState = remember {
        object : CollapsingToolbarScaffoldScrollableState, ScrollableState by lazyListState {
            override val firstVisibleItemIndex: Int
                get() = lazyListState.firstVisibleItemIndex
            override val firstVisibleItemScrollOffset: Int
                get() = lazyListState.firstVisibleItemScrollOffset
        }
    }

    val nestedScrollConnection = remember {
        CollapsingToolbarNestedScrollConnection(
            toolbarState = toolbarState,
            scrollState = scrollState,
            coroutineScope = coroutineScope,
        )
    }

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets.navigationBars,
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .nestedScroll(nestedScrollConnection)
        ) {
            val titleStyle = MaterialTheme.typography.headlineSmall
            CollapsingToolbar(
                toolbarState = toolbarState,
                toolbarHeightRange = toolbarHeightRange,
                titleExpanded = {
                    Text(
                        text = stringResource(R.string.settings),
                        style = titleStyle.copy(textMotion = TextMotion.Animated),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                expandedTitleFontSize = titleStyle.fontSize,
                titleBottomPadding = 24.dp,
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                        )
                    }
                },
            )

            if (!screenState.isLoading) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = lazyListState,
                ) {
                    item {
                        SettingsTitle(
                            modifier = Modifier.padding(start = 16.dp, top = 16.dp),
                            title = stringResource(R.string.settings_theme)
                        )
                    }
                    item {
                        DarkThemeConfigSetting(
                            darkThemeConfig = screenState.userPreferences.darkThemeConfig,
                            isThemeDropdownExpanded = isThemeDropdownExpanded,
                            onChangeDarkThemeConfig = {
                                onIntent(SettingsScreenIntent.ChangeDarkThemeConfig(it))
                            },
                            onThemeDropdownExpandedChanged = { isThemeDropdownExpanded = it }
                        )
                    }
                    item {
                        SystemAccentColorSetting(
                            useSystemAccentColor = screenState.userPreferences.useSystemAccentColor,
                            onUseSystemAccentColorChanged = {
                                onIntent(SettingsScreenIntent.ChangeUseSystemAccentColor(it))
                            }
                        )
                    }
                    item {
                        AnimatedVisibility(visible = !screenState.userPreferences.useSystemAccentColor) {
                            ColorSchemePicker(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        start = 40.dp,
                                        end = 16.dp,
                                        top = 8.dp,
                                        bottom = 8.dp,
                                    ),
                                useDarkTheme = when (screenState.userPreferences.darkThemeConfig) {
                                    DarkThemeConfig.FOLLOW_SYSTEM -> isSystemInDarkTheme()
                                    DarkThemeConfig.LIGHT -> false
                                    DarkThemeConfig.DARK -> true
                                },
                                selectedTheme = screenState.userPreferences.selectedTheme,
                                onSchemeSelected = {
                                    onIntent(SettingsScreenIntent.ChangeColorScheme(it))
                                },
                            )
                        }
                    }
                    item {
                        SettingsTitle(
                            modifier = Modifier.padding(start = 16.dp, top = 16.dp),
                            title = stringResource(R.string.settings_preferences)
                        )
                    }
                    item {
                        OpenLastViewedListSetting(
                            openLastViewedList = screenState.userPreferences.openLastViewedList,
                            onChangeOpenLastViewedListConfig = {
                                onIntent(SettingsScreenIntent.ChangeOpenLastViewedListConfig(it))
                            }
                        )
                    }
                    item {
                        AnimatedVisibility(visible = !screenState.userPreferences.openLastViewedList) {
                            DefaultListSetting(
                                groceryLists = screenState.groceryLists,
                                defaultListId = screenState.userPreferences.defaultListId,
                                isDefaultListDropdownExpanded = isDefaultListDropdownExpanded,
                                onChangeDefaultList = {
                                    onIntent(SettingsScreenIntent.OnChangeDefaultList(it))
                                },
                                onDefaultListDropdownExpandedChanged = {
                                    isDefaultListDropdownExpanded = it
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsTitle(
    modifier: Modifier = Modifier,
    title: String,
) {
    Text(
        modifier = modifier,
        text = title,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
fun DarkThemeConfigSetting(
    darkThemeConfig: DarkThemeConfig,
    isThemeDropdownExpanded: Boolean,
    onChangeDarkThemeConfig: (DarkThemeConfig) -> Unit,
    onThemeDropdownExpandedChanged: (Boolean) -> Unit
) {
    CustomIconSetting(
        title = stringResource(R.string.settings_theme_mode),
        icon = {
            Icon(
                painterResource(R.drawable.day_night),
                contentDescription = null,
            )
        },
        trailingComponent = {
            val themeModes = DarkThemeConfig.entries.map { it.asLocalString() }
            val selectedOptionIndex = remember(darkThemeConfig) {
                DarkThemeConfig.entries.indexOf(darkThemeConfig)
            }
            TonalDataInput(
                onClick = { onThemeDropdownExpandedChanged(!isThemeDropdownExpanded) },
                dropDownMenu = {
                    DropdownMenu(
                        expanded = isThemeDropdownExpanded,
                        onDismissRequest = { onThemeDropdownExpandedChanged(false) }
                    ) {
                        themeModes.forEachIndexed { index, themeMode ->
                            DropdownMenuItem(
                                onClick = {
                                    onChangeDarkThemeConfig(DarkThemeConfig.entries[index])
                                    onThemeDropdownExpandedChanged(false)
                                },
                                text = {
                                    Text(text = themeMode)
                                }
                            )
                        }
                    }
                }
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        modifier = Modifier.widthIn(min = 56.dp, max = 136.dp),
                        text = themeModes[selectedOptionIndex],
                        style = MaterialTheme.typography.labelMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                    )
                    DropDownMenuToggleIcon(expanded = isThemeDropdownExpanded)
                }
            }
        }
    )
}

@Composable
private fun SystemAccentColorSetting(
    modifier: Modifier = Modifier,
    useSystemAccentColor: Boolean,
    onUseSystemAccentColorChanged: (Boolean) -> Unit
) {
    CustomIconSetting(
        modifier = modifier.clickable {
            onUseSystemAccentColorChanged(!useSystemAccentColor)
        },
        title = stringResource(R.string.settings_use_system_accent_color),
        icon = {
            Icon(
                painterResource(R.drawable.baseline_palette_24),
                contentDescription = null,
            )
        },
        trailingComponent = {
            Switch(
                checked = useSystemAccentColor,
                onCheckedChange = onUseSystemAccentColorChanged,
            )
        }
    )
}

@Composable
fun ColorSchemePicker(
    modifier: Modifier = Modifier,
    useDarkTheme: Boolean,
    selectedTheme: GroceryGeniusColorScheme,
    onSchemeSelected: (GroceryGeniusColorScheme) -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = SpaceEvenly,
    ) {
        for (scheme in GroceryGeniusColorScheme.entries) {
            val colors = scheme.deriveColorScheme(useDarkTheme)
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(color = colors.primaryContainer)
                    .clickable { onSchemeSelected(scheme) },
                contentAlignment = Alignment.Center,
            ) {
                if (scheme == selectedTheme) {
                    Icon(
                        imageVector = Icons.Default.Done,
                        contentDescription = null,
                        tint = Color(0xFF231B00),
                    )
                }
            }
        }
    }
}

@Composable
private fun OpenLastViewedListSetting(
    modifier: Modifier = Modifier,
    openLastViewedList: Boolean,
    onChangeOpenLastViewedListConfig: (Boolean) -> Unit
) {
    CustomIconSetting(
        modifier = modifier.clickable {
            onChangeOpenLastViewedListConfig(!openLastViewedList)
        },
        title = stringResource(R.string.settings_open_last_viewed_list),
        icon = {
            Icon(
                painterResource(id = R.drawable.baseline_history_24),
                contentDescription = null,
            )
        },
        trailingComponent = {
            Switch(
                checked = openLastViewedList,
                onCheckedChange = onChangeOpenLastViewedListConfig
            )
        }
    )
}

@Composable
fun DefaultListSetting(
    groceryLists: List<GroceryList>,
    defaultListId: String? = null,
    isDefaultListDropdownExpanded: Boolean,
    onChangeDefaultList: (String?) -> Unit,
    onDefaultListDropdownExpandedChanged: (Boolean) -> Unit
) {
    CustomIconSetting(
        title = stringResource(R.string.settings_default_list),
        icon = {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
            )
        },
        trailingComponent = {
            val unspecifiedListTitle = stringResource(R.string.settings_default_list_unspecified)
            val options = listOf(unspecifiedListTitle) + groceryLists.map { it.name }
            TonalDataInput(
                onClick = { onDefaultListDropdownExpandedChanged(!isDefaultListDropdownExpanded) },
                dropDownMenu = {
                    when {
                        options.isEmpty() -> {}
                        options.size <= 6 -> {
                            DropdownMenu(
                                expanded = isDefaultListDropdownExpanded,
                                onDismissRequest = { onDefaultListDropdownExpandedChanged(false) }
                            ) {
                                options.forEachIndexed { index, option ->
                                    DropdownMenuItem(
                                        onClick = {
                                            onChangeDefaultList(
                                                if (index == 0) null else groceryLists[index - 1].id
                                            )
                                            onDefaultListDropdownExpandedChanged(false)
                                        },
                                        text = {
                                            Text(text = option)
                                        }
                                    )
                                }
                            }
                        }

                        else -> {
                            LazyDropdownMenu(
                                expanded = isDefaultListDropdownExpanded,
                                onDismissRequest = { onDefaultListDropdownExpandedChanged(false) },
                                options = options,
                                onOptionSelected = { index ->
                                    val groceryListId =
                                        if (index == 0) null else groceryLists[index - 1].id
                                    groceryListId?.let(onChangeDefaultList)
                                    onDefaultListDropdownExpandedChanged(false)
                                }
                            )
                        }
                    }
                }
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        modifier = Modifier.widthIn(min = 56.dp, max = 136.dp),
                        text = remember(groceryLists, defaultListId) {
                            groceryLists.find {
                                it.id == defaultListId
                            }?.name ?: unspecifiedListTitle
                        },
                        style = MaterialTheme.typography.labelMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                    )
                    DropDownMenuToggleIcon(expanded = isDefaultListDropdownExpanded)
                }
            }
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewSettingsScreen() {
    GroceryGeniusTheme {
        Surface {
            SettingsScreen(
                screenState = remember {
                    SettingsScreenState(
                        userPreferences = UserPreferences(
                            useSystemAccentColor = false,
                            openLastViewedList = false,
                            selectedTheme = GroceryGeniusColorScheme.YellowColorScheme,
                        ),
                        isLoading = false,
                    )
                },
                onIntent = {},
                navigateBack = {}
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun ColorSchemePickerPreview() {
    GroceryGeniusTheme {
        Surface(modifier = Modifier.width(400.dp)) {
            ColorSchemePicker(
                modifier = Modifier.padding(16.dp),
                selectedTheme = GroceryGeniusColorScheme.PurpleColorScheme,
                onSchemeSelected = {},
                useDarkTheme = isSystemInDarkTheme()
            )
        }
    }
}

@Composable
private fun DarkThemeConfig.asLocalString() = when (this) {
    DarkThemeConfig.FOLLOW_SYSTEM ->
        stringResource(R.string.settings_dark_theme_config_system_default)

    DarkThemeConfig.LIGHT ->
        stringResource(R.string.settings_dark_theme_config_light)

    DarkThemeConfig.DARK ->
        stringResource(R.string.settings_dark_theme_config_dark)
}