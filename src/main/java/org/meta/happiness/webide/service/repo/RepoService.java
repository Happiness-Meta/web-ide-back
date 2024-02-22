package org.meta.happiness.webide.service.repo;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.meta.happiness.webide.dto.S3ObjectAndContent;
import org.meta.happiness.webide.dto.file.FileDto;
import org.meta.happiness.webide.dto.repo.*;

import org.meta.happiness.webide.dto.response.RepoGetAllFilesResponse;
import org.meta.happiness.webide.dto.response.RepoTreeResponse;
import org.meta.happiness.webide.entity.FileMetaData;
import org.meta.happiness.webide.entity.userrepo.UserRepo;
import org.meta.happiness.webide.entity.repo.Repo;
import org.meta.happiness.webide.entity.user.User;
import org.meta.happiness.webide.exception.IsNotUserInviteRepo;
import org.meta.happiness.webide.exception.RepoNotFoundException;
import org.meta.happiness.webide.exception.UserNotFoundException;
import org.meta.happiness.webide.repository.filemetadata.FileMetaDataRepository;
import org.meta.happiness.webide.repository.repo.S3RepoRepository;
import org.meta.happiness.webide.repository.user.UserRepository;
import org.meta.happiness.webide.repository.userrepo.UserRepoRepository;
import org.meta.happiness.webide.repository.repo.RepoRepository;
import org.meta.happiness.webide.security.JwtUtil;
import org.meta.happiness.webide.service.file.FileService;
import org.meta.happiness.webide.service.file.S3FileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RepoService {

    private final UserRepository userRepository;
    private final RepoRepository repoRepository;
    private final UserRepoRepository userRepoRepository;

    private final S3FileService s3FileService;
    private final S3RepoService s3RepoService;
    private final S3RepoRepository s3RepoRepository;

    private final FileService fileService;
    private final FileMetaDataRepository fileMetaDataRepository;

    private final JwtUtil jwtUtil;

    public static final String DELIMITER = "/";

    public static String createRepoPathPrefix(String repo) {
        return ("repo" + DELIMITER + repo + DELIMITER);
    }

    @Transactional
    public RepoResponseDto createRepository(RepoCreateRequestDto request, String userEmail) {
        User creator = userRepository.findByEmail(userEmail)
                .orElseThrow(UserNotFoundException::new);

        Repo repo = Repo.createRepo(request, creator);
        Repo savedRepo = repoRepository.save(repo);

        UserRepo userRepo = UserRepo.addUserRepo(repo, creator);
        UserRepo savedUserRepo = userRepoRepository.save(userRepo);

        log.info("saved repo >>>>>> {}", savedRepo.getId());

        s3RepoService.createRepository(
                createRepoPathPrefix(savedRepo.getId())
        );

        fileService.createFile(savedRepo.getId(), "README.md");
        FileMetaData fileMetaData = fileMetaDataRepository.findByRepoAndPath(savedRepo, "README.md")
                .orElseThrow(() -> new IllegalArgumentException("path 오류"));

        s3FileService.updateFile(savedRepo.getId(), fileMetaData.getId(), "# Hello! This is README");


        return RepoResponseDto.builder()
                .id(savedRepo.getId())
                .name(savedRepo.getName())
                .programmingLanguage(savedRepo.getProgrammingLanguage())
                .createdAt(savedRepo.getCreatedDate())
                .modifiedAt(savedRepo.getLastModifiedDate())
                .build();
    }

    @Transactional(readOnly = true)
    public RepoResponseDto findRepo(String repoId, String userEmail) {


        User findUser = userRepository.findByEmail(userEmail)
                .orElseThrow(UserNotFoundException::new);


        Repo findRepo = repoRepository.findById(repoId)
                .orElseThrow(RepoNotFoundException::new);

        if(!userRepoRepository.existsByRepoAndUser(findRepo, findUser)){
            throw new IsNotUserInviteRepo();
        }

        return RepoResponseDto.convertRepoToDto(repoRepository.findById(repoId).orElseThrow(RepoNotFoundException::new));
    }

    @Transactional
    public void invite(String requestPassword, String repoId, String userEmail){
        User findUser = userRepository.findByEmail(userEmail)
                .orElseThrow(UserNotFoundException::new);

        Repo findRepo = repoRepository.findById(repoId)
                .orElseThrow(RepoNotFoundException::new);

        if(userRepoRepository.existsByRepoAndUser(findRepo, findUser)){
            return;
        }

        if(findRepo.getPassword().equals(requestPassword)) {
            userRepoRepository.save(UserRepo.addUserRepo(findRepo, findUser));
        }
        else {
            throw new IllegalArgumentException("repo의 비밀번호 불일치..");
        }
    }

    public RepoResponseDto findRepo(String repoId, Long userId) {
        // TODO: 들어온 user에게 권한이 있는지 확인해야 함..?

        return RepoResponseDto.convertRepoToDto(repoRepository.findById(repoId).orElseThrow(RepoNotFoundException::new));
    }

    @Transactional
    public RepoResponseDto updateRepositoryName(String repoId, RepoUpdateNameRequestDto request, String userEmail) {
        // TODO: 들어온 user에게 권한이 있는지 확인해야 함
        User creator = userRepository.findByEmail(userEmail)
                .orElseThrow(UserNotFoundException::new);

        Repo targetRepo = repoRepository.findById(repoId)
                .orElseThrow(() -> new IllegalArgumentException("레포지토리가 존재하지 않음"));

        if (!targetRepo.getCreator().equals(creator)){
            throw new IllegalArgumentException("생성자가 아님");
        }

        targetRepo.changeName(request.getUpdatedName());
        return RepoResponseDto.convertRepoToDto(targetRepo);
    }

    @Transactional
    public void deleteRepository(String repoId, String userEmail) {
        User creator = userRepository.findByEmail(userEmail)
                .orElseThrow(UserNotFoundException::new);

        Repo repo = repoRepository.findById(repoId)
                .orElseThrow(() -> new IllegalArgumentException("레포지토리가 존재하지 않습니다."));

        if (!repo.getCreator().equals(creator)) {
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
    public List<RepoResponseDto> findAllRepoByUser(String userEmail) {
        // 1. 사용자 ID를 이용하여 해당 사용자를 데이터베이스에서 조회
        User findUser = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("해당 userId를 찾을 수 없습니다." + userEmail));
        // 2. 사용자가 참여한 모든 레포지토리를 데이터베이스에서 조회
        List<UserRepo> userRepos = userRepoRepository.findByUser(findUser);

        // 3. 조회된 레포지토리들을 RepoResponseDto 형태로 변환하여 리스트에 담아 반환
        return userRepos.stream()
                .map(userRepo -> {
                    Repo repo = userRepo.getRepo();
                    log.info("repo name >>> {}", repo.getName());
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

    @Transactional
    public RepoGetAllFilesResponse getAllFilesFromRepo(String repoId){
        Repo targetRepo = repoRepository.findById(repoId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 레포"));
        List<FileMetaData> s3files = targetRepo.getS3fileMetadata();

        if(s3files.isEmpty()){
            RepoGetAllFilesResponse.builder()
                    .fileData(Collections.emptyList())
                    .treeData(RepoTreeResponse.builder().build())
                    .build();
        }

        List<FileDto> fileData = s3files.stream()
                .map((fileMetaData -> {
                    log.info(fileMetaData.getId());
                    return toFileResponse(repoId, fileMetaData);
                }))
                .toList();
//        List<String> s3Keys = fileData.stream().map(FileDto::getFilePath)
//                        .toList();

        RepoTreeResponse fileTree = RepoTreeResponse.buildTreeFromKeys(fileData);

        return RepoGetAllFilesResponse.builder()
                .fileData(fileData)
                .treeData(fileTree)
                .build();
    }


    private FileDto toFileResponse(String repoId, FileMetaData metaData) {
        FileDto fileDto = FileDto.builder()
                .uuid(metaData.getId())
                .filePath(metaData.getPath())
                .content(s3RepoRepository.getFileContent(repoId, metaData.getId()))
                .build();

        log.info("?>>>?>>>?>>> {}", fileDto.getFilePath());
        log.info("?>>>?>>>?>>> {}", fileDto.getContent());

        return fileDto;
    }
    public RepoInviteResponseDto findRepoInviteInfo(String repoId, String userEmail) {
        Repo findRepo = repoRepository.findById(repoId).orElseThrow(RepoNotFoundException::new);
        if(!findRepo.getCreator().getEmail().equals(userEmail)){
            throw new IllegalArgumentException("Creator 아님!");
        }

        return RepoInviteResponseDto.convertRepoToInvite(findRepo);
    }

    @Transactional
    public RepoResponseDto createTemplateRepository(RepoTemplateCreateRequestDto request, String userEmail) {
        User creator = userRepository.findByEmail(userEmail)
                .orElseThrow(UserNotFoundException::new);

        Repo repo = Repo.createRepo(request, creator);
        Repo savedRepo = repoRepository.save(repo);

        UserRepo userRepo = UserRepo.addUserRepo(repo, creator);
        UserRepo savedUserRepo = userRepoRepository.save(userRepo);
        log.info("saved repo >>>>>> {}", savedRepo.getId());

        s3RepoService.createRepository(
                createRepoPathPrefix(savedRepo.getId())
        );

        List<S3ObjectAndContent> templateFiles = s3RepoRepository.getTemplateFiles();
        for (S3ObjectAndContent templateFile : templateFiles) {
//            log.info("s3 >>> {}", templateFile.getS3Object().toString());
//            log.info("s3 contents >>> {}", templateFile.getContent());
            String key = templateFile.getS3Object().key().split(DELIMITER)[1];
            log.info("s3 key remove root todo/ >>> {}", key);

            fileService.createFile(savedRepo.getId(), key);
            FileMetaData fileMetaData = fileMetaDataRepository.findByRepoAndPath(savedRepo, key)
                    .orElseThrow(() -> new IllegalArgumentException("path 오류"));

            s3FileService.updateFile(savedRepo.getId(), fileMetaData.getId(), templateFile.getContent());
        }

        return RepoResponseDto.builder()
                .id(savedRepo.getId())
                .name(savedRepo.getName())
                .programmingLanguage(savedRepo.getProgrammingLanguage())
                .createdAt(savedRepo.getCreatedDate())
                .modifiedAt(savedRepo.getLastModifiedDate())
                .build();
    }
}
