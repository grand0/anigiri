# Anigiri
Приложение для поиска и просмотра аниме (пока без просмотра).

API: [AniLibria](https://anilibria.top/api/docs/v1#/) - включает в себя авторизацию.

Функционал:
- Просмотр последних появившихся озвучек командой AniLibria
- Поиск тайтлов по названию и различным фильтрам
- Просмотр информации о тайтле
- Вход в аккаунт AniLibria
- Сохранение тайтлов в избранное

Стек:
- Jetpack Compose
- Orbit MVI
- Room
- Ktor (+ kotlin-serialization)
- Coil
- Voyager
- Koin

Подключены:
- Firebase Crashlytics
- Firebase Analytics
- Firebase Performance

CI/CD:
- запуск detekt и юнит-тестов (на pull request и push в master)
- Firebase App Distribution (action запускается на push тега)

Приглашение в группу тестировщиков, первый релиз уже есть там: https://appdistribution.firebase.dev/i/18ec3aba38cd97b1

Юнит-тесты написаны на все use-case.
