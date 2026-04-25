-- Database creation
CREATE DATABASE IF NOT EXISTS smart_campus_companion;
USE smart_campus_companion;

-- 1. Users Table (Aligned with Laravel and App)
CREATE TABLE users (
  id bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  name varchar(255) NOT NULL,
  username varchar(255) DEFAULT NULL,
  email varchar(255) NOT NULL,
  email_verified_at timestamp NULL DEFAULT NULL,
  password varchar(255) NOT NULL,
  role varchar(255) DEFAULT 'Student',
  remember_token varchar(100) DEFAULT NULL,
  created_at timestamp NULL DEFAULT NULL,
  updated_at timestamp NULL DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY users_email_unique (email),
  UNIQUE KEY users_username_unique (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2. Personal Access Tokens (Required for Laravel Sanctum)
CREATE TABLE personal_access_tokens (
  id bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  tokenable_type varchar(255) NOT NULL,
  tokenable_id bigint UNSIGNED NOT NULL,
  name varchar(255) NOT NULL,
  token varchar(64) NOT NULL,
  abilities text DEFAULT NULL,
  last_used_at timestamp NULL DEFAULT NULL,
  expires_at timestamp NULL DEFAULT NULL,
  created_at timestamp NULL DEFAULT NULL,
  updated_at timestamp NULL DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY personal_access_tokens_token_unique (token),
  KEY personal_access_tokens_tokenable_type_tokenable_id_index (tokenable_type, tokenable_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3. Tasks Table (Merged from Laravel Task model and Android Task model)
CREATE TABLE tasks (
  id bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  user_id bigint UNSIGNED NOT NULL,
  title varchar(255) NOT NULL,
  description text DEFAULT NULL,
  status varchar(255) DEFAULT 'pending',
  is_completed tinyint(1) NOT NULL DEFAULT 0,
  due_date datetime DEFAULT NULL,
  created_at timestamp NULL DEFAULT NULL,
  updated_at timestamp NULL DEFAULT NULL,
  PRIMARY KEY (id),
  KEY tasks_user_id_foreign (user_id),
  CONSTRAINT tasks_user_id_foreign FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 4. Announcements Table
CREATE TABLE announcements (
  id bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  title varchar(255) NOT NULL,
  content text NOT NULL,
  category varchar(255) DEFAULT 'General',
  is_important tinyint(1) NOT NULL DEFAULT 0,
  is_read tinyint(1) NOT NULL DEFAULT 0,
  created_at timestamp NULL DEFAULT NULL,
  updated_at timestamp NULL DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 5. Departments Table
CREATE TABLE departments (
  id bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  name varchar(255) NOT NULL,
  description text DEFAULT NULL,
  contact_email varchar(255) DEFAULT NULL,
  contact_phone varchar(255) DEFAULT NULL,
  created_at timestamp NULL DEFAULT NULL,
  updated_at timestamp NULL DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 6. Password Reset Tokens (Standard Laravel)
CREATE TABLE password_reset_tokens (
  email varchar(255) NOT NULL,
  token varchar(255) NOT NULL,
  created_at timestamp NULL DEFAULT NULL,
  PRIMARY KEY (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 7. Sessions Table
CREATE TABLE sessions (
  id varchar(255) NOT NULL,
  user_id bigint UNSIGNED DEFAULT NULL,
  ip_address varchar(45) DEFAULT NULL,
  user_agent text DEFAULT NULL,
  payload longtext NOT NULL,
  last_activity int NOT NULL,
  PRIMARY KEY (id),
  KEY sessions_user_id_index (user_id),
  KEY sessions_last_activity_index (last_activity)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;