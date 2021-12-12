CREATE TABLE language (
	id SERIAL PRIMARY KEY,
	language_name VARCHAR(100) UNIQUE
);

CREATE TABLE genre (
	id SERIAL PRIMARY KEY,
	genre_name VARCHAR(100) UNIQUE
);

CREATE TABLE movie (
	id SERIAL PRIMARY KEY,
	title VARCHAR(256) NOT NULL,
	duration_mins INT NOT NULL,
	genre_id INT NOT NULL,
	synopsis VARCHAR(256) NOT NULL,
	language_id INT NOT NULL,
	release_date DATE NOT NULL,
	CONSTRAINT fk_genre FOREIGN KEY(genre_id) REFERENCES genre(id),
	CONSTRAINT fk_language FOREIGN KEY(language_id) REFERENCES language(id)
);

CREATE TABLE actor (
	id SERIAL PRIMARY KEY,
	name VARCHAR(256) NOT NULL
);

CREATE TABLE movie_cast (
	movie_id INT NOT NULL,
	actor_id INT NOT NULL,
	PRIMARY KEY (movie_id, actor_id),
	CONSTRAINT fk_movie FOREIGN KEY(movie_id) REFERENCES movie(id),
	CONSTRAINT fk_actor FOREIGN KEY(actor_id) REFERENCES actor(id)
);

INSERT INTO language (language_name)
VALUES ('English'), ('Mandarin Chinese'), ('Hindi'), ('Spanish'), ('Arabic'), ('French'), ('Russian'), ('Portuguese'), ('Japanese'), ('Korean');

INSERT INTO genre (genre_name)
VALUES ('Action'), ('Romance'), ('Drama'), ('Thriller'), ('Comedy'), ('Animation');

INSERT INTO actor (name)
VALUES
('Tom Holland'), ('Zendaya'), ('Williem Dafoe'), ('Alfred Molina'), ('Jamie Foxx'), ('Maria Tomei'),
('Daniel Kaluuya'), ('Allison Williams'), ('Catherine Keener'), ('Lil Rel Howery'),
('Simu Liu'), ('Awkwafina'), ('Meng er Zhang'), ('Tony Leung'), ('Michelle Yeoh'),
('Gong Yoo'), ('Kim Su-an'), ('Ma Dong-seok'), ('Jung Yu-mi');

INSERT INTO movie (title, duration_mins, genre_id, synopsis, language_id, release_date)
VALUES
('Spider-Man: No Way Home', 148, 1, 'With Spider-Man identity now revealed, our friendly neighborhood web-slinger is unmasked and no longer able to separate his normal life as Peter Parker from the high stakes of being a superhero.', 1, '2021-12-16'),
('Get Out', 104, 4, 'Chris, an African-American man, decides to visit his Caucasian girlfriend parents during a weekend getaway. Although they seem normal at first, he is not prepared to experience the horrors ahead.', 1, '2017-03-16'),
('Shang-Chi and The Legend of The Ten Rings', 132, 1, 'Martial-arts master Shang-Chi confronts the past he thought he left behind when he is drawn into the web of the mysterious Ten Rings organization.', 1, '2021-09-02'),
('Train to Busan', 118, 1, 'Seok-woo and his daughter are on a train to Busan on her birthday to see his wife. However, the journey turns into a nightmare when they are trapped amidst a zombie outbreak in South Korea.', 9, '2016-09-04');

INSERT INTO movie_cast
VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6),
(2, 7), (2, 8), (2, 9), (2, 10),
(3, 11), (3, 12), (3, 13), (3, 14), (3, 15),
(4, 16), (4, 17), (4, 18), (4, 19);