create table if not exists stocks(
    id bigserial primary key,
    stock_name varchar(255) unique not null,
    quantity bigint not null,
    version bigint default 0
);

create table if not exists wallets(
    id bigserial primary key,
    wallet_id varchar(255) unique not null
);

create table if not exists wallet_stock(
    id bigserial primary key,
    wallet_id bigint not null references wallets(id) on delete cascade,
    stock_name varchar(255) not null references stocks(stock_name),
    quantity bigint not null,
    unique(wallet_id, stock_name),
    version bigint default 0
);

create table if not exists audit_log(
    id bigserial primary key,
    trade_type varchar(50) not null,
    wallet_id varchar(255) not null,
    stock_name varchar(255) not null
);