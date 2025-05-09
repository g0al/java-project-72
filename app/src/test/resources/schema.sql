DROP TABLE IF EXISTS url_checks;
DROP TABLE IF EXISTS urls;

CREATE TABLE urls (
  id                            BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name                          VARCHAR(255),
  created_at                    TIMESTAMP NOT NULL,
  CONSTRAINT pk_url PRIMARY KEY (id)
);

CREATE TABLE url_checks (
  id                            BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  status_code                   INTEGER NOT NULL,
  title                         VARCHAR(255),
  h1                            VARCHAR(255),
  description                   TEXT,
  url_id                        BIGINT NOT NULL,
  created_at                    TIMESTAMP NOT NULL,
  CONSTRAINT pk_url_checks PRIMARY KEY (id)
);

CREATE INDEX ix_url_check_url_id ON url_checks (url_id);
ALTER TABLE url_checks ADD CONSTRAINT fk_url_checks_url_id FOREIGN KEY (url_id) REFERENCES urls (id) ON DELETE RESTRICT ON UPDATE RESTRICT;