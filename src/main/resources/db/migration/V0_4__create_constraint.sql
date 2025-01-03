alter table if exists players
    add constraint fk_player_team_id foreign key (team_id) references teams;
