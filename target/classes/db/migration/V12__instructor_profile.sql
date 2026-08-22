-- Add INSTRUCTOR to User Role (PostgreSQL handles this via STRING enum natively)
ALTER TABLE users ADD COLUMN IF NOT EXISTS is_instructor BOOLEAN DEFAULT FALSE;

-- Instructor profile table
CREATE TABLE IF NOT EXISTS instructor_profiles (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    job_title VARCHAR(200),
    short_bio VARCHAR(500),
    bio TEXT,
    specialization VARCHAR(200),
    years_experience INTEGER DEFAULT 0,
    profile_image_url TEXT,
    linkedin_url TEXT,
    github_url TEXT,
    facebook_url TEXT,
    twitter_url TEXT,
    website_url TEXT,
    created_at TIMESTAMP DEFAULT NOW()
);

-- Link courses to instructor username for profile lookup
ALTER TABLE courses ADD COLUMN IF NOT EXISTS instructor_username VARCHAR(100);
