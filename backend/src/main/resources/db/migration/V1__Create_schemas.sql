-- Create schemas and grant user permissions
CREATE SCHEMA configuration;
CREATE SCHEMA business;

GRANT ALL ON SCHEMA configuration TO jinji_adm;
GRANT ALL ON SCHEMA business TO jinji_adm;