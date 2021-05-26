import requests
import time
import json

lockKey="1"
open = False

while open!= True:


	r =requests.get(url ="http://pygmalion.redbrick.dcu.ie:7726/queue/", params = "")

	cont = str(r.content)[2:-1]
	print(cont)
	if cont!="queue is empty":
		cont = cont.replace("\'", "\"")
		jsonload = json.loads(cont)
		if lockKey in jsonload:
			if jsonload[lockKey] == "open":
				open = True
			else:
				print("sleeping")
				time.sleep(5)
		else:
			print("sleeping")
			time.sleep(5)
	else:
		print("sleeping")
		time.sleep(5)


print("Lock open")
r = requests.post(url ="http://pygmalion.redbrick.dcu.ie:7726/delqueue/", json ={lockKey:"del"})
print(r.content)

