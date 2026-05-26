DO
$$
BEGIN
   IF NOT EXISTS (
      SELECT FROM pg_roles WHERE rolname = 'jinji_adm'
   ) THEN
CREATE ROLE jinji_adm LOGIN PASSWORD 'password';
END IF;
END
$$;