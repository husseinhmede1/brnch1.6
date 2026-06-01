package com.mdsl.utils.enumerations;

import lombok.Getter;

@Getter
public enum PlaceholdersEnum {

    CARD_NUMBER("[CN]"),
    BRANCH("[BR]"),
    CARD_TYPE("[CT]"),
    AMOUNT("[AM]"),
    AVAILABLE_BALANCE("[AB]"),
    LEDGER_BALANCE("[LB]"),
    OTP("[OTP]"),
    PIN("[PIN]"),
    ACCOUNT_NUMBER("[AN]"),
    TRACE_NUMBER("[TN]"),
    CURRENCY_SYMBOL("[CS]"),
    NEW_AMOUNT_IN_REVERSALS("[RA]"),
    LOCAL_TRANSACTION_DATE("[LD]"),
    LOCAL_TRANSACTION_TIME("[LT]"),
    TERMINAL_LOCATION("[TL]"),
    TERMINAL_ADDRESS("[TA]"),
    TERMINAL_CITY("[TCT]"),
    TERMINAL_COUNTRY("[TCR]"),
    RESPONSE_CODE("[RC]"),
    RESPONSE_DESC("[RD]"),
    OTP_EXP_DATE("[EXP]"),
    CASH_WITHDRAWL_DAILY_LIMIT("[CW]"),
    POS_DAILY_LIMIT("[PO]"),

    //BELOW ARE ONLY USED FOR EXCEPTION HANDLING AND ARE NOT PLACEHOLDERS FOR NOTIFICATIONS
    HOST_PARAMETER("[HP]"),
    HOST_TYPE("[HT]"),
    SOURCE("[SRC]"),
    SERVICE_SET("[SS]");

    private final String value;

    PlaceholdersEnum(String value) {
        this.value = value;
    }
}