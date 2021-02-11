/*memberships*/
INSERT INTO `la`.`membership` (`id`, `name`, `duration`, `price`) VALUES ('1','Basic','30','50');
INSERT INTO `la`.`membership` (`id`, `name`, `duration`, `price`) VALUES ('2','Premium','90','110');

/*users*/
INSERT INTO `la`.`users` (`type`, `id`, `city`, `state`, `email`, `active`, `password`, `username`, `last_name`, `first_name`, `deleted`) VALUES ('ADMINISTRATOR', '1', 'Novi Sad', 'Srbija', 'marko@gmail.com', b'1', '$2a$10$xBbFGBwJcF9H3V/s2GfcnuVpM9niJ9oVrhY6CQjrrHZJYzYA6Z5nS', 'admin', 'Markovic', 'Marko', b'0');
INSERT INTO `la`.`users` (`type`, `id`, `city`, `state`, `email`, `active`, `password`, `username`, `last_name`, `first_name`, `deleted`) VALUES ('BOARD_MEMBER', '2', 'Beograd', 'Srbija', 'jovan@gmail.com', b'1', '$2a$10$xBbFGBwJcF9H3V/s2GfcnuVpM9niJ9oVrhY6CQjrrHZJYzYA6Z5nS', 'boardmember', 'Jovic', 'Jovan', b'0');
INSERT INTO `la`.`users` (`type`, `id`, `city`, `state`, `email`, `active`, `password`, `username`, `last_name`, `first_name`, `deleted`) VALUES ('EDITOR', '3', 'Novi Sad', 'Srbija', 'jovana@gmail.com', b'1', '$2a$10$xBbFGBwJcF9H3V/s2GfcnuVpM9niJ9oVrhY6CQjrrHZJYzYA6Z5nS', 'editor', 'Jovanovic', 'Jovana', b'0');
INSERT INTO `la`.`users` (`type`, `id`, `city`, `state`, `email`, `active`, `password`, `username`, `last_name`, `first_name`, `deleted`, `membership_id`) VALUES ('WRITER', '4', 'Kragujevac', 'Srbija', 'jelena@gmail.com', b'1', '$2a$10$xBbFGBwJcF9H3V/s2GfcnuVpM9niJ9oVrhY6CQjrrHZJYzYA6Z5nS', 'writer', 'Jelic', 'Jelena', b'0', '1');
INSERT INTO `la`.`users` (`type`, `id`, `city`, `state`, `email`, `active`, `password`, `username`, `last_name`, `first_name`, `deleted`) VALUES ('LECTOR', '5', 'Indjija', 'Srbija', 'igor@gmail.com', b'1', '$2a$10$xBbFGBwJcF9H3V/s2GfcnuVpM9niJ9oVrhY6CQjrrHZJYzYA6Z5nS', 'lector', 'Malesevic', 'Igor', b'0');
INSERT INTO `la`.`users` (`type`, `id`, `city`, `state`, `email`, `active`, `password`, `username`, `last_name`, `first_name`, `deleted`, `membership_id`, `beta`) VALUES ('READER', '6', 'Backa Palanka', 'Srbija', 'ivana@gmail.com', b'1', '$2a$10$xBbFGBwJcF9H3V/s2GfcnuVpM9niJ9oVrhY6CQjrrHZJYzYA6Z5nS', 'reader', 'Brkic', 'Ivana', b'0', '2', b'0');
INSERT INTO `la`.`users` (`type`, `id`, `city`, `state`, `email`, `active`, `password`, `username`, `last_name`, `first_name`, `deleted`, `activated`, `payment_deadline`) VALUES ('WRITER_MEMBERSHIP_REQUEST', '7', 'Novi Sad', 'Srbija', 'ana@gmail.com', b'1', '$2a$10$xBbFGBwJcF9H3V/s2GfcnuVpM9niJ9oVrhY6CQjrrHZJYzYA6Z5nS', 'writermembershiprequest', 'Anic', 'Ana', b'0', b'0', '2020-12-12');
INSERT INTO `la`.`users` (`type`, `id`, `city`, `state`, `email`, `active`, `password`, `username`, `last_name`, `first_name`, `deleted`, `beta`) VALUES ('READER', '8', 'Subotica', 'Srbija', 'boza@gmail.com', b'1', '$2a$10$xBbFGBwJcF9H3V/s2GfcnuVpM9niJ9oVrhY6CQjrrHZJYzYA6Z5nS', 'bozabozic', 'Bozic', 'Boza', b'0', b'0');

