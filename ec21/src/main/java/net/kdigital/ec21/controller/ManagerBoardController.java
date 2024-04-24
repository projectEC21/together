package net.kdigital.ec21.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
import net.kdigital.ec21.service.ManagerBoardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class ManagerBoardController {
    private final ManagerBoardService managerBoardService;

    @ResponseBody
    @GetMapping("/manager/board/registerProductCount")
    public List<Map<String, Object>> registerProductCount() {
        List<Map<String, Object>> result = managerBoardService.getRegisterProductCount();
        return result;
    }

    @ResponseBody
    @GetMapping("/manager/board/categoryProductCount")
    public Map<String, Map<String, Integer>> categoryData() {
        Map<String, Map<String, Integer>> result = managerBoardService.getCategoryData();
        return result;
    }

}
