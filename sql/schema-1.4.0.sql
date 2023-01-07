--
-- PostgreSQL database dump
--

-- Dumped from database version 13.8 (Debian 13.8-1.pgdg110+1)
-- Dumped by pg_dump version 13.8 (Debian 13.8-1.pgdg110+1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: datos_equipo_usuario; Type: TABLE; Schema: public; Owner: mads
--

CREATE TABLE public.datos_equipo_usuario (
    id bigint NOT NULL,
    rol character varying(255),
    equipo_id bigint,
    usuario_id bigint
);


ALTER TABLE public.datos_equipo_usuario OWNER TO mads;

--
-- Name: datos_equipo_usuario_id_seq; Type: SEQUENCE; Schema: public; Owner: mads
--

CREATE SEQUENCE public.datos_equipo_usuario_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.datos_equipo_usuario_id_seq OWNER TO mads;

--
-- Name: datos_equipo_usuario_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: mads
--

ALTER SEQUENCE public.datos_equipo_usuario_id_seq OWNED BY public.datos_equipo_usuario.id;


--
-- Name: equipo_usuario; Type: TABLE; Schema: public; Owner: mads
--

CREATE TABLE public.equipo_usuario (
    fk_equipo bigint NOT NULL,
    fk_usuario bigint NOT NULL
);


ALTER TABLE public.equipo_usuario OWNER TO mads;

--
-- Name: equipos; Type: TABLE; Schema: public; Owner: mads
--

CREATE TABLE public.equipos (
    id bigint NOT NULL,
    descripcion character varying(255),
    lider bigint,
    nombre character varying(255)
);


ALTER TABLE public.equipos OWNER TO mads;

--
-- Name: equipos_id_seq; Type: SEQUENCE; Schema: public; Owner: mads
--

CREATE SEQUENCE public.equipos_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.equipos_id_seq OWNER TO mads;

--
-- Name: equipos_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: mads
--

ALTER SEQUENCE public.equipos_id_seq OWNED BY public.equipos.id;


--
-- Name: tareas; Type: TABLE; Schema: public; Owner: mads
--

CREATE TABLE public.tareas (
    id bigint NOT NULL,
    titulo character varying(255) NOT NULL,
    usuario_id bigint NOT NULL
);


ALTER TABLE public.tareas OWNER TO mads;

--
-- Name: tareas_id_seq; Type: SEQUENCE; Schema: public; Owner: mads
--

CREATE SEQUENCE public.tareas_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.tareas_id_seq OWNER TO mads;

--
-- Name: tareas_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: mads
--

ALTER SEQUENCE public.tareas_id_seq OWNED BY public.tareas.id;


--
-- Name: tareasequipo; Type: TABLE; Schema: public; Owner: mads
--

CREATE TABLE public.tareasequipo (
    id bigint NOT NULL,
    descripcion character varying(255) NOT NULL,
    estado character varying(255) NOT NULL,
    fecha date,
    titulo character varying(255) NOT NULL,
    equipo_id bigint NOT NULL
);


ALTER TABLE public.tareasequipo OWNER TO mads;

--
-- Name: tareasequipo_id_seq; Type: SEQUENCE; Schema: public; Owner: mads
--

CREATE SEQUENCE public.tareasequipo_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.tareasequipo_id_seq OWNER TO mads;

--
-- Name: tareasequipo_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: mads
--

ALTER SEQUENCE public.tareasequipo_id_seq OWNED BY public.tareasequipo.id;


--
-- Name: usuarios; Type: TABLE; Schema: public; Owner: mads
--

CREATE TABLE public.usuarios (
    id bigint NOT NULL,
    acceso boolean NOT NULL,
    administrador boolean NOT NULL,
    email character varying(255) NOT NULL,
    enabled boolean NOT NULL,
    fecha_nacimiento date,
    nombre character varying(255),
    password character varying(255),
    verification_code character varying(64)
);


ALTER TABLE public.usuarios OWNER TO mads;

--
-- Name: usuarios_id_seq; Type: SEQUENCE; Schema: public; Owner: mads
--

CREATE SEQUENCE public.usuarios_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.usuarios_id_seq OWNER TO mads;

--
-- Name: usuarios_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: mads
--

ALTER SEQUENCE public.usuarios_id_seq OWNED BY public.usuarios.id;


--
-- Name: datos_equipo_usuario id; Type: DEFAULT; Schema: public; Owner: mads
--

ALTER TABLE ONLY public.datos_equipo_usuario ALTER COLUMN id SET DEFAULT nextval('public.datos_equipo_usuario_id_seq'::regclass);


--
-- Name: equipos id; Type: DEFAULT; Schema: public; Owner: mads
--

ALTER TABLE ONLY public.equipos ALTER COLUMN id SET DEFAULT nextval('public.equipos_id_seq'::regclass);


--
-- Name: tareas id; Type: DEFAULT; Schema: public; Owner: mads
--

ALTER TABLE ONLY public.tareas ALTER COLUMN id SET DEFAULT nextval('public.tareas_id_seq'::regclass);


--
-- Name: tareasequipo id; Type: DEFAULT; Schema: public; Owner: mads
--

ALTER TABLE ONLY public.tareasequipo ALTER COLUMN id SET DEFAULT nextval('public.tareasequipo_id_seq'::regclass);


--
-- Name: usuarios id; Type: DEFAULT; Schema: public; Owner: mads
--

ALTER TABLE ONLY public.usuarios ALTER COLUMN id SET DEFAULT nextval('public.usuarios_id_seq'::regclass);


--
-- Name: datos_equipo_usuario datos_equipo_usuario_pkey; Type: CONSTRAINT; Schema: public; Owner: mads
--

ALTER TABLE ONLY public.datos_equipo_usuario
    ADD CONSTRAINT datos_equipo_usuario_pkey PRIMARY KEY (id);


--
-- Name: equipo_usuario equipo_usuario_pkey; Type: CONSTRAINT; Schema: public; Owner: mads
--

ALTER TABLE ONLY public.equipo_usuario
    ADD CONSTRAINT equipo_usuario_pkey PRIMARY KEY (fk_equipo, fk_usuario);


--
-- Name: equipos equipos_pkey; Type: CONSTRAINT; Schema: public; Owner: mads
--

ALTER TABLE ONLY public.equipos
    ADD CONSTRAINT equipos_pkey PRIMARY KEY (id);


--
-- Name: tareas tareas_pkey; Type: CONSTRAINT; Schema: public; Owner: mads
--

ALTER TABLE ONLY public.tareas
    ADD CONSTRAINT tareas_pkey PRIMARY KEY (id);


--
-- Name: tareasequipo tareasequipo_pkey; Type: CONSTRAINT; Schema: public; Owner: mads
--

ALTER TABLE ONLY public.tareasequipo
    ADD CONSTRAINT tareasequipo_pkey PRIMARY KEY (id);


--
-- Name: usuarios usuarios_pkey; Type: CONSTRAINT; Schema: public; Owner: mads
--

ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT usuarios_pkey PRIMARY KEY (id);


--
-- Name: datos_equipo_usuario fk5nbu06l8dxeun3aj9agl40r4p; Type: FK CONSTRAINT; Schema: public; Owner: mads
--

ALTER TABLE ONLY public.datos_equipo_usuario
    ADD CONSTRAINT fk5nbu06l8dxeun3aj9agl40r4p FOREIGN KEY (usuario_id) REFERENCES public.usuarios(id);


--
-- Name: datos_equipo_usuario fk752tnn8j8mq4hesjnnkrpfftw; Type: FK CONSTRAINT; Schema: public; Owner: mads
--

ALTER TABLE ONLY public.datos_equipo_usuario
    ADD CONSTRAINT fk752tnn8j8mq4hesjnnkrpfftw FOREIGN KEY (equipo_id) REFERENCES public.equipos(id);


--
-- Name: tareas fkdmoaxl7yv4q6vkc9h32wvbddr; Type: FK CONSTRAINT; Schema: public; Owner: mads
--

ALTER TABLE ONLY public.tareas
    ADD CONSTRAINT fkdmoaxl7yv4q6vkc9h32wvbddr FOREIGN KEY (usuario_id) REFERENCES public.usuarios(id);


--
-- Name: equipo_usuario fkk4y9gec15ccnirp3r7w29o66p; Type: FK CONSTRAINT; Schema: public; Owner: mads
--

ALTER TABLE ONLY public.equipo_usuario
    ADD CONSTRAINT fkk4y9gec15ccnirp3r7w29o66p FOREIGN KEY (fk_equipo) REFERENCES public.equipos(id);


--
-- Name: tareasequipo fkmxqmetbly1abh7nsl18dmbvyb; Type: FK CONSTRAINT; Schema: public; Owner: mads
--

ALTER TABLE ONLY public.tareasequipo
    ADD CONSTRAINT fkmxqmetbly1abh7nsl18dmbvyb FOREIGN KEY (equipo_id) REFERENCES public.equipos(id);


--
-- Name: equipo_usuario fksabsvjgvfuen6hmyg7gn7oq4v; Type: FK CONSTRAINT; Schema: public; Owner: mads
--

ALTER TABLE ONLY public.equipo_usuario
    ADD CONSTRAINT fksabsvjgvfuen6hmyg7gn7oq4v FOREIGN KEY (fk_usuario) REFERENCES public.usuarios(id);


--
-- PostgreSQL database dump complete
--

