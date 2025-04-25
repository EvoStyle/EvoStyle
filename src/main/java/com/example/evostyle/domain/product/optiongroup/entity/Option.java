package com.example.evostyle.domain.product.optiongroup.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "options")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_group_id")
    private OptionGroup optionGroup;

    @Column(nullable = false)
    private String type;

    private Option (OptionGroup optionGroup, String type){
        this.optionGroup = optionGroup;
        this.type = type;
    }

    public static Option of(OptionGroup optionGroup, String type){
        return new Option(optionGroup, type);
    }

    public void updateType(String type){
        this.type = type;
    }

}
