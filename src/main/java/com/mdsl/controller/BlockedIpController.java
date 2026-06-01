package com.mdsl.controller;


import com.mdsl.model.dto.response.BlockedIpResponseDto;
import com.mdsl.service.BlockedIpService;
import com.mdsl.utils.ResponseCode;
import com.mdsl.utils.Validations;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api("Blocked IP Controller")
@CrossOrigin(origins={"*"})
@RestController
@RequestMapping("/blocked-ip")
@RequiredArgsConstructor
public class BlockedIpController {

    private final BlockedIpService blockedIpService;

    @GetMapping("/all")
    public ResponseEntity<List<BlockedIpResponseDto>> getAllData(){
         return ResponseEntity.ok(blockedIpService.getAllBlockedIps());
    }

    @PostMapping("/unblock/{id}")
    public ResponseEntity<String> unblockIp(@PathVariable("id")String id){
        return ResponseEntity.ok(blockedIpService.unblockIp(id));
    }
}