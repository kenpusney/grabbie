
import requests
import shlex
import time

USER = "admin"
PASS = "password"
HOST = "localhost"
PORT = "8080"

CLIENT_ID = "3"

class Remote(object):
    def __init__(self, user, pwd, host, port, client_id):
        self.user = user
        self.pwd = pwd
        self.host = host
        self.port = port

        self.auth = (user, pwd)
        
        self.client_id = client_id

        self.base = "http://" + host + ":" + port

        self.client_url = self.to("/client/"+client_id)

        self.execution = self.to("/execution")
        

    def to(self, url):
        return self.base + url

    def connect(self):
        if requests.get(self.client_url, auth = self.auth):
            pass

    def repl(self):
        while True:
            try:                
                inputs = shlex.split(input(">>> "))
                command, args = inputs[0], inputs[1:]
    
                response = requests.post(self.to("/task"),
                                         auth = self.auth,
                                         json = {"command": command, "args": args})
                task_id = response.json()['id']
                response = requests.post(self.to("/execution"),
                                         auth = self.auth,
                                         json = {
                                             "client": { "id": self.client_id },
                                             "task": { "id": task_id }
                                             })
                self.wait_for_result(response)
            except EOFError:
                print("Byebye!")
                break

    def wait_for_result(self, response):
        location = response.headers["Location"]
        while True:
            result = requests.get(self.to(location),
                                  auth = self.auth).json()
            print(".", end="")
            if (result['executed'] == True):
                print()
                print(result['result'])
                break
            time.sleep(1)
        
Remote(USER, PASS, HOST, PORT, CLIENT_ID).repl()
