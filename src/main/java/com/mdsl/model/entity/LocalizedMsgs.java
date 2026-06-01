package com.mdsl.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "MD_CFG_LOCALIZED_MSGS")
@Getter
@Setter
@NoArgsConstructor
public class LocalizedMsgs {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MSG_ID")
    @SequenceGenerator(name="MSG_ID", sequenceName = "MD_LOCALIZED_MSGS_SEQ", allocationSize = 1)
    @Column(name = "MSG_ID", unique = true)
    private int msgId;

    @Column(name = "LANG_CODE")
    private String languageCode;

    @Column(name = "CATEGORY")
    private String category;

    @Column(name = "MESSAGE_KEY")
    private String messageKey;

    @Column(name = "MESSAGE")
    private String message;
}