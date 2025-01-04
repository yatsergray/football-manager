alter table if exists players
    drop constraint if exists fk_transfer_player_id;
alter table if exists transfers
    drop constraint if exists fk_transfer_buying_team_id;
alter table if exists transfers
    drop constraint if exists fk_transfer_player_id;
alter table if exists transfers
    drop constraint if exists fk_transfer_selling_team_id;
