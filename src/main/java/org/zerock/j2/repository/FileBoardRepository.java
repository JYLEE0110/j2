package org.zerock.j2.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.j2.entity.FileBoard;
import org.zerock.j2.repository.search.FileBoardSearch;

// One to Many는 pk가 관리해서 image repository는 안만든다.
public interface FileBoardRepository extends JpaRepository<FileBoard,Long>, FileBoardSearch {

    @Query("select b from FileBoard b where b.bno = :bno")
    @EntityGraph(attributePaths = {"images"})
    FileBoard selectOne(@Param("bno") Long bno);

    
}
