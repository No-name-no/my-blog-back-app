-- Таблица с постами
create table if not exists post(
    id bigserial primary key,
    title VARCHAR(255) not null,
    text text not null,
    tags VARCHAR(255)[],
    likesCount integer not null DEFAULT 0,
    commentsCount integer not null DEFAULT 0,
);

-- Таблица с комментариями
create table if not exists comment(
    id bigserial primary key,
    text text not null,
    FOREIGN KEY (postId) REFERENCES post (id) ON DELETE CASCADE
);

-- Индексы
create index if not exists idx_post_title on post (title);
create index if not exists idx_post_tags on post GIN (tags);
create index if not exists idx_comment_postId on comment (postId);