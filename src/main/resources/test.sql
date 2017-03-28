INSERT INTO credential (id, email, password, role) VALUES (0, 'admin@unizar.es', 'password', 'admin');
INSERT INTO credential (id, email, password, role) VALUES (1, 'professor@unizar.es', 'password', 'professor');
INSERT INTO credential (id, email, password, role) VALUES (2, 'maintenance@unizar.es', 'password', 'maintenance');

INSERT INTO worker (id, email, name) VALUES (7, 'maintenance@unizar.es', 'Worker7');
INSERT INTO report (id, roomid, state, worker_id, description) VALUES (10, 'HD-403', 'INBOX', 7, 'Report1');
INSERT INTO report (id, roomid, state, worker_id, description) VALUES (11, 'HD-403', 'INBOX', 7, 'Report2');
INSERT INTO report (id, roomid, state, description) VALUES (12, 'HD-404', 'INBOX', 'Report3');
INSERT INTO report (id, roomid, state, description) VALUES (13, 'HD-404', 'INBOX', 'Report4');
INSERT INTO report (id, roomid, state, description) VALUES (14, 'HD-404', 'APPROVED', 'Report5');
INSERT INTO report (id, roomid, state, worker_id, description) VALUES (15, 'HD-404', 'INBOX', 7, 'Report6');
