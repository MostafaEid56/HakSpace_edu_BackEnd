-- Allow phone_number to be NULL (name-only blacklist entries)
ALTER TABLE blacklist ALTER COLUMN phone_number DROP NOT NULL;

-- Allow full_name to be NULL (phone-only blacklist entries remain valid)
ALTER TABLE blacklist ALTER COLUMN full_name DROP NOT NULL;
