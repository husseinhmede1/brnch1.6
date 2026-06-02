package com.mdsl.model.objects;

import lombok.*;

@Getter
@Setter
@Builder 
@AllArgsConstructor
@NoArgsConstructor
public class FrontUrl {
	private String url;
	private String isMenu;
	private String accessRight;   // ACCESS_RIGHT from MD_CFG_ACTIVITY_API e.g. "YNNNN", "YYYYYYY"
}