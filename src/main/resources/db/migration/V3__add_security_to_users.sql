ALTER TABLE users ADD COLUMN email VARCHAR(255);
ALTER TABLE users ADD COLUMN password VARCHAR(255);
ALTER TABLE users ADD COLUMN role VARCHAR(50) DEFAULT 'ROLE_USER';

-- Update existing records to ensure non-null constraints pass
-- Use a predictable email format for existing mock users
UPDATE users SET email = LOWER(COALESCE(first_name, 'user')) || '_' || id || '@example.com' WHERE email IS NULL;
-- Set a default password 'password' for all existing users
UPDATE users SET password = '$2a$10$wE0Wc9bX7gWbLz6iGg3lGukqJvJgT12/l3hR17p3k7/oE.aD.a/mO' WHERE password IS NULL;

-- Make columns NOT NULL after backfilling
ALTER TABLE users ALTER COLUMN email SET NOT NULL;
ALTER TABLE users ADD CONSTRAINT uk_users_email UNIQUE (email);
ALTER TABLE users ALTER COLUMN password SET NOT NULL;
