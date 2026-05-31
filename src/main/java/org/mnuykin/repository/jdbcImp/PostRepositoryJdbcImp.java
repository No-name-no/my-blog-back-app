package org.mnuykin.repository.jdbcImp;

import org.mnuykin.model.Page;
import org.mnuykin.model.Post;
import org.mnuykin.model.PostFilter;
import org.mnuykin.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class PostRepositoryJdbcImp implements PostRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PostRepositoryJdbcImp(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Post> postRowMapper = (rs, rowNum) -> new Post(
            rs.getLong("id"),
            rs.getString("title"),
            rs.getString("text"),
            Arrays.asList((String[]) rs.getArray("tags").getArray()),
            rs.getInt("likesCount"),
            rs.getInt("commentCount")
    );
    private final RowMapper<Page> pageRowMapper = (rs, rowNum) -> {
        ArrayList<Post> posts = new ArrayList<>();
        Integer countPosts = null;
        while (rs.next()){
            if (countPosts == null)
                countPosts = rs.getInt("countPosts");

            posts.add(postRowMapper.mapRow(rs, rowNum));
        }
        return new Page(posts, null, null, countPosts);
    };

    @Override
    public Optional<Page> findPage(PostFilter filter, Integer pageNumber, Integer pageSize) {
        //MapSqlParameterSource params = new MapSqlParameterSource();

        String sql ="""
                        Select id, title, text, tags, likesCount, commentCount, COUNT(p.id) OVER() AS countPosts
                        From post
                        Where title like ?
                            and tags @> ARRAY[?]::text[]"
                        """;

        Page page = jdbcTemplate.queryForObject(
                sql,
                pageRowMapper,
                filter.getSearchFilter(),
                filter.getTags().toArray()
        );

        page.setPageNumber(pageNumber);
        page.setPageSize(pageSize);

        return Optional.of(page);
    }

    @Override
    public Optional<Post> get(Long id) {
        return Optional.of(
                jdbcTemplate.queryForObject(
                        """
                        Select id, title, text, tags, likesCount, commentCount
                        From post
                        Where id = ?
                        """,
                        postRowMapper,
                        id
                )
        );
    }

    @Override
    public Optional<Post> save(Post post) {
        return Optional.of(
                jdbcTemplate.queryForObject(
                        """
                        Insert into post (title, text, tags, likesCount, commentCount)
                            values (?, ?, ?, ?, ?)
                        Returning id, title, text, tags, likesCount, commentCount
                        """,
                        postRowMapper,
                        post.getTitle(),
                        post.getText(),
                        post.getTags(),
                        0L,
                        0L
                )
        );
    }

    @Override
    public Optional<Post> update(Post post) {
        return Optional.of(
                jdbcTemplate.queryForObject(
                        """
                        Update post set title = ?, text = ?, tags = ?
                        Where id = ?
                        Returning id, title, text, tags, likesCount, commentCount
                        """,
                        postRowMapper,
                        post.getTitle(),
                        post.getText(),
                        post.getTags(),
                        post.getId()
                )
        );
    }

    @Override
    public Integer delete(Long id) {
        return jdbcTemplate.update(
                "Delete From post where id = ?",
                id
        );
    }

    @Override
    public Integer addLike(Long id) {
        return jdbcTemplate.queryForObject(
                    """
                    Update post set likesCount = likesCount + 1
                    Where id = ?
                    Returning id
                    """,
                    Integer.class,
                    id
            );
    }
}