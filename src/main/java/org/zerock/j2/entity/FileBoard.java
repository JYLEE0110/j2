package org.zerock.j2.entity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.BatchSize;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "images")
// one to many = FileBoard가 images를 관리한다. => crud한다.
public class FileBoard {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    private String title;

    private String content;

    private String writer;

    // 한 게시물은 여러개의 이미지들을 가지고있다.
    // 수정 작업 시 List는 주소 값이 달라지므로 new로 선언해서 주소값을 고정 시킨다. 
    // 일괄처리

    //최대 20개의 bno를 일괄처리로 가져온다. 
    @BatchSize(size = 20)
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "board")
    @Builder.Default
    private List<FileBoardImage> images = new ArrayList<>();

    public void addImage(FileBoardImage boardImage){

        // ord값은 ArrayList size로 준다.
        boardImage.changeOrd((images.size()));
        
        // image List 추가
        images.add(boardImage);
    }

    public void clearImages(){

        images.clear();

    }

}
