package org.mnuykin.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mnuykin.configuration.DataSourceConfiguration;
import org.mnuykin.configuration.WebConfiguration;
import org.mnuykin.dto.rq.CommentCreateRqDto;
import org.mnuykin.dto.rq.CommentUpdateRqDto;
import org.mnuykin.dto.rq.PostCreateRqDto;
import org.mnuykin.dto.rq.PostUpdateRqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringJUnitConfig(classes = {
        DataSourceConfiguration.class,
        WebConfiguration.class,
})
@WebAppConfiguration
@TestPropertySource(locations = "classpath:application-test.properties")
public class PostControllerTest extends TestPgContainers {
    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        jdbcTemplate.execute("TRUNCATE TABLE post CASCADE");
        jdbcTemplate.execute("""
                    INSERT INTO post (id, title, text, tags, likesCount, commentsCount) VALUES
                        (-1, 'Введение в PostgreSQL',
                            'PostgreSQL - мощная реляционная СУБД с открытым исходным кодом.',
                            ARRAY['postgresql', 'database', 'sql'], 45, 2),
                        (-2, 'Оптимизация SQL запросов в PostgreSQL',
                            'Индексы и правильная структура запросов критически важны для производительности.',
                            ARRAY['sql', 'optimization', 'performance', 'postgresql'], 128, 2),
                        (-3, 'Docker для начинающих',
                            'Docker позволяет упаковывать приложения в контейнеры для унификации окружения.',
                            ARRAY['docker', 'devops', 'containerization'], 312, 2)
                   """);
        jdbcTemplate.execute("""
                INSERT INTO comment (id, text, postId) VALUES
                    (-1, 'Отличная статья! Очень помогла', -1),
                    (-2, 'Спасибо, жду продолжения', -1),
                    (-3, 'А какие индексы лучше использовать для текстового поиска?', -2),
                    (-4, 'Очень полезно, добавил в закладки', -2),
                    (-5, 'Наконец-то понятное объяснение!', -3),
                    (-6, 'Спасибо за туториал', -3)
                """);
    }

    @Test
    void testPost() throws Exception {
        final Long postId = -1L;
        mockMvc.perform(get("/api/posts/{id}", postId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(postId));
    }

    @Test
    void testPage()throws Exception{
        mockMvc.perform(get("/api/posts?search=PostgreSQL&pageNumber=0&pageSize=2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.posts").isArray())
                .andExpect(jsonPath("$.posts.length()").value(2))
                .andExpect(jsonPath("$.hasPrev").value(false))
                .andExpect(jsonPath("$.hasNext").value(true))
                .andExpect(jsonPath("$.lastPage").value(1))
        ;
    }

    @Test
    void testCreatePost()throws Exception{
        PostCreateRqDto post = new PostCreateRqDto(
                "title",
                "text",
                List.of("tag1", "tag2")
        );

        mockMvc.perform(post("/api/posts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(post)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.title").value(post.title()))
            .andExpect(jsonPath("$.text").value(post.text()));
    }

    @Test
    void testUpdatePost() throws Exception{
        PostUpdateRqDto post = new PostUpdateRqDto(
                -1L,
                "title",
                "text",
                List.of("tag1", "tag2")
        );
        mockMvc.perform(put("/api/posts/{id}", post.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value(post.id()))
                .andExpect(jsonPath("$.title").value(post.title()))
                .andExpect(jsonPath("$.text").value(post.text()));
    }

    @Test
    void testDeletePost() throws Exception{
        mockMvc.perform(delete("/api/posts/{id}", -1L))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateImage() throws Exception {
        Resource resource = new ClassPathResource("images/test-image.jpg");
        assertTrue(resource.exists());

        byte[] fileBytes;
        try (InputStream inputStream = resource.getInputStream()) {
            fileBytes = inputStream.readAllBytes();
        }

        MockMultipartFile multipartFile = new MockMultipartFile(
                "image",
                "image.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                fileBytes
        );

        mockMvc.perform(multipart("/api/posts/{postId}/image", -1L)
                        .file(multipartFile)
                        .with(request -> {       // обязательно для PUT с multipart
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteImage() throws Exception{
        Resource resource = new ClassPathResource("images/test-image.jpg");
        assertTrue(resource.exists());

        byte[] fileBytes;
        try (InputStream inputStream = resource.getInputStream()) {
            fileBytes = inputStream.readAllBytes();
        }

        mockMvc.perform(get("/api/posts/{postId}/image", -1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG))
                .andExpect(result -> {
                    byte[] body = result.getResponse().getContentAsByteArray();
                    assertTrue(body.length > 0);
                    assertArrayEquals(body, fileBytes);
                });
    }

    @Test
    void testAddLike() throws Exception{

        mockMvc.perform(put("/api/posts/{postId}/likes", -1L))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(  46)));
    }

    @Test
    void testGetComments() throws Exception{
        mockMvc.perform(get("/api/posts/{postId}/comments", -1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2L));
    }

    @Test
    void testGetComment() throws Exception{
        mockMvc.perform(get("/api/posts/{postId}/comments/{commentId}", -1L, -2L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(-2L))
                .andExpect(jsonPath("$.postId").value(-1L));
    }

    @Test
    void testCreateComment() throws Exception{
        CommentCreateRqDto comment = new CommentCreateRqDto(
                "Новый коммент!",
                    -1L
                );

        mockMvc.perform(post("/api/posts/{postId}/comments", comment.postId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.postId").value(comment.postId()))
                .andExpect(jsonPath("$.text").value(comment.text()));
    }

    @Test
    void testUpdateComment() throws Exception{
        CommentUpdateRqDto comment = new CommentUpdateRqDto(
                -1L,
                "Новый коммент!",
                -1L
        );

        mockMvc.perform(put("/api/posts/{postId}/comments/{commentId}", comment.postId(), comment.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value(comment.id()))
                .andExpect(jsonPath("$.postId").value(comment.postId()))
                .andExpect(jsonPath("$.text").value(comment.text()));
    }

    @Test
    void testDeleteComment() throws Exception{
        mockMvc.perform(delete("/api/posts/1/comments/{id}", -1L))
                .andExpect(status().isOk());
    }
}
