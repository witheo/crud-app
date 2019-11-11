
INSERT INTO client (
    company_name,
    website_uri,
    phone_number,
    street_address,
    city,
    state,
    zip_code,
    mailing_street_address,
    mailing_city,
    mailing_state,
    mailing_zip_code
) VALUES (
    'Uber corp',
    'uber.corp',
    '8287773454',
    '123 Any St.',
    'Asheville',
    'NC',
    '28801',
    '123 Any St.',
    'Asheville',
    'NC',
    '28801'
), (
    'tiny corp',
    'tiny.corp',
    '8344950304',
    '123 Any St.',
    'Asheville',
    'NC',
    '28801',
    '123 Any St.',
    'Asheville',
    'NC',
    '28801'
);
INSERT INTO person (
    first_name,
    last_name,
    email_address,
    street_address,
    city,
    state,
    zip_code,
    client_id
) VALUES (
    'John',
    'Smith',
    'fake1@aquent.com',
    '123 Any St.',
    'Asheville',
    'NC',
    '28801',
    SELECT client_id FROM client where rownum=1
), (
    'Jane',
    'Smith',
    'fake2@aquent.com',
    '123 Any St.',
    'Asheville',
    'NC',
    '28801',
    SELECT client_id FROM client where company_name = 'tiny corp' and rownum=1
), (
    'jim',
    'Smith',
    'fake2@aquent.com',
    '123 Any St.',
    'Asheville',
    'NC',
    '28801',
    null
);