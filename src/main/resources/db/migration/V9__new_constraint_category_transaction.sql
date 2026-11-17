ALTER TABLE transaction
ADD COLUMN category_id INTEGER NOT NULL,
    ADD CONSTRAINT fk_category_id
    FOREIGN KEY (category_id) REFERENCES categories(id);
