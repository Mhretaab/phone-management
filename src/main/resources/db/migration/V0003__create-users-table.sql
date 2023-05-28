CREATE TABLE IF NOT EXISTS users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  first_name VARCHAR(15) NOT NULL,
  last_name VARCHAR(15) NOT NULL,
  email VARCHAR(25) NOT NULL,
  phone_number VARCHAR(15) NOT NULL,
  role VARCHAR(20) NOT NULL,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  version INT NOT NULL DEFAULT 0,
  INDEX index_first_name_last_name (first_name, last_name),
  INDEX index_email_phone_number (email, phone_number)
);

ALTER TABLE users ADD CONSTRAINT unq_email_3TUcEQjUAg UNIQUE (email);
ALTER TABLE users ADD CONSTRAINT unq_phone_bmKD3y8sEH UNIQUE (phone_number);

