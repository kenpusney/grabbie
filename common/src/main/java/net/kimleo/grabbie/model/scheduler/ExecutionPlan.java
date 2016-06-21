package net.kimleo.grabbie.model.scheduler;

import javax.persistence.*;
import java.util.List;

@Entity
public class ExecutionPlan {
    @Id
    @GeneratedValue
    Long id;

    String name;

    @OneToOne
    Step firstStep;
}
