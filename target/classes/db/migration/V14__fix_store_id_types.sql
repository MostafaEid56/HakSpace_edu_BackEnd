-- Fix: V13 used SERIAL (INTEGER) but JPA entities map Long → BIGINT.
-- Alter all store table PKs and FKs to BIGINT / BIGSERIAL.

-- store_categories.id: INTEGER → BIGINT
ALTER TABLE store_categories ALTER COLUMN id TYPE BIGINT;
ALTER TABLE store_categories ALTER COLUMN id SET DEFAULT nextval('store_categories_id_seq');
ALTER SEQUENCE store_categories_id_seq AS BIGINT;

-- store_products.id: INTEGER → BIGINT
ALTER TABLE store_products ALTER COLUMN id TYPE BIGINT;
ALTER TABLE store_products ALTER COLUMN id SET DEFAULT nextval('store_products_id_seq');
ALTER SEQUENCE store_products_id_seq AS BIGINT;

-- store_products.category_id FK: INTEGER → BIGINT (must match store_categories.id)
ALTER TABLE store_products ALTER COLUMN category_id TYPE BIGINT;

-- store_leads.id: INTEGER → BIGINT
ALTER TABLE store_leads ALTER COLUMN id TYPE BIGINT;
ALTER TABLE store_leads ALTER COLUMN id SET DEFAULT nextval('store_leads_id_seq');
ALTER SEQUENCE store_leads_id_seq AS BIGINT;

-- store_leads.user_id FK: INTEGER → BIGINT (must match users.id)
ALTER TABLE store_leads ALTER COLUMN user_id TYPE BIGINT;

-- store_leads.product_id FK: INTEGER → BIGINT (must match store_products.id)
ALTER TABLE store_leads ALTER COLUMN product_id TYPE BIGINT;
