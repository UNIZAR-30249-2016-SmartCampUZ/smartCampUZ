INSERT INTO credential (id, email, password, role) VALUES (0, 'admin@unizar.es', 'password', 'admin');
INSERT INTO credential (id, email, password, role) VALUES (1, 'professor@unizar.es', 'password', 'professor');
INSERT INTO credential (id, email, password, role) VALUES (2, 'maintenance@unizar.es', 'password', 'maintenance');

INSERT INTO worker (id, email, name) VALUES (7, 'maintenance@unizar.es', 'Worker7');
INSERT INTO report (id, roomid, state, worker_id) VALUES (10, 'HD-403', 'INBOX', 7);
INSERT INTO report (id, roomid, state, worker_id) VALUES (11, 'HD-403', 'INBOX', 7);