-- Adding users soon to be writers
INSERT INTO `la`.`users` (`type`, `city`, `state`, `email`, `active`, `password`, `username`, `last_name`, `first_name`, `deleted`) VALUES ('WRITER', 'Subotica', 'Srbija', 'w1@gmail.com', b'1', '$2a$10$xBbFGBwJcF9H3V/s2GfcnuVpM9niJ9oVrhY6CQjrrHZJYzYA6Z5nS', 'w1', 'Cornwell', 'Bernard ', b'0');
INSERT INTO `la`.`users` (`type`, `city`, `state`, `email`, `active`, `password`, `username`, `last_name`, `first_name`, `deleted`) VALUES ('WRITER', 'Subotica', 'Srbija', 'w2@gmail.com', b'1', '$2a$10$xBbFGBwJcF9H3V/s2GfcnuVpM9niJ9oVrhY6CQjrrHZJYzYA6Z5nS', 'w2', 'Gilbert', 'Elizabeth ', b'0');
INSERT INTO `la`.`users` (`type`, `city`, `state`, `email`, `active`, `password`, `username`, `last_name`, `first_name`, `deleted`) VALUES ('WRITER', 'Subotica', 'Srbija', 'w3@gmail.com', b'1', '$2a$10$xBbFGBwJcF9H3V/s2GfcnuVpM9niJ9oVrhY6CQjrrHZJYzYA6Z5nS', 'w3', 'Coben', 'Harlan ', b'0');
INSERT INTO `la`.`users` (`type`, `city`, `state`, `email`, `active`, `password`, `username`, `last_name`, `first_name`, `deleted`) VALUES ('WRITER', 'Subotica', 'Srbija', 'w4@gmail.com', b'1', '$2a$10$xBbFGBwJcF9H3V/s2GfcnuVpM9niJ9oVrhY6CQjrrHZJYzYA6Z5nS', 'w4', 'de Quincey', 'Thomas ', b'0');
INSERT INTO `la`.`users` (`type`, `city`, `state`, `email`, `active`, `password`, `username`, `last_name`, `first_name`, `deleted`) VALUES ('WRITER', 'Subotica', 'Srbija', 'w5@gmail.com', b'1', '$2a$10$xBbFGBwJcF9H3V/s2GfcnuVpM9niJ9oVrhY6CQjrrHZJYzYA6Z5nS', 'w5', 'King', 'Stephen ', b'0');
INSERT INTO `la`.`users` (`type`, `city`, `state`, `email`, `active`, `password`, `username`, `last_name`, `first_name`, `deleted`) VALUES ('WRITER', 'Subotica', 'Srbija', 'w6@gmail.com', b'1', '$2a$10$xBbFGBwJcF9H3V/s2GfcnuVpM9niJ9oVrhY6CQjrrHZJYzYA6Z5nS', 'w6', 'Ward', 'Jason ', b'0');
INSERT INTO `la`.`users` (`type`, `city`, `state`, `email`, `active`, `password`, `username`, `last_name`, `first_name`, `deleted`) VALUES ('WRITER', 'Subotica', 'Srbija', 'w7@gmail.com', b'1', '$2a$10$xBbFGBwJcF9H3V/s2GfcnuVpM9niJ9oVrhY6CQjrrHZJYzYA6Z5nS', 'w7', 'Sanderson', 'Brandon ', b'0');
INSERT INTO `la`.`users` (`type`, `city`, `state`, `email`, `active`, `password`, `username`, `last_name`, `first_name`, `deleted`) VALUES ('WRITER', 'Subotica', 'Srbija', 'w8@gmail.com', b'1', '$2a$10$xBbFGBwJcF9H3V/s2GfcnuVpM9niJ9oVrhY6CQjrrHZJYzYA6Z5nS', 'w8', 'Allende', 'Isabel ', b'0');

