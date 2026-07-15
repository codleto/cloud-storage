package org.codleto.cloudstorage.storage.dto;

public record ResourceResponse(
        String path,
        String name,
        Long size,
        ResourceType type
) {
}
