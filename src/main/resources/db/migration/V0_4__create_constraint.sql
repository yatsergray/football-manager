alter table if exists players
    add constraint fk_transfer_player_id foreign key (team_id) references teams;
alter table if exists transfers
    add constraint fk_transfer_buying_team_id foreign key (buying_team_id) references teams (id) on delete set null;
alter table if exists transfers
    add constraint fk_transfer_player_id foreign key (player_id) references players (id) on delete set null;
alter table if exists transfers
    add constraint fk_transfer_selling_team_id foreign key (selling_team_id) references teams (id) on delete set null;