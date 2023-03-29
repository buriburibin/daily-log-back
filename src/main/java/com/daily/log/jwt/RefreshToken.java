package com.daily.log.jwt;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Table(name = "refresh_token")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refresh_token_id")
    private Long id;

    @Column(name = "user_id")
    private String userId;
    private String token;


    private RefreshToken(String userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    public static RefreshToken createToken(String userId, String token){
        return new RefreshToken(userId, token);
    }

    public void changeToken(String token) {
        this.token = token;
    }
}
