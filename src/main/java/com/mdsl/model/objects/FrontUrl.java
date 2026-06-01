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
}