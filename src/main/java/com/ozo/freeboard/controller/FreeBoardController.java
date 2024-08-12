package com.ozo.freeboard.controller;

import com.ozo.freeboard.dto.FreeBoardDTO;
import com.ozo.freeboard.dto.PageRequestDTO;
import com.ozo.freeboard.service.FreeBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/freeboard")
@Log4j2
@RequiredArgsConstructor
public class FreeBoardController {

    private final FreeBoardService service;

    @GetMapping("/")
    public String index(){
        return "redirect:/freeboard/list";
    }


    @GetMapping( "/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){
        log.info("list>......");
        model.addAttribute("result", service.getList(pageRequestDTO));
    }


    @GetMapping("/register")
    public void register(){log.info("글작성 페이지 온");}


    @PostMapping("/register")
    public String registerPost(FreeBoardDTO freeBoardDTO, RedirectAttributes redirectAttributes){
        log.info("dto..." + freeBoardDTO);

        if(freeBoardDTO.getTitle().length() > 200){
            redirectAttributes.addFlashAttribute("msg", "제목 200자 제한임 ㅅㄱ");
        } else if (freeBoardDTO.getContent().length() > 1000) {
            redirectAttributes.addFlashAttribute("msg", "내용은 1000자 이하로 쓰셈 ㅋ");
        }else {
            // 새로 추가된 엔티티의 번호
            Long bno = service.register(freeBoardDTO);
            redirectAttributes.addFlashAttribute("msg", bno);
        }




        return "redirect:/freeboard/list";
    }
}
