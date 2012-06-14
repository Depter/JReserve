CREATE SCHEMA RESERVE;

CREATE TABLE RESERVE.LOB (
    ID BIGINT NOT NULL PRIMARY KEY,
    NAME VARCHAR(64) NOT NULL,
    LONG_NAME VARCHAR(255) NOT NULL
);

CREATE TABLE RESERVE.CLAIM_TYPE (
    ID BIGINT NOT NULL PRIMARY KEY,
    LOB_ID BIGINT NOT NULL,
    NAME VARCHAR(64) NOT NULL,
    LONG_NAME VARCHAR(255) NOT NULL
);

ALTER TABLE RESERVE.CLAIM_TYPE 
ADD FOREIGN KEY (LOB_ID)
REFERENCES RESERVE.LOB(ID);

CREATE TABLE RESERVE.DATA_TYPE (
    ID BIGINT NOT NULL PRIMARY KEY,
    NAME VARCHAR(64) NOT NULL,
    IS_TRIANGLE BOOLEAN NOT NULL
);

CREATE TABLE RESERVE.CLAIM (
    ID BIGINT NOT NULL PRIMARY KEY,
    LOB_ID BIGINT NOT NULL,
    ACCIDENT_DATE DATE NOT NULL,
    REPORTING_DATE DATE NOT NULL
);

ALTER TABLE RESERVE.CLAIM
ADD FOREIGN KEY (LOB_ID)
REFERENCES RESERVE.LOB(ID);

CREATE TABLE RESERVE.CLAIM_MUTATION (
    CLAIM_ID BIGINT NOT NULL,
    MUTATION_DATE DATE NOT NULL,
    CLAIM_TYPE_ID BIGINT NOT NULL,
    DATA_TYPE_ID BIGINT NOT NULL,
    PAYMENT DOUBLE NOT NULL,
    RESERVE_CHANGE DOUBLE NOT NULL,
    PRIMARY KEY(CLAIM_ID, MUTATION_DATE, CLAIM_TYPE_ID, DATA_TYPE_ID)
);

ALTER TABLE RESERVE.CLAIM_MUTATION
ADD FOREIGN KEY (CLAIM_ID)
REFERENCES RESERVE.CLAIM(ID);

ALTER TABLE RESERVE.CLAIM_MUTATION
ADD FOREIGN KEY (CLAIM_TYPE_ID)
REFERENCES RESERVE.CLAIM_TYPE(ID);

ALTER TABLE RESERVE.CLAIM_MUTATION
ADD FOREIGN KEY (DATA_TYPE_ID)
REFERENCES RESERVE.DATA_TYPE(ID);

CREATE TABLE RESERVE.PROJECT (
    ID BIGINT NOT NULL PRIMARY KEY,
    LOB_ID BIGINT NOT NULL,
    NAME VARCHAR(64) NOT NULL,
    DESCRIPTION LONG VARCHAR
);

ALTER TABLE RESERVE.PROJECT
ADD FOREIGN KEY (LOB_ID)
REFERENCES RESERVE.LOB(ID);

CREATE TABLE RESERVE.TRIANGLE (
    ID BIGINT NOT NULL PRIMARY KEY,
    PROJECT_ID BIGINT NOT NULL,
    LOB_ID BIGINT NOT NULL,
    CLAIM_TYPE_ID BIGINT NOT NULL,
    DATA_TYPE_ID BIGINT NOT NULL,
    NAME VARCHAR(64) NOT NULL,
    START_DATE DATE NOT NULL,
    END_DATE DATE NOT NULL,
    MONTH_IN_DEVELOPMENT SMALLINT NOT NULL,
    MONTH_IN_ACCIDDENT SMALLINT NOT NULL
);

ALTER TABLE RESERVE.TRIANGLE
ADD FOREIGN KEY (PROJECT_ID)
REFERENCES RESERVE.PROJECT(ID);

ALTER TABLE RESERVE.TRIANGLE
ADD FOREIGN KEY (LOB_ID)
REFERENCES RESERVE.LOB(ID);

ALTER TABLE RESERVE.TRIANGLE
ADD FOREIGN KEY (CLAIM_TYPE_ID)
REFERENCES RESERVE.CLAIM_TYPE(ID);

ALTER TABLE RESERVE.TRIANGLE
ADD FOREIGN KEY (DATA_TYPE_ID)
REFERENCES RESERVE.DATA_TYPE(ID);

CREATE TABLE RESERVE.TRIANGLE_CORRECTION (
    TRIANGLE_ID BIGINT NOT NULL,
    ACCIDENT_DATE DATE NOT NULL,
    DEVELOPMENT_DATE DATE NOT NULL,
    CORRECTION DOUBLE NOT NULL,
    PRIMARY KEY (TRIANGLE_ID, ACCIDENT_DATE, DEVELOPMENT_DATE)
);

ALTER TABLE RESERVE.TRIANGLE_CORRECTION
ADD FOREIGN KEY (TRIANGLE_ID)
REFERENCES RESERVE.TRIANGLE(ID);

