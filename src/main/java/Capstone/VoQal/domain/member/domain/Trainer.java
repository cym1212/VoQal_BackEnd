package Capstone.VoQal.domain.member.domain;


import Capstone.VoQal.domain.lesson.domain.LessonNote;
import Capstone.VoQal.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "trainer")
public class Trainer extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "trainer", fetch = FetchType.LAZY)
    private Set<Student> student;

    @OneToOne(mappedBy = "trainer", fetch = FetchType.LAZY)
    private LessonNote lessonNote;
}
