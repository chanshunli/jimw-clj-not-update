CREATE TABLE robot_blog (
  id BIGSERIAL PRIMARY KEY,
  who_share TEXT,
  url TEXT,
  is_catch BOOLEAN NOT NULL DEFAULT FALSE,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);
