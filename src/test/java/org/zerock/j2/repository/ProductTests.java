package org.zerock.j2.repository;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.zerock.j2.dto.PageRequestDTO;
import org.zerock.j2.dto.PageResponseDTO;
import org.zerock.j2.dto.ProductListDTO;
import org.zerock.j2.entity.Product;

import jakarta.transaction.Transactional;

@SpringBootTest
public class ProductTests {

    @Autowired
    ProductRepository productRepository;

    // @Test
    // public void testInsert() {

    //     for (int i = 0; i < 200; i++) {
    //         Product product = Product.builder()
    //                 .pname("Test"+i)
    //                 .pdesc("Test"+i)
    //                 .writer("user"+i)
    //                 .price(4000)
    //                 .build();

    //         product.addImage(UUID.randomUUID().toString() + "_aaa.jpg");
    //         product.addImage(UUID.randomUUID().toString() + "_bbb.jpg");
    //         product.addImage(UUID.randomUUID().toString() + "_ccc.jpg");

    //         productRepository.save(product);
    //     }

    // }

    @Transactional
    @Test
    public void testRead1() {

        Optional<Product> result = productRepository.findById(1L);

        Product product = result.orElseThrow();

        System.out.println(product);
        System.out.println("============");
        System.out.println(product.getImages());

    }

    @Test
    public void testRead2() {

        // selectOne => EntityGraph 조인해줌
        // image까지 한번에 가져오기 위해서 작성 => Product 1번 이미지한번 transactional로 묶어서 2번 쿼리를 날리면 효율이
        // 떨어진다.
        Product product = productRepository.selectOne(2L);

        System.out.println(product);
        System.out.println("============");
        System.out.println(product.getImages());

    }

    // Entity가 아니라 cascade를 할 필요가 없다.
    // 자동 Join
    @Test
    public void testDelete() {
        productRepository.deleteById(1L);
    }

    @Test
    @Transactional
    @Commit
    public void testUpdate() {
        Optional<Product> result = productRepository.findById(2L);
        Product product = result.orElseThrow();

        // Entity는 읽기만 가능해서 change메소드로 만든다.
        product.changePrice(6000);

        // product.clearImages();

        product.addImage(UUID.randomUUID() + "_newImage.jpg");

        productRepository.save(product);

    }

    @Test
    public void testList(){

        PageRequestDTO pageRequestDTO = new PageRequestDTO();

        PageResponseDTO<ProductListDTO> result = productRepository.list(pageRequestDTO);

        for(ProductListDTO dto : result.getDtoList()){

            System.out.println(dto);

        }

    }

    @Test
    public void testList2(){

        PageRequestDTO pageRequestDTO = new PageRequestDTO();

        PageResponseDTO<ProductListDTO> result = productRepository.listWithReview(pageRequestDTO);

        for(ProductListDTO dto : result.getDtoList()){

            System.out.println(dto);

        }

    }



}
