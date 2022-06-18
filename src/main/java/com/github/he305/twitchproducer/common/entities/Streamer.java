package com.github.he305.twitchproducer.common.entities;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "streamer")
@Getter
@Setter
public class Streamer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nickname", unique = true, nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(name = "platform", length = 8, nullable = false)
    private Platform platform;

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof Streamer))
            return false;

        Streamer other = (Streamer) obj;
        boolean idEquals = (this.id == null && other.id == null)
                || (this.id != null && this.id.equals(other.id));
        boolean nicknameEquals = (this.nickname == null && other.nickname == null)
                || (this.nickname != null && this.nickname.equals(other.nickname));
        boolean platformEquals = (this.platform == null && other.platform == null)
                || (this.platform != null && this.platform.equals(other.platform));

        return idEquals && nicknameEquals && platformEquals;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((nickname == null) ? 0 : nickname.hashCode());
        result = prime * result + ((platform == null) ? 0 : platform.hashCode());
        return result;
    }
}
