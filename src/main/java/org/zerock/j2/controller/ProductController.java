package org.zerock.j2.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.j2.dto.PageRequestDTO;
import org.zerock.j2.dto.PageResponseDTO;
import org.zerock.j2.dto.ProductDTO;
import org.zerock.j2.dto.ProductListDTO;
import org.zerock.j2.service.ProductService;
import org.zerock.j2.util.FileUploader;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@CrossOrigin
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Log4j2
public class ProductController {
    
    private final ProductService productService;

    private final FileUploader uploader; 


    @PostMapping("/")
    public Map<String, Long> register(ProductDTO productDTO){

        log.info(productDTO);

        List<String> filenames = uploader.uploadFiles(productDTO.getFiles(), true);
        
        // 서버에 올라간 이미지 title를 DTO Images에 넣어줌
        productDTO.setImages(filenames);

        Long pno = productService.register(productDTO);

        return Map.of("result", pno);
    }

    @GetMapping(value = "list")
    public PageResponseDTO<ProductListDTO> getProductList(PageRequestDTO pageRequestDTO){

        log.info("--------------");
        log.info(pageRequestDTO);

        return productService.list(pageRequestDTO);

    }

}
