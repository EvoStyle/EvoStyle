package com.example.evostyle.domain.product.entity;

import com.example.evostyle.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Table(name = "option_groups")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OptionGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(length = 15, nullable = false)
    private String name;

    @ColumnDefault("false")
    private boolean isDeleted = false;


    private OptionGroup (String name, Product product) {
        this.name = name ;
        this.product = product ;
    }

    public static OptionGroup of(String name, Product product) {
        return new OptionGroup(name, product);
    }

    public void update(String name){this.name = name ;}

    public void delete(){
        this.isDeleted = false;
    }
}
