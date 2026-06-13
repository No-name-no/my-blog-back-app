package org.mnuykin.repository.jdbcImp;

import org.mnuykin.model.Comment;
import org.mnuykin.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CommentRepositoryJdbcImp implements CommentRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CommentRepositoryJdbcImp(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Comment> commentRowMapper = (rs, rowNum) -> new Comment(
            rs.getLong("id"),
            rs.getString("text"),
            rs.getLong("postId")
    );

    @Override
    public List<Comment> findAll(Long postId) {
        return jdbcTemplate.query(
                "Select id, text, postId From comment where postId = ?",
                commentRowMapper,
                postId
        );
    }

    @Override
    public Optional<Comment> get (Long postId, Long commentId) {
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(
                        "Select id, text, postId From comment where postId = ? and id = ?",
                        commentRowMapper,
                        postId,
                        commentId
                )
        );
    }

    @Override
    public Optional<Comment> save (Comment comment) {
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(
                        "Insert into comment (postId, text) values (?, ?) returning id, postId, text",
                        commentRowMapper,
                        comment.getPostId(),
                        comment.getText()
                )
        );
    }

    @Override
    public Optional<Comment> update (Comment comment) {
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(
                        "Update comment set text = ? where id = ? and postId = ? returning id, postId, text",
                        commentRowMapper,
                        comment.getText(),
                        comment.getId(),
                        comment.getPostId()
                )
        );
    }

    @Override
    public Integer delete (Long postId, Long commentId) {
        return jdbcTemplate.update(
                "Delete From comment where id = ? and postId = ?",
                postId,
                commentId
        );
    }
}
