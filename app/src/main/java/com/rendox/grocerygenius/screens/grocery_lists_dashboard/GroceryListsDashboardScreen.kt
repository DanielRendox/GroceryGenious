package com.rendox.grocerygenius.screens.grocery_lists_dashboard


import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rendox.grocerygenius.R
import com.rendox.grocerygenius.model.GroceryList
import com.rendox.grocerygenius.screens.grocery_lists_dashboard.recyclerview.DashboardRecyclerViewAdapter
import com.rendox.grocerygenius.ui.helpers.ObserveUiEvent
import com.rendox.grocerygenius.ui.theme.GroceryGeniusTheme

@Composable
fun GroceryListsDashboardRoute(
    viewModel: GroceryListsDashboardViewModel = hiltViewModel(),
    navigateToGroceryListScreen: (String) -> Unit,
) {
    val screenState by viewModel.groceryListsFlow.collectAsStateWithLifecycle()
    val navigateToNewlyCreatedGroceryListEvent by viewModel.navigateToNewlyCreatedGroceryListEvent.collectAsStateWithLifecycle()
    ObserveUiEvent(navigateToNewlyCreatedGroceryListEvent) { groceryListId ->
        navigateToGroceryListScreen(groceryListId)
    }

    GroceryListsDashboardScreen(
        groceryLists = screenState,
        onIntent = viewModel::onIntent,
        navigateToGroceryListScreen = navigateToGroceryListScreen,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroceryListsDashboardScreen(
    groceryLists: List<GroceryList>,
    onIntent: (GroceryListsDashboardIntent) -> Unit = {},
    navigateToGroceryListScreen: (String) -> Unit = {},
) {
    var scrollState by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) }
            )
        }
    ) { paddingValues ->
        AndroidView(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .navigationBarsPadding(),
            factory = { context ->
                RecyclerView(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                    )
                    layoutManager = LinearLayoutManager(context).apply {
                        descendantFocusability
                        scrollToPositionWithOffset(0, -scrollState)
                    }
                    addOnScrollListener(object : RecyclerView.OnScrollListener() {
                        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                            super.onScrolled(recyclerView, dx, dy)
                            scrollState += dy
                        }
                    })
                    val adapter = DashboardRecyclerViewAdapter(
                        recyclerView = this,
                        groceryLists = groceryLists,
                        updateLists = { newValue ->
                            onIntent(GroceryListsDashboardIntent.OnUpdateGroceryLists(newValue))
                        },
                        onItemClicked = navigateToGroceryListScreen,
                        onAdderItemClicked = {
                            onIntent(GroceryListsDashboardIntent.OnCreateNewGroceryList)
                        },
                    )
                    this.adapter = adapter
                }
            },
            update = { recyclerView ->
                val adapter = recyclerView.adapter as DashboardRecyclerViewAdapter
                val isInitialUpdate = adapter.groceryLists.isEmpty()
                adapter.updateGroceryLists(groceryLists)

                // this code is required to keep scroll position when updating the list
                // otherwise recyclerView will automatically follow the AdderItem and scroll
                // to bottom once the list is fetched
                if (isInitialUpdate) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    layoutManager.scrollToPositionWithOffset(0, -scrollState)
                }
            }
        )
    }
}

@Preview
@Composable
private fun GroceryListsDashboardPreview() {
    GroceryGeniusTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            GroceryListsDashboardScreen(
                groceryLists = sampleDashboard,
            )
        }
    }
}

val sampleDashboard = List(20) {
    GroceryList(
        id = it.toString(),
        name = "List $it",
        numOfGroceries = it,
    )
}