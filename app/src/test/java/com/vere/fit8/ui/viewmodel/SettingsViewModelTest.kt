package com.vere.fit8.ui.viewmodel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.vere.fit8.data.model.AppSettings
import com.vere.fit8.data.repository.Fit8Repository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * 设置ViewModel单元测试
 */
@ExperimentalCoroutinesApi
class SettingsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()
    
    private lateinit var application: Application
    private lateinit var repository: Fit8Repository
    private lateinit var viewModel: SettingsViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        
        application = mockk(relaxed = true)
        repository = mockk(relaxed = true)
        viewModel = SettingsViewModel(application, repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadSettings should load default settings when no settings exist`() = runTest {
        // Given
        coEvery { repository.getAppSettings() } returns null
        coEvery { repository.saveAppSettings(any()) } returns Unit

        // When
        viewModel.loadSettings()

        // Then
        val settings = viewModel.settings.first()
        assertEquals("燃力用户", settings.userName)
        assertEquals(170f, settings.userHeight)
        assertTrue(settings.trainingReminderEnabled)
        assertEquals("18:00", settings.trainingReminderTime)
        
        coVerify { repository.saveAppSettings(any()) }
    }

    @Test
    fun `loadSettings should load existing settings`() = runTest {
        // Given
        val existingSettings = AppSettings(
            userName = "测试用户",
            userHeight = 175f,
            trainingReminderEnabled = false,
            trainingReminderTime = "19:00"
        )
        coEvery { repository.getAppSettings() } returns existingSettings

        // When
        viewModel.loadSettings()

        // Then
        val settings = viewModel.settings.first()
        assertEquals("测试用户", settings.userName)
        assertEquals(175f, settings.userHeight)
        assertFalse(settings.trainingReminderEnabled)
        assertEquals("19:00", settings.trainingReminderTime)
    }

    @Test
    fun `updateTrainingReminderEnabled should update setting and show message`() = runTest {
        // Given
        coEvery { repository.updateTrainingReminderEnabled(true) } returns Unit

        // When
        viewModel.updateTrainingReminderEnabled(true)

        // Then
        val settings = viewModel.settings.first()
        assertTrue(settings.trainingReminderEnabled)
        
        val message = viewModel.message.first()
        assertEquals("训练提醒已开启", message)
        
        coVerify { repository.updateTrainingReminderEnabled(true) }
    }

    @Test
    fun `updateUserName should update name and show message`() = runTest {
        // Given
        val newName = "新用户名"
        coEvery { repository.updateUserName(newName) } returns Unit

        // When
        viewModel.updateUserName(newName)

        // Then
        val settings = viewModel.settings.first()
        assertEquals(newName, settings.userName)
        
        val message = viewModel.message.first()
        assertEquals("姓名已更新", message)
        
        coVerify { repository.updateUserName(newName) }
    }

    @Test
    fun `updateDarkModeEnabled should update setting`() = runTest {
        // Given
        coEvery { repository.updateDarkModeEnabled(true) } returns Unit

        // When
        viewModel.updateDarkModeEnabled(true)

        // Then
        val settings = viewModel.settings.first()
        assertTrue(settings.darkModeEnabled)
        
        coVerify { repository.updateDarkModeEnabled(true) }
    }

    @Test
    fun `clearMessage should clear current message`() = runTest {
        // Given - 先设置一个消息
        coEvery { repository.updateUserName(any()) } returns Unit
        viewModel.updateUserName("test")

        // When
        viewModel.clearMessage()

        // Then
        val message = viewModel.message.first()
        assertEquals(null, message)
    }
}
