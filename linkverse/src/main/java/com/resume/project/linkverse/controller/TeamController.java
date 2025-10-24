package com.resume.project.linkverse.controller;

import com.resume.project.linkverse.dto.TeamDtos;
import com.resume.project.linkverse.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;


    @PostMapping
    public ResponseEntity<TeamDtos.TeamResponseDto> createTeam(@Valid @RequestBody TeamDtos.CreateTeamRequestDto requestDto) {
        TeamDtos.TeamResponseDto newTeam = teamService.createTeam(requestDto);
        return new ResponseEntity<>(newTeam, HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<List<TeamDtos.TeamResponseDto>> getMyTeams() {
        List<TeamDtos.TeamResponseDto> teams = teamService.getMyTeams();
        return ResponseEntity.ok(teams);
    }


    @GetMapping("/{teamId}")
    public ResponseEntity<TeamDtos.TeamResponseDto> getTeamById(@PathVariable Long teamId) {
        TeamDtos.TeamResponseDto team = teamService.getTeamDetails(teamId);
        return ResponseEntity.ok(team);
    }


    @PostMapping("/{teamId}/add-member/{userId}")
    public ResponseEntity<TeamDtos.TeamResponseDto> addMemberToTeam(@PathVariable Long teamId, @PathVariable Long userId) {
        TeamDtos.TeamResponseDto updatedTeam = teamService.addMemberToTeam(teamId, userId);
        return ResponseEntity.ok(updatedTeam);
    }
}