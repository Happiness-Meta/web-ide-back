package org.meta.happiness.webide.service.repo;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meta.happiness.webide.dto.repo.RepoCreateRequestDto;
import org.meta.happiness.webide.dto.repo.RepoDeleteRequestDto;
import org.meta.happiness.webide.dto.repo.RepoResponseDto;
import org.meta.happiness.webide.dto.repo.RepoUpdateNameRequestDto;
import org.meta.happiness.webide.entity.userrepo.UserRepo;
import org.meta.happiness.webide.entity.repo.Repo;
import org.meta.happiness.webide.entity.user.User;
import org.meta.happiness.webide.exception.RepoNotFoudException;
import org.meta.happiness.webide.exception.UserNotFoundException;
import org.meta.happiness.webide.repository.user.UserRepository;
import org.meta.happiness.webide.repository.userrepo.UserRepoRepository;
import org.meta.happiness.webide.repository.repo.RepoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RepoService {

    private final UserRepository userRepository;
    private final RepoRepository repoRepository;
    private final UserRepoRepository userRepoRepository;

    private final S3RepoService s3RepoService;

    public static final String DELIMITER = "/";

    public static String createRepoPathPrefix(String repo) {
        return ("repo" + DELIMITER + repo + DELIMITER);
    }

    @Transactional
    public RepoResponseDto createRepository(RepoCreateRequestDto request) {
        User creator = userRepository.findByEmail(request.getUserEmail())
                .orElseThrow(UserNotFoundException::new);

        Repo repo = Repo.createRepo(request, creator);
        Repo savedRepo = repoRepository.save(repo);

        UserRepo userRepo = UserRepo.addUserRepo(repo, creator);
        UserRepo savedUserRepo = userRepoRepository.save(userRepo);

        log.info("saved repo >>>>>> {}", savedRepo.getId());

        s3RepoService.createRepository(
                createRepoPathPrefix(savedRepo.getId())
        );

        return RepoResponseDto.builder()
                .id(savedRepo.getId())
                .name(savedRepo.getName())
                .programmingLanguage(savedRepo.getProgrammingLanguage())
                .createdAt(savedRepo.getCreatedDate())
                .modifiedAt(savedRepo.getLastModifiedDate())
                .build();
    }

    public RepoResponseDto findRepo(String repoId, Long userId) {
        // TODO: 들어온 user에게 권한이 있는지 확인해야 함..?

        return RepoResponseDto.convertRepoToDto(repoRepository.findById(repoId).orElseThrow(RepoNotFoudException::new));
    }

    @Transactional
    public RepoResponseDto updateRepositoryName(String repoId, RepoUpdateNameRequestDto request, User user) {
        // TODO: 들어온 user에게 권한이 있는지 확인해야 함

        Repo targetRepo = repoRepository.findById(repoId)
                .orElseThrow(() -> new IllegalArgumentException("레포지토리가 존재하지 않음"));


        targetRepo.changeName(request.getUpdatedName());
        return RepoResponseDto.convertRepoToDto(targetRepo);
    }

    @Transactional
    public void deleteRepository(String repoId, String email) {
        User creator = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
        Repo repo = repoRepository.findById(repoId)
                .orElseThrow(() -> new IllegalArgumentException("레포지토리가 존재하지 않습니다."));

        if(!repo.getCreator().equals(creator)){
            throw new IllegalArgumentException("프로젝트 생성자가 아님.");
        }

        UserRepo userRepo = userRepoRepository.findByUserAndRepo(creator, repo).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않음")
        );

        String repositoryPath = createRepoPathPrefix(repo.getId());
        s3RepoService.deleteRepository(
                repositoryPath
        );

        userRepoRepository.delete(userRepo);
        repoRepository.delete(repo);
    }

    @Transactional(readOnly = true)
    public List<RepoResponseDto> findAllRepoByUser(Long userId) {
        // 1. 사용자 ID를 이용하여 해당 사용자를 데이터베이스에서 조회
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 userId를 찾을 수 없습니다." + userId));

        // 2. 사용자가 참여한 모든 레포지토리를 데이터베이스에서 조회
        List<UserRepo> userRepos = userRepoRepository.findByUser(findUser);

        // 3. 조회된 레포지토리들을 RepoResponseDto 형태로 변환하여 리스트에 담아 반환
        return userRepos.stream()
                .map(userRepo -> {
                    Repo repo = userRepo.getRepo();
                    return new RepoResponseDto(repo.getId(), repo.getCreator(),
                            repo.getName(), repo.getProgrammingLanguage(),
                            repo.getCreatedDate(), repo.getLastModifiedDate());
                })
                .collect(Collectors.toList());
    }


//    @Transactional(readOnly = true)
//    public List<RepoResponseDto> findRecentRepoByUser(Long userId) {
//        User findUser = userRepository.findById(userId)
//                .orElseThrow(() -> new IllegalArgumentException("해당 userId를 찾을 수 없습니다." + userId));
//
//        List<UserRepo> recentUserRepos = userRepoRepository.findTop2ByUserOrderByCreatedAtDesc(findUser);
//
//        // 3. 조회된 최근 사용 레포지토리들을 RepoResponseDto 형태로 변환하여 리스트에 담아 반환
//        return recentUserRepos.stream()
//                .map(userRepo -> {
//                    Repo repo = userRepo.getRepo();
//                    return new RepoResponseDto(repo.getId(), repo.getCreator(),
//                            repo.getName(), repo.getProgrammingLanguage(),
//                            repo.getCreatedDate(), repo.getLastModifiedDate());
//                })
//                .collect(Collectors.toList());return userRepoRepository.findByUserAndRepo(user, repo).stream()
//                .map(RepoResponseDto::convertRepoToDto)
//                .collect(Collectors.toList());
//    }

}
