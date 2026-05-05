ALTER TABLE users ADD COLUMN first_name VARCHAR(255);
ALTER TABLE users ADD COLUMN last_name VARCHAR(255);

-- Update users that have a space in their name
UPDATE users 
SET first_name = SUBSTRING(name FROM 1 FOR POSITION(' ' IN name) - 1),
    last_name = SUBSTRING(name FROM POSITION(' ' IN name) + 1)
WHERE POSITION(' ' IN name) > 0;

-- Update users without a space
UPDATE users 
SET first_name = name, 
    last_name = ''
WHERE POSITION(' ' IN name) = 0;

ALTER TABLE users DROP COLUMN name;
