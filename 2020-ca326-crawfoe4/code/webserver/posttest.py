import requests
import unittest

class posttest(unittest.TestCase):
	def postTest(self):
		r = requests.post(url ="http://pygmalion.redbrick.dcu.ie:7726/open-lock/", json ={"Payload":"{\"UserId\":\"a0f0cc93-beb0-4e3b-9da1-7c2bb595cd3b\",\"LockId\":\"11111111\"}"})
		assert r.content != "Error: User not authorised"


if __name__ == '__main__':
	unittest.main()

