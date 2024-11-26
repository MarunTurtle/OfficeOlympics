# Challenges 관련 로직 구현 리뷰

## 목차
- [주요 코드 구조 및 로직](#주요-코드-구조-및-로직)
- [작업 내역](#작업-내역)
- [각 파일의 내용](#각-파일의-내용)

## 주요 코드 구조 및 로직

### Swagger API 이미지

![be_challenges.png](../media/backend/be_challenges.png)

- **챌린지 관리**: `ChallengeController`와 `ChallengeScoreService`를 통해 챌린지의 생성, 조회, 기록 제출, 리더보드 조회 기능이 구현되어 있습니다.
- **데이터베이스 연동**: `ChallengeScoreDao`와 `ChallengeScoreMapper.xml`을 이용해 MyBatis로 데이터베이스와 연동하여 챌린지와 관련된 데이터 조작을 수행합니다.

## 작업 내역

- `ChallengeController.java`
- `ChallengeScoreDao.java`
- `ChallengeScoreMapper.xml`
- `Challenge.java`, `Score.java`, `Rank.java` (DTO)
- `ChallengeScoreService.java`
- `ChallengeScoreServiceImpl.java`

## 각 파일의 내용

- controller   
    `ChallengeController`는 챌린지의 세부 정보 조회, 기록 제출, 리더보드 조회 등 주요 기능을 처리합니다.

    - **챌린지 세부 정보 조회 (GET /challenges/{challengeId})**  
        특정 `challengeId`를 가진 챌린지의 세부 정보를 반환합니다. 챌린지가 없을 경우 204 상태 코드를 반환합니다.

    - **기록 제출 (POST /challenges/{challengeId}/record)**  
        특정 챌린지에 점수를 제출하는 엔드포인트입니다. 플레이어 이름과 점수를 JSON 형식으로 전달하며, 성공 시 DB에 총 점수를 갱신합니다.

    - **챌린지 리더보드 조회 (GET /challenges/{challengeId}/rank)**  
        특정 챌린지의 현재 리더보드를 반환합니다. 상위 3명의 플레이어를 등수와 함께 조회할 수 있습니다.

    - **최종 리더보드 조회 (GET /challenges/{challengeId}/final-rank)**  
        올림픽이 종료되었을 때 해당 챌린지의 최종 리더보드를 조회하여 반환합니다.

- service   
    `ChallengeScoreServiceImpl` 파일에서는 챌린지 조회, 점수 기록, 점수 업데이트 및 리더보드 조회 로직이 구현되어 있습니다.

    - **챌린지 조회 로직**  
        - `selectChallenge` 메서드를 통해 `challengeId`를 기반으로 챌린지 정보를 조회합니다.

    - **기록 제출 로직**  
        - `insertScores` 메서드를 통해 플레이어 이름을 ID로 변환 후 점수를 데이터베이스에 기록합니다.

    - **점수 갱신 로직**  
        - `updateScore` 메서드를 통해 각 플레이어의 총 점수를 갱신합니다.

    - **리더보드 조회 로직**  
        - `selectChallengeScore`와 `selectFinalScore` 메서드를 통해 챌린지의 현재 및 최종 리더보드를 조회하여 반환합니다.

- dao   
    `ChallengeScoreDao`는 챌린지와 관련된 데이터베이스 연산을 정의하는 인터페이스입니다. 

    - **주요 메서드**:
        - `selectChallenge`: `challengeId`로 특정 챌린지를 조회
        - `nameToId`: 플레이어 이름 리스트를 받아 해당 이름에 맞는 `playerId` 리스트를 반환
        - `insertScore`: 챌린지 점수 데이터를 받아 다수의 레코드를 삽입
        - `updateScore`: 각 플레이어의 `total_score`를 갱신
        - `getPlayersByOlympicsId(int olympics_id)`: 특정 팀의 선수 목록 조회
        - `selectChallengeScore`, `selectFinalScore`, `selectMainScore`: 챌린지 리더보드를 조회하여 순위를 반환

- mapper   
    `ChallengeScoreMapper.xml` 파일은 MyBatis SQL 매핑 파일로, 챌린지 데이터 조회, 점수 기록, 점수 업데이트, 리더보드 조회 쿼리들이 포함됩니다.

    - **챌린지 조회 쿼리**: `selectChallenge`는 `challengeId`로 챌린지를 조회합니다.
    - **이름->ID 변환 쿼리**: `nameToId`는 이름을 `playerId`로 변환해 리스트로 반환합니다.
    - **기록 제출 쿼리**: `insertScore`는 챌린지 점수를 여러 개 삽입합니다.
    - **점수 갱신 쿼리**: `updateScore`는 각 플레이어의 `total_score`를 갱신합니다.
    - **리더보드 조회 쿼리**: `selectChallengeScore`, `selectFinalScore`, `selectMainScore`는 각 상황에 맞는 순위를 반환합니다.

- dto   
    - **Challenge.java**: `challengeId`, `challengeName`, `challengeDesc`, `challengeUrl`, `regDate`를 포함하는 챌린지 정보를 나타냅니다.
    - **Score.java**: `scoreId`, `challengeId`, `playerNames`, `playerId`, `scores`, `regDate`를 포함하며, 챌린지 점수와 관련된 데이터를 나타냅니다.
    - **Rank.java**: 순위 정보를 담고 있으며, `rank`, `playerName`, `score` 속성을 포함하여 리더보드 표시 시 사용됩니다.