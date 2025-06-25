CREATE DATABASE IF NOT EXISTS edutech_db;
CREATE USER 'edutech'@'localhost' IDENTIFIED BY 'admin123!';
GRANT ALL PRIVILEGES ON edutech_db.* TO 'edutech'@'localhost';
FLUSH PRIVILEGES;