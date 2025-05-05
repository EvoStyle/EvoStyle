package com.example.evostyle.domain.product.entity;


import com.example.evostyle.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "product_likes",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"member_id", "product_id"})}
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private ProductLike (Member member, Product product){
        this.member = member;
        this.product = product;
    }

    public static ProductLike of(Member member, Product product){
        return new ProductLike(member, product);
    }
}
