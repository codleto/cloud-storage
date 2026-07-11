package org.codleto.cloudstorage.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "resources")
public class ResourceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "parent_path", nullable = false, length = 1024)
    private String parentPath;

    @Column(nullable = false, length = 255)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ResourceType type;

    private Long size;

    @Column(name = "object_key", length = 1024)
    private String objectKey;

    public ResourceEntity(
            UserEntity user,
            String parentPath,
            String name,
            ResourceType type,
            Long size,
            String objectKey
    ) {
        this.user = user;
        this.parentPath = parentPath;
        this.name = name;
        this.type = type;
        this.size = size;
        this.objectKey = objectKey;
    }
}
