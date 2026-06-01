package com.mdsl.utils;

public class ApiListUtis {

	public String[] listOfApiTwoSlashes() {

		String[] str = new String[] { "getAcquiringTransactions", "haltpaytransaction", "representment",
				"reversalTransaction", "search", "assignment", "status-change", "countries", "active-cardscheme",
				"active-country", "active-currencies", "usage", "active-employee", "active-entities", "getEntities",
				"institution", "active-institutions", "getAllMerchantTransactions", "getAllNonActivityTransactions",
				"getMCCList", "getAllNonActivityFeeQueries", "active-nonactivity-package-details", "active",
				"prefix-suffix", "active-terminal", "getAllTerminal", "active-terminaltype", "active-transactiongroup",
				"institution-id", "password-change", "password-reset" };

		return str;

	}

	public String[] listOfApiThreeSlashes() {

		String[] str = new String[] { "entities", "institution", "active-activitypackage", "mapped-entities",
				"activitypackage", "packagedetailid", "packagedetail", "packagetier", "country", "province", "search",
				"usage", "clone", "active-nonactivity-packages", "active", "package", "institution-id",
				"deletetransactionchargedetail" };

		return str;

	}

}
