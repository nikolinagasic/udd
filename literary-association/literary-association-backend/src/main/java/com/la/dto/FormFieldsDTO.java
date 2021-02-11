package com.la.dto;

import lombok.*;
import org.camunda.bpm.engine.form.FormField;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class FormFieldsDTO {
	String taskId;
	List<FormField> formFields;
	String processInstanceId;
	String postFormEndpoint;
	String uploadFileEndpoint;
}
