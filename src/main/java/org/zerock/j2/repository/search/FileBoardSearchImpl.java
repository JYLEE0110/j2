package org.zerock.j2.repository.search;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.j2.dto.FileBoardListDTO;
import org.zerock.j2.dto.PageRequestDTO;
import org.zerock.j2.dto.PageResponseDTO;
import org.zerock.j2.entity.FileBoard;
import org.zerock.j2.entity.QFileBoard;
import org.zerock.j2.entity.QFileBoardImage;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class FileBoardSearchImpl extends QuerydslRepositorySupport implements FileBoardSearch {

    public FileBoardSearchImpl() {
        super(FileBoard.class);
    }

    @Override
    public PageResponseDTO<FileBoardListDTO> list(PageRequestDTO pageRequestDTO) {

        QFileBoard board = QFileBoard.fileBoard;
        QFileBoardImage boardImage = QFileBoardImage.fileBoardImage;

        JPQLQuery<FileBoard> query = from(board);
        // FileBoard와 FileBoardImage 공통된 컬럼이 없어서 on 조건이 X
        //query.leftJoin(boardImage);
         

        // board 밑에 images를 boardImage로 쓴다.
        query.leftJoin(board.images,boardImage);

        query.where(boardImage.ord.eq(0));

        int pageNum = pageRequestDTO.getPage() - 1 < 0 ? 0 :pageRequestDTO.getPage()-1;

        Pageable pageable = PageRequest.of(
            pageNum, 
            pageRequestDTO.getSize(), 
            Sort.by("bno").descending());

        this.getQuerydsl().applyPagination(pageable, query);
        // ord값이 0인값 1개로 group by X


        JPQLQuery<FileBoardListDTO> listQuery = query.select(
            Projections.bean(FileBoardListDTO.class,
                board.bno,
                board.title,
                boardImage.uuid,
                boardImage.fname
                ));

        List<FileBoardListDTO> list = listQuery.fetch();
        long totalCount = listQuery.fetchCount();

        return new PageResponseDTO(list,totalCount,pageRequestDTO);

    }

}
