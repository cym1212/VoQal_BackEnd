package Capstone.VoQal.domain.member.domain;


import Capstone.VoQal.domain.lesson.domain.LessonNote;
import Capstone.VoQal.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "coach")
public class Coach extends BaseEntity {

    @ElementCollection
    @CollectionTable(name = "pending_student_list", joinColumns = @JoinColumn(name = "coach_id"))
    @Column(name = "pending_student_id")
    private List<Long> pendingStudentIds = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "coach", fetch = FetchType.LAZY)
    private Set<Student> student;

    @OneToOne(mappedBy = "coach", fetch = FetchType.LAZY)
    private LessonNote lessonNote;


    public void addPendingStudentId(Long studentId) {
        if (this.pendingStudentIds == null) {
            this.pendingStudentIds = new ArrayList<>();
        }
        this.pendingStudentIds.add(studentId);
    }


    @PostLoad
    public void initPendingStudentIds() {
        if (pendingStudentIds == null) {
            pendingStudentIds = new ArrayList<>();
        }
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
