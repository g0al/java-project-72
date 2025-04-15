DROP TABLE IF EXISTS url_checks;
DROP TABLE IF EXISTS urls;

CREATE TABLE urls (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP
);

CREATE TABLE url_checks (
    id INT PRIMARY KEY AUTO_INCREMENT,
    url_id INT,
    FOREIGN KEY (url_id) REFERENCES urls(id),
    status_code INT,
    h1 VARCHAR(255),
    title VARCHAR(255),
    description VARCHAR(255),
    created_at TIMESTAMP
);