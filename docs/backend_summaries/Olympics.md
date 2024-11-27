# Olympics 관련 로직 구현 리뷰

## 목차
- [주요 코드 구조 및 로직](#주요-코드-구조-및-로직)
- [작업 내역](#작업-내역)
- [각 파일의 내용](#각-파일의-내용)

## 주요 코드 구조 및 로직

### Swagger API 이미지

![be_olympics.png](../../media/backend/be_olympics.png)

- **올림픽 팀 관리**: `OlympicsController`와 `PlayerService`를 통해 올림픽 팀을 생성, 조회, 삭제할 수 있는 기능이 구현되어 있습니다.
- **데이터베이스 연동**: `PlayerDao`와 `PlayerMapper.xml`을 이용해 MyBatis로 데이터베이스와 상호작용하며, `Player` 및 `OlympicsSetup` 객체와 관련된 데이터 CRUD 작업을 수행합니다.
- **DTO 및 서비스 계층 구성**: `OlympicsSetup` 및 `Player` DTO를 통해 데이터 구조를 정의하며, `PlayerServiceImpl`을 통해 서비스 계층에서 비즈니스 로직을 처리합니다.

## 작업 내역

- `OlympicsController.java`
- `PlayerDao.java`
- `OlympicsSetup.java`
- `Player.java`
- `PlayerMapper.xml`
- `PlayerService.java`
- `PlayerServiceImpl.java`

## 각 파일의 내용

- controller   
    `OlympicsController`는 올림픽 팀의 생성, 조회, 삭제 등 주요 기능을 처리합니다.
    
    - **팀 생성 (POST /olympics/team)**  
        올림픽 팀을 생성하고 플레이어 목록을 추가하는 엔드포인트입니다. 세션에서 로그인한 사용자 ID를 가져와 사용하며, 예외 발생 시 에러 메시지를 반환합니다.
    
    - **팀 조회 (GET /olympics/team/{olympic_id})**  
        특정 올림픽 팀의 모든 플레이어 목록을 조회하여 반환합니다.
    
    - **팀 삭제 (DELETE /olympics/team/{olympic_id})**  
        특정 팀의 생성자와 세션 사용자 ID가 일치하는 경우에만 팀을 삭제할 수 있습니다.

- service   
    `PlayerServiceImpl` 파일에서는 팀 생성, 플레이어 일괄 추가, 팀 삭제 등의 로직이 구현되어 있습니다.
    
    - **팀 생성 로직**  
        - `insertOlympics` 메서드를 통해 팀을 생성하고, `OlympicsSetup` 객체를 데이터베이스에 삽입하여 새 팀을 만듭니다.
    
    - **플레이어 일괄 추가**  
        - `insertPlayers` 메서드를 통해 여러 플레이어 정보를 한 번에 데이터베이스에 추가합니다.
    
    - **팀 삭제**  
        - `deleteOlympics` 메서드를 통해 해당 팀을 삭제하며, 성공 여부를 반환합니다.

- dao   
    `PlayerDao`는 데이터베이스와 직접 상호작용하는 인터페이스로, 올림픽 팀 및 플레이어 데이터 CRUD 메서드를 제공합니다.
    
    - **주요 메서드**:
        - `insertOlympics(OlympicsSetup olympics)`: 올림픽 팀 생성
        - `insertPlayers(List<Player> players)`: 다수의 플레이어 추가
        - `getPlayersByOlympicsId(int olympics_id)`: 특정 팀의 선수 목록 조회
        - `deleteOlympics(int id)`: 팀 삭제

- mapper   
    `PlayerMapper.xml` 파일은 MyBatis SQL 매핑 파일로, 데이터베이스와의 SQL 쿼리 매핑을 제공합니다.
    
    - **올림픽 팀 생성**: `insertOlympics`는 새 올림픽 팀 데이터를 `olympics` 테이블에 삽입합니다.
    - **올림픽 팀에 플레이어 추가**: `insertPlayers`: 다수의 플레이어를 `players` 테이블에 삽입합니다.
    - **특정 팀의 선수 목록 조회**: `getPlayersByOlympicsId`: 특정 팀에 속한 선수 목록을 조회합니다.
    - **주요 매핑**: `deleteOlympics`: 특정 팀을 삭제합니다.

- dto   
    `OlympicsSetup`: 팀 생성에 필요한 정보인 사용자 ID, 팀 이름, 플레이어 이름 목록 등을 저장하는 DTO입니다.
    - `Player`: 개별 선수 정보를 저장하며, `olympicsId`, `playerId`, `playerName`, `totalScore`, `regDate` 등의 필드를 포함합니다.
