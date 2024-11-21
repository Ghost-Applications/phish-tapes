package nes.app.ui.about

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import nes.app.util.ResourceManager
import javax.inject.Inject

@HiltViewModel
class AboutViewModel @Inject constructor(
    private val resourceManager: ResourceManager,
): ViewModel() {

    private val _aboutText: MutableStateFlow<AboutText> = MutableStateFlow(AboutText(""))
    val aboutText: StateFlow<AboutText> = _aboutText

    init {
        loadAboutText()
    }

    private fun loadAboutText() {
        viewModelScope.launch {
            _aboutText.emit(AboutText(resourceManager.loadAboutText()))
        }
    }
}
