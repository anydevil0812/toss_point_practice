package com.toss.point.entity;

import com.toss.point.dto.MemberDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private LocalDateTime registerDate;

    private int views;

    public static MemberDto entityToDTO(Member member) {
        if (member == null) return null;

        return MemberDto.builder()
                        .id(member.getId())
                        .name(member.getName())
                        .registerDate(member.getRegisterDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                        .views(member.getViews())
                        .build();
    }

    public void updateView() {
        this.views = views + 1;
    }


}