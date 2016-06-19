GRABBIE [![Build Status](https://travis-ci.org/kenpusney/grabbie.svg?branch=master)](https://travis-ci.org/kenpusney/grabbie)
======

Grabbie is a task scheduling framework, it has 3 core components, which mainly focused on executes
distributed tasks cooperatively.

 - Grabbie Server
 - Grabbie Agent
 - Grabbie Scheduler (planning)


Grabbie server is a centralized task repository, it contains task definition and manages agents,
also can create task executions on specified agents.

Grabbie agent is a basic task execution unit which retrieve tasks from grabbie server and executes,
and report it's result.

Grabbie scheduler is a simple scheduler which maintains task plans and agent groups,
which scheduling based on agent activities from grabbie server. Grabbie scheduler can
talk to many grabbie servers and synchronizing task plan status among them. With this
design, grabbie server and agent shared nothing about plan details, this minimized the
complexity.
