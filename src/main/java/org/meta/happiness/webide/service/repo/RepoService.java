package org.meta.happiness.webide.service.repo;


import lombok.RequiredArgsConstructor;
import org.meta.happiness.webide.dto.repo.RepoCreateRequestDto;
import org.meta.happiness.webide.dto.repo.RepoResponseDto;
import org.meta.happiness.webide.dto.repo.RepoUpdateNameRequestDto;
import org.meta.happiness.webide.entity.userrepo.UserRepo;
import org.meta.happiness.webide.entity.repo.Repo;
import org.meta.happiness.webide.entity.user.User;
import org.meta.happiness.webide.repository.userrepo.UserRepoRepository;
import org.meta.happiness.webide.repository.repo.RepoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RepoService {

    private final RepoRepository repoRepository;
    private final UserRepoRepository userRepoRepository;

    @Transactional
    public RepoResponseDto createRepository(RepoCreateRequestDto request, User creator) {

        Long creatorUserId = creator.getId();
        Repo repo = Repo.createRepo(request, creator);
        Repo savedRepo = repoRepository.save(repo);

        userRepoRepository.save(new UserRepo(repo, creator));

//        return RepoDto.of(repo);
        return new RepoResponseDto(savedRepo.getId(), creatorUserId,
                savedRepo.getName(), savedRepo.getProgrammingLanguage(),
                savedRepo.getCreatedDate(), savedRepo.getLastModifiedDate());
    }

    @Transactional
    public void updateRepositoryName(String repoId, RepoUpdateNameRequestDto repoUpdateNameRequestDto){
        Repo targetRepo = repoRepository.findById(repoId)
                .orElseThrow(()-> new IllegalArgumentException("레포지토리가 존재하지 않음"));

        targetRepo.setName(repoUpdateNameRequestDto.getUpdatedName());
    }

    @Transactional
    public void deleteRepository(String repoId, User user) {

        // TODO: 들어온 user에게 권한이 있는지 확인해야 함

        Repo repo = repoRepository.findById(repoId)
                .orElseThrow(() -> new IllegalArgumentException("레포지토리가 존재하지 않습니다."));

        repoRepository.delete(repo);
    }
}
