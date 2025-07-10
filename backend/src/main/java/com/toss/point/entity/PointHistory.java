package com.toss.point.entity;

import com.toss.point.dto.PointHistoryDto;
import com.toss.point.response.PointType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "point_history")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class PointHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "fk_member_id"))
    private Member member;

    @Enumerated(EnumType.STRING)
    private PointType type;

    private LocalDateTime date;

    public static PointHistory createEntity(Member member, int amount) {
        return PointHistory.builder()
                           .member(member)
                           .type(PointType.CHARGE)
                           .date(LocalDateTime.now())
                           .amount(amount)
                           .build();
    }

    public static PointHistoryDto entityToDTO(PointHistory ph) {
        return PointHistoryDto.builder()
                .id(ph.getMember().getId())
                .amount(ph.getAmount())
                .build();
    }

}

