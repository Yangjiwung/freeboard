package com.ozo.freeboard.service;

import com.ozo.freeboard.dto.FreeBoardDTO;
import com.ozo.freeboard.dto.PageRequestDTO;
import com.ozo.freeboard.dto.PageResultDTO;
import com.ozo.freeboard.entity.FreeBoard;
import com.ozo.freeboard.entity.QFreeBoard;
import com.ozo.freeboard.repository.FreeBoardRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class FreeBoardServiceImpl implements FreeBoardService {

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

        Pageable pageable = requestDTO.getPageable(Sort.by("bno").descending());
        
        BooleanBuilder booleanBuilder = getSearch(requestDTO); // 검색 조건 처리

        Page<FreeBoard> result = repository.findAll(booleanBuilder , pageable); // querydsl 사용

        Function<FreeBoard, FreeBoardDTO> fn = (entity -> entityToDTO(entity));

        return new PageResultDTO<>(result, fn);

    }

    @Override
    public FreeBoardDTO read(Long bno) {

        Optional<FreeBoard> result = repository.findById(bno);

        return result.isPresent() ? entityToDTO(result.get()) : null;
    }

    @Override
    public void modify(FreeBoardDTO freeBoardDTO) {

        Optional<FreeBoard> result = repository.findById(freeBoardDTO.getBno());

        if (result.isPresent()) {
            FreeBoard entity = result.get();

            entity.changeTitle(freeBoardDTO.getTitle());
            entity.changeContent(freeBoardDTO.getContent());
            repository.save(entity);
        }
    }

    @Override
    public void remove(Long bno) {

        repository.deleteById(bno);
    }


    private BooleanBuilder getSearch(PageRequestDTO requestDTO) {
        String type = requestDTO.getType();
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QFreeBoard qFreeBoard = QFreeBoard.freeBoard;

        String keyword = requestDTO.getKeyword();

        BooleanExpression expression = qFreeBoard.bno.gt(0L); // bno > 0 조건만 생성
        booleanBuilder.and(expression);

        if (type == null || type.trim().length() == 0) { // 검색 조건이 없는 경우
            return booleanBuilder;
        }

        BooleanBuilder conditionBuilder = new BooleanBuilder();

        if (type.contains("t")) {
            conditionBuilder.or(qFreeBoard.title.contains(keyword));
        }
        if (type.contains("c")) {
            conditionBuilder.or(qFreeBoard.content.contains(keyword));
        }
        if (type.contains("t")) {
            conditionBuilder.or(qFreeBoard.writer.contains(keyword));
        }

        // 모든 조건 통합
        booleanBuilder.and(conditionBuilder);

        return booleanBuilder;


    }
}