# Movie Browser App

> Мобилно приложение за търсене и управление на любими филми, изградено с **Kotlin**, **MVVM** архитектура и **TMDB API**.

---

## Идея

Movie Browser позволява на потребителите да **търсят филми**, да разглеждат детайли за тях и да **запазват любими** — всичко свързано с личен акаунт. Данните за филмите идват от [The Movie Database (TMDB)](https://www.themoviedb.org/), а любимите се пазят локално в **Room база данни**.

---

## Как работи

1. При стартиране се показва **Splash Screen** с логото на приложението
2. Потребителят се **регистрира** с име, имейл и парола или **влиза** в съществуващ акаунт
3. Главният екран зарежда **популярни филми** от TMDB API
4. **Търсачката** филтрира филми по заглавие в реално време
5. При натискане на филм се отваря **детайлен изглед** с постер, описание и рейтинг
6. Бутонът **добавя или премахва** филма от любими
7. Табът **"Любими"** показва всички запазени филми за текущия потребител
8. Горе вдясно има бутон за **изход от акаунта**

---

## Архитектура

Приложението следва **MVVM (Model-View-ViewModel)** pattern с **Repository** слой:

```
UI Layer (Fragments)
      ↕
ViewModel Layer (HomeViewModel, DetailViewModel, FavoritesViewModel)
      ↕
Repository Layer (MovieRepository)
      ↕              ↕
Remote (Retrofit)  Local (Room)
TMDB API           SQLite Database
```

### Структура на проекта

```
app/src/main/java/com/example/moviebrowserapp/
├── data/
│   ├── local/
│   │   ├── MovieDatabase.kt       ← Room база данни
│   │   ├── MovieDao.kt            ← CRUD за филми
│   │   ├── MovieEntity.kt         ← Таблица favorites
│   │   ├── UserDao.kt             ← CRUD за потребители
│   │   └── UserEntity.kt          ← Таблица users
│   ├── remote/
│   │   ├── TmdbApi.kt             ← Retrofit интерфейс
│   │   ├── MovieDto.kt            ← API модели
│   │   └── RetrofitInstance.kt    ← Singleton Retrofit
│   └── repository/
│       └── MovieRepository.kt     ← Единствен source of truth
├── ui/
│   ├── splash/SplashActivity.kt
│   ├── auth/
│   │   ├── LoginFragment.kt
│   │   └── RegisterFragment.kt
│   ├── home/
│   │   ├── HomeFragment.kt
│   │   ├── HomeViewModel.kt
│   │   └── adapter/MovieAdapter.kt
│   ├── detail/
│   │   ├── DetailFragment.kt
│   │   └── DetailViewModel.kt
│   └── favorites/
│       ├── FavoritesFragment.kt
│       ├── FavoritesViewModel.kt
│       └── adapter/FavoritesAdapter.kt
├── utils/
│   └── SessionManager.kt          ← SharedPreferences за сесия
└── MovieApp.kt                    ← Application клас
```

### Пример — Repository pattern

```kotlin
class MovieRepository(
    private val movieDao: MovieDao,
    private val userDao: UserDao,
    private val api: TmdbApi = RetrofitInstance.api
) {
    suspend fun getPopularMovies(apiKey: String) =
        api.getPopularMovies(apiKey)

    fun getAllFavorites(userId: Int): LiveData<List<MovieEntity>> =
        movieDao.getAllFavorites(userId)

    suspend fun isFavorite(movieId: Int, userId: Int): Boolean =
        movieDao.isFavorite(movieId, userId) > 0
}
```

---

## Потребителски поток

```
[Splash Screen]
      ↓
[Login / Register]
      ↓
[Home — Популярни филми]
   ↓          ↓
[Търсене]  [Детайли за филм]
               ↓
         [Добави в Любими]
               ↓
         [Таб Любими]
               ↓
         [Детайли → Премахни]
```

---

## Технологии и версии

| Технология | Версия | Употреба |
|---|---|---|
| **Kotlin** | 1.9.22 | Основен език |
| **Android Gradle Plugin** | 8.3.2 | Build система |
| **Min SDK** | 24 (Android 7.0) | Минимална поддръжка |
| **Target SDK** | 35 (Android 15) | Целева версия |
| **Room** | 2.6.1 | Локална база данни |
| **Retrofit** | 2.11.0 | REST клиент за TMDB API |
| **OkHttp Logging** | 4.12.0 | HTTP логване |
| **Glide** | 4.16.0 | Зареждане на изображения |
| **Navigation Component** | 2.7.7 | Навигация между екрани |
| **Safe Args** | 2.7.7 | Type-safe навигация |
| **ViewModel + LiveData** | 2.8.2 | MVVM слой |
| **Coroutines** | 1.8.0 | Асинхронни операции |
| **Material 3** | 1.12.0 | UI компоненти |
| **Mockito Kotlin** | 5.2.1 | Unit тестове |
| **Espresso** | 3.6.1 | UI тестове |

---

## Стъпки за стартиране

### Предварителни изисквания

- **Android Studio** Hedgehog или по-нов
- **JDK 11+**
- **TMDB API ключ** — безплатен от [themoviedb.org](https://www.themoviedb.org/settings/api)

### Инсталация

**1. Клонирай репото:**

```bash
git clone https://github.com/<ФН>/MobileApps2025-<ФН>.git
cd MobileApps2025-<ФН>
```

**2. Добави API ключ** в `app/build.gradle.kts`:

```kotlin
defaultConfig {
    buildConfigField("String", "TMDB_API_KEY", "\"твоя_api_key_тук\"")
}
```

**3. Sync Gradle:**

```bash
./gradlew assembleDebug
```

**4. Пусни на емулатор или устройство:**

```bash
./gradlew installDebug
```

### Пускане на тестовете

**Unit тестове:**

```bash
./gradlew test
```

**UI тестове (Espresso):**

```bash
./gradlew connectedAndroidTest
```

---

## Тестови акаунти

Приложението използва **локална Room база данни** — няма предварително създадени акаунти. Регистрирай се директно в приложението.

**Пример за тестов акаунт:**

| Поле | Стойност |
|---|---|
| Име | Test User |
| Имейл | `test@movies.com` |
| Парола | `123456` |

> **Забележка:** Акаунтите се пазят локално на устройството. При деинсталиране на приложението данните се изтриват.

---

## Скрийншотове

| Splash Screen | Логин | Регистрация |
|---|---|---|
| ![Splash](docs/images/splash.png) | ![Login](docs/images/login.png) | ![Register](docs/images/register.png) |

| Популярни филми | Детайли | Любими |
|---|---|---|
| ![Home](docs/images/home.png) | ![Detail](docs/images/detail.png) | ![Favorites](docs/images/favorites.png) |

> Скрийншотовете се намират в [`/docs/images/`](docs/images/)

---

## APK

Готовият APK файл се намира в:

```
/apk/app-release.apk
```

**Инсталация чрез adb:**

```bash
adb install apk/app-release.apk
```

> Размер: < 10 MB | Min SDK: 24 | Target SDK: 35

---

## Тестово покритие

- **Unit тестове:** `MovieRepositoryTest.kt` — 10 теста покриващи Repository слоя
- **UI тест:** `LoginFragmentTest.kt` — 3 Espresso теста за Login екрана

```bash
# Генериране на coverage report
./gradlew testDebugUnitTest
```

---

## Автор

**Факултетен номер:** `2301681026`  
**Дисциплина:** Мобилни приложения — 2026  
**GitHub:** [MobileApps2026-\2301681026>](https://github.com/GeorgiK04/MobileApps2026-2301681026)
