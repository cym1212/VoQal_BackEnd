package Capstone.VoQal.domain.reservation.domain;

import Capstone.VoQal.domain.member.domain.Member;
import Capstone.VoQal.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "reservations")
public class Reservation extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void changeRoom(Room newRoom) {
        this.room = newRoom;
    }

    public void reschedule(LocalDateTime newStartTime, LocalDateTime newEndTime) {
        this.startTime = newStartTime;
        this.endTime = newEndTime;
    }

}
