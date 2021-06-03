from flask import Flask
from flask import request
from flask import jsonify
import pymongo
import json
import uuid
app = Flask(__name__)


cluster = pymongo.MongoClient("mongodb+srv://seandhammond99:number77@cluster0-c3eli.mongodb.net/test?retryWrites=true&w=majority")
db = cluster["SafeHouse"]
userscollection = db["users"]
publickeyscollection = db["publicKeys"]
lockscollection = db["locks"]
users_lockscollection = db["users_locks"]
users_publickeyscollection = db["users_publicKeys"]


QUEUE = {}
INITQUEUE = {}

@app.route("/queue/", methods=["GET","POST"])
def getQueue():
	if request.method == "GET":
		if QUEUE:
			queueString = str(QUEUE)
		else:
			queueString = "queue is empty"
		return queueString


@app.route("/initqueue/", methods=["GET","POST"])
def getInitQueue():
	if request.method == "GET":
		if INITQUEUE:
			queueString = str(INITQUEUE)
		else:
			queueString = "queue is empty"
		return queueString


@app.route("/open-lock/", methods=["POST"])
def addQueue():
	if request.method == "POST":
		content = request.json
		print(content)
		global QUEUE
		payload = content["Payload"]
		load = json.loads(payload)
		updater = ""
		finder = users_lockscollection.find()
		print(load["LockId"])
		for item in finder:
			if item["userID"] == load["UserId"] and item["lockID"] == load["LockId"][-8:]:
				updater = {load["LockId"]:"open"}
		if updater != "":
			QUEUE.update(updater)
			return str(QUEUE)
		else:
			print("User not authorised")
			return("Error: User not authorised")


@app.route("/delqueue/", methods=["POST"])
def delQueue():
	if request.method == "POST":
		global QUEUE
		content = request.json
		keylist = []
		for key in content.keys():
			keylist.append(key)
		if keylist[0] in QUEUE:
			del QUEUE[keylist[0]]
		return "deleted "+keylist[0]+" from queue"

@app.route("/delinitqueue/", methods=["POST"])
def delInitQueue():
	if request.method == "POST":
		global INITQUEUE
		content = request.json
		keylist = []
		for key in content.keys():
			keylist.append(key)
		if keylist[0] in INITQUEUE:
			del INITQUEUE[keylist[0]]
		return "deleted "+keylist[0]+" from initqueue"



@app.route("/create-account/", methods=["POST"])
def createAccount():
	if request.method == "POST":
		content = request.json
		pubKeyID = str(uuid.uuid4())
		jsonload = content
		isPresent = False
		dbquery = {"Email" : jsonload["Email"]}
		finder = userscollection.find(dbquery)
		for item in finder:
			isPresent = True
		if not isPresent:
			pkdbadd = {"publicKeyID" : pubKeyID, "Key" : jsonload["Key"]}
			publickeyscollection.insert(pkdbadd)
			userID = str(uuid.uuid4())
			udbadd = {"userID" : userID, "Email" : jsonload["Email"]}
			userscollection.insert(udbadd)
			upkadd = {"userID" : userID, "publicKeyID" : pubKeyID}
			users_publickeyscollection.insert(upkadd)
			return userID
		return "Error: email already registered"

@app.route("/test-lock/", methods = ["POST"])
def testLock():
		if request.method == "POST":
			from signtest import verify_sign
			content = request.json
			payload = content["Payload"]
			load = json.loads(payload)
			uid = load["UserId"]
			publickeyid= ""
			for item in users_publickeyscollection.find():
				if item["userID"]== uid:
					publickeyid = item["publicKeyID"]

			publickey = ""

			for item in publickeyscollection.find():
				if item["publicKeyID"]==publickeyid:
					publickey = item["Key"]
					break
			print(publickey)
			signature = content["Signature"]
			print(signature)
			print(payload)
#			if verify_sign(publickey, signature, payload):
#				print("yay")
#			else:
#				print("awwwh")
			global INITQUEUE
			lockid = load["LockId"]
			lockexists = False
			for item in lockscollection.find():
				if item["lockID"] == lockid:
					lockexists = True
					break
			if lockexists:
				INITQUEUE.update({lockid:"test"})
			return content


@app.route("/init-lock/", methods = ["POST"])
def initLock():
	if request.method == "POST":
		global INITQUEUE
		content = request.json
		payload = content["Payload"]
		load = json.loads(payload)
		INITQUEUE.update({load["LockId"] : "init"})
		myquery = {"lockID" : load["LockId"]}
		newvalues = {"$set": {"name" : load["LockName"]}}
		lockscollection.update_one(myquery, newvalues)
		users_lockscollection.insert_one({"userID":load["UserId"],"lockID":load["LockId"]})
		return "lock initialised"



@app.route('/admin-add-user/', methods = ["POST"])
def adminAddUser():
	if request.method == "POST":
		headers = request.json
		payload = headers["Payload"]
		content = json.loads(payload)
		print("content", content)
		userlist = userscollection.find()
		UserId = ""
		for item in userlist:
			print(item["Email"])
			print(content["Email"])
			if item["Email"] == content["Email"]:
				UserId = item["userID"]
				print(UserId)
				break
		if UserId == "":
			return "Error: Email not found"
		users_lockscollection.insert_one({"userID":UserId,"lockID":content["LockId"]})
		return "User Added to lock."

@app.route('/rename-lock/', methods = ["POST"])
def renameLock():
	if request.method == "POST":
		content = request.json
		updater = ""
		finder = users_lockscollection.find()
		for item in finder:
			if item["userID"] == content["UserId"] and item["lockID"] == content["LockId"]:
				updater = {"$set": {"name":content["name"]}}
		if updater == "":
			return "Error: User not authorised."
		lockquery = {"lockID":content["LockId"]}
		lockscollection.update(lockquery,updater)
		return "Lock name updated."


if (__name__ == "__main__"):
	app.run(host="0.0.0.0", port=7726)
