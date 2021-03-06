/* ALTER TABLE todos ADD COLUMN done BOOLEAN NOT NULL DEFAULT FALSE; */
/* ALTER TABLE todos ADD COLUMN wctags JSONB NOT NULL DEFAULT '{}'; */
CREATE TABLE todos (
  id BIGSERIAL PRIMARY KEY,
  blog BIGSERIAL NOT NULL REFERENCES blogs (id),
  parid BIGSERIAL NOT NULL,
  content TEXT NOT NULL,
  done BOOLEAN NOT NULL DEFAULT FALSE,
  sort_id INT,
  wctags JSONB NOT NULL DEFAULT '{}';
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  app_id INT,
  file TEXT,
  islast BOOLEAN
  percent INT,
  begin INT,
  mend INT,
  origin_content TEXT
);

/*
ALTER TABLE todos ADD COLUMN  app_id INT;
ALTER TABLE todos ADD COLUMN  file TEXT;
ALTER TABLE todos ADD COLUMN  islast BOOLEAN;
ALTER TABLE todos ADD COLUMN  percent INT;
ALTER TABLE todos ADD COLUMN  begin INT;
ALTER TABLE todos ADD COLUMN  mend INT;
ALTER TABLE todos ADD COLUMN  origin_content TEXT;
*/

CREATE SEQUENCE todos_new_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE todos_new_id_seq OWNER TO jim;

ALTER SEQUENCE todos_new_id_seq OWNED BY todos.id;

ALTER TABLE ONLY todos ALTER COLUMN id SET DEFAULT nextval('todos_new_id_seq'::regclass);


CREATE SEQUENCE todos_sort_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE todos_sort_id_seq OWNER TO jim;

ALTER SEQUENCE todos_sort_id_seq OWNED BY todos.sort_id;

ALTER TABLE ONLY todos ALTER COLUMN sort_id SET DEFAULT nextval('todos_sort_id_seq'::regclass);


