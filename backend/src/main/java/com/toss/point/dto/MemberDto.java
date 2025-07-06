package com.toss.point.dto;

import com.toss.point.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MemberDto {

    private Long id;
    private String name;
    private int views;
    private String registerDate;

    public static Member DtoToEntity(MemberDto memberDto) {
        if (memberDto == null) return null;

        return Member.builder()
                     .id(memberDto.getId())
                     .name(memberDto.getName())
                     .registerDate(LocalDateTime.now())
                     .views(memberDto.getViews())
                     .build();
    }


}
