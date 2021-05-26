import pymongo
cluster = pymongo.MongoClient("mongodb+srv://seandhammond99:number77@cluster0-c3eli.mongodb.net/test?retryWrites=true&w=majority")
db = cluster["SafeHouse"]
collection = db["users"]


results = collection.delete_many({"user":"EamonCraw98"})


