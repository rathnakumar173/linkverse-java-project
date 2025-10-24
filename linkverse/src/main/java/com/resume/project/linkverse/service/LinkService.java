package com.resume.project.linkverse.service;

import com.resume.project.linkverse.dto.LinkDtos;
import com.resume.project.linkverse.entity.Link;
import com.resume.project.linkverse.entity.Tag;
import com.resume.project.linkverse.entity.Team;
import com.resume.project.linkverse.entity.User;
import com.resume.project.linkverse.exception.ResourceNotFoundException;
import com.resume.project.linkverse.repository.LinkRepository;
import com.resume.project.linkverse.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LinkService {

    private final LinkRepository linkRepository;
    private final TagRepository tagRepository;
    private final TeamService teamService;
    private final AuthService authService;


    @Transactional
    public LinkDtos.LinkResponseDto createLink(LinkDtos.CreateLinkRequestDto requestDto) {
        User currentUser = authService.getCurrentAuthenticatedUser();
        Team team = teamService.getTeamById(requestDto.teamId());

        teamService.checkIfUserIsMember(team, currentUser);

        Set<Tag> tags = findOrCreateTags(requestDto.tags());

        Link link = new Link();
        link.setTitle(requestDto.title());
        link.setUrl(requestDto.url());
        link.setTeam(team);
        link.setCreator(currentUser);
        link.setTags(tags);

        Link savedLink = linkRepository.save(link);

        return LinkDtos.LinkResponseDto.fromLink(savedLink);
    }


    public List<LinkDtos.LinkResponseDto> getLinksByTeam(Long teamId) {
        User currentUser = authService.getCurrentAuthenticatedUser();
        Team team = teamService.getTeamById(teamId);

        teamService.checkIfUserIsMember(team, currentUser);

        List<Link> links = linkRepository.findByTeamId(teamId);
        return links.stream()
                .map(LinkDtos.LinkResponseDto::fromLink)
                .collect(Collectors.toList());
    }

    public LinkDtos.LinkResponseDto getLinkById(Long linkId) {
        User currentUser = authService.getCurrentAuthenticatedUser();
        Link link = getLinkByIdOrThrow(linkId);

        teamService.checkIfUserIsMember(link.getTeam(), currentUser);

        return LinkDtos.LinkResponseDto.fromLink(link);
    }


    @Transactional
    public LinkDtos.LinkResponseDto updateLink(Long linkId, LinkDtos.UpdateLinkRequestDto requestDto) {
        User currentUser = authService.getCurrentAuthenticatedUser();
        Link link = getLinkByIdOrThrow(linkId);

        teamService.checkIfUserIsMember(link.getTeam(), currentUser);

        if (!link.getCreator().equals(currentUser)) {
            throw new AccessDeniedException("You are not the creator of this link.");
        }

        Set<Tag> tags = findOrCreateTags(requestDto.tags());

        link.setTitle(requestDto.title());
        link.setUrl(requestDto.url());
        link.setTags(tags);

        Link updatedLink = linkRepository.save(link);
        return LinkDtos.LinkResponseDto.fromLink(updatedLink);
    }


    @Transactional
    public void deleteLink(Long linkId) {
        User currentUser = authService.getCurrentAuthenticatedUser();
        Link link = getLinkByIdOrThrow(linkId);

        teamService.checkIfUserIsMember(link.getTeam(), currentUser);

        if (!link.getCreator().equals(currentUser)) {
            throw new AccessDeniedException("You are not the creator of this link.");
        }

        linkRepository.delete(link);
    }



    private Link getLinkByIdOrThrow(Long linkId) {
        return linkRepository.findById(linkId)
                .orElseThrow(() -> new ResourceNotFoundException("Link not found with id: " + linkId));
    }


    private Set<Tag> findOrCreateTags(Set<String> tagNames) {
        if (tagNames == null || tagNames.isEmpty()) {
            return new HashSet<>();
        }

        Set<Tag> tags = new HashSet<>();
        for (String tagName : tagNames) {
            Tag tag = tagRepository.findByNameIgnoreCase(tagName.trim())
                    .orElseGet(() -> tagRepository.save(new Tag(tagName.trim().toLowerCase())));
            tags.add(tag);
        }
        return tags;
    }
}