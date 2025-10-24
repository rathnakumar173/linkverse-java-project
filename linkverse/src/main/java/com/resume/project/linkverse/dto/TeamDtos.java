package com.resume.project.linkverse.dto;

import com.resume.project.linkverse.entity.Team;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;
import java.util.stream.Collectors;

public class TeamDtos {

    public record CreateTeamRequestDto(
            @NotEmpty(message = "Team name is required")
            String name,
            String description
    ) {}

    public record TeamResponseDto(
            Long id,
            String name,
            String description,
            Set<AuthDtos.UserDto> members
    ) {
        public static TeamResponseDto fromTeam(Team team) {
            Set<AuthDtos.UserDto> memberDtos = team.getMembers().stream()
                    .map(AuthDtos.UserDto::fromUser)
                    .collect(Collectors.toSet());

            return new TeamResponseDto(
                    team.getId(),
                    team.getName(),
                    team.getDescription(),
                    memberDtos
            );
        }
    }
}