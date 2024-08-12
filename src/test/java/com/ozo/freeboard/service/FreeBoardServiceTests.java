package com.ozo.freeboard.service;

import com.ozo.freeboard.dto.FreeBoardDTO;
import com.ozo.freeboard.dto.PageRequestDTO;
import com.ozo.freeboard.dto.PageResultDTO;
import com.ozo.freeboard.entity.FreeBoard;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class FreeBoardServiceTests {

    @Autowired
    private FreeBoardService service ;

    @Test
    public void testRegister(){

        FreeBoardDTO freeBoardDTO = FreeBoardDTO.builder()
                .title("Sample Title....")
                .content("Sample Content....")
                .writer("수정유저")
                .build();
        log.info(service.register(freeBoardDTO));

    }

    // 리스트 출력 테스트
    @Test
    public void testList(){
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1).size(10)
                .build();

        PageResultDTO<FreeBoardDTO, FreeBoard> resultDTO = service.getList(pageRequestDTO);

        log.info("prev : " + resultDTO.isPrev());
        log.info("next : " + resultDTO.isNext());
        log.info("TOTAL : " + resultDTO.getTotalPage());
        log.info("---------------------------------------");

        for(FreeBoardDTO freeBoardDTO : resultDTO.getDtoList()){
            log.info(freeBoardDTO);
        }

        log.info("---------------------------------");
        resultDTO.getPageList().forEach(i ->  log.info(i));
    }
}
