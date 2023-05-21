create table telegram_update
(
    id           bigserial primary key,
    chat_id      bigint,
    message_id   integer,
    message_text varchar(255),
    update_id    bigint
);

alter table telegram_update
    owner to postgres;

