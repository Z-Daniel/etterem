CREATE SEQUENCE hibernate_sequence INCREMENT 1 CYCLE NO MINVALUE MAXVALUE 999999999999999;

CREATE TABLE dolgozo
(
    id integer NOT NULL,
    dolgozo_nev character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT dolgozo_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE dolgozo
    OWNER to postgres;

CREATE TABLE termek
(
    id integer NOT NULL,
    termek_ar integer,
    termek_nev character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT termek_pkey PRIMARY KEY (id),
    CONSTRAINT uk_een9sis590evedp43kv72nl1b UNIQUE (termek_nev),
    CONSTRAINT termek_termek_ar_check CHECK (termek_ar >= 0)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

CREATE TABLE vasarlas
(
    id integer NOT NULL,
    vasarlas_datum timestamp without time zone,
    vegosszeg integer,
    dolgozo_id integer,
    CONSTRAINT vasarlas_pkey PRIMARY KEY (id),
    CONSTRAINT fk9fgxc463qqhl42kab0v6yed13 FOREIGN KEY (dolgozo_id)
        REFERENCES dolgozo (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

CREATE TABLE tetel
(
    id integer NOT NULL,
    darabszam integer,
    termek_id integer,
    vasarlas_id integer,
    CONSTRAINT tetel_pkey PRIMARY KEY (id),
    CONSTRAINT fk63cep7ka3c3rdgjngvuwkwj83 FOREIGN KEY (termek_id)
        REFERENCES termek (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkeq6ijxgmk5u131996htp1v50n FOREIGN KEY (vasarlas_id)
        REFERENCES vasarlas (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE tetel
    OWNER to postgres;

ALTER TABLE vasarlas
    OWNER to postgres;

ALTER TABLE termek
    OWNER to postgres;