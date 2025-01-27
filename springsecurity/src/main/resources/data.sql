

INSERT IGNORE INTO tb_roles (role_id, name) VALUES (1,'admin')
INSERT IGNORE INTO tb_roles (role_id, name) VALUES (2,'basic')

--Gerar chave priv: openssl genpkey -algorithm RSA -out chave_privada.pem -pkeyopt rsa_keygen_bits:2048
--Extrair chave public: openssl rsa -pubout -in chave_privada.pem -out chave_publica.pem

