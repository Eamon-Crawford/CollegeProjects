import pymongo
cluster = pymongo.MongoClient("mongodb+srv://seandhammond99:number77@cluster0-c3eli.mongodb.net/test?retryWrites=true&w=majority")
db = cluster["SafeHouse"]
collection = db["users"]


post = {"user": "EamonCraw98", "pass":"shiekismidtierIstg","email":"eamoncraw98@gmail.com", "locks":[]}
collection.insert_one(post)




