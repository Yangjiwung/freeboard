package com.ozo.freeboard.repository;

import com.ozo.freeboard.entity.FreeBoard;

import com.ozo.freeboard.entity.QFreeBoard;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class FreeBoardRepositoryTests {

    @Autowired
    private FreeBoardRepository freeBoardRepository;

    @Test
    public void insertDummies(){

        IntStream.rangeClosed(1,100).forEach(i -> {
            FreeBoard freeBoard = FreeBoard.builder()
                    .title("테스트 제목..." + i)
                    .content("테스트 내용..." + i)
                    .writer("user"+ (i % 10))
                    .build();
            log.info(freeBoardRepository.save(freeBoard));
        });
    }


    @Test
    public void updateTest(){
        Optional<FreeBoard> result = freeBoardRepository.findById(100L);

        if(result.isPresent()){
            FreeBoard freeBoard = result.get();

            freeBoard.changeTitle("수정");
            freeBoard.changeContent("내용수정");

            freeBoardRepository.save(freeBoard);
        }
    }

    //단일항목 검색
    @Test
    public void testQuery1(){
        Pageable pageable= PageRequest.of(0,10,Sort.by("bno").descending());

        QFreeBoard qFreeBoard = QFreeBoard.freeBoard; //

        String keyword = "1";

        BooleanBuilder builder = new BooleanBuilder(); //

        BooleanExpression expression = qFreeBoard.title.contains(keyword);

        builder.and(expression);

        Page<FreeBoard> result = freeBoardRepository.findAll(builder, pageable);

        result.stream().forEach(freeBoard -> {
            log.info(freeBoard);
        });

    }

    @Test
    public void testQuery2(){

        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());

        QFreeBoard qFreeBoard = QFreeBoard.freeBoard;

        String keyword = "1";

        BooleanBuilder builder = new BooleanBuilder();

        BooleanExpression eTitle = qFreeBoard.title.contains(keyword);

        BooleanExpression eContent = qFreeBoard.content.contains(keyword);

        BooleanExpression exAll = eTitle.or(eContent);

        builder.and(exAll);

        builder.and(qFreeBoard.bno.gt(0L));

        Page<FreeBoard> result = freeBoardRepository.findAll(builder,pageable);

        result.stream().forEach(freeBoard -> {
            log.info(freeBoard);
        });
    }
}
