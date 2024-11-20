INSERT INTO `users` VALUES 
(1,'ssafy1@ssafy.com','87e310ef0dd31cb1f3250a608df890a34aafe7d57e2a618aad59c6a8abcfff4b','8iDvseT5+vOm4ONeeixwYA==','사용자1','닉네임1','profile1.png','0ab7bf98-b57f-4594-9773-ed939eecf45f.png','2024-11-18 11:18:37'),
(2,'ssafy2@ssafy.com','af16034ba4340164f0e1cc4bb3a12f10ea8331d399c4bef728acb13b6d97ff42','jfTalvGnu9+2HsGusI95kQ==','사용자2','닉네임2','profile2.png','a480e227-5ca9-4bd4-86a2-3243d5d604ee.png','2024-11-18 11:18:50'),
(3,'ssafy3@ssafy.com','1a31f7b07a6e3ab336c579b2867ac2e27a6bb2126124663311afcd07e4514a9a','ujaYVCyVY+GNglJMy+gEGQ==','사용자3','닉네임3','profile3.png','0421fe1e-5b60-4970-9709-8aa28763502c.png','2024-11-18 11:19:02');

INSERT INTO olympics (user_id, olympics_name) VALUES
(1, '팀 A 올림픽'),
(2, '팀 B 올림픽'),
(3, '팀 C 올림픽');

INSERT INTO players (olympics_id, player_name) VALUES
(1, '플레이어1'),
(1, '플레이어2'),
(1, '플레이어3'),
(2, '플레이어4'),
(2, '플레이어5'),
(2, '플레이어6'),
(3, '플레이어7'),
(3, '플레이어8'),
(3, '플레이어9');

INSERT INTO challenges (challenge_id, challenge_name, challenge_desc, challenge_url) VALUES
(1, '스테이플러 던지기', '참가자들은 스테이플러를 책상 끝으로 던져 가장 근접하게 도달한 사람이 승리합니다. 각 참가자는 3번의 기회를 가지며, 가장 좋은 점수만 기록됩니다. 안전을 위해 반드시 빈 스테이플러를 사용하세요.', 'https://youtu.be/dQw4w9WgXcQ'),
(2, '포스트잇 얼굴 붙이기', '60초 동안 얼굴에 최대한 많은 포스트잇을 붙이는 게임입니다. 포스트잇은 반드시 한 면만 접착되어야 하며, 떨어진 포스트잇은 점수에서 제외됩니다. 가장 많은 포스트잇을 붙인 사람이 승리합니다.', 'https://youtu.be/a3Z7zEc7AXQ'),
(3, '서류 더미 쌓기', '참가자들은 주어진 시간 동안 책상 위에 서류를 최대한 높이 쌓아야 합니다. 서류 더미가 무너지면 해당 라운드의 점수는 0점 처리됩니다. 안정성과 높이를 동시에 고려해야 합니다.', 'https://youtu.be/fzMJYPYVTlQ'),
(4, '의자 레이싱', '사무실 의자를 타고 정해진 코스를 완주하는 속도를 겨루는 게임입니다. 손으로 바닥을 밀거나 다리로 추진력을 이용할 수 있습니다. 코스에서 벗어나면 5초 패널티가 적용됩니다.', 'https://youtu.be/hQ4pHp0nm50'),
(5, '클립 체인 만들기', '5분 안에 클립을 연결하여 가장 긴 체인을 만들어야 합니다. 연결이 단단하지 않거나 체인이 끊어지면 점수에서 제외됩니다. 체인의 길이는 끝부터 끝까지 측정합니다.', 'https://youtu.be/CevxZvSJLk8'),
(6, '병뚜껑 알까기', '참가자는 병뚜껑으로 다른 병뚜껑을 튕겨서 뒤집어야 합니다. 각 참가자는 5개의 병뚜껑을 던질 수 있으며, 성공적으로 뒤집은 병뚜껑 수로 점수가 결정됩니다.', 'https://youtu.be/L_jWHffIx5E'),
(7, '종이비행기 대회', '종이비행기를 접어 가장 멀리 날리는 사람이 승리합니다. 각 참가자는 3번의 기회를 가지며, 비행기가 멈춘 지점까지의 거리를 측정합니다. 날리는 방향에 맞춰 장애물(의자, 책상)을 피해야 합니다.', 'https://youtu.be/EkHTsc9PU2A'),
(8, '사무용품 균형 잡기', '책, 마우스, 스테이플러 등 사무용품을 머리 위에 올리고 가장 오래 균형을 유지하는 사람이 승리합니다. 물건이 떨어지거나 손으로 잡으면 실격 처리됩니다.', 'https://youtu.be/tgbNymZ7vqY'),
(9, '책 던지기', '참가자들은 책을 던져 지정된 목표에 가장 근접하게 도달해야 합니다. 각 참가자는 3번의 기회를 가지며, 책이 목표에서 벗어날 경우 가장 가까운 점수를 기록합니다. 반드시 가벼운 책만 사용하세요.', 'https://youtu.be/9bZkp7q19f0'),
(10, '미니 농구', '작은 공이나 동그란 물체를 사용해 쓰레기통에 가장 많이 넣는 게임입니다. 각 참가자는 60초 동안 최대한 많은 성공을 기록해야 합니다. 쓰레기통이 넘어가면 해당 라운드는 무효 처리됩니다.', 'https://youtu.be/3JWTaaS7LdU'),
(11, '의자 멀리 밀기', '참가자는 의자에 앉아 벽을 발로 밀어 최대한 멀리 나아가야 합니다. 각 참가자는 3번의 시도를 할 수 있으며, 가장 먼 기록만 채택됩니다. 의자가 코스를 벗어나거나 뒤집히면 해당 라운드는 무효 처리됩니다.', 'https://youtu.be/E7A4v7jUeNE'),
(12, '스피드 타이핑', '1분 동안 주어진 텍스트를 가장 빠르고 정확하게 타이핑한 사람이 승리합니다. 오타가 있을 경우 단어당 1초의 패널티가 적용됩니다. 속도와 정확도가 중요한 게임입니다.', 'https://youtu.be/VYOjWnS4cMY'),
(13, '딱지치기', '각 참가자는 자신이 접은 딱지로 상대방의 딱지를 뒤집어야 합니다. 각 라운드마다 3번의 시도가 주어지며, 상대 딱지를 뒤집으면 승리입니다. 딱지가 찢어지거나 규칙 외의 방식으로 뒤집을 경우 실격 처리됩니다.', 'https://youtu.be/TkVyRx2MAYs'),
(14, '종이컵 탑 쌓기', '제한된 시간 안에 종이컵을 최대한 높이 쌓는 게임입니다. 종이컵이 무너지면 점수가 0점으로 처리됩니다. 안정성과 속도를 모두 고려해야 합니다.', 'https://youtu.be/HyHNuVaZJ-k'),
(15, '책상 미니 볼링', '작은 공을 굴려 펜이나 클립처럼 세워진 목표물을 쓰러뜨려야 합니다. 각 참가자는 2번씩 3라운드의 기회를 가지며, 쓰러뜨린 개수의 합으로 점수가 결정됩니다.', 'https://youtu.be/2Vv-BfVoq4g');

