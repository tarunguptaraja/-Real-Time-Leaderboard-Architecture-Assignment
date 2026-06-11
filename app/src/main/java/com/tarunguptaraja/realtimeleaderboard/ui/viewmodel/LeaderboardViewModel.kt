package com.tarunguptaraja.realtimeleaderboard.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tarunguptaraja.realtimeleaderboard.domain.repository.LeaderboardRepository
import com.tarunguptaraja.realtimeleaderboard.ui.state.LeaderboardUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LeaderboardViewModel(
    private val repository: LeaderboardRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LeaderboardUiState())
    val uiState: StateFlow<LeaderboardUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.Default) {
            repository.startProcessing()
        }

        viewModelScope.launch {
            repository.observeLeaderboard().collect { entries ->
                _uiState.update { state ->
                    state.copy(
                        entries = entries,
                        isRunning = true,
                        playerCount = entries.size
                    )
                }
            }
        }
    }
}
