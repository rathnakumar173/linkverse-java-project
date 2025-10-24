package com.resume.project.linkverse.controller;

import com.resume.project.linkverse.dto.LinkDtos;
import com.resume.project.linkverse.service.LinkService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/links")
@RequiredArgsConstructor
public class LinkController {

    private final LinkService linkService;


    @PostMapping
    public ResponseEntity<LinkDtos.LinkResponseDto> createLink(@Valid @RequestBody LinkDtos.CreateLinkRequestDto requestDto) {
        LinkDtos.LinkResponseDto newLink = linkService.createLink(requestDto);
        return new ResponseEntity<>(newLink, HttpStatus.CREATED);
    }

    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<LinkDtos.LinkResponseDto>> getLinksByTeam(@PathVariable Long teamId) {
        List<LinkDtos.LinkResponseDto> links = linkService.getLinksByTeam(teamId);
        return ResponseEntity.ok(links);
    }


    @GetMapping("/{linkId}")
    public ResponseEntity<LinkDtos.LinkResponseDto> getLinkById(@PathVariable Long linkId) {
        LinkDtos.LinkResponseDto link = linkService.getLinkById(linkId);
        return ResponseEntity.ok(link);
    }

    @PutMapping("/{linkId}")
    public ResponseEntity<LinkDtos.LinkResponseDto> updateLink(@PathVariable Long linkId,
                                                               @Valid @RequestBody LinkDtos.UpdateLinkRequestDto requestDto) {
        LinkDtos.LinkResponseDto updatedLink = linkService.updateLink(linkId, requestDto);
        return ResponseEntity.ok(updatedLink);
    }


    @DeleteMapping("/{linkId}")
    public ResponseEntity<Void> deleteLink(@PathVariable Long linkId) {
        linkService.deleteLink(linkId);
        return ResponseEntity.noContent().build(); // Standard 204 No Content response
    }
}