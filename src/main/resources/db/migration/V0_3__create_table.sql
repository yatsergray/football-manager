create table players
(
    id                   uuid         not null,
    team_id              uuid         not null,
    age                  integer      not null,
    months_of_experience integer      not null,
    first_name           varchar(255) not null,
    last_name            varchar(255) not null,
    primary key (id)
);

create table teams
(
    id                    uuid           not null,
    bank_account_balance  numeric(19, 2) not null,
    commission_percentage integer        not null,
    name                  varchar(255)   not null unique,
    primary key (id)
);
