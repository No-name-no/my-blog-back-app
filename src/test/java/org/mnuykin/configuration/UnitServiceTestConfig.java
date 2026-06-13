package org.mnuykin.configuration;

import org.mnuykin.repository.CommentRepository;
import org.mnuykin.repository.PostRepository;
import org.mnuykin.service.CommentService;
import org.mnuykin.service.PostFileService;
import org.mnuykin.service.PostService;
import org.mnuykin.service.imp.CommentServiceImp;
import org.mnuykin.service.imp.PostFileServiceImp;
import org.mnuykin.service.imp.PostServiceImp;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.TestPropertySource;

@Configuration
@Profile("unit-test")
@TestPropertySource("classpath:application-test.properties")
public class UnitServiceTestConfig {

    @Bean
    public CommentRepository mockCommentRepository() {
        return Mockito.mock(CommentRepository.class);
    }

    @Bean
    public PostRepository mockPostRepository() {
        return Mockito.mock(PostRepository.class);
    }

    @Bean
    public CommentService commentServiceImp(CommentRepository commentRepository, PostRepository postRepository){
        return new CommentServiceImp(commentRepository, postRepository);
    }

    @Bean
    public PostService postServiceImp(PostRepository postRepository){
        return new PostServiceImp(postRepository);
    }

    @Bean
    public PostFileService postFileServiceImp(){
        return new PostFileServiceImp();
    }
}
