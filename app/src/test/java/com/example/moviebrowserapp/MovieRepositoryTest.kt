package com.example.moviebrowser

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.moviebrowser.data.local.MovieDao
import com.example.moviebrowser.data.local.MovieEntity
import com.example.moviebrowser.data.local.UserDao
import com.example.moviebrowser.data.local.UserEntity
import com.example.moviebrowser.data.remote.MovieDto
import com.example.moviebrowser.data.remote.MovieResponse
import com.example.moviebrowser.data.remote.TmdbApi
import com.example.moviebrowser.data.repository.MovieRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class MovieRepositoryTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: MovieRepository
    private val movieDao: MovieDao = mock()
    private val userDao: UserDao = mock()
    private val api: TmdbApi = mock()

    @Before
    fun setup() {
        repository = MovieRepository(movieDao, userDao, api)
    }

    @Test
    fun `getPopularMovies returns results from api`() = runTest {
        val fakeMovies = listOf(
            MovieDto(1, "Movie 1", "Overview 1", null, "2024-01-01", 7.5),
            MovieDto(2, "Movie 2", "Overview 2", null, "2024-02-01", 8.0)
        )
        val fakeResponse = MovieResponse(1, fakeMovies, 1, 2)
        whenever(api.getPopularMovies(any(), any(), any())).thenReturn(fakeResponse)

        val result = repository.getPopularMovies("fake_key")

        assertEquals(2, result.results.size)
        assertEquals("Movie 1", result.results[0].title)
    }

    @Test
    fun `searchMovies returns filtered results`() = runTest {
        val fakeMovies = listOf(
            MovieDto(1, "Batman", "Dark knight", null, "2022-01-01", 8.5)
        )
        val fakeResponse = MovieResponse(1, fakeMovies, 1, 1)
        whenever(api.searchMovies(any(), any(), any(), any())).thenReturn(fakeResponse)

        val result = repository.searchMovies("fake_key", "Batman")

        assertEquals(1, result.results.size)
        assertEquals("Batman", result.results[0].title)
    }

    @Test
    fun `isFavorite returns true when movie is in favorites`() = runTest {
        whenever(movieDao.isFavorite(1, 1)).thenReturn(1)

        val result = repository.isFavorite(1, 1)

        assertTrue(result)
    }

    @Test
    fun `isFavorite returns false when movie is not in favorites`() = runTest {
        whenever(movieDao.isFavorite(99, 1)).thenReturn(0)

        val result = repository.isFavorite(99, 1)

        assertFalse(result)
    }

    @Test
    fun `addFavorite calls dao insertFavorite`() = runTest {
        val entity = MovieEntity(1, 1, "Test", "Overview", null, null, 7.0)

        repository.addFavorite(entity)

        verify(movieDao).insertFavorite(entity)
    }

    @Test
    fun `removeFavorite calls dao deleteFavorite`() = runTest {
        val entity = MovieEntity(1, 1, "Test", "Overview", null, null, 7.0)

        repository.removeFavorite(entity)

        verify(movieDao).deleteFavorite(entity)
    }

    @Test
    fun `login returns user when credentials are correct`() = runTest {
        val fakeUser = UserEntity(1, "Test User", "test@test.com", "123456")
        whenever(userDao.login("test@test.com", "123456")).thenReturn(fakeUser)

        val result = repository.login("test@test.com", "123456")

        assertNotNull(result)
        assertEquals("Test User", result?.name)
    }

    @Test
    fun `login returns null when credentials are wrong`() = runTest {
        whenever(userDao.login("wrong@test.com", "wrong")).thenReturn(null)

        val result = repository.login("wrong@test.com", "wrong")

        assertNull(result)
    }

    @Test
    fun `register calls userDao insertUser`() = runTest {
        val user = UserEntity(name = "New User", email = "new@test.com", password = "123456")
        whenever(userDao.insertUser(user)).thenReturn(1L)

        val result = repository.register(user)

        assertEquals(1L, result)
        verify(userDao).insertUser(user)
    }

    @Test
    fun `getUserByEmail returns null for unknown email`() = runTest {
        whenever(userDao.getUserByEmail("unknown@test.com")).thenReturn(null)

        val result = repository.getUserByEmail("unknown@test.com")

        assertNull(result)
    }
}