INSERT INTO challenge_scores (challenge_id, player_id, score) VALUES
(1, 1, 35),
(2, 1, 30),
(3, 1, 25),
(1, 2, 28),
(2, 2, 32),
(3, 2, 30),
(1, 3, 25),
(2, 3, 28),
(3, 3, 33),
(1, 4, 40),
(2, 4, 38),
(3, 4, 35),
(1, 5, 37),
(2, 5, 35),
(3, 5, 33),
(1, 6, 30),
(2, 6, 33),
(3, 6, 32),
(1, 7, 45),
(2, 7, 42),
(3, 7, 40),
(1, 8, 40),
(2, 8, 37),
(3, 8, 38),
(1, 9, 35),
(2, 9, 32),
(3, 9, 33);

UPDATE players
SET total_score = (
					SELECT SUM(score) * 100
					FROM challenge_scores 
					WHERE challenge_scores.player_id = players.player_id), reg_date = NOW();

INSERT INTO comments (user_id, challenge_id, comment_text, reg_date) VALUES
(1, 1, '정말 힘든 도전이었어요!', '2024-11-11 12:01:00'),
(2, 1, '도전정신이 생겼습니다.', '2024-11-11 13:15:00'),
(3, 1, '다음엔 더 잘할게요.', '2024-11-11 14:30:00'),
(1, 2, '생각보다 쉬웠네요!', '2024-11-12 09:45:00'),
(2, 2, '좋은 운동이었어요.', '2024-11-12 10:20:00'),
(3, 2, '생각보다 시간이 많이 필요했네요.', '2024-11-12 11:50:00'),
(1, 3, '재미있었습니다!', '2024-11-13 08:10:00'),
(2, 3, '이 운동은 정말 도전적이었어요.', '2024-11-13 10:25:00'),
(3, 3, '다음에도 참가할게요.', '2024-11-13 11:40:00'),
(1, 4, '생각보다 어려운 운동이었어요!', '2024-11-14 14:05:00'),
(2, 4, '도전할 수 있어서 기뻤어요.', '2024-11-14 15:30:00'),
(3, 4, '팀워크가 좋았습니다.', '2024-11-15 10:50:00'),
(1, 5, '운동이 부족했어요.', '2024-11-16 12:35:00'),
(2, 5, '다음에도 참가할게요.', '2024-11-17 13:15:00'),
(3, 5, '최선을 다했어요!', '2024-11-18 09:45:00');