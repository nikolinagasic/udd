package com.la.model;

import com.la.model.enums.PublishStatus;
import com.la.model.users.Lector;

import javax.persistence.*;
import java.util.Date;

@Entity
public class ScriptRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private PublishStatus status;

    @Column
    private Date submitDeadline;

    @Column
    private String corrections;

    @Column
    private Date changesDeadline;

    @Column
    private String suggestions;

    @OneToOne
    private Script script;

    @OneToOne
    private Lector lector;

    public ScriptRequest() {
    }

    public ScriptRequest(PublishStatus status, Date submitDeadline, String corrections, Date changesDeadline, String suggestions) {
        this.status = status;
        this.submitDeadline = submitDeadline;
        this.corrections = corrections;
        this.changesDeadline = changesDeadline;
        this.suggestions = suggestions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getSubmitDeadline() {
        return submitDeadline;
    }

    public void setSubmitDeadline(Date submitDeadline) {
        this.submitDeadline = submitDeadline;
    }

    public String getCorrections() {
        return corrections;
    }

    public void setCorrections(String corrections) {
        this.corrections = corrections;
    }

    public Date getChangesDeadline() {
        return changesDeadline;
    }

    public void setChangesDeadline(Date changesDeadline) {
        this.changesDeadline = changesDeadline;
    }

    public String getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(String suggestions) {
        this.suggestions = suggestions;
    }

    public PublishStatus getStatus() {
        return status;
    }

    public void setStatus(PublishStatus status) {
        this.status = status;
    }

    public Script getScript() {
        return script;
    }

    public void setScript(Script script) {
        this.script = script;
    }

    public Lector getLector() {
        return lector;
    }

    public void setLector(Lector lector) {
        this.lector = lector;
    }
}
