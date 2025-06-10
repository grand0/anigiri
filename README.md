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

Известные проблемы:
- По какой-то причине Compose добавляет высоту системных компонентов, находящихся внизу экрана (панель с кнопками, клавиатура) к верхнему внутреннему отступу Scaffold, при условии что у него задан topBar. Из-за этого списки могут выглядеть спущенными вниз. Я попробовал исправить это костылём в SearchScreen, но некоторые проблемы всё равно остались и в других местах уже не пытался чинить. Также это чинится если убрать edge-to-edge.

Скриншоты:

<img src="https://github.com/user-attachments/assets/ee1291da-1478-4fe0-8b15-f02c985974fc" alt="Главная страница" width="200"/>
<img src="https://github.com/user-attachments/assets/571b7197-d8af-4c2c-b6bd-da7dcf0db0a2" alt="Поиск" width="200"/>
<img src="https://github.com/user-attachments/assets/97d44312-bcd3-4e22-803b-c459c76bcea7" alt="Фильтры" width="200"/>
<img src="https://github.com/user-attachments/assets/f068f2f8-d13a-47a6-a844-a85ac4d158ff" alt="Страница релиза" width="200"/>
<img src="https://github.com/user-attachments/assets/5d2134b4-3c8b-4606-a83c-c21d7c9ce1cc" alt="Избранное" width="200"/>
<img src="https://github.com/user-attachments/assets/5be5c535-aa2e-4cd5-8266-aa61945ca6a9" alt="Профиль" width="200"/>
<img src="https://github.com/user-attachments/assets/5fda5255-aeda-41f4-9e90-788c4e7a6149" alt="Страница входа" width="200"/>