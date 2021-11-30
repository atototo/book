package com.bs.search.service;

import com.bs.search.domain.Member;
import com.bs.search.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository repository;

    public void save() {
        repository.save(Member.builder().name("liu").build());
    }


    public void saveAll() {
        LinkedList<Member> list = new LinkedList<>();
        list.add(Member.builder().name("liu").build());
        list.add(Member.builder().name("llllu1").build());
        list.add(Member.builder().name("llllu2").build());

        repository.saveAll(list);
    }

    public List findAll() {
        return Collections.singletonList(repository.findAll());
    }

    public List findAllByName(String name) {
        return repository.findAllByNameOrderByName(name);
    }

}
