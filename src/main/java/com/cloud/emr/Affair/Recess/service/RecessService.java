package com.cloud.emr.Affair.Recess.service;

import com.cloud.emr.Affair.Recess.dto.RecessRequest;
import com.cloud.emr.Affair.Recess.dto.RecessResponse;
import com.cloud.emr.Affair.Recess.entity.RecessEntity;
import com.cloud.emr.Affair.Recess.repository.RecessRepository;
import com.cloud.emr.Main.User.entity.UserEntity;
import com.cloud.emr.Main.User.type.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecessService {

    private final RecessRepository recessRepository;

    /** 1. 휴진 등록 **/
    @Transactional
    public RecessResponse registerRecess(UserEntity user, RecessRequest recessRequest) {
        checkTimeRange(recessRequest.getRecessStart(), recessRequest.getRecessEnd());

        LocalDateTime start = roundTimeRange(recessRequest.getRecessStart());
        LocalDateTime end = roundTimeRange(recessRequest.getRecessEnd());

        // 등록 시 (새 휴진이므로 ID 없음)
        checkDupeRecess(user, start, end, null);

        RecessEntity recessEntity = RecessEntity.builder()
                .userEntity(user)
                .recessStart(start)
                .recessEnd(end)
                .recessReason(recessRequest.getRecessReason())
                .build();

        recessRepository.save(recessEntity);
        return RecessResponse.from(recessEntity);
    }



    /** 2. 휴진 수정 **/
    @Transactional
    public RecessResponse updateRecess(UserEntity user, Long recessId, RecessRequest recessRequest) {
        checkTimeRange(recessRequest.getRecessStart(), recessRequest.getRecessEnd());

        RecessEntity recessEntity = recessRepository.findById(recessId)
                .orElseThrow(() -> new RuntimeException("휴진 정보를 찾을 수 없습니다."));

        if (!recessEntity.getUserEntity().getId().equals(user.getId())) {
            throw new RuntimeException("본인의 휴진 정보만 수정할 수 있습니다.");
        }

        LocalDateTime start = roundTimeRange(recessRequest.getRecessStart());
        LocalDateTime end = roundTimeRange(recessRequest.getRecessEnd());

        // 수정 시 (기존 휴진 ID를 고려)
        checkDupeRecess(user, start, end, recessId);

        RecessEntity updated = RecessEntity.builder()
                .id(recessEntity.getId())
                .userEntity(user)
                .recessStart(start)
                .recessEnd(end)
                .recessReason(recessRequest.getRecessReason())
                .recessCreate(recessEntity.getRecessCreate())
                .recessUpdate(LocalDateTime.now())
                .build();

        recessRepository.save(updated);
        return RecessResponse.from(updated);
    }



    /** 3. 휴진 삭제 **/
    @Transactional
    public void deleteRecess(UserEntity user, Long recessId) {
        RecessEntity recessEntity = recessRepository.findById(recessId)
                .orElseThrow(() -> new RuntimeException("휴진 정보를 찾을 수 없습니다."));

        if (!recessEntity.getUserEntity().getId().equals(user.getId())) {
            throw new RuntimeException("본인의 휴진 정보만 삭제할 수 있습니다.");
        }

        recessRepository.delete(recessEntity);
    }



    /** 4. 휴진 목록 조회 **/
    @Transactional(readOnly = true)
    public List<RecessResponse> listByRole(UserEntity user, RoleType role) {

        if (user.getRole() != role) {
            throw new RuntimeException("조회 권한이 없습니다.");
        }

        return recessRepository.findByUserEntity_Role(role).stream()
                .map(RecessResponse::from)
                .collect(Collectors.toList());
    }





    /** 내부 유틸 함수 **/

    // 시작-종료 시간 유효성 검사
    private void checkTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime.getMinute() % 30 != 0 || endTime.getMinute() % 30 != 0) {
            throw new IllegalArgumentException("시간은 반드시 30분 단위여야 합니다.");
        }
        if (endTime.isBefore(startTime) || endTime.equals(startTime)) {
            throw new IllegalArgumentException("종료 시간은 시작 시간 이후여야 합니다.");
        }
    }

    //입력 시간을 30분 단위로 반올림
    private LocalDateTime roundTimeRange(LocalDateTime time) {
        int minute = time.getMinute();
        int adjustedMinute = (minute < 30) ? 0 : 30;
        return time.withMinute(adjustedMinute).withSecond(0).withNano(0);
    }

    // 중복 휴진 체크 함수 (예외처리라서 403뜨고 콘솔에서 자세한 내용 출력됨)
    private void checkDupeRecess(UserEntity user, LocalDateTime start, LocalDateTime end, Long excludeRecessId) {
        RoleType role = user.getRole();

        // 동일 역할을 가진 휴진들 중에 겹치는 시간이 있는지 조회
        List<RecessEntity> overlapping = recessRepository.findByUserEntity_RoleAndRecessEndAfterAndRecessStartBefore(
                role, start, end);

        // Objects.equals(a, b)는 null일 때의 분기를 따로 나누지 않아도 안전하게 비교가 가능해서 코드가 간결해진다.
        boolean conflict = overlapping.stream()
                .anyMatch(r -> !Objects.equals(r.getId(), excludeRecessId));

        if (conflict) {
            throw new IllegalArgumentException("같은 역할군 내에 겹치는 휴진이 존재합니다.");
        }
    }

}




