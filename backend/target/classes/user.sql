INSERT INTO users (student_id, name, email, password, role, is_active, created_at, updated_at)
VALUES 
('6731503032', 'Wacharaphong Sutthiboriban', '6731503032@lamduan.mfu.ac.th', 'password', 'STUDENT', TRUE, NOW(), NOW());

INSERT INTO users (student_id, name, email, password, role, is_active, created_at, updated_at)
VALUES 
('ADMIN001', 'Manager', 'admin@example.com', 'adminpassword', 'MANAGER', TRUE, NOW(), NOW());

INSERT INTO users (student_id, name, email, password, role, is_active, created_at, updated_at)
VALUES 
('STU001', 'John Doe', 'john@example.com', 'password123', 'STUDENT', TRUE, NOW(), NOW());

INSERT INTO users (student_id, name, email, password, role, is_active, created_at, updated_at)
VALUES 
('STU002', 'Jane Smith', 'jane@example.com', 'password123', 'STUDENT', TRUE, NOW(), NOW());

INSERT INTO machines (machine_number, status, location, description, current_user_id, usage_start_time) 
VALUES ('001', 'Idle', 'Lamduan 3', 'Description', NULL, NULL);

INSERT INTO machines (machine_number, status, location, description, current_user_id, usage_start_time) 
VALUES ('002', 'Idle', 'Lamduan 3', 'Description', NULL, NULL);

INSERT INTO bookings (user_id, machine_id, booking_date, status, amount, service, created_at, updated_at)
VALUES 
(3, 1, '2025-10-28 10:00:00', 'CONFIRMED', 5.50, 'Washing', NOW(), NOW());

INSERT INTO bookings (user_id, machine_id, booking_date, status, amount, service, created_at, updated_at)
VALUES 
(3, 2, '2025-10-28 14:00:00', 'PENDING', 5.50, 'Washing', NOW(), NOW());

INSERT INTO bookings (user_id, machine_id, booking_date, status, amount, service, created_at, updated_at)
VALUES 
(4, 1, '2025-10-29 09:00:00', 'CONFIRMED', 5.50, 'Washing', NOW(), NOW());

INSERT INTO bookings (user_id, machine_id, booking_date, status, amount, service, created_at, updated_at)
VALUES 
(4, 3, '2025-10-28 16:00:00', 'CANCELLED', 3.75, 'Drying', NOW(), NOW());
