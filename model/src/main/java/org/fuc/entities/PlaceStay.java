package org.fuc.entities;

import javax.persistence.*;

@Entity
@Table(name = "placestay")
public class PlaceStay {
    @Id
    @SequenceGenerator(name = "place_stay_id_seq", sequenceName = "place_stay_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "place_stay_id_seq")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PARTICIPANT_ID", nullable = false)
    private Account participant;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PLACE_ID", nullable = false)
    private Place place;

    private String status;

    private String comment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getParticipant() {
        return participant;
    }

    public void setParticipant(Account participant) {
        this.participant = participant;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
