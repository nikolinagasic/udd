package com.la.model.dtos;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BoardMemberWorkToApproveDTO {
    private String writerFirstName;
    private String writerLastName;
    private List<String> filenames;
    private String taskId;
    private String processInstanceId;
}
