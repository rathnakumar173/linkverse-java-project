package com.resume.project.linkverse.dto;

import com.resume.project.linkverse.entity.Link;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public class LinkDtos {

    public record CreateLinkRequestDto(
            @NotEmpty(message = "Title is required")
            String title,

            @NotEmpty(message = "URL is required")
            @URL(message = "Invalid URL format")
            String url,

            @NotNull(message = "Team ID is required")
            Long teamId,

            Set<String> tags // Simple list of tag names
    ) {}

    public record UpdateLinkRequestDto(
            @NotEmpty(message = "Title is required")
            String title,

            @NotEmpty(message = "URL is required")
            @URL(message = "Invalid URL format")
            String url,

            Set<String> tags
    ) {}

    public record LinkResponseDto(
            Long id,
            String title,
            String url,
            Long teamId,
            AuthDtos.UserDto creator,
            LocalDateTime createdAt,
            Set<String> tags
    ) {
        public static LinkResponseDto fromLink(Link link) {
            Set<String> tagNames = link.getTags().stream()
                    .map(com.resume.project.linkverse.entity.Tag::getName)
                    .collect(Collectors.toSet());

            return new LinkResponseDto(
                    link.getId(),
                    link.getTitle(),
                    link.getUrl(),
                    link.getTeam().getId(),
                    AuthDtos.UserDto.fromUser(link.getCreator()),
                    link.getCreatedAt(),
                    tagNames
            );
        }
    }
}