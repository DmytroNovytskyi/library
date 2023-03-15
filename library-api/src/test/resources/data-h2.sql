INSERT INTO book(id, author, name, available)
VALUES (1001, 'Author1', 'Name1', 100);
INSERT INTO book(id, author, name, available)
VALUES (1002, 'Author2', 'Name2', 100);
INSERT INTO user(id, username, email, password)
VALUES (1001, 'username1', 'email1@gmail.com', 'password1!');
INSERT INTO user(id, username, email, password)
VALUES (1002, 'username2', 'email2@gmail.com', 'password2@');
INSERT INTO user_books(users_id, books_id)
VALUES (1002, 1002);