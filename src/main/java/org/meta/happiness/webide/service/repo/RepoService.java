package org.meta.happiness.webide.service.repo;


import lombok.RequiredArgsConstructor;
import org.meta.happiness.webide.dto.repo.RepoCreateRequestDto;
import org.meta.happiness.webide.dto.repo.RepoDto;
import org.meta.happiness.webide.dto.repo.RepoUpdateNameRequestDto;
import org.meta.happiness.webide.entity.userrepo.UserRepo;
import org.meta.happiness.webide.entity.repo.Repo;
import org.meta.happiness.webide.entity.user.User;
import org.meta.happiness.webide.repository.groupuser.UserRepoRepository;
import org.meta.happiness.webide.repository.repo.RepoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RepoService {

    private final RepoRepository repoRepository;
    private final UserRepoRepository userRepoRepository;

    @Transactional
    public RepoDto createRepository(RepoCreateRequestDto repoCreateRequestDto, User creator) {

        Repo repo = Repo.createRepo(
                repoCreateRequestDto.getName(),
                repoCreateRequestDto.getProgrammingLanguage(),
                creator
        );

        // TODO: EFS 사용 시 -> 엑세스 포인트 생성
        // TODO: S3 사용 시 -> 파일 변환 로직 + access point

        repoRepository.save(repo);
        userRepoRepository.save(new UserRepo(repo, creator));

        return RepoDto.of(repo);
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
