insert into teams (id, name, commission_percentage, bank_account_balance)
values ('8a02f3d3-7e26-4467-a6ff-10bdf8a4bb90', 'Real Madrid', 5, 300000000.00),
       ('f96b53e8-8d02-4f58-b79e-d8d2e6e43145', 'Manchester City', 7, 250000000.00);

insert into players (id, first_name, last_name, age, months_of_experience, team_id)
values ('01d8b832-9c90-4f74-b098-236340b6f501', 'Thibaut', 'Courtois', 31, 156, '8a02f3d3-7e26-4467-a6ff-10bdf8a4bb90'),
       ('e6a5e409-bf63-4576-a362-4e8e80d1bfa9', 'Luka', 'Modric', 38, 228, '8a02f3d3-7e26-4467-a6ff-10bdf8a4bb90'),
       ('7ac6a259-9f25-43c8-a8d8-763b29d6fc4e', 'Vinicius', 'Junior', 23, 84, '8a02f3d3-7e26-4467-a6ff-10bdf8a4bb90'),
       ('b4d34525-9424-4cbb-8f9f-6ff51b0af846', 'Eduardo', 'Camavinga', 21, 48, '8a02f3d3-7e26-4467-a6ff-10bdf8a4bb90'),
       ('f5cbba47-d8eb-494c-8c8b-dcb6904184dc', 'Toni', 'Kroos', 33, 192, '8a02f3d3-7e26-4467-a6ff-10bdf8a4bb90'),
       ('843d12a5-ecc3-4c63-a8bb-63d6e4038f4c', 'Erling', 'Haaland', 23, 84, 'f96b53e8-8d02-4f58-b79e-d8d2e6e43145'),
       ('4f8f0b7d-cd99-43c5-a60e-7d74803e43c4', 'Kevin', 'De Bruyne', 32, 180, 'f96b53e8-8d02-4f58-b79e-d8d2e6e43145'),
       ('c6c7e786-6c55-43a9-9f02-781ef0b79243', 'Phil', 'Foden', 23, 60, 'f96b53e8-8d02-4f58-b79e-d8d2e6e43145'),
       ('3d3b7129-0bd7-4ddf-a786-9f0cfc9e0e12', 'Ruben', 'Dias', 26, 108, 'f96b53e8-8d02-4f58-b79e-d8d2e6e43145'),
       ('8a3f1237-7bc2-47a9-8f24-7c68d7a9e8b2', 'Jack', 'Grealish', 28, 144, 'f96b53e8-8d02-4f58-b79e-d8d2e6e43145');
