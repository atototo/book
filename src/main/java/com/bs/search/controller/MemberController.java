package com.bs.search.controller;

import com.bs.search.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService service;

    @GetMapping(value = "/save")
    public ResponseEntity<String> save() {
        service.save();
        return ResponseEntity.status(200).body("성공");
    }

    @GetMapping(value = "/saveAll")
    public ResponseEntity<String> saveAll() {
        service.saveAll();
        return ResponseEntity.status(200).body("성공");
    }

    @GetMapping(value = "/members")
    public ResponseEntity<List> members() {
        List members = service.findAll();
        return new ResponseEntity<>(members, HttpStatus.OK);
    }

    @GetMapping(value = "/members/name/{name}")
    public ResponseEntity<List> membersByName(@PathVariable(value = "name") String name) {
        List members = service.findAllByName(name);

        if(members.size() == 0){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Arrays.asList(new String[]{"데이터 없음."}));
        }
        return ResponseEntity.status(200).body(members);
    }

}
