package com.mdsl.service;

import com.mdsl.repository.RoleApiAccessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleApiAccessService {

    private final RoleApiAccessRepository repository;

    public boolean hasAccessView(
            List<Integer> roleIds,
            Integer apiId
    ) {

        return repository.findByRoleIdInAndApiId(roleIds, apiId)
                .stream()
                .anyMatch(x -> x.getAccessView() == 1);
    }

    public boolean hasAccessAdd(
            List<Integer> roleIds,
            Integer apiId
    ) {

        return repository.findByRoleIdInAndApiId(roleIds, apiId)
                .stream()
                .anyMatch(x -> x.getAccessAdd() == 1);
    }

    public boolean hasAccessUpdate(
            List<Integer> roleIds,
            Integer apiId
    ) {

        return repository.findByRoleIdInAndApiId(roleIds, apiId)
                .stream()
                .anyMatch(x -> x.getAccessUpdate() == 1);
    }

    public boolean hasAccessDelete(
            List<Integer> roleIds,
            Integer apiId
    ) {

        return repository.findByRoleIdInAndApiId(roleIds, apiId)
                .stream()
                .anyMatch(x -> x.getAccessDelete() == 1);
    }

    public boolean hasAccessChecker(
            List<Integer> roleIds,
            Integer apiId
    ) {

        return repository.findByRoleIdInAndApiId(roleIds, apiId)
                .stream()
                .anyMatch(x -> x.getAccessChecker() == 1);
    }
}