package com.example.evostyle.domain.review.entity;

import com.example.evostyle.common.entity.BaseEntity;
import com.example.evostyle.domain.member.entity.Member;
import com.example.evostyle.domain.order.entity.OrderItem;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "reviews")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, name = "title", nullable = false)
    private String title;

    @Column(name = "rating", nullable = false)
    @Min(1)
    @Max(5)
    private byte rating;

    @Column(name = "contents", nullable = false)
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id", nullable = false)
    private OrderItem orderItem;

    private Review(String title, byte rating, String contents, Member member, OrderItem orderItem) {
        this.title = title;
        this.rating = rating;
        this.contents = contents;
        this.member = member;
        this.orderItem = orderItem;
    }

    public static Review of(String title, byte rating, String contents, Member member, OrderItem orderItem) {
        return new Review(title, rating, contents, member, orderItem);
    }
}
