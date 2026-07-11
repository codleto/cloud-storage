package org.codleto.cloudstorage.repository;

import org.codleto.cloudstorage.entity.ResourceEntity;
import org.codleto.cloudstorage.entity.ResourceType;
import org.codleto.cloudstorage.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ResourceRepository extends JpaRepository<ResourceEntity, Long> {

    Optional<ResourceEntity> findByUserAndParentPathAndNameAndType(
            UserEntity user,
            String parentPath,
            String name,
            ResourceType type
    );

    boolean existsByUserAndParentPathAndNameAndType(
            UserEntity user,
            String parentPath,
            String name,
            ResourceType type
    );

    List<ResourceEntity> findAllByUserAndParentPath(
            UserEntity user,
            String parentPath
    );
}
