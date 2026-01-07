-- Ensure the 'patient' table exists
CREATE TABLE IF NOT EXISTS patients (
    id UUID PRIMARY KEY,

    mrn VARCHAR(64) NOT NULL UNIQUE,

    first_name VARCHAR(80) NOT NULL,
    last_name  VARCHAR(80) NOT NULL,

    gender VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    preferred_language VARCHAR(10),

    email VARCHAR(255) NOT NULL UNIQUE,
    phone_number VARCHAR(32) NOT NULL UNIQUE,

    address_line1 VARCHAR(120) NOT NULL,
    address_line2 VARCHAR(120),
    city VARCHAR(80) NOT NULL,
    state VARCHAR(80) NOT NULL,
    postal_code VARCHAR(20) NOT NULL,
    country VARCHAR(80) NOT NULL,

    date_of_birth DATE NOT NULL,
    registered_date DATE NOT NULL,

    emergency_contact_name VARCHAR(120) NOT NULL,
    emergency_contact_phone VARCHAR(32) NOT NULL,
    emergency_contact_relation VARCHAR(40) NOT NULL,

    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
    );


INSERT INTO patients (
    id,
    mrn,
    first_name,
    last_name,
    gender,
    status,
    preferred_language,
    email,
    phone_number,
    address_line1,
    address_line2,
    city,
    state,
    postal_code,
    country,
    date_of_birth,
    registered_date,
    emergency_contact_name,
    emergency_contact_phone,
    emergency_contact_relation,
    created_at,
    updated_at
)
SELECT
    '111e4567-e89b-12d3-a456-426614174001',
    'MRN-100001',
    'John',
    'Doe',
    'MALE',
    'ACTIVE',
    'en',
    'john.doe@example.com',
    '+15551230001',
    '123 Main St',
    NULL,
    'Springfield',
    'CA',
    '90001',
    'USA',
    '1985-06-15',
    '2024-01-10',
    'Jane Doe',
    '+15559870001',
    'Spouse',
    NOW(),
    NOW()
    WHERE NOT EXISTS (
    SELECT 1 FROM patients WHERE id = '111e4567-e89b-12d3-a456-426614174001'
);

INSERT INTO patients (
    id, mrn, first_name, last_name, gender, status, preferred_language,
    email, phone_number, address_line1, address_line2, city, state,
    postal_code, country, date_of_birth, registered_date,
    emergency_contact_name, emergency_contact_phone, emergency_contact_relation,
    created_at, updated_at
)
SELECT
    '111e4567-e89b-12d3-a456-426614174002',
    'MRN-100002',
    'Emily',
    'Smith',
    'FEMALE',
    'ACTIVE',
    'en',
    'emily.smith@example.com',
    '+15551230002',
    '456 Elm St',
    'Apt 3B',
    'Shelbyville',
    'CA',
    '90002',
    'USA',
    '1990-09-23',
    '2023-12-01',
    'Robert Smith',
    '+15559870002',
    'Father',
    NOW(),
    NOW()
    WHERE NOT EXISTS (
    SELECT 1 FROM patients WHERE id = '111e4567-e89b-12d3-a456-426614174002'
);

INSERT INTO patients (
    id, mrn, first_name, last_name, gender, status, preferred_language,
    email, phone_number, address_line1, address_line2, city, state,
    postal_code, country, date_of_birth, registered_date,
    emergency_contact_name, emergency_contact_phone, emergency_contact_relation,
    created_at, updated_at
)
SELECT
    '111e4567-e89b-12d3-a456-426614174003',
    'MRN-100003',
    'Carlos',
    'Martinez',
    'MALE',
    'ACTIVE',
    'es',
    'carlos.martinez@example.com',
    '+15551230003',
    '789 Oak Ave',
    NULL,
    'Los Angeles',
    'CA',
    '90003',
    'USA',
    '1978-03-12',
    '2022-06-20',
    'Maria Martinez',
    '+15559870003',
    'Spouse',
    NOW(),
    NOW()
    WHERE NOT EXISTS (
    SELECT 1 FROM patients WHERE id = '111e4567-e89b-12d3-a456-426614174003'
);

INSERT INTO patients (
    id, mrn, first_name, last_name, gender, status, preferred_language,
    email, phone_number, address_line1, address_line2, city, state,
    postal_code, country, date_of_birth, registered_date,
    emergency_contact_name, emergency_contact_phone, emergency_contact_relation,
    created_at, updated_at
)
SELECT
    '111e4567-e89b-12d3-a456-426614174004',
    'MRN-100004',
    'Sophia',
    'Lee',
    'FEMALE',
    'ACTIVE',
    'en',
    'sophia.lee@example.com',
    '+15551230004',
    '321 Pine Rd',
    NULL,
    'Pasadena',
    'CA',
    '90004',
    'USA',
    '1995-02-05',
    '2024-03-01',
    'David Lee',
    '+15559870004',
    'Brother',
    NOW(),
    NOW()
    WHERE NOT EXISTS (
    SELECT 1 FROM patients WHERE id = '111e4567-e89b-12d3-a456-426614174004'
);

INSERT INTO patients (
    id, mrn, first_name, last_name, gender, status, preferred_language,
    email, phone_number, address_line1, address_line2, city, state,
    postal_code, country, date_of_birth, registered_date,
    emergency_contact_name, emergency_contact_phone, emergency_contact_relation,
    created_at, updated_at
)
SELECT
    '111e4567-e89b-12d3-a456-426614174005',
    'MRN-100005',
    'Michael',
    'Brown',
    'MALE',
    'INACTIVE',
    'en',
    'michael.brown@example.com',
    '+15551230005',
    '654 Maple St',
    NULL,
    'Burbank',
    'CA',
    '90005',
    'USA',
    '1982-11-30',
    '2023-05-14',
    'Laura Brown',
    '+15559870005',
    'Wife',
    NOW(),
    NOW()
    WHERE NOT EXISTS (
    SELECT 1 FROM patients WHERE id = '111e4567-e89b-12d3-a456-426614174005'
);
