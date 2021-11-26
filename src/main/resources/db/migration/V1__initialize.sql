SET NAMES 'utf8';
SET CHARACTER SET 'utf8';

CREATE TABLE user
(
    user_id    INT AUTO_INCREMENT PRIMARY KEY,
    email      VARCHAR(50) UNIQUE               NOT NULL,
    password   VARCHAR(128)                     NOT NULL,
    first_name VARCHAR(50)                      NOT NULL,
    last_name  VARCHAR(50)                      NOT NULL,
    phone      VARCHAR(12) UNIQUE               NOT NULL,
    role       ENUM ('USER', 'MANAGER','ADMIN') NOT NULL DEFAULT 'USER',
    status     ENUM ('ACTIVE','DEACTIVATED')    NOT NULL DEFAULT 'ACTIVE',
    about      TINYTEXT
);

CREATE TABLE room
(
    room_id        INT AUTO_INCREMENT PRIMARY KEY,
    number         INT UNIQUE,
    adult_capacity TINYINT UNSIGNED                               NOT NULL DEFAULT 0,
    child_capacity TINYINT UNSIGNED                               NOT NULL DEFAULT 0,
    price          DECIMAL(7, 2),
    bed_size       ENUM ('KING', 'QUEEN','TWIN', 'DOUBLE', 'COT') NOT NULL DEFAULT 'COT',
    about          TINYTEXT
);

CREATE TABLE room_image
(
    room_image_id INT AUTO_INCREMENT PRIMARY KEY,
    room_id       INT,
    caption       VARCHAR(50) NOT NULL,
    mime          VARCHAR(50) NOT NULL,
    image         LONGBLOB    NOT NULL,
    FOREIGN KEY (room_id) REFERENCES room (room_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE room_request
(
    room_request_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id         INT,
    status          ENUM ('REQUESTED', 'ASSIGNED', 'APPROVED', 'DECLINED', 'EXPIRED'),
    bed_size        ENUM ('KING', 'QUEEN','TWIN', 'DOUBLE', 'COT') NOT NULL DEFAULT 'COT',
    adult_capacity  TINYINT UNSIGNED                               NOT NULL DEFAULT 0,
    child_capacity  TINYINT UNSIGNED                               NOT NULL DEFAULT 0,
    arrival_date    DATE,
    departure_date  DATE,
    FOREIGN KEY (user_id) REFERENCES user (user_id) ON DELETE CASCADE ON UPDATE CASCADE

);

CREATE TABLE room_order
(
    room_order_id   INT AUTO_INCREMENT PRIMARY KEY,
    user_id         INT,
    room_id         INT,
    room_request_id INT,
    order_status    ENUM ('OFFERED', 'PENDING_PAYMENT', 'BOOKED', 'EXPIRED', 'INACCESSIBLE'),
    order_date      DATETIME,
    arrival_date    DATE,
    departure_date  DATE,
    price           DOUBLE(7, 2),
    FOREIGN KEY (user_id) REFERENCES user (user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (room_request_id) REFERENCES room_request (room_request_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (room_id) REFERENCES room (room_id) ON DELETE CASCADE ON UPDATE CASCADE
);

