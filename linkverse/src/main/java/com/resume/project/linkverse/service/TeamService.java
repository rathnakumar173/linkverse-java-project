package com.resume.project.linkverse.service;

import com.resume.project.linkverse.dto.TeamDtos;
import com.resume.project.linkverse.entity.Team;
import com.resume.project.linkverse.entity.User;
import com.resume.project.linkverse.exception.ResourceNotFoundException;
import com.resume.project.linkverse.repository.TeamRepository;
import com.resume.project.linkverse.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final AuthService authService;


    @Transactional
    public TeamDtos.TeamResponseDto createTeam(TeamDtos.CreateTeamRequestDto requestDto) {
        User currentUser = authService.getCurrentAuthenticatedUser();

        Team team = new Team();
        team.setName(requestDto.name());
        team.setDescription(requestDto.description());
        team.getMembers().add(currentUser);

        Team savedTeam = teamRepository.save(team);

        currentUser.getTeams().add(savedTeam);
        userRepository.save(currentUser);

        return TeamDtos.TeamResponseDto.fromTeam(savedTeam);
    }


    @Transactional
    public TeamDtos.TeamResponseDto addMemberToTeam(Long teamId, Long userId) {
        User currentUser = authService.getCurrentAuthenticatedUser();
        Team team = getTeamById(teamId);

        checkIfUserIsMember(team, currentUser);

        User userToAdd = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        team.getMembers().add(userToAdd);
        userToAdd.getTeams().add(team);

        teamRepository.save(team);
        userRepository.save(userToAdd);

        return TeamDtos.TeamResponseDto.fromTeam(team);
    }


    public TeamDtos.TeamResponseDto getTeamDetails(Long teamId) {
        User currentUser = authService.getCurrentAuthenticatedUser();
        Team team = getTeamById(teamId);

        checkIfUserIsMember(team, currentUser);

        return TeamDtos.TeamResponseDto.fromTeam(team);
    }


    public List<TeamDtos.TeamResponseDto> getMyTeams() {
        User currentUser = authService.getCurrentAuthenticatedUser();
        List<Team> teams = teamRepository.findByMembers_Id(currentUser.getId());
        return teams.stream()
                .map(TeamDtos.TeamResponseDto::fromTeam)
                .collect(Collectors.toList());
    }



    public Team getTeamById(Long teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with id: " + teamId));
    }


    public void checkIfUserIsMember(Team team, User user) {
        if (!team.getMembers().contains(user)) {
            throw new AccessDeniedException("You are not a member of this team.");
        }
    }
}