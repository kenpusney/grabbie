DROP SCHEMA PUBLIC CASCADE;

CREATE SCHEMA PUBLIC;

CREATE SEQUENCE PUBLIC.AGENT_ID_SEQ
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE TABLE PUBLIC.AGENT
(
    ID INTEGER NOT NULL DEFAULT nextval('AGENT_ID_SEQ'::regclass),
    URI VARCHAR(512) NOT NULL,
    LOGON_TIME TIMESTAMP DEFAULT now(),
    LAST_UPDATE_TIME TIMESTAMP DEFAULT now(),
    ACTIVE BOOLEAN NOT NULL DEFAULT FALSE,
    TAGS VARCHAR(36)[] NOT NULL DEFAULT '{}',
    CONSTRAINT "AGENT_PRIMARY_KEY" PRIMARY KEY (ID),
    CONSTRAINT "AGENT_URI_UNIQUE" UNIQUE (URI)
) WITH (OIDS=FALSE);

CREATE TYPE PUBLIC.TASK_TYPE_T AS ENUM ('PUSH', 'RETRIEVE', 'EXECUTE');

CREATE TYPE PUBLIC.RUNNING_STATUS_T AS ENUM ('NOT_STARTED', 'COMPLETED', 'FAILED', 'RESTARTED', 'PENDING');

CREATE VIEW PUBLIC.TASK_TYPE AS SELECT unnest(enum_range(NULL::TASK_TYPE_T)) AS TYPE;

CREATE VIEW PUBLIC.RUNNING_STATUS AS SELECT unnest(enum_range(NULL::RUNNING_STATUS_T)) AS STATUS;

CREATE INDEX INDEX_AGENT_TAGS ON AGENT USING GIN (TAGS);

CREATE SEQUENCE PUBLIC.TASK_ID_SEQ
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE TABLE PUBLIC.TASK
(
    ID INTEGER NOT NULL DEFAULT nextval('TASK_ID_SEQ'::regclass),
    TASKTYPE TASK_TYPE_T NOT NULL,
    TARGET VARCHAR(256),
    ARGUMENTS VARCHAR(256)[] NOT NULL DEFAULT '{}',
    PAYLOAD TEXT NOT NULL DEFAULT '',
    DATA_TYPE VARCHAR(128) NOT NULL DEFAULT 'text/plain',
    TAGS VARCHAR(36)[] NOT NULL DEFAULT '{}',
    LAST_UPDATED TIME NOT NULL DEFAULT now(),
    STATUS RUNNING_STATUS_T NOT NULL DEFAULT 'NOT_STARTED',
    CONSTRAINT "TASK_PRIMARY_KEY" PRIMARY KEY (ID)
) WITH (OIDS=FALSE);

CREATE INDEX INDEX_TASK_ARGUMENTS ON TASK USING GIN (ARGUMENTS);

CREATE INDEX INDEX_TASK_TAGS ON TASK USING GIN (TAGS);

CREATE SEQUENCE PUBLIC.EXECUTION_ID_SEQ
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE TABLE PUBLIC.EXECUTION
(
    ID INTEGER NOT NULL DEFAULT nextval('EXECUTION_ID_SEQ'::regclass),
    TASK_ID INTEGER NOT NULL REFERENCES TASK(ID),
    AGENT_ID INTEGER NOT NULL REFERENCES AGENT(ID),
    OUTPUT TEXT NOT NULL DEFAULT '',
    RESULT TEXT NOT NULL DEFAULT '',
    DATA_TYPE VARCHAR(128) NOT NULL DEFAULT 'text/plain', -- MIME
    LAST_UPDATED TIME NOT NULL DEFAULT now(),
    STATUS RUNNING_STATUS_T NOT NULL DEFAULT 'NOT_STARTED',

    CONSTRAINT "EXECUTION_PRIMARY_KEY" PRIMARY KEY (ID)
) WITH (OIDS=FALSE);


CREATE VIEW PUBLIC.AVAILABILITY AS
SELECT t.*, a.ID as agent_id, a.URI as agent_uri FROM TASK t, AGENT a
WHERE a.TAGS @> t.TAGS
    AND t.STATUS != 'PENDING'
    AND NOT (a.ID IN (SELECT ex.AGENT_ID FROM EXECUTION ex WHERE ex.STATUS = 'PENDING'));
