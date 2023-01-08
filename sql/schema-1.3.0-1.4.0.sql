CREATE TABLE public.datos_equipo_usuario (
    id bigint NOT NULL,
    rol character varying(255),
    equipo_id bigint,
    usuario_id bigint
);
ALTER TABLE public.datos_equipo_usuario OWNER TO mads;
CREATE SEQUENCE public.datos_equipo_usuario_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE public.datos_equipo_usuario_id_seq OWNER TO mads;

ALTER SEQUENCE public.datos_equipo_usuario_id_seq OWNED BY public.datos_equipo_usuario.id;

ALTER TABLE public.usuarios
ADD COLUMN enabled boolean NOT NULL,
ADD COLUMN verification_code character varying(64);

ALTER TABLE public.tareas
ADD COLUMN fecha_final date;

CREATE TABLE public.tareasequipo (
    id bigint NOT NULL,
    descripcion character varying(255) NOT NULL,
    estado character varying(255) NOT NULL,
    fecha date,
    titulo character varying(255) NOT NULL,
    equipo_id bigint NOT NULL
);


ALTER TABLE public.tareasequipo OWNER TO mads;

CREATE SEQUENCE public.tareasequipo_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.tareasequipo_id_seq OWNER TO mads;

ALTER SEQUENCE public.tareasequipo_id_seq OWNED BY public.tareasequipo.id;

ALTER TABLE ONLY public.datos_equipo_usuario ALTER COLUMN id SET DEFAULT nextval('public.datos_equipo_usuario_id_seq'::regclass);

ALTER TABLE ONLY public.tareasequipo ALTER COLUMN id SET DEFAULT nextval('public.tareasequipo_id_seq'::regclass);

ALTER TABLE ONLY public.datos_equipo_usuario
    ADD CONSTRAINT datos_equipo_usuario_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.tareasequipo
    ADD CONSTRAINT tareasequipo_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.datos_equipo_usuario
    ADD CONSTRAINT fk5nbu06l8dxeun3aj9agl40r4p FOREIGN KEY (usuario_id) REFERENCES public.usuarios(id);

ALTER TABLE ONLY public.datos_equipo_usuario
    ADD CONSTRAINT fk752tnn8j8mq4hesjnnkrpfftw FOREIGN KEY (equipo_id) REFERENCES public.equipos(id);

ALTER TABLE ONLY public.tareasequipo
    ADD CONSTRAINT fkmxqmetbly1abh7nsl18dmbvyb FOREIGN KEY (equipo_id) REFERENCES public.equipos(id);










