package com.mdsl.framework;

import java.text.MessageFormat;
import java.util.*;

import com.mdsl.model.entity.LocalizedMsgs;
import com.mdsl.repository.LocalizedMessageRepository;
import com.mdsl.utils.enumerations.PlaceholdersEnum;
import org.springframework.context.support.AbstractMessageSource;

//import com.mdsl.model.entity.LocalizedMsgs;
//import com.mdsl.repository.LocalizedMessageRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DatabaseMessageSource extends AbstractMessageSource{

	private static final Map<String, String> codeMessage = new HashMap<>();
	private final LocalizedMessageRepository messageRepository;

	public List<String> getMessagesFromKeys(List<String> keys, Locale locale, Boolean validateSource) {
		final String langCode = locale.getLanguage();
		final List<String> rtnMessages = new ArrayList<>();
		String code = "";
		String suffix = "";
		String finalMessage = "";

		for(String codeString: keys) {
			code = "";
			suffix = "";
			finalMessage = "";
			try {
				code = codeString.split(",")[0];
				suffix  = codeString.split(",")[1];
			} catch (Exception e) {
				suffix = "";
			}
			final String key = code + "_" + langCode + "_" + suffix;
			if (!codeMessage.containsKey(key)) {
				Optional<LocalizedMsgs> localizedMsgs = messageRepository.findByLanguageCodeAndMessageKey(langCode, code);
				if(localizedMsgs.isPresent()) {
					if(!suffix.equalsIgnoreCase("") && localizedMsgs.get().getMessage().contains(PlaceholdersEnum.SOURCE.getValue())) {
						finalMessage = localizedMsgs.get().getMessage().replace(PlaceholdersEnum.SOURCE.getValue(), suffix.equalsIgnoreCase("[CA]")?"card holder":suffix.equalsIgnoreCase("[CU]")?"customer":"");
					} else if(localizedMsgs.get().getMessage().contains(PlaceholdersEnum.SOURCE.getValue())) {
						finalMessage = localizedMsgs.get().getMessage().replace(PlaceholdersEnum.SOURCE.getValue()+" ", "");
					} else {
						finalMessage = localizedMsgs.get().getMessage();
					}
					codeMessage.put(key, finalMessage);
				}
			}
			if (codeMessage.containsKey(key) && !rtnMessages.contains(code + ": " + codeMessage.get(key))) {
				rtnMessages.add(code + ": " + codeMessage.get(key));
			}
		}
		return rtnMessages;
	}

	public List<String> getMessageFromKey(String key, Locale locale, Boolean replaceDefault) {
		final String langCode = locale.getLanguage();
		final List<String> rtnMessages = new ArrayList<>();

		final String langKey = key + "_" + langCode;
		if (!codeMessage.containsKey(langKey)) {
			Optional<LocalizedMsgs> localizedMsgs = messageRepository.findByLanguageCodeAndMessageKey(langCode, key);
			if(localizedMsgs.isPresent()) {
				String message = replaceDefault?localizedMsgs.get().getMessage().replace(PlaceholdersEnum.SOURCE.getValue()+" ", ""):localizedMsgs.get().getMessage();
				codeMessage.put(langKey,message);
			}
		}
		if (codeMessage.containsKey(langKey)) {
			rtnMessages.add(key + ": " + codeMessage.get(langKey));
		}

		return rtnMessages;
	}

	public List<String> getMessageFromKeyNoCode(String key, Locale locale) {
		final String langCode = locale.getLanguage();
		final List<String> rtnMessages = new ArrayList<>();

		final String langKey = key + "_" + langCode;
		if (!codeMessage.containsKey(langKey)) {
			messageRepository.findByLanguageCodeAndMessageKey(langCode, key)
					.ifPresent(d -> codeMessage.put(langKey, d.getMessage().replace(PlaceholdersEnum.SOURCE.getValue()+" ", "")));
		}
		if (codeMessage.containsKey(langKey)) {
			rtnMessages.add(codeMessage.get(langKey));
		}

		return rtnMessages;
	}

	public List<String> getMessageAndCodeFromKey(String key, String code, Locale locale){
		final List<String> rtnMessages = getMessageFromKey(key,locale, false);
		int indexCounter = 0;
		for(String message: rtnMessages) {
			List<String> splitMessage =  Arrays.asList(message.split(" "));
			if(splitMessage.contains(PlaceholdersEnum.CARD_TYPE.getValue())) {
				message = message.replace(PlaceholdersEnum.CARD_TYPE.getValue(), code);
			}
			if(splitMessage.contains(PlaceholdersEnum.CARD_NUMBER.getValue())) {
				message = message.replace(PlaceholdersEnum.CARD_NUMBER.getValue(), code);
			}
			if(splitMessage.contains(PlaceholdersEnum.HOST_PARAMETER.getValue())) {
				message = message.replace(PlaceholdersEnum.HOST_PARAMETER.getValue(), code.split(",")[1]);
			}
			if(splitMessage.contains(PlaceholdersEnum.HOST_TYPE.getValue())) {
				message = message.replace(PlaceholdersEnum.HOST_TYPE.getValue(), code.split(",")[0]);
			}
			if(splitMessage.contains(PlaceholdersEnum.SOURCE.getValue())) {
				message = message.replace(PlaceholdersEnum.SOURCE.getValue(), code);
			}
			if(splitMessage.contains(PlaceholdersEnum.SERVICE_SET.getValue())) {
				message = message.replace(PlaceholdersEnum.SERVICE_SET.getValue(), code);
			}
			rtnMessages.set(indexCounter, message);
			indexCounter++;
		}
		return rtnMessages;
	}

	@Override
	protected MessageFormat resolveCode(String key, Locale locale) {
		final String langCode = locale.getLanguage();

		Optional<LocalizedMsgs> message =  messageRepository.findByLanguageCodeAndMessageKey(langCode, key);

		LocalizedMsgs localizedMessage = message.orElseGet(() -> {
			LocalizedMsgs msg = new LocalizedMsgs();
			msg.setLanguageCode(langCode);
			msg.setMessageKey(key);
			msg.setMessage(key); // Use key as message. May choose another fallback strategy
			return msg;
		});

		return new MessageFormat(localizedMessage.getMessage(), locale);
	}

	@Override
	protected String resolveCodeWithoutArguments(String code, Locale locale) {
		final String langCode = locale.getLanguage();
		Optional<LocalizedMsgs> message =  messageRepository.findByLanguageCodeAndMessageKey(langCode, code);

		LocalizedMsgs localizedMessage = message.orElseGet(() -> {
			LocalizedMsgs msg = new LocalizedMsgs();
			msg.setLanguageCode(langCode);
			msg.setMessageKey(code);
			msg.setMessage(code); // Use key as message. May choose another fallback strategy
			return msg;
		});

		return localizedMessage.getMessage();
	}
}