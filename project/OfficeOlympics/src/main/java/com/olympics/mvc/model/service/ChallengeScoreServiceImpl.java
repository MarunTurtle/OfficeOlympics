package com.olympics.mvc.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.olympics.mvc.model.dao.ChallengeScoreDao;
import com.olympics.mvc.model.dto.Challenge;
import com.olympics.mvc.model.dto.Rank;
import com.olympics.mvc.model.dto.Score;

@Service
public class ChallengeScoreServiceImpl implements ChallengeScoreService{
	
	private final ChallengeScoreDao challengeDao;
	
	public ChallengeScoreServiceImpl(ChallengeScoreDao challengeDao) {
		this.challengeDao = challengeDao;
	}

	// 챌린지 조회
	@Override
	public Challenge selectChallenge(int challengeId) {
		return challengeDao.selectChallenge(challengeId);
	}

	// 챌린지 결과 기록
	@Override
	public boolean insertScores(Score score) {
		// 이름을 ID로 변환
	    List<Integer> ids = challengeDao.nameToId(score.getPlayerNames());
	    score.setPlayerId(ids);

	    // 변환된 데이터를 insert에 사용 가능한 형태로 준비
	    List<Map<String, Object>> insertData = new ArrayList<>();
	    for (int i = 0; i < score.getPlayerId().size(); i++) {
	        Map<String, Object> map = new HashMap<>();
	        map.put("challengeId", score.getChallengeId());
	        map.put("playerId", score.getPlayerId().get(i));
	        map.put("score", score.getScores().get(i));
	        insertData.add(map);
	        System.out.println(insertData.get(i));
	    }

	    // DB에 점수 삽입
	    int isInserted = challengeDao.insertScore(insertData);
	    return isInserted == score.getScores().size();
	}
	
	// 챌린지 점수 업데이트
	@Override
	public void updateScore() {
		challengeDao.updateScore();
	}

	// 현재 챌린지에서의 순위 조회
	@Override
	public List<Rank> selectChallengeScore(int challengeId) {
		return challengeDao.selectChallengeScore(challengeId);
	}

	// 올림픽 팀 내 순위 조회
	@Override
	public List<Rank> selectFinalScore(int olympicsId) {
		return challengeDao.selectFinalScore(olympicsId);
	}

	// 리더보드에서 전체 순위 조회
	@Override
	public List<Rank> selectMainScore() {
		return challengeDao.selectMainScore();
	}

}
