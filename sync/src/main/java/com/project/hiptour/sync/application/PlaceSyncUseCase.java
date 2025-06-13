package com.project.hiptour.sync.application;

public class PlaceSyncUseCase {
}
/**
 * 간이 설계
 * 1. 지역코드 목록(1~39)을 불러온다
 * 2. 지역코드 별 반복 수행 :
 *  2-1. TourApiCaller를 통해 외부 API를 호출 > 해당 지역 관광지 목록을 받는다
 *  2-2. 받은 JSON 응답을 TourApiReponseMapper가 DTO로 변환
 *  2-3. DTO 목록을 Entity(Place)로 변환
 *  2-4. Entity 목록을 PlaceRepository를 통해 저장
 *  2-5. 중간 로그를 기록하여 진행 상태를 저장
 * 3. 모든 지역에 대한 동기화가 끝나면 로그를 성공 상태로 마무리
 * **/