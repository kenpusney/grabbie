Grabbie spec
======


## 1. Data Types

```typescript
// Task
type Task = {
    id: Long,
    command: Array<String>,

}
// AgentTag
type AgentTag = {
    id: Long,
    name: String
}
// Agent
{
   id: Long,
   url: String,
   lastActiveTime: Timestamp,
   agentTags: List<AgentTag>
}

type ExecutionStatus = EXECUTED_SUCCESS |
                       EXECUTED_FAILURE |
                       NOT_EXECUTED |
                       STARTED |
                       IGNORED_BY_CLIENT |
                       RETRIED_SUCCESS |
                       RETRIED_FAILED

// Execution
type Execution = {
   id: Long,
   agent: Agent,
   task: Task,
   result: Text,
   executed: Boolean,
   status: ExecutionStatus,
   timeStarted: Timestamp,
   duration: Long,
   date: Timestamp
}
```

## 2. DSL

## 3. Scheduling