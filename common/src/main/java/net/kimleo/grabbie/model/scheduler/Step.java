package net.kimleo.grabbie.model.scheduler;

import net.kimleo.grabbie.model.Task;
import net.kimleo.grabbie.model.resource.Resource;

import javax.persistence.*;

@Entity
public class Step {

    @Id
    @GeneratedValue
    Long id;

    @Lob
    String resource;

    ResourceType resourceType;

    @Transient
    Resource _resourceObj;

    @ManyToOne
    Step onSuccess;

    @ManyToOne
    Step onFail;

    String requiredTag;
}
