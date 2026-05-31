package org.mnuykin.service.imp;

import jakarta.annotation.PostConstruct;
import org.mnuykin.service.PostFileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class PostFileServiceImp implements PostFileService {
    @Value("${post.image.dir:src/main/resources/images/}")
    private String imagePath;
    @Value("${post.image.pattern:postId_%d.jpg")
    private String imageNamePattern;

    @PostConstruct
    void init() {
        try {
            Files.createDirectories(Path.of(imagePath));
        } catch (IOException e) {
            throw new RuntimeException("Ошибка создания директории для хранения файлов", e);
        }
    }

    @Override
    public void upload(Long postId, MultipartFile file) {
        String fileName = String.format(imageNamePattern, postId);
        try {
            file.transferTo(Path.of(imagePath).resolve(fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Resource download(Long postId) {
        Path filePath = Path.of(imagePath).resolve(String.format(imageNamePattern, postId));
        if (Files.notExists(filePath)){
            return null;
        }

        return new FileSystemResource(filePath);
    }
}
