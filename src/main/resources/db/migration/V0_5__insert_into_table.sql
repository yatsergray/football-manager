insert into teams (id, name, commission_percentage, bank_account_balance)
values
    ('ae204b8f-7fa3-46bd-a2c2-165e88d50eb9', 'Dynamo Kyiv', 5, 1000000.00),
    ('55c2bf52-1af1-44e4-9402-b435d2e09932', 'Shakhtar Donetsk', 7, 1500000.00);

insert into players (id, first_name, last_name, age, months_of_experience, team_id)
values
    ('4d129aae-fec3-4cb1-88b3-ff1a4587bbb8', 'Georgiy', 'Bushchan', 29, 120, 'ae204b8f-7fa3-46bd-a2c2-165e88d50eb9'),
    ('91f88268-f46f-4ca9-bb22-0ee6a9cb57a5', 'Oleksandr', 'Tymchyk', 26, 96, 'ae204b8f-7fa3-46bd-a2c2-165e88d50eb9'),
    ('875d3f1b-1419-4743-b623-25ecc676314d', 'Denys', 'Popov', 24, 72, 'ae204b8f-7fa3-46bd-a2c2-165e88d50eb9'),
    ('d9c80d95-ca4d-46b0-b043-2aef052bfd0f', 'Vitaliy', 'Buyalskyi', 30, 144, 'ae204b8f-7fa3-46bd-a2c2-165e88d50eb9'),
    ('907d1b56-7e83-4835-955d-bc799de76142', 'Vladyslav', 'Vanat', 21, 48, 'ae204b8f-7fa3-46bd-a2c2-165e88d50eb9'),
    ('26a0486b-c6b8-4da6-91e8-c400fe2a3e04', 'Dmytro', 'Riznyk', 26, 96, '55c2bf52-1af1-44e4-9402-b435d2e09932'),
    ('46b11c29-273b-4e3a-b57a-3d6283c1b730', 'Yukhym', 'Konoplia', 24, 72, '55c2bf52-1af1-44e4-9402-b435d2e09932'),
    ('cf16cee8-f860-45ab-824d-7ee9c14c278b', 'Mykola', 'Matviyenko', 27, 108, '55c2bf52-1af1-44e4-9402-b435d2e09932'),
    ('8f00589e-cec9-4c58-b0de-cc895a9efb14', 'Oleksandr', 'Zubkov', 26, 96, '55c2bf52-1af1-44e4-9402-b435d2e09932'),
    ('5b635ceb-ac91-4814-bee9-87f75d72bdc2', 'Danylo', 'Sikan', 22, 60, '55c2bf52-1af1-44e4-9402-b435d2e09932');