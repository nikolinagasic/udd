package com.la.model.publish;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PublishBookRequest implements Serializable {
    private String title;
    private String synopsis;
    private String editor;
    private String writer;
    private String lector;
    private String genre;
    private String status;
    private String deadline;
    private String path;
    private List<BetaReaderComment> betaReaderCommentList;
    private String suggestion;
    private String correction;
}
