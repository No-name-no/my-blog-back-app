package org.mnuykin.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface PostFileService {
    void upload(Long postId, MultipartFile file);
    Resource download(Long postId);
}
