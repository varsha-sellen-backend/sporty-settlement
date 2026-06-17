-- Test data covers two scenarios: correct prediction (WON) and wrong prediction (LOST)
-- Seed data: sample bets across two events
-- EVT-001: football match — TEAM_A_WIN / DRAW / TEAM_B_WIN
-- EVT-002: tennis match — PLAYER_A_WIN / PLAYER_B_WIN

INSERT INTO bets (event_id, user_id, predicted_outcome, status, amount, placed_at) VALUES
('EVT-001', 'user-101', 'TEAM_A_WIN',  'PENDING', 50.00,  '2024-06-10 10:00:00'),
('EVT-001', 'user-102', 'DRAW',        'PENDING', 25.00,  '2024-06-10 10:15:00'),
('EVT-001', 'user-103', 'TEAM_B_WIN',  'PENDING', 100.00, '2024-06-10 10:30:00'),
('EVT-001', 'user-104', 'TEAM_A_WIN',  'PENDING', 75.00,  '2024-06-10 10:45:00'),
('EVT-002', 'user-105', 'PLAYER_A_WIN','PENDING', 30.00,  '2024-06-10 11:00:00'),
('EVT-002', 'user-106', 'PLAYER_B_WIN','PENDING', 60.00,  '2024-06-10 11:10:00');
