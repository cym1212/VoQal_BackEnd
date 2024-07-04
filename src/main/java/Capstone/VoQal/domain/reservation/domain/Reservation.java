package Capstone.VoQal.domain.reservation.domain;

import Capstone.VoQal.domain.member.domain.Member;
import Capstone.VoQal.domain.member.domain.Student;
import Capstone.VoQal.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "reservations")
public class Reservation extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    private LocalDateTime startTime;

    private LocalDateTime endTime;


    @ManyToOne( fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public static class ReservationBuilder {
        private Long id;

        public ReservationBuilder id(Long id) {
            this.id = id;
            return this;
        }
    }
}
