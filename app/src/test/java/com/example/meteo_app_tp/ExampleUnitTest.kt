package com.example.meteo_app_tp

import com.example.meteo_app_tp.data.model.WeatherForecast
import com.example.meteo_app_tp.data.repository.WeatherForecastRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherForecastRepositoryTest {

    @Mock
    private lateinit var weatherApiRepository: WeatherApiRepository

    @Mock
    private lateinit var weatherCacheRepository: WeatherCacheRepository

    private lateinit var weatherForecastRepository: WeatherForecastRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        weatherForecastRepository = WeatherForecastRepository(weatherApiRepository, weatherCacheRepository, isConnected = true)
    }

    @Test
    fun `getWeatherData emits data from API when connected`() = runTest {
        val apiData = listOf(
            WeatherForecast(
                time = "2024-11-25T12:00:00Z",
                temp = 20.5,
                humidity = 60,
                app_temp = 22.0,
                rain = 0.0,
                wind_speed = 15.0
            )
        )
        `when`(weatherApiRepository.getWeatherData("10", "20")).thenReturn(flow { emit(apiData) })
        doNothing().`when`(weatherCacheRepository).cacheData(apiData, "10", "20")
        val result = weatherForecastRepository.getWeatherData("10", "20").toList()
        verify(weatherApiRepository).getWeatherData("10", "20")
        verify(weatherCacheRepository).cacheData(apiData, "10", "20")
        assertEquals(apiData, result[0])
    }

    @Test
    fun `getWeatherData emits data from cache when not connected`() = runTest {
        weatherForecastRepository = WeatherForecastRepository(weatherApiRepository, weatherCacheRepository, isConnected = false)
        val cachedData = listOf(
            WeatherForecast(
                time = "2024-11-25T12:00:00Z",
                temp = 18.5,
                humidity = 70,
                app_temp = 19.0,
                rain = 2.0,
                wind_speed = 10.0
            )
        )
        `when`(weatherCacheRepository.getWeatherData("10", "20")).thenReturn(flow { emit(cachedData) })
        val result = weatherForecastRepository.getWeatherData("10", "20").toList()
        verify(weatherCacheRepository).getWeatherData("10", "20")
        assertEquals(cachedData, result[0])
    }

    @Test
    fun `getWeatherData falls back to cache on API error`() = runTest {
        `when`(weatherApiRepository.getWeatherData("10", "20")).thenThrow(RuntimeException("API Error"))
        val cachedData = listOf(
            WeatherForecast(
                time = "2024-11-25T12:00:00Z",
                temp = 15.0,
                humidity = 65,
                app_temp = 14.0,
                rain = 5.0,
                wind_speed = 20.0
            )
        )
        `when`(weatherCacheRepository.getWeatherData("10", "20")).thenReturn(flow { emit(cachedData) })
        val result = weatherForecastRepository.getWeatherData("10", "20").toList()
        verify(weatherApiRepository).getWeatherData("10", "20")
        verify(weatherCacheRepository).getWeatherData("10", "20")
        assertEquals(cachedData, result[0])
    }
}
