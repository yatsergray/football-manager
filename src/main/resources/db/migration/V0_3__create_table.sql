create table players
(
    age                  integer      not null,
    months_of_experience integer      not null,
    id                   uuid         not null,
    team_id              uuid         not null,
    first_name           varchar(255) not null,
    last_name            varchar(255) not null,
    primary key (id)
);

create table teams
(
    bank_account_balance  numeric(19, 2) not null,
    commission_percentage integer        not null,
    id                    uuid           not null,
    name                  varchar(255)   not null unique,
    primary key (id)
);

create table transfers
(
    date            date           not null,
    total_cost      numeric(19, 2) not null,
    buying_team_id  uuid,
    id              uuid           not null,
    player_id       uuid,
    selling_team_id uuid,
    primary key (id)
);
