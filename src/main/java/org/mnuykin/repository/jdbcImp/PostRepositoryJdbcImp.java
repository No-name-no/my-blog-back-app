package org.mnuykin.repository.jdbcImp;

import org.mnuykin.model.Page;
import org.mnuykin.model.Post;
import org.mnuykin.model.PostFilter;
import org.mnuykin.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.SqlArrayValue;
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
            rs.getInt("commentsCount")
    );
    private final RowMapper<Page> pageRowMapper = (rs, rowNum) -> {
        ArrayList<Post> posts = new ArrayList<>();
        Integer countPosts = rs.getInt("countPosts");
        posts.add(postRowMapper.mapRow(rs, rowNum));
        return new Page(posts, countPosts, null, null);
    };

    @Override
    public Optional<Page> findPage(PostFilter filter, Integer pageNumber, Integer pageSize) {
        //MapSqlParameterSource params = new MapSqlParameterSource();

        String sql ="""
                        Select id, title, text, tags, likesCount, commentsCount, COUNT(post.id) OVER() AS countPosts
                        From post
                        Where title like ?
                            and tags @> ?
                                LIMIT ? OFFSET ?
                        """;

        Page page = jdbcTemplate.queryForObject(
                sql,
                pageRowMapper,
                "%" + filter.getSearchFilter() + "%",
                new SqlArrayValue("varchar", filter.getTags().toArray()),
                pageSize,
                pageNumber
        );

        if(page != null){
            page.setPageNumber(pageNumber);
            page.setPageSize(pageSize);
        }

        return Optional.ofNullable(page);
    }

    @Override
    public Optional<Post> get(Long id) {
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(
                        """
                        Select id, title, text, tags, likesCount, commentsCount
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
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(
                        """
                        Insert into post (title, text, tags, likesCount, commentsCount)
                            values (?, ?, ?, ?, ?)
                        Returning id, title, text, tags, likesCount, commentsCount
                        """,
                        postRowMapper,
                        post.getTitle(),
                        post.getText(),
                        new SqlArrayValue("varchar", post.getTags().toArray()),
                        0L,
                        0L
                )
        );
    }

    @Override
    public Optional<Post> update(Post post) {
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(
                        """
                        Update post set title = ?, text = ?, tags = ?
                        Where id = ?
                        Returning id, title, text, tags, likesCount, commentsCount
                        """,
                        postRowMapper,
                        post.getTitle(),
                        post.getText(),
                        new SqlArrayValue("varchar", post.getTags().toArray()),
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