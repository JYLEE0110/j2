package org.zerock.j2.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PageRequestDTO {


    private int page = 1;

    private int size = 12;

    private String type, keyword;

    public PageRequestDTO() {
        this(1, 12);
    }

    public PageRequestDTO(int page, int size) {

        this(page, size, null, null);

    }

    // setter로 탐
    public PageRequestDTO(int page, int size, String type, String keyword) {

        this.page = page <= 0 ? 1 : page;
        this.size = size < 0 || size >= 100 ? 12 : size;
        this.type = type;
        this.keyword = keyword;
    }

    public void setPage(int page){
        this.page = page <= 0 ? 1 : page;
    }

    public void setSize(int size){

        this.size = size < 0 || size >= 100 ? 12 : size;

    }

}
