package com.example.realmsindiscord

import com.example.realmsindiscord.domain.models.Resource
import com.example.realmsindiscord.domain.usecase.LoginUseCase
import com.example.realmsindiscord.viewmodel.login.LoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    // Necesario para pruebas con Corutinas
    private val testDispatcher = StandardTestDispatcher()

    // El sujeto de prueba
    private lateinit var viewModel: LoginViewModel

    // El "Mock" (imitación) del caso de uso
    private val loginUseCase: LoginUseCase = mock()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = LoginViewModel(loginUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `login success updates state to Success`() = runTest {
        // Arrange: Le decimos al Mock que cuando llamen a login, responda Éxito
        whenever(loginUseCase("usuario", "123")).thenReturn(LoginUseCase.LoginResult.Success(mock()))

        // Act: Llamamos al método real del ViewModel
        viewModel.login("usuario", "123")

        // Avanzamos el tiempo de la corutina
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert: Verificamos que el estado final sea Success
        val state = viewModel.uiState.value
        assertTrue(state.loginResource is Resource.Success)
    }
}