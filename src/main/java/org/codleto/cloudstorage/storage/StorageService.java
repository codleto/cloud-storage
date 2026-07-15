package org.codleto.cloudstorage.storage;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.codleto.cloudstorage.storage.dto.ResourceResponse;
import org.codleto.cloudstorage.storage.dto.ResourceType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;

@Service
@RequiredArgsConstructor
public class StorageService {

    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucketName;

    @PostConstruct
    public void init() {
        try {
            boolean bucketExists = minioClient.bucketExists(
                    BucketExistsArgs.builder()
                            .bucket(bucketName)
                            .build()
            );

            if (!bucketExists) {
                minioClient.makeBucket(
                        MakeBucketArgs.builder()
                                .bucket(bucketName)
                                .build()
                );
            }

        } catch (Exception exception) {
            throw new RuntimeException("Failed to initialize MinIO bucket", exception);
        }
    }

    public ResourceResponse createDirectory(Long userId, String path) {

        String directoryPath = normalizeDirectoryPath(path);
        String objectName = buildUserObjectName(userId, path);

        putEmptyDirectoryObject(objectName);

        return buildDirectoryResponse(directoryPath);
    }

    private String normalizeDirectoryPath(String path) {
        if (path == null || path.isBlank()) {
            throw new IllegalArgumentException("Path must not be blank");
        }

        String normalizedPath = path.trim();

        while (normalizedPath.startsWith("/")) {
            normalizedPath = normalizedPath.substring(1);
        }

        while (normalizedPath.endsWith("/")) {
            normalizedPath = normalizedPath.substring(0, normalizedPath.length() - 1);
        }

        if (normalizedPath.isBlank()) {
            throw new IllegalArgumentException("Path must not be blank");
        }

        return normalizedPath + "/";
    }

    private String buildUserObjectName(Long userId, String directoryPath) {
        return "user-" + userId + "-files/" + directoryPath;
    }

    private void putEmptyDirectoryObject(String objectName) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(new ByteArrayInputStream(new byte[0]), 0L, -1L)
                            .contentType("application/x-directory")
                            .build()
            );
        } catch (Exception exception) {
            throw new RuntimeException("Failed to create directory", exception);
        }
    }

    private ResourceResponse buildDirectoryResponse(String directoryPath) {
        String pathWithoutTrailingSlash = directoryPath.substring(0, directoryPath.length() - 1);

        int lastSlashIndex = pathWithoutTrailingSlash.lastIndexOf("/");

        if (lastSlashIndex == -1) {
            return new ResourceResponse(
                    "",
                    pathWithoutTrailingSlash,
                    null,
                    ResourceType.DIRECTORY
            );
        }

        String parentPath = pathWithoutTrailingSlash.substring(0, lastSlashIndex + 1);
        String name = pathWithoutTrailingSlash.substring(lastSlashIndex + 1);

        return new ResourceResponse(
                parentPath,
                name,
                null,
                ResourceType.DIRECTORY
        );
    }
}
