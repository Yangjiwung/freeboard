package com.ozo.freeboard.service;

import com.ozo.freeboard.dto.FreeBoardDTO;
import com.ozo.freeboard.dto.PageRequestDTO;
import com.ozo.freeboard.dto.PageResultDTO;
import com.ozo.freeboard.entity.FreeBoard;
import com.ozo.freeboard.repository.FreeBoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class FreeBoardServiceImpl implements FreeBoardService{

    private final FreeBoardRepository repository; // @RequiredArgsConstructor 의존성 자동주입 필수

    @Override
    public Long register(FreeBoardDTO freeBoardDTO) {

        log.info("DTO=====================");
        log.info(freeBoardDTO);

        FreeBoard entity = dtoToEntity(freeBoardDTO); // dto로 받은 객체를 엔티티로 변환

        log.info(entity);

        repository.save(entity);

        return entity.getBno();
    }

    @Override
    public PageResultDTO<FreeBoardDTO, FreeBoard> getList(PageRequestDTO requestDTO) {

        Pageable pageable =requestDTO.getPageable(Sort.by("bno").descending());

        Page<FreeBoard> result = repository.findAll(pageable);

        Function<FreeBoard, FreeBoardDTO> fn = (entity -> entityToDTO(entity));

        return new PageResultDTO<>(result,fn);

    }
}
