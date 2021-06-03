import requests
import time
import json
import RPi.GPIO as GPIO

GPIO.setmode(GPIO.BOARD)
GPIO.setup(7,GPIO.OUT)
p = GPIO.PWM(7,50)
p.start(7.5)
p.ChangeDutyCycle(0)

sleepTime = 5
with open("lockname.txt") as f:
    lockKey = f.read()
    
with open("lockstatus.txt") as f:
    lockStatus = f.read()
    
print(lockKey)
print(lockStatus)
OPEN = False
TEST = False
INITIALISE = False
lockname = ""

if lockStatus != "init":
    print("lock opening")
    p.ChangeDutyCycle(12.5)
    time.sleep(0.5)
    p.ChangeDutyCycle(0)
else:
    print("lock closing")
    p.ChangeDutyCycle(2.5)
    time.sleep(0.5)
    p.ChangeDutyCycle(0)


while True:
    try:
        with open("lockname.txt") as g:
            lockKey = g.read()
    
        with open("lockstatus.txt") as g:
            lockStatus = g.read()
            
        if lockStatus == "init":
            while OPEN!= True:


                    r =requests.get(url ="http://pygmalion.redbrick.dcu.ie:7726/queue/", params = "")

                    cont = str(r.content)[2:-1]
                    print(cont)
                    if cont!="queue is empty":
                            cont = cont.replace("\'", "\"")
                            jsonload = json.loads(cont)
                            if lockKey in jsonload:
                                    if jsonload[lockKey] == "open":
                                           OPEN = True
                                    else:
                                            print("sleeping")
                                            time.sleep(sleepTime)
                            else:
                                    print("sleeping")
                                    time.sleep(sleepTime)
                    else:
                            print("sleeping")
                            time.sleep(sleepTime)

            r = requests.post(url ="http://pygmalion.redbrick.dcu.ie:7726/delqueue/", json  ={lockKey:"del"})
            print("Lock opening")
            p.ChangeDutyCycle(12.5)
            time.sleep(5)
            print("lock closing")
            p.ChangeDutyCycle(2.5)
            time.sleep(0.5)
            OPEN = False
            p.ChangeDutyCycle(0)
            
        else:
            while TEST == False and INITIALISE == False:


                    r =requests.get(url ="http://pygmalion.redbrick.dcu.ie:7726/initqueue/", params = "")

                    cont = str(r.content)[2:-1]
                    print(cont)
                    if cont!="queue is empty":
                            cont = cont.replace("\'", "\"")
                            jsonload = json.loads(cont)
                            
                            if lockKey in jsonload:
                                    if jsonload[lockKey] == "test":
                                           TEST = True
                                    elif jsonload[lockKey] == "init":
                                            INITIALISE = True
                                    else:
                                            print("sleeping")
                                            time.sleep(sleepTime)
                            else:
                                    print("sleeping")
                                    time.sleep(sleepTime)
                    else:
                            print("sleeping")
                            time.sleep(sleepTime)
                            
            if TEST == True:
                r = requests.post(url ="http://pygmalion.redbrick.dcu.ie:7726/delinitqueue/", json  ={lockKey:"del"})
                print("Lock closing")
                p.ChangeDutyCycle(2.5)
                time.sleep(5)
                print("lock opening")
                p.ChangeDutyCycle(12.5)
                time.sleep(0.5)
                TEST = False
                p.ChangeDutyCycle(0)
                
            elif INITIALISE == True:
                r = requests.post(url ="http://pygmalion.redbrick.dcu.ie:7726/delinitqueue/", json  ={lockKey:"del"})
                f = open("lockstatus.txt", "w")
                f.write("init")
                f.close()
                print("Lock closing")
                p.ChangeDutyCycle(2.5)
                time.sleep(0.5)
                p.ChangeDutyCycle(0)
            
    except KeyboardInterrupt:
        p.stop()
        GPIO.cleanup()





