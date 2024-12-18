package com.project.easyBuild.product.biz;

import org.kohsuke.github.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Service
public class GitHubService {
    private final GitHub github;

    public GitHubService(@Value("${github.token}") String token) throws IOException {
        this.github = new GitHubBuilder().withOAuthToken(token).build();
    }

    public String uploadImage(String repoName, String path, MultipartFile file) throws IOException {
        GHRepository repo = github.getRepository(repoName);
        byte[] content = file.getBytes();
        
        String uniqueFilename = generateUniqueFilename(file.getOriginalFilename());
        String fullPath = path + "/" + uniqueFilename;  // 여기서 path는 디렉토리 경로만 포함
        
        try {
            // 파일 생성 시도
            repo.createContent()
                .content(content)
                .path(fullPath)
                .message("Upload image")
                .commit();
        } catch (GHFileNotFoundException e) {
            // 파일이 이미 존재하는 경우 업데이트
            GHContent existingFile = repo.getFileContent(fullPath);
            existingFile.update(content, "Update image", existingFile.getSha());
        }
        
        return String.format("https://raw.githubusercontent.com/%s/master/%s", repoName, fullPath);
    }


    private String generateUniqueFilename(String originalFilename) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        int randomNumber = new Random().nextInt(900000) + 100000;
        String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
        return String.format("%s_%d%s", timestamp, randomNumber, extension);
    }

}
