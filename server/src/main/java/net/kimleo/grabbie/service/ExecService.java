package net.kimleo.grabbie.service;

import net.kimleo.grabbie.model.Execution;
import net.kimleo.grabbie.repository.ExecRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExecService {

    @Autowired
    private ExecRepo execRepo;

    public List<Execution> getTaskExecution(Long taskId, Boolean executed) {
        if (executed != null) {
            return execRepo.findByTaskIdAndExecuted(taskId, executed);
        }
        return execRepo.findByTaskId(taskId);
    }
    public List<Execution> getAgentExecution(Long agentId, Boolean executed) {
        if (executed != null) {
            return execRepo.findByAgentIdAndExecuted(agentId, executed);
        }
        return execRepo.findByAgentId(agentId);
    }

}
