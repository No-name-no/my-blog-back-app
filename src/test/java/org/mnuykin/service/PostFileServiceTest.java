package org.mnuykin.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class PostFileServiceTest {
    @Autowired
    private PostFileService postFileService;
    @Value("${post.image.dir:src/main/resources/images/}")
    private String imagePath;
    @Value("${post.image.pattern:postId_%d.jpg}")
    private String imageNamePattern;
    private MultipartFile multipartFile;

    @BeforeEach
    void before(){
        multipartFile = Mockito.mock(MultipartFile.class);
    }

    @Test
    void init() {
        Assertions.assertTrue(Files.exists(Path.of(imagePath)));
    }

    @Test
    void testUpload() throws IOException {
        final Long postId = 1L;
        final String expectedFileName = String.format(imageNamePattern, postId);
        final Path expectedPath = Path.of(imagePath).resolve(expectedFileName);

        postFileService.upload(postId, multipartFile);
        Mockito.verify(multipartFile).transferTo(expectedPath);
    }

    @Test
    void testDownload() throws IOException {
        final Long postId = 1L;

        final String expectedFileName = String.format(imageNamePattern, postId);
        final Path expectedPath = Path.of(imagePath).resolve(expectedFileName);

        try {
            Files.createFile(expectedPath);

            Resource resource = postFileService.download(-1000L);
            assertNull(resource);

            resource = postFileService.download(postId);
            assertNotNull(resource);
            assertEquals(resource.getFilename(), expectedFileName);
        } finally {
            Files.deleteIfExists(expectedPath);
        }
    }
}