-- Adding beta readers
INSERT INTO `la`.`users` (`type`, `city`, `state`, `email`, `active`, `password`, `username`, `last_name`, `first_name`, `deleted`, `beta`, `penalty_points`) VALUES ('READER', 'Subotica', 'Srbija', 'reader1@gmail.com', b'1', '$2a$10$xBbFGBwJcF9H3V/s2GfcnuVpM9niJ9oVrhY6CQjrrHZJYzYA6Z5nS', 'reader1', 'Bozic', 'Boza', b'0', b'1', -2);
INSERT INTO `la`.`users` (`type`, `city`, `state`, `email`, `active`, `password`, `username`, `last_name`, `first_name`, `deleted`, `beta`, `penalty_points`) VALUES ('READER', 'Subotica', 'Srbija', 'reader2@gmail.com', b'1', '$2a$10$xBbFGBwJcF9H3V/s2GfcnuVpM9niJ9oVrhY6CQjrrHZJYzYA6Z5nS', 'reader2', 'Bozic', 'Boza', b'0', b'1', -3);
INSERT INTO `la`.`users` (`type`, `city`, `state`, `email`, `active`, `password`, `username`, `last_name`, `first_name`, `deleted`, `beta`, `penalty_points`) VALUES ('READER', 'Subotica', 'Srbija', 'reader3@gmail.com', b'1', '$2a$10$xBbFGBwJcF9H3V/s2GfcnuVpM9niJ9oVrhY6CQjrrHZJYzYA6Z5nS', 'reader3', 'Bozic', 'Boza', b'0', b'1', -4);

/*roles*/
INSERT INTO `la`.`role` (`id`, `name`) VALUES ('1', 'ADMIN');
INSERT INTO `la`.`role` (`id`, `name`) VALUES ('2', 'BOARD_MEMBER');
INSERT INTO `la`.`role` (`id`, `name`) VALUES ('3', 'EDITOR');
INSERT INTO `la`.`role` (`id`, `name`) VALUES ('4', 'WRITER');
INSERT INTO `la`.`role` (`id`, `name`) VALUES ('5', 'LECTOR');
INSERT INTO `la`.`role` (`id`, `name`) VALUES ('6', 'READER');
INSERT INTO `la`.`role` (`id`, `name`) VALUES ('7', 'WRITER_MEMBERSHIP_REQUEST');

/*sysUser-roles*/
INSERT INTO `la`.`user_roles` (`user_id`, `role_id`) VALUES ('1', '1');
INSERT INTO `la`.`user_roles` (`user_id`, `role_id`) VALUES ('2', '2');
INSERT INTO `la`.`user_roles` (`user_id`, `role_id`) VALUES ('3', '3');
INSERT INTO `la`.`user_roles` (`user_id`, `role_id`) VALUES ('4', '4');
INSERT INTO `la`.`user_roles` (`user_id`, `role_id`) VALUES ('5', '5');
INSERT INTO `la`.`user_roles` (`user_id`, `role_id`) VALUES ('6', '6');
INSERT INTO `la`.`user_roles` (`user_id`, `role_id`) VALUES ('7', '7');
INSERT INTO `la`.`user_roles` (`user_id`, `role_id`) VALUES ('8', '6');

-- Writers
INSERT INTO `la`.`user_roles` (`user_id`, `role_id`) VALUES ('9', '4');
INSERT INTO `la`.`user_roles` (`user_id`, `role_id`) VALUES ('10', '4');
INSERT INTO `la`.`user_roles` (`user_id`, `role_id`) VALUES ('11', '4');
INSERT INTO `la`.`user_roles` (`user_id`, `role_id`) VALUES ('12', '4');
INSERT INTO `la`.`user_roles` (`user_id`, `role_id`) VALUES ('13', '4');
INSERT INTO `la`.`user_roles` (`user_id`, `role_id`) VALUES ('14', '4');
INSERT INTO `la`.`user_roles` (`user_id`, `role_id`) VALUES ('15', '4');
INSERT INTO `la`.`user_roles` (`user_id`, `role_id`) VALUES ('16', '4');

-- Beta readers
INSERT INTO `la`.`user_roles` (`user_id`, `role_id`) VALUES ('17', '6');
INSERT INTO `la`.`user_roles` (`user_id`, `role_id`) VALUES ('18', '6');
INSERT INTO `la`.`user_roles` (`user_id`, `role_id`) VALUES ('19', '6');

