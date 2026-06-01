package com.mdsl.model.entity;

import com.mdsl.model.entity.keys.RoleApiAccessId;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Immutable
@IdClass(RoleApiAccessId.class)
@Table(name = "MV_ROLE_API_ACCESS")
public class RoleApiAccess {

    @Id
    @Column(name = "ROLE_ID")
    private Integer roleId;

    @Id
    @Column(name = "API_ID")
    private Integer apiId;

    @Column(name = "ACCESS_VIEW")
    private Integer accessView;

    @Column(name = "ACCESS_ADD")
    private Integer accessAdd;

    @Column(name = "ACCESS_UPDATE")
    private Integer accessUpdate;

    @Column(name = "ACCESS_DELETE")
    private Integer accessDelete;

    @Column(name = "ACCESS_CHECKER")
    private Integer accessChecker;
}