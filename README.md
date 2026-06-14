# my-blog-back-app
Бэкенд веб-приложения блога написан с использованием Spring Boot, выполняющегося во встроенном сервлет-контейне Tomcat.

## ️ База данных
База данных - PostgreSQL.
База данных инициализируется через SQL-скрипт.

## ️ Сборка проекта
Проект собирается с помощью Maven:
`
mvn clean package
`
После сборки артефакт (.jar) появится в target/.

## Запуск
Maven:
`mvn spring-boot:run`

Бэкенд доступен по адресу:
http://localhost:8080/

## Тестирование
Тесты используют JUnit 5 и Spring TestContext Framework.
Тестовая база PostgreSQL с использованием контейнирования.
Для запуска тестов необходим docker.

Запуск юнит и интеграционных тестов:
`mvn test`

# API Endpoints

Base URL: `/api/posts`

## Posts

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/posts` | Get paginated posts with search |
| GET | `/api/posts/{postId}` | Get post by ID |
| POST | `/api/posts` | Create new post |
| PUT | `/api/posts/{postId}` | Update post |
| DELETE | `/api/posts/{postId}` | Delete post |
| PUT | `/api/posts/{postId}/likes` | Add like to post |
| PUT | `/api/posts/{postId}/image` | Upload image for post (multipart/form-data) |
| GET | `/api/posts/{postId}/image` | Get post image (JPEG) |

## Comments

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/posts/{postId}/comments` | Get all comments for post |
| GET | `/api/posts/{postId}/comments/{commentId}` | Get comment by ID |
| POST | `/api/posts/{postId}/comments` | Create comment |
| PUT | `/api/posts/{postId}/comments/{commentId}` | Update comment |
| DELETE | `/api/posts/{postId}/comments/{commentId}` | Delete comment |