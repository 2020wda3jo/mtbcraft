package com.mtbcraft.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.mtbcraft.dto.Badge;
import com.mtbcraft.dto.CompIng;
import com.mtbcraft.dto.Competition;
import com.mtbcraft.dto.Course;
import com.mtbcraft.dto.CustomBadge;
import com.mtbcraft.dto.DangerousArea;
import com.mtbcraft.dto.EntertainRider;
import com.mtbcraft.dto.Login;
import com.mtbcraft.dto.Member;
import com.mtbcraft.dto.Mission;
import com.mtbcraft.dto.MissionComplete;
import com.mtbcraft.dto.No_Danger;
import com.mtbcraft.dto.RidingRecord;

@Repository("com.mtbcraft.mapper.EntertainmentMapper")
public interface EntertainmentMapper {

	// 포인트 차감
	public void pricePoint(String rr_id) throws Exception;

	// 유저 포인트 조회
	public int getBadgeCheck(String rr_id) throws Exception;

	// 진행중인 경쟁전 조회
	public List<CompIng> getCompIng() throws Exception;

	// 등록된 경쟁전 조회
	public List<Competition> getCompetition() throws Exception;

	// 등록된 미션 조회
	public List<Mission> getMission() throws Exception;

	// 미션 등록
	public void missionUpload(Mission mission) throws Exception;

	// 배지 조회
	public List<Badge> getBadge(String rider);
	
	// 커스텀 배지 조회
	public List<CustomBadge> getCustomBadge(String rider);

	// 배지 등록
	public void BadgeUpload(CustomBadge badge) throws Exception;

	// 참여한 최근 경쟁전 3개 조회
	public List<Competition> getRecentComp3(String rider);

	// 참여한 최근 경쟁전 4개 조회
	public List<Competition> getRecentComp4(String rider);

	// 진행중인 경쟁전 2개 조회
	public List<Competition> getIngComp2();

	// 종료된 경쟁전 2개 조회
	public List<Competition> getEndComp2();

	// 번호로 경쟁전 조회
	public Competition getComp(int comp_num);

	// 성공한 미션 조회
	public List<Mission> getCompleteMission(String rider);

	// 미션 상세 정보 조회
	public Mission getMissionDetail(int m_num);

	// 미션 상세 정보 조회시 성공 인원 조회
	public List<EntertainRider> getER_List(int m_num);

	// 전체 회원 조회
	public int getTotalRiders();

	// 미완료 미션 조회
	public List<Mission> getNoCompleteMission(String rider);

}