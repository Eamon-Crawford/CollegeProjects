import requests
import unittest

class initialisetest(unittest.TestCase):
	def initTest(self):
		r = requests.post(url ="http://pygmalion.redbrick.dcu.ie:7726/init-lock/", json ={"Payload":"{\"UserId\":\"a0f0cc93-beb0-4e3b-9da1-7c2bb595cd3b\",\"LockId\":\"11111111\",\"LockName\":\"Ness\"}"})
		assert r.content == "lock initialised"


if __name__ == '__main__':
	unittest.main()


