package com.ozo.freeboard.service;

import com.ozo.freeboard.dto.FreeBoardDTO;
import com.ozo.freeboard.dto.PageRequestDTO;
import com.ozo.freeboard.dto.PageResultDTO;
import com.ozo.freeboard.entity.FreeBoard;

public interface FreeBoardService {

    Long register(FreeBoardDTO freeBoardDTO);

    PageResultDTO<FreeBoardDTO, FreeBoard> getList(PageRequestDTO requestDTO);

    FreeBoardDTO read(Long bno);

    void modify(FreeBoardDTO freeBoardDTO);

    void remove(Long bno);


    default FreeBoard dtoToEntity(FreeBoardDTO freeBoardDTO){
        FreeBoard entity = FreeBoard.builder()
                .bno(freeBoardDTO.getBno())
                .title(freeBoardDTO.getTitle())
                .content(freeBoardDTO.getContent())
                .writer(freeBoardDTO.getWriter())
                .build();
        return entity;
    }

    default FreeBoardDTO entityToDTO(FreeBoard freeBoard){

        FreeBoardDTO dto = FreeBoardDTO.builder()
                .bno(freeBoard.getBno())
                .title(freeBoard.getTitle())
                .content(freeBoard.getContent())
                .writer(freeBoard.getWriter())
                .regDate(freeBoard.getRegDate())
                .modDate(freeBoard.getModDate())
                .build();
        return dto;
    }

}
