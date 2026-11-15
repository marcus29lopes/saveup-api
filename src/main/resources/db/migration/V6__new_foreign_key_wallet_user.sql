ALTER TABLE wallet
ADD COLUMN user_id INTEGER not null ,
ADD CONSTRAINT fk_wallet_user
    FOREIGN KEY (user_id)
    REFERENCES api_user(id);
