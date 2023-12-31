package org.zerock.j2.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString(exclude = "images")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pno;

    private String pname;

    private String pdesc;

    private String writer;

    private int price;

    private boolean delFlag;

    // @Embeded 선언된 클래스와 매핑된다.
    // @EntityGraph 한번에 가져온다. Image 가져오는 query문 따로 안날아간다.
    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private List<ProductImage> images = new ArrayList<>();

    public void addImage(String name){
        ProductImage pImage = ProductImage.builder().fname(name).ord(images.size()).build();

        images.add(pImage);
    }

    public void clearImages(){

        images.clear();

    }
    // Entity는 읽기만 가능해서 change메소드로 만든다.
    public void changePrice(int price){
        this.price = price;
    }

    public void changePname(String pname){ this.pname = pname;}
    public void changePdesc(String pdesc){ this.pdesc = pdesc;}
    public void changeDelflag(boolean delFlag){ this.delFlag = delFlag;}

}
