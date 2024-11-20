-- 기존에 같은 이름의 데이터베이스가 있다면 삭제합니다.
DROP DATABASE IF EXISTS olympic_db;

-- 새로운 데이터베이스를 생성하고, 문자셋을 utf8mb4로 설정합니다.
CREATE DATABASE olympic_db DEFAULT CHARACTER SET utf8mb4;

-- olympic_db 데이터베이스를 사용합니다.
USE olympic_db;

-- 사용자 정보를 저장하는 테이블 생성
CREATE TABLE users (
  user_id INT AUTO_INCREMENT PRIMARY KEY, -- 사용자 ID (자동 증가)
  email VARCHAR(255) NOT NULL UNIQUE, -- 로그인 ID로 사용할 이메일 (중복 불가)
  password VARCHAR(128) NOT NULL, -- 비밀번호 (SHA-256 해시)
  salt VARCHAR(128), -- 솔트 (SHA-256 해싱처리시 필요)
  name VARCHAR(255) NOT NULL, -- 닉네임
  nickname VARCHAR(255) NOT NULL, -- 닉네임
  profile_img VARCHAR(255), -- 사용자가 등록한 이미지 이름
  img_src VARCHAR(255), -- 서버에 저장한 이름
  reg_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- 등록 날짜 (기본값: 현재 시간)
);

-- 올림픽 이벤트 정보를 저장하는 테이블 생성
CREATE TABLE olympics (
  olympics_id INT AUTO_INCREMENT PRIMARY KEY, -- 올림픽 ID (자동 증가)
  user_id INT NOT NULL, -- 올림픽 생성자 ID (외래키)
  olympics_name VARCHAR(255) NOT NULL, -- 올림픽 이름
  FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE NO ACTION -- 사용자 삭제 시 연쇄 삭제
);

-- 플레이어 정보를 저장하는 테이블 생성
CREATE TABLE players (
  player_id INT AUTO_INCREMENT PRIMARY KEY, -- 플레이어 ID (자동 증가)
  olympics_id INT, -- 참여한 올림픽 ID (외래키)
  player_name VARCHAR(255) NOT NULL, -- 플레이어 이름
  total_score INT DEFAULT 0, -- 누적 점수 (기본값: 0)
  reg_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 등록 날짜 (기본값: 현재 시간)
  FOREIGN KEY (olympics_id) REFERENCES olympics(olympics_id) ON DELETE CASCADE ON UPDATE NO ACTION -- 올림픽 삭제 시 연쇄 삭제
);

-- 챌린지 정보를 저장하는 테이블 생성
CREATE TABLE challenges (
  challenge_id INT AUTO_INCREMENT PRIMARY KEY, -- 챌린지 ID (자동 증가)
  challenge_name VARCHAR(255) NOT NULL, -- 챌린지 이름
  challenge_desc TEXT, -- 챌린지 설명
  challenge_url TEXT, -- 챌린지 영상 url
  reg_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- 등록 날짜 (기본값: 현재 시간)
);

-- 챌린지에서 획득한 점수를 저장하는 테이블 생성
CREATE TABLE challenge_scores (
  score_id INT AUTO_INCREMENT PRIMARY KEY, -- 점수 ID (자동 증가)
  challenge_id INT, -- 챌린지 ID (외래키)
  player_id INT, -- 플레이어 ID (외래키)
  score INT NOT NULL, -- 획득한 점수
  reg_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 등록 날짜 (기본값: 현재 시간)
  FOREIGN KEY (challenge_id) REFERENCES challenges(challenge_id) ON DELETE CASCADE ON UPDATE NO ACTION, -- 챌린지 삭제 시 연쇄 삭제
  FOREIGN KEY (player_id) REFERENCES players(player_id) ON DELETE CASCADE ON UPDATE NO ACTION -- 플레이어 삭제 시 연쇄 삭제
);

-- 챌린지에 대한 댓글을 저장하는 테이블 생성
CREATE TABLE comments (
  comment_id INT AUTO_INCREMENT PRIMARY KEY, -- 댓글 ID (자동 증가)
  user_id INT, -- 사용자 ID (외래키)
  challenge_id INT, -- 챌린지 ID (외래키)
  comment_depth INT DEFAULT 0, -- 0 이면 댓글, 1이면 대댓글
  comment_group INT, -- 댓글 그룹 (원댓글 ID)
  comment_text VARCHAR(255) NOT NULL, -- 댓글 내용
  reg_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 등록 날짜 (기본값: 현재 시간)
  update_date TIMESTAMP DEFAULT NULL, -- 수정 날짜 (NULL 가능)
  FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE NO ACTION, -- 사용자 삭제 시 연쇄 삭제
  FOREIGN KEY (challenge_id) REFERENCES challenges(challenge_id) ON DELETE CASCADE ON UPDATE NO ACTION -- 챌린지 삭제 시 연쇄 삭제
);

CREATE INDEX idx_comments_challenge_id ON comments(challenge_id);
CREATE INDEX idx_comments_user_id ON comments(user_id);
CREATE INDEX idx_users_user_id ON users(user_id);