/*permissions*/

/*role-permissions*/

/*genre*/
INSERT INTO `la`.`genre` (`id`, `value`) VALUES ('1', 'Horror');
INSERT INTO `la`.`genre` (`id`, `value`) VALUES ('2', 'Comedy');
INSERT INTO `la`.`genre` (`id`, `value`) VALUES ('3', 'Drama');

/*reader_genre*/
INSERT INTO `la`.`user_genre` (`user_id`, `genre_id`) VALUES ('6','1');
INSERT INTO `la`.`user_genre` (`user_id`, `genre_id`) VALUES ('6','2');
INSERT INTO `la`.`user_genre` (`user_id`, `genre_id`) VALUES ('8','1');
INSERT INTO `la`.`user_genre` (`user_id`, `genre_id`) VALUES ('8','3');

/*beta_reader_genre*/
INSERT INTO `la`.`beta_reader_genre` (`beta_reader_id`, `genre_id`) VALUES ('8','1');

INSERT INTO `la`.`beta_reader_genre` (`beta_reader_id`, `genre_id`) VALUES ('17','1');
INSERT INTO `la`.`beta_reader_genre` (`beta_reader_id`, `genre_id`) VALUES ('17','2');

INSERT INTO `la`.`beta_reader_genre` (`beta_reader_id`, `genre_id`) VALUES ('18','1');

INSERT INTO `la`.`beta_reader_genre` (`beta_reader_id`, `genre_id`) VALUES ('19','2');

-- Publishers
INSERT INTO `la`.`publisher` (`city`, `name`, `password`, `state`, `username`) VALUES ('Madrid', 'Vulkan', '$2a$10$xBbFGBwJcF9H3V/s2GfcnuVpM9niJ9oVrhY6CQjrrHZJYzYA6Z5nS', 'Spain', 'vulkan');

