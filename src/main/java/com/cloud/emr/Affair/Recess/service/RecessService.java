package com.cloud.emr.Affair.Recess.service;

import com.cloud.emr.Affair.Recess.dto.RecessRequest;
import com.cloud.emr.Affair.Recess.dto.RecessResponse;
import com.cloud.emr.Affair.Recess.entity.RecessEntity;
import com.cloud.emr.Affair.Recess.repository.RecessRepository;
import com.cloud.emr.Main.User.entity.UserEntity;
import com.cloud.emr.Main.User.repository.UserRepository;
import com.cloud.emr.Main.User.type.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecessService {

    private final RecessRepository recessRepository;
    private final UserRepository userRepository;

    /** 1. 휴진 등록 **/
    @Transactional
    public RecessResponse registerRecess(RecessRequest req) {
        validateTimeSlot(req.getRecessStart(), req.getRecessEnd());

        UserEntity user = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        if (user.getRole() != req.getRole()) {
            throw new RuntimeException("사용자의 역할이 일치하지 않습니다.");
        }

        LocalDateTime start = adjustToHalfHour(req.getRecessStart());
        LocalDateTime end   = adjustToHalfHour(req.getRecessEnd());

        RecessEntity e = RecessEntity.builder()
                .userEntity(user)
                .recessStart(start)
                .recessEnd(end)
                .recessReason(req.getRecessReason())
                .build();

        recessRepository.save(e);
        return toDto(e, user);
    }

    /** 2. 휴진 수정 **/
    @Transactional
    public RecessResponse updateRecess(Long id, RecessRequest req) {
        validateTimeSlot(req.getRecessStart(), req.getRecessEnd());

        RecessEntity e = recessRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("휴진 정보를 찾을 수 없습니다."));
        UserEntity user = e.getUserEntity();
        if (user.getRole() != req.getRole()) {
            throw new RuntimeException("사용자의 역할이 일치하지 않습니다.");
        }

        LocalDateTime start = adjustToHalfHour(req.getRecessStart());
        LocalDateTime end   = adjustToHalfHour(req.getRecessEnd());

        // 엔티티 새로 빌드 후 저장
        RecessEntity updated = RecessEntity.builder()
                .id(e.getId())
                .userEntity(user)
                .recessStart(start)
                .recessEnd(end)
                .recessReason(req.getRecessReason())
                .build();

        recessRepository.save(updated);
        return toDto(updated, user);
    }

    /** 3. 휴진 삭제 **/
    @Transactional
    public void deleteRecess(Long id) {
        RecessEntity e = recessRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("휴진 정보를 찾을 수 없습니다."));
        recessRepository.delete(e);
    }

    /** 4. 휴진 목록 조회 **/
    @Transactional(readOnly = true)
    public List<RecessResponse> listByRole(RoleType role) {
        return recessRepository.findByUserEntity_Role(role).stream()
                .map(e -> toDto(e, e.getUserEntity()))
                .collect(Collectors.toList());
    }

    // ─────────────────────────────────────────────────────────

    private void validateTimeSlot(LocalDateTime s, LocalDateTime e) {
        if (s.getMinute() % 30 != 0 || e.getMinute() % 30 != 0) {
            throw new RuntimeException("시간은 반드시 30분 단위여야 합니다.");
        }
        if (e.isBefore(s)) {
            throw new RuntimeException("종료 시간은 시작 시간 이후여야 합니다.");
        }
    }

    private LocalDateTime adjustToHalfHour(LocalDateTime t) {
        int m = t.getMinute();
        int adj = (m < 30) ? 0 : 30;
        return t.withMinute(adj).withSecond(0).withNano(0);
    }

    private RecessResponse toDto(RecessEntity e, UserEntity u) {
        return new RecessResponse(
                e.getId(),
                u.getId(),
                u.getName(),
                u.getRole().name(),
                e.getRecessStart(),
                e.getRecessEnd(),
                e.getRecessReason(),
                e.getRecessCreate()
        );
    }
}