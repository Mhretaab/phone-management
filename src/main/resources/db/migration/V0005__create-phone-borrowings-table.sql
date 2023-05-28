CREATE TABLE IF NOT EXISTS phone_borrowings (
  id INT AUTO_INCREMENT PRIMARY KEY,
  phone_id INT NOT NULL,
  tester_id INT NOT NULL,
  borrowed_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  returned_date DATETIME NULL,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  version INT NOT NULL DEFAULT 0,

  INDEX index_phone_returned_date (phone_id, returned_date),

  CONSTRAINT fk_phone_id FOREIGN KEY (phone_id) REFERENCES phones(id),
  CONSTRAINT fk_tester_id FOREIGN KEY (tester_id) REFERENCES users(id)
);


