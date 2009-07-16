package com.flexpoker.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "gameStages")
public class GameStage {

    public static final String REGISTERING = "registering";

    public static final String STARTING = "starting";

    public static final String IN_PROGRESS = "in progress";

    public static final String FINISHED = "finished";

    private Integer id;

    private String name;

    @Id
    @GeneratedValue
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
