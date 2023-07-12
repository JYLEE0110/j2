package org.zerock.j2.repository.search;

import org.zerock.j2.dto.PageRequestDTO;
import org.zerock.j2.dto.PageResponseDTO;
import org.zerock.j2.dto.ProductListDTO;

public interface ProductSearch {
    
    PageResponseDTO<ProductListDTO> list(PageRequestDTO pageRequestDTO);

    //Review까지 달린 ProductList
    PageResponseDTO<ProductListDTO> listWithReview(PageRequestDTO pageRequestDTO);

}
