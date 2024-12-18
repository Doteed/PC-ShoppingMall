package com.project.easyBuild.product.biz;

import org.kohsuke.github.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Service
public class GitHubService {
    private final GitHub github;

    public GitHubService(@Value("${github.token}") String token) throws IOException {
        this.github = new GitHubBuilder().withOAuthToken(token).build();
    }

    public String uploadImage(String repoName, String path, MultipartFile file) throws IOException {
        GHRepository repo = github.getRepository(repoName);
        byte[] content = file.getBytes();
        repo.createContent()
            .content(content)
            .path(path)
            .message("Upload image")
            .commit();
        return String.format("https://raw.githubusercontent.com/%s/master/%s", repoName, path);
    }
}