CREATE TABLE RESERVE.TRIANGLE_COMMENT (
    ID BIGINT NOT NULL PRIMARY KEY,
    TRIANGLE_ID BIGINT NOT NULL,
    ACCIDENT_DATE DATE NOT NULL,
    DEVELOPMENT_DATE DATE NOT NULL,
    USER_NAME VARCHAR(64) NOT NULL,
    TIME TIMESTAMP NOT NULL,
    COMMENT LONG VARCHAR NOT NULL
);

ALTER TABLE RESERVE.TRIANGLE_COMMENT
ADD FOREIGN KEY (TRIANGLE_ID)
REFERENCES RESERVE.TRIANGLE(ID);

CREATE TABLE RESERVE.VECTOR (
    ID BIGINT NOT NULL PRIMARY KEY,
    PROJECT_ID BIGINT NOT NULL,
    LOB_ID BIGINT NOT NULL,
    CLAIM_TYPE_ID BIGINT NOT NULL,
    DATA_TYPE_ID BIGINT NOT NULL,
    NAME VARCHAR(64) NOT NULL,
    START_DATE DATE NOT NULL,
    END_DATE DATE NOT NULL,
    MONTH_IN_ACCIDDENT SMALLINT NOT NULL
);

ALTER TABLE RESERVE.VECTOR
ADD FOREIGN KEY (PROJECT_ID)
REFERENCES RESERVE.PROJECT(ID);

ALTER TABLE RESERVE.VECTOR
ADD FOREIGN KEY (LOB_ID)
REFERENCES RESERVE.LOB(ID);

ALTER TABLE RESERVE.VECTOR
ADD FOREIGN KEY (CLAIM_TYPE_ID)
REFERENCES RESERVE.CLAIM_TYPE(ID);

ALTER TABLE RESERVE.VECTOR
ADD FOREIGN KEY (DATA_TYPE_ID)
REFERENCES RESERVE.DATA_TYPE(ID);

CREATE TABLE RESERVE.VECTOR_COMMENT (
    ID BIGINT NOT NULL PRIMARY KEY,
    VECTOR_ID BIGINT NOT NULL,
    ACCIDENT_YEAR SMALLINT NOT NULL,
    ACCIDENT_MONTH SMALLINT NOT NULL,
    USER_NAME VARCHAR(64) NOT NULL,
    TIME TIMESTAMP NOT NULL,
    COMMENT LONG VARCHAR NOT NULL
);

ALTER TABLE RESERVE.VECTOR_COMMENT
ADD FOREIGN KEY (VECTOR_ID)
REFERENCES RESERVE.VECTOR(ID);

CREATE TABLE RESERVE.VECTOR_VALUE (
    VECTOR_ID BIGINT NOT NULL,
    ACCIDENT_DATE DATE NOT NULL,
    ORIGINAL_VALUE DOUBLE NOT NULL,
    CORRIGATED_VALUE DOUBLE NOT NULL,
    PRIMARY KEY (VECTOR_ID, ACCIDENT_DATE)
);

ALTER TABLE RESERVE.VECTOR_VALUE
ADD FOREIGN KEY (VECTOR_ID)
REFERENCES RESERVE.VECTOR(ID);

CREATE TABLE RESERVE.CHANGE_LOG (
    ID BIGINT NOT NULL PRIMARY KEY,
    PROJECT_ID BIGINT NOT NULL,
    TIME TIMESTAMP NOT NULL,
    CHANGE LONG VARCHAR NOT NULL
);

ALTER TABLE RESERVE.CHANGE_LOG
ADD FOREIGN KEY (PROJECT_ID)
REFERENCES RESERVE.PROJECT(ID);

CREATE TABLE RESERVE.SEQUENCE_STORE (
	SEQUENCE_NAME VARCHAR(255) NOT NULL PRIMARY KEY,
	SEQUENCE_VALUE BIGINT NOT NULL
);

INSERT INTO RESERVE.SEQUENCE_STORE VALUES ('RESERVE.CHANGE_LOG.ID', 0);
INSERT INTO RESERVE.SEQUENCE_STORE VALUES ('RESERVE.CLAIM.ID', 0);
INSERT INTO RESERVE.SEQUENCE_STORE VALUES ('RESERVE.CLAIM_TYPE.ID', 0);
INSERT INTO RESERVE.SEQUENCE_STORE VALUES ('RESERVE.DATA_TYPE.ID', 0);
INSERT INTO RESERVE.SEQUENCE_STORE VALUES ('RESERVE.LOB.ID', 0);
INSERT INTO RESERVE.SEQUENCE_STORE VALUES ('RESERVE.PROJECT.ID', 0);
INSERT INTO RESERVE.SEQUENCE_STORE VALUES ('RESERVE.TRIANGLE.ID', 0);
INSERT INTO RESERVE.SEQUENCE_STORE VALUES ('RESERVE.TRIANGLE_COMMENT.ID', 0);
INSERT INTO RESERVE.SEQUENCE_STORE VALUES ('RESERVE.VECTOR.ID', 0);
INSERT INTO RESERVE.SEQUENCE_STORE VALUES ('RESERVE.VECTOR_COMMENT.ID', 0);
