package com.sparta.cookietalk.category.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
public class Category {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // 상위 카테고리 필드
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    // 하위 카테고리
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private Set<Category> children = new HashSet<Category>();

    public Category(String name, Category parent) {
        this.name = name;
        this.parent = parent;
    }

    public void addChildCategory(Category child) {
        children.add(child);
        child.setParent(this);
    }
}
