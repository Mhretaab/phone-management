CREATE TABLE IF NOT EXISTS phones (
  id INT AUTO_INCREMENT PRIMARY KEY,
  brand VARCHAR(25) NOT NULL,
  model VARCHAR(25) NOT NULL,
  assigned_id VARCHAR(25) NOT NULL UNIQUE,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  version INT NOT NULL DEFAULT 0,
  INDEX index_assigned_id (assigned_id),
  INDEX index_brand_model (brand, model)
);