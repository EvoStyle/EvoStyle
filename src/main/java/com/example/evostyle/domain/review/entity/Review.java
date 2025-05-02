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
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

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
    private Byte rating;

    @Column(name = "contents", nullable = false)
    private String contents;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "is_deleted")
    @ColumnDefault("false")
    private boolean isDeleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id", nullable = false)
    private OrderItem orderItem;

    private Review(String title, Byte rating, String contents, Member member, OrderItem orderItem) {
        this.title = title;
        this.rating = rating;
        this.contents = contents;
        this.member = member;
        this.orderItem = orderItem;
    }

    public static Review of(String title, Byte rating, String contents, Member member, OrderItem orderItem) {
        return new Review(title, rating, contents, member, orderItem);
    }

    public void update(String title, Byte rating, String contents) {
        if (title != null && !title.isBlank()) {
            this.title = title;
        }
        if (rating != null && rating >= 1 && rating <= 5) {
            this.rating = rating;
        }
        if (contents != null && !contents.isBlank()) {
            this.contents = contents;
        }
    }

    public void delete() {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
    }
}
