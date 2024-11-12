# Library
REST API for managing books and readers.

## Book API
| Method | Endpoint | Description |
| - | - | - |
| GET | /book/{id} | Returns a book with the given ID. |
| GET | /book | Retrieves a sorted and paginated list of books. Page can be configured with request params. |
| POST | /book | Creates a new book. Data passed through body.|
| PATCH | /book | Updates an existing book. Data passed through body.|
| DELETE | /book/{id} | Deletes the Book entity with the specified ID. |

## User API
| Method | Endpoint | Description |
| - | - | - |
| GET | /user/{id} | Retrieves a user with the specified ID. |
| GET | /user | Retrieves a sorted and paginated list of users. Page can be configured with request params. |
| POST | /user | Creates a new user. Data passed through body.|
| PATCH | /user | Updates an existing user. Data passed through body.|
| DELETE | /user/{id} | Deletes a user with the specified ID. |
| POST | /user/issue/{userId}/{bookId} | Issues a book to a user. |
| POST | /user/return/{userId}/{bookId} | Returns a book from a user. |
