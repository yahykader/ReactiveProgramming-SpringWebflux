package com.greta.reactive.web;

import com.greta.reactive.dao.SocieteRepository;
import com.greta.reactive.dao.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebMVCController {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private SocieteRepository societeRepository;


    @GetMapping("/index")
    public String index(Model model){
       model.addAttribute("societe",societeRepository.findAll());
        return "index";
    }


}
