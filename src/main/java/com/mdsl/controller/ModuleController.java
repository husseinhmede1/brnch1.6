package com.mdsl.controller;

import com.mdsl.model.dto.response.ModuleActivityResponseDto;
import com.mdsl.service.CommonService;
import com.mdsl.service.ModuleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api("Module Controller")
@RestController
@Transactional
@RequestMapping("/v1/lookup/modules")
@RequiredArgsConstructor
public class ModuleController {

    private final ModuleService moduleService;
    private final CommonService commonService;

    @GetMapping("/user")
    @ApiOperation(value = "Get all Module Activities by User", response = ModuleActivityResponseDto.class)
    public ResponseEntity<List<ModuleActivityResponseDto>> getModulesActivitiesByUser(HttpServletRequest request) {
        return ResponseEntity.ok(moduleService.getModulesActivitiesByUser(
                Integer.parseInt(request.getHeader("instId")),
                commonService.getLoggedInUser().getId()
        ));
    }
}
