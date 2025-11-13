INSERT INTO users (student_id, name, email, password, role, is_active, created_at, updated_at)
VALUES 
('6731503032', 'Wacharaphong Sutthiboriban', '6731503032@lamduan.mfu.ac.th', 'password', 'STUDENT', TRUE, NOW(), NOW()),
('ADMIN', 'Manager', 'admin@example.com', 'adminpassword', 'MANAGER', TRUE, NOW(), NOW());


INSERT INTO machines (machine_number, name, machine_type, brand, model, capacity, status, location, description, price_per_hour, price_per_day, current_user_id, usage_start_time, created_at, updated_at) 
VALUES 
('001', 'Washing Machine - High Speed', 'Washing Machine', 'LG', 'WM-2000', '8kg', 'AVAILABLE', 'Laundry Room A', 'High-speed washing machine with eco mode', 70, 15.00, NULL, NULL, NOW(), NOW()),
('002', 'Washing Machine - Standard', 'Washing Machine', 'Samsung', 'DRY-500', '6kg', 'AVAILABLE', 'Laundry Room A', 'Standard dryer machine with multiple heat settings', 60, 10.00, NULL, NULL, NOW(), NOW()),
('003', 'Washing Machine - Eco', 'Washing Machine', 'Electrolux', 'WM-E100', '7kg', 'AVAILABLE', 'Laundry Room B', 'Eco-friendly model', 40, 12.00, NULL, NULL, NOW(), NOW()),
('004', 'Washing Machine - Standard', 'Washing Machine', 'Samsung', 'WM-S700', '8kg', 'AVAILABLE', 'Laundry Room B', 'Standard workhorse machine', 50, 15.00, NULL, NULL, NOW(), NOW()),
('005', 'Washing Machine - Large', 'Washing Machine', 'LG', 'WM-L3000', '12kg', 'AVAILABLE', 'Laundry Room C', 'Large capacity for blankets', 70, 20.00, NULL, NULL, NOW(), NOW()),
('006', 'Washing Machine - Compact', 'Washing Machine', 'Toshiba', 'WM-C50', '5kg', 'AVAILABLE', 'Laundry Room C', 'Compact machine for small loads', 50, 10.00, NULL, NULL, NOW(), NOW());