-- Books
INSERT INTO `la`.`book` (`isbn`, `pages_number`, `price`, `published_year`, `synopsis`, `title`, `editor_id`, `genre_id`, `lector_id`, `publisher_id`, `writer_id`) VALUES ('9781460758120', '112', '5.43', '2019', 'Hey there where ya goin’, not exactly knowin’, who says you have to call just one place home. He’s goin’ everywhere, B.J. McKay and his best friend Bear. He just keeps on movin’, ladies keep improvin’, every day is better than the last. New dreams and better scenes, and best of all I don’t pay property tax. Rollin’ down to Dallas, who’s providin’ my palace, off to New Orleans or who knows where. Places new and ladies, too, I’m B.J. McKay and this is my best friend Bear.', 'THE LAST KINGDOM', '3', '1', '5', '1', '9');
INSERT INTO `la`.`book` (`isbn`, `pages_number`, `price`, `published_year`, `synopsis`, `title`, `editor_id`, `genre_id`, `lector_id`, `publisher_id`, `writer_id`) VALUES ('9781460758121', '231', '10.23', '2020', 'Hong Kong Phooey, number one super guy. Hong Kong Phooey, quicker than the human eye. He’s got style, a groovy style, and a car that just won’t stop. When the going gets tough, he’s really rough, with a Hong Kong Phooey chop (Hi-Ya!). Hong Kong Phooey, number one super guy. Hong Kong Phooey, quicker than the human eye. Hong Kong Phooey, he’s fan-riffic!', 'CITY OF GIRLS', '3', '2', '5', '1', '10');
INSERT INTO `la`.`book` (`isbn`, `pages_number`, `price`, `published_year`, `synopsis`, `title`, `editor_id`, `genre_id`, `lector_id`, `publisher_id`, `writer_id`) VALUES ('9781460758122', '153', '9.99', '2021', 'Barnaby The Bear’s my name, never call me Jack or James, I will sing my way to fame, Barnaby the Bear’s my name. Birds taught me to sing, when they took me to their king, first I had to fly, in the sky so high so high, so high so high so high, so — if you want to sing this way, think of what you’d like to say, add a tune and you will see, just how easy it can be. Treacle pudding, fish and chips, fizzy drinks and liquorice, flowers, rivers, sand and sea, snowflakes and the stars are free.', 'THE BOY FROM THE WOODS', '3', '3', '5', '1', '11');
INSERT INTO `la`.`book` (`isbn`, `pages_number`, `price`, `published_year`, `synopsis`, `title`, `editor_id`, `genre_id`, `lector_id`, `publisher_id`, `writer_id`) VALUES ('9781460758123', '321', '6.43', '2018', 'Thundercats are on the move, Thundercats are loose. Feel the magic, hear the roar, Thundercats are loose. Thunder, thunder, thunder, Thundercats! Thunder, thunder, thunder, Thundercats! Thunder, thunder, thunder, Thundercats! Thunder, thunder, thunder, Thundercats! Thundercats!', 'CONFESSIONS OF AN ENGLISH OPIUM EATER', '3', '1', '5', '1', '12');
INSERT INTO `la`.`book` (`isbn`, `pages_number`, `price`, `published_year`, `synopsis`, `title`, `editor_id`, `genre_id`, `lector_id`, `publisher_id`, `writer_id`) VALUES ('9781460758124', '234', '23.33', '2018', 'One for all and all for one, Muskehounds are always ready. One for all and all for one, helping everybody. One for all and all for one, it’s a pretty story. Sharing everything with fun, that’s the way to be. One for all and all for one, Muskehounds are always ready. One for all and all for one, helping everybody. One for all and all for one, can sound pretty corny. If you’ve got a problem chum, think how it could be.', 'RITA HAYWORTH AND SHAWSHANK REDEMPTION', '3', '2', '5', '1', '13');
INSERT INTO `la`.`book` (`isbn`, `pages_number`, `price`, `published_year`, `synopsis`, `title`, `editor_id`, `genre_id`, `lector_id`, `publisher_id`, `writer_id`) VALUES ('9781460758125', '654', '54.55', '2020', 'Mutley, you snickering, floppy eared hound. When courage is needed, you’re never around. Those medals you wear on your moth-eaten chest should be there for bungling at which you are best. So, stop that pigeon, stop that pigeon, stop that pigeon, stop that pigeon, stop that pigeon, stop that pigeon, stop that pigeon. Howwww! Nab him, jab him, tab him, grab him, stop that pigeon now.', 'EDGAR ALLAN POE PUZZLE COLLECTION', '3', '3', '5', '1', '14');
INSERT INTO `la`.`book` (`isbn`, `pages_number`, `price`, `published_year`, `synopsis`, `title`, `editor_id`, `genre_id`, `lector_id`, `publisher_id`, `writer_id`) VALUES ('9781460758126', '66', '20', '2017', 'There’s a voice that keeps on calling me. Down the road, that’s where I’ll always be. Every stop I make, I make a new friend. Can’t stay for long, just turn around and I’m gone again. Maybe tomorrow, I’ll want to settle down, Until tomorrow, I’ll just keep moving on.', 'STARSIGHT', '3', '1', '5', '1', '15');
INSERT INTO `la`.`book` (`isbn`, `pages_number`, `price`, `published_year`, `synopsis`, `title`, `editor_id`, `genre_id`, `lector_id`, `publisher_id`, `writer_id`) VALUES ('9781460758127', '101', '24.99', '2000', 'Ulysses, Ulysses — Soaring through all the galaxies. In search of Earth, flying in to the night. Ulysses, Ulysses — Fighting evil and tyranny, with all his power, and with all of his might. Ulysses — no-one else can do the things you do. Ulysses — like a bolt of thunder from the blue. Ulysses — always fighting all the evil forces bringing peace and justice to all.', 'MAYAS NOTEBOOK', '3', '2', '5', '1', '16');
INSERT INTO `la`.`book` (`isbn`, `pages_number`, `price`, `published_year`, `synopsis`, `title`, `editor_id`, `genre_id`, `lector_id`, `publisher_id`, `writer_id`) VALUES ('9781460758128', '433', '80.67', '2003', 'Knight Rider, a shadowy flight into the dangerous world of a man who does not exist. Michael Knight, a young loner on a crusade to champion the cause of the innocent, the helpless in a world of criminals who operate above the law.', 'STOUNHENGE', '3', '3', '5', '1', '9');
