#!/usr/bin/env python3

import requests
import time
import os
'''
NOTE:
    This script will never update for new featuress
'''
BASE_URL = "http://localhost:8080"

USER = 'admin'
PASS = 'password'

def to(url):
    return BASE_URL + url

class Agent(object):

    def __init__(self, url):
        self.url = url
        response = requests.post(to("/agent"),
                                 auth = (USER, PASS),
                                 json = {"url": url})
        print(response)
        self.agent_url = response.headers["Location"]

    def getUnexecutedTask(self):
        response = requests.get(to(self.agent_url+"/execution"),
                                auth = (USER, PASS),
                                params={"executed": False})
        return Executions(response.json())

class Executions(object):

    def __init__(self, execs):
        self.execs = map(Execution, execs)

    def execute(self):
        for e in self.execs:
            e.execute()
        else:
            print("No task to execute")

class Execution(object):

    def __init__(self, execution):

        self.id = execution['id']
        self.task = execution['task']

        self.cmd = self.task['command']
        self.args = self.task['args']

    def execute(self):
        result = os.popen("%s %s" % (self.cmd, " ".join(self.args))).read()
        
        requests.put(to("/execution/" + str(self.id)),
                     auth = (USER, PASS),
                     json={"executed": True, "result": result})
        print("Executed", self.cmd, self.args)


if __name__ == '__main__':
    agent = Agent("localhost")
    while True:
        try:
            time.sleep(3)
            agent.getUnexecutedTask().execute()
        except KeyboardInterrupt:
            print("User Interrupted")
            break
        except:
            print("An error occured")
            pass
