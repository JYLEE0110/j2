package org.zerock.j2.repository;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Commit;
import org.zerock.j2.dto.PageRequestDTO;
import org.zerock.j2.entity.FileBoard;
import org.zerock.j2.entity.FileBoardImage;

import jakarta.transaction.Transactional;

@SpringBootTest
public class RepositoryTest {

    @Autowired
    FileBoardRepository repository;

    @Test
    public void insert() {

        for (int i = 0; i < 100; i++) {
            FileBoard fileBoard = FileBoard.builder()
                    .title("AA")
                    .content("AA")
                    .writer("AA")
                    .build();

            /* 파일추가 */
            FileBoardImage img1 = FileBoardImage.builder()
                    .uuid(UUID.randomUUID().toString())
                    .fname("aaa.jpg")
                    .build();

            FileBoardImage img2 = FileBoardImage.builder()
                    .uuid(UUID.randomUUID().toString())
                    .fname("bbb.jpg")
                    .build();

            // image 추가
            fileBoard.addImage(img1);
            fileBoard.addImage(img2);
            /*  */

            repository.save(fileBoard);
        }
    }// end for

    @Test
    @Transactional
    @Commit
    public void testRemove() {
        Long bno = 2L;
        repository.deleteById(bno);
    }

    @Test
    @Transactional
    // Tostring exclude를 안넣어주면 board와 image 다 가지고온다.
    // board image select 2번 날아가므로 fetch Eager를 붙여준다. N+1문제(List일때) 이것도 문제가됨 =>무조건
    // fetch는 필요할때만 가져오게 Lazy를 붙여준다.
    // => read는 유용하지만 List 할때는 N+1문제가 생긴다.
    public void testRead() {

        Long bno = 100L;

        Optional<FileBoard> result = repository.findById(bno);

        FileBoard board = result.orElseThrow();

        System.out.println(board);

    }

    @Test
    @Transactional
    public void testList() {

        Pageable pagealbe = PageRequest.of(0, 10);

        Page<FileBoard> result = repository.findAll(pagealbe);

        // System.out.println(result);

        // get() =>Stream
        result.get().forEach(board -> {
            System.out.println(board);
            // 필요할때 => Lazy
            System.out.println(board.getImages());
        });

    }

    @Transactional
    @Test
    public void testListQueryDsl() {
        PageRequestDTO requestDTO = new PageRequestDTO();

        repository.list(requestDTO);
        System.out.println(repository.list(requestDTO));
    }

    @Test
    public void testSelectOne() {

        Long bno = 100L;

        FileBoard board = repository.selectOne(bno);

        System.out.println(board);
        // Lazy라 Transactional을 걸어야함 => 그래도 쿼리가 2번 날아간다.=>EntityGraph로 해결
        System.out.println(board.getImages());

    }

    @Commit
    @Transactional
    @Test
    public void testDelete() {
        Long bno = 102L;

        repository.deleteById(bno);
    }

    @Commit
    @Transactional
    @Test
    public void testUpdate() {

        Optional<FileBoard> result = repository.findById(101L);

        FileBoard board = result.orElseThrow();

        board.clearImages();

        /* 파일추가 */
        FileBoardImage img1 = FileBoardImage.builder()
                .uuid(UUID.randomUUID().toString())
                .fname("szzz.jpg")
                .build();

        // image 추가
        board.addImage(img1);

        repository.save(board);

    }

}
