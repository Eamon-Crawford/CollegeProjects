import requests
import unittest

class deletetest(unittest.TestCase):
	def delTest(self):
		r = requests.post(url ="http://pygmalion.redbrick.dcu.ie:7726/delqueue/", json ={"11111111":"open"})
		assert r.content == "deleted 11111111 from queue"

if __name__ == '__main__':
	unittest.main()