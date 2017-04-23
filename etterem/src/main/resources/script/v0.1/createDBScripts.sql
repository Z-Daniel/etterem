CREATE TABLE public.dolgozo
(
    id integer NOT NULL,
    dolgozo_nev character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT dolgozo_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.dolgozo
    OWNER to postgres;

CREATE TABLE public.termek
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

ALTER TABLE public.termek
    OWNER to postgres;
CREATE TABLE public.tetel
(
    id integer NOT NULL,
    darabszam integer,
    termek_id integer,
    vasarlas_id integer,
    CONSTRAINT tetel_pkey PRIMARY KEY (id),
    CONSTRAINT fk63cep7ka3c3rdgjngvuwkwj83 FOREIGN KEY (termek_id)
        REFERENCES public.termek (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkeq6ijxgmk5u131996htp1v50n FOREIGN KEY (vasarlas_id)
        REFERENCES public.vasarlas (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.tetel
    OWNER to postgres;
CREATE TABLE public.vasarlas
(
    id integer NOT NULL,
    datum timestamp without time zone,
    vegosszeg integer,
    dolgozo_id integer,
    CONSTRAINT vasarlas_pkey PRIMARY KEY (id),
    CONSTRAINT fk9fgxc463qqhl42kab0v6yed13 FOREIGN KEY (dolgozo_id)
        REFERENCES public.dolgozo (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.vasarlas
    OWNER to postgres;
