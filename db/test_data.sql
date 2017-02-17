DELETE FROM EXECUTION;
DELETE FROM AGENT;
DELETE FROM TASK;

INSERT INTO AGENT (URI, tags) VALUES ('gb://localhost:8333/client', '{Windows, VS2010, Java 8}');
INSERT INTO AGENT (URI, tags) VALUES ('gb://192.168.0.1:8080/client', '{like, python3, node}');
INSERT INTO AGENT (URI, tags) VALUES ('gb://batch-server.kimleo.net:8080/client', '{batch, deploy}');
INSERT INTO AGENT (URI) VALUES ('gb://sql.kimleo.net:8333/client');
INSERT INTO AGENT (URI) VALUES ('gb://xiaomiwifi:8000/client');

INSERT INTO TASK (tasktype, target) VALUES ('RETRIEVE', '/bin/bash');
INSERT INTO TASK (tasktype, target, payload, data_type) VALUES ('PUSH', '/tmp/postgrest-0.4.0.0.tar.bz2', 'hello world', 'text/plain');
INSERT INTO TASK (tasktype, target) VALUES ('EXECUTE', '/bin/bash -c "echo hello world"');
INSERT INTO TASK (tasktype, target) VALUES ('RETRIEVE', '/var/log/nginx/2016-12-24/access.log');
INSERT INTO TASK (tasktype, target, payload, data_type) VALUES ('PUSH', '/bin/bash', '', 'application/executable;action=delete');


UPDATE TASK SET TAGS = array_cat(TAGS, '{unix-like}');

UPDATE AGENT SET TAGS = array_cat(TAGS, '{unix-like}') WHERE URI LIKE '%8333%';

INSERT INTO EXECUTION (task_id, agent_id, status)
SELECT max(t.ID), a.ID , 'PENDING' FROM TASK t, AGENT a
WHERE t.DATA_TYPE != '' AND a.URI LIKE '%kimleo%' AND a.TAGS @> t.TAGS
GROUP BY a.ID;

UPDATE TASK SET STATUS = 'PENDING' WHERE ID IN (SELECT TASK_ID FROM EXECUTION WHERE STATUS = 'PENDING');